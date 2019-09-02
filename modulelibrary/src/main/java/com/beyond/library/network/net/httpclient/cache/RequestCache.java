package com.beyond.library.network.net.httpclient.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.CipherSuite;
import okhttp3.Handshake;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.TlsVersion;
import okhttp3.internal.Util;
import okhttp3.internal.cache.CacheRequest;
import okhttp3.internal.cache.DiskLruCache;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.RealResponseBody;
import okhttp3.internal.http.StatusLine;
import okhttp3.internal.io.FileSystem;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.ForwardingSink;
import okio.ForwardingSource;
import okio.GzipSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

/**
 * Created by linjinfa on 2017/6/8.
 * email 331710168@qq.com
 */
public class RequestCache {

    private static final int VERSION = 201105;
    private static final int ENTRY_METADATA = 0;
    private static final int ENTRY_BODY = 1;
    private static final int ENTRY_COUNT = 2;

    private final DiskLruCache cache;

    /* read and write statistics, all guarded by 'this' */
    private int writeSuccessCount;
    private int writeAbortCount;
    private int networkCount;
    private int hitCount;
    private int requestCount;

    public RequestCache(File directory, long maxSize) {
        this(directory, maxSize, FileSystem.SYSTEM);
    }

    RequestCache(File directory, long maxSize, FileSystem fileSystem) {
        this.cache = DiskLruCache.create(fileSystem, directory, VERSION, ENTRY_COUNT, maxSize);
    }

    private String urlToKey(Request request) {

        return Util.md5Hex(request.url().toString());
    }

    /**
     * 获取缓存的key
     * @param request
     * @return
     */
    private String getUrlKey(Request request){
        if("post".equalsIgnoreCase(request.method())){
            MediaType contentType = request.body().contentType();
            Charset charset = null;
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("UTF-8"));
            }
            Buffer buffer = new Buffer();
            try {
                request.body().writeTo(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String requestParamStr = buffer.readString(charset);

            buffer.close();

            String key = Util.md5Hex(request.url().toString()+requestParamStr);
            return key;
        }else{
            return urlToKey(request);
        }
    }

    /**
     * 获取缓存 Response    忽略header
     * @param request
     * @return
     */
    public Response getCacheResponse(Request request){
        String urlMd5 = getUrlKey(request);

        File bodyCacheFile = new File(this.cache.getDirectory(), urlMd5);
        if(bodyCacheFile.exists()){
            Response.Builder cacheResponseBuilder = new Response.Builder();
            cacheResponseBuilder.request(request.newBuilder().build());
            cacheResponseBuilder.protocol(Protocol.HTTP_1_1);
            cacheResponseBuilder.code(200);

            Response cacheResponse = cacheResponseBuilder.build();
            Source cacheSource = null;
            try {
                cacheSource = Okio.source(bodyCacheFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(cacheSource!=null){
                if("gzip".equalsIgnoreCase(cacheResponse.header("Content-Encoding"))){
                    cacheSource = new GzipSource(cacheSource);
                }
                RealResponseBody responseBody = new RealResponseBody(cacheResponse.headers(),Okio.buffer(cacheSource));
                return cacheResponse.newBuilder().body(responseBody).build();
            }
        }
        return null;
    }

    /**
     * 保存 Response
     * @param response
     * @return
     */
    public Response putCacheResponse(Response response) {
        String urlMd5 = getUrlKey(response.request().newBuilder().build());

        File appDir = this.cache.getDirectory();
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        File bodyCacheFile = new File(appDir, urlMd5);
        if(bodyCacheFile.exists()){
            bodyCacheFile.delete();
        }
        byte[] responseBytes = new byte[0];
        try {
            bodyCacheFile.createNewFile();
            BufferedSink sink = Okio.buffer(Okio.sink(bodyCacheFile));
            responseBytes = response.body().bytes();
            sink.write(responseBytes);
            sink.close();
        } catch (Exception e) {
            e.printStackTrace();
            return response;
        }
        response = response.newBuilder().request(response.request().newBuilder().build()).body(ResponseBody.create(response.body().contentType(), responseBytes)).build();
        return response;
    }

    /**
     * 读取 response header
     * @param headerCacheFile
     * @param responseBuilder
     */
    private void readCacheResponseHeaders(File headerCacheFile,Response.Builder responseBuilder) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(headerCacheFile)));
            String lineText;
            int code = 0;
            boolean isHeaderBegin = false;
            while((lineText = reader.readLine()) != null){

                if(isHeaderBegin){
                    int sepIndex = lineText.indexOf(":");
                    String headerName = lineText.substring(0,sepIndex);
                    String headerVal = lineText.substring(sepIndex + 1).trim();
//                    responseBuilder.addHeader(headerName,headerVal);
                }

                if(code != 0){
                    isHeaderBegin = true;
                    continue;
                }

                try {
                    code = Integer.parseInt(lineText.trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(lineText.startsWith("HTTP/1.1")){
                    responseBuilder.protocol(Protocol.HTTP_1_1);
                    String httpStrs[] = lineText.split(" ");
                    if(httpStrs.length > 1){
                        try {
                            code = Integer.valueOf(httpStrs[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    responseBuilder.code(code);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    Response get(Request request) {
        String key = urlToKey(request);
        DiskLruCache.Snapshot snapshot;
        RequestCache.Entry entry;
        try {
            snapshot = cache.get(key);
            if (snapshot == null) {
                return null;
            }
        } catch (IOException e) {
            // Give up because the cache cannot be read.
            return null;
        }

        try {
            entry = new RequestCache.Entry(snapshot.getSource(ENTRY_METADATA));
        } catch (IOException e) {
            Util.closeQuietly(snapshot);
            return null;
        }

        Response response = entry.response(snapshot);

        if (!entry.matches(request, response)) {
            Util.closeQuietly(response.body());
            return null;
        }

        return response;
    }

    public void update(Response cached, Response network) {
        RequestCache.Entry entry = new RequestCache.Entry(network);
        DiskLruCache.Snapshot snapshot = ((RequestCache.CacheResponseBody) cached.body()).snapshot;
        DiskLruCache.Editor editor = null;
        try {
            editor = snapshot.edit(); // Returns null if snapshot is not current.
            if (editor != null) {
                entry.writeTo(editor);
                editor.commit();
            }
        } catch (IOException e) {
            abortQuietly(editor);
        }
    }

    public CacheRequest put(Response response) {
        RequestCache.Entry entry = new RequestCache.Entry(response);
        DiskLruCache.Editor editor = null;
        try {
            editor = cache.edit(urlToKey(response.request()));
            if (editor == null) {
                return null;
            }
            entry.writeTo(editor);
            return new RequestCache.CacheRequestImpl(editor);
        } catch (IOException e) {
            abortQuietly(editor);
            return null;
        }
    }

    private void remove(Request request) throws IOException {
        cache.remove(urlToKey(request));
    }

    private void abortQuietly(DiskLruCache.Editor editor) {
        // Give up because the cache cannot be written.
        try {
            if (editor != null) {
                editor.abort();
            }
        } catch (IOException ignored) {
        }
    }


    private final class CacheRequestImpl implements CacheRequest {
        private final DiskLruCache.Editor editor;
        private Sink cacheOut;
        private boolean done;
        private Sink body;

        public CacheRequestImpl(final DiskLruCache.Editor editor) {
            this.editor = editor;
            this.cacheOut = editor.newSink(ENTRY_BODY);
            this.body = new ForwardingSink(cacheOut) {
                @Override public void close() throws IOException {
                    synchronized (RequestCache.this) {
                        if (done) {
                            return;
                        }
                        done = true;
                        writeSuccessCount++;
                    }
                    super.close();
                    editor.commit();
                }
            };
        }

        @Override public void abort() {
            synchronized (RequestCache.this) {
                if (done) {
                    return;
                }
                done = true;
                writeAbortCount++;
            }
            Util.closeQuietly(cacheOut);
            try {
                editor.abort();
            } catch (IOException ignored) {
            }
        }

        @Override public Sink body() {
            return body;
        }
    }

    private static final class Entry {
        /** Synthetic response header: the local time when the request was sent. */
        private static final String SENT_MILLIS = Platform.get().getPrefix() + "-Sent-Millis";

        /** Synthetic response header: the local time when the response was received. */
        private static final String RECEIVED_MILLIS = Platform.get().getPrefix() + "-Received-Millis";

        private final String url;
        private final Headers varyHeaders;
        private final String requestMethod;
        private final Protocol protocol;
        private final int code;
        private final String message;
        private final Headers responseHeaders;
        private final Handshake handshake;
        private final long sentRequestMillis;
        private final long receivedResponseMillis;

        /**
         * Reads an entry from an input stream. A typical entry looks like this:
         * <pre>{@code
         *   http://google.com/foo
         *   GET
         *   2
         *   Accept-Language: fr-CA
         *   Accept-Charset: UTF-8
         *   HTTP/1.1 200 OK
         *   3
         *   Content-Type: image/png
         *   Content-Length: 100
         *   Cache-Control: max-age=600
         * }</pre>
         *
         * <p>A typical HTTPS file looks like this:
         * <pre>{@code
         *   https://google.com/foo
         *   GET
         *   2
         *   Accept-Language: fr-CA
         *   Accept-Charset: UTF-8
         *   HTTP/1.1 200 OK
         *   3
         *   Content-Type: image/png
         *   Content-Length: 100
         *   Cache-Control: max-age=600
         *
         *   AES_256_WITH_MD5
         *   2
         *   base64-encoded peerCertificate[0]
         *   base64-encoded peerCertificate[1]
         *   -1
         *   TLSv1.2
         * }</pre>
         * The file is newline separated. The first two lines are the URL and the request method. Next
         * is the number of HTTP Vary request header lines, followed by those lines.
         *
         * <p>Next is the response status line, followed by the number of HTTP response header lines,
         * followed by those lines.
         *
         * <p>HTTPS responses also contain SSL session information. This begins with a blank line, and
         * then a line containing the cipher suite. Next is the length of the peer certificate chain.
         * These certificates are base64-encoded and appear each on their own line. The next line
         * contains the length of the local certificate chain. These certificates are also
         * base64-encoded and appear each on their own line. A length of -1 is used to encode a null
         * array. The last line is optional. If present, it contains the TLS version.
         */
        public Entry(Source in) throws IOException {
            try {
                BufferedSource source = Okio.buffer(in);
                url = source.readUtf8LineStrict();
                requestMethod = source.readUtf8LineStrict();
                HeaderBuilder varyHeadersBuilder = new HeaderBuilder();
                int varyRequestHeaderLineCount = readInt(source);
                for (int i = 0; i < varyRequestHeaderLineCount; i++) {
                    varyHeadersBuilder.addLenient(source.readUtf8LineStrict());
                }
                varyHeaders = varyHeadersBuilder.build();

                StatusLine statusLine = StatusLine.parse(source.readUtf8LineStrict());
                protocol = statusLine.protocol;
                code = statusLine.code;
                message = statusLine.message;
                HeaderBuilder responseHeadersBuilder = new HeaderBuilder();
                int responseHeaderLineCount = readInt(source);
                for (int i = 0; i < responseHeaderLineCount; i++) {
                    responseHeadersBuilder.addLenient(source.readUtf8LineStrict());
                }
                String sendRequestMillisString = responseHeadersBuilder.get(SENT_MILLIS);
                String receivedResponseMillisString = responseHeadersBuilder.get(RECEIVED_MILLIS);
                responseHeadersBuilder.removeAll(SENT_MILLIS);
                responseHeadersBuilder.removeAll(RECEIVED_MILLIS);
                sentRequestMillis = sendRequestMillisString != null
                        ? Long.parseLong(sendRequestMillisString)
                        : 0L;
                receivedResponseMillis = receivedResponseMillisString != null
                        ? Long.parseLong(receivedResponseMillisString)
                        : 0L;
                responseHeaders = responseHeadersBuilder.build();

                if (isHttps()) {
                    String blank = source.readUtf8LineStrict();
                    if (blank.length() > 0) {
                        throw new IOException("expected \"\" but was \"" + blank + "\"");
                    }
                    String cipherSuiteString = source.readUtf8LineStrict();
                    CipherSuite cipherSuite = CipherSuite.forJavaName(cipherSuiteString);
                    List<Certificate> peerCertificates = readCertificateList(source);
                    List<Certificate> localCertificates = readCertificateList(source);
                    TlsVersion tlsVersion = !source.exhausted()
                            ? TlsVersion.forJavaName(source.readUtf8LineStrict())
                            : null;
                    handshake = Handshake.get(tlsVersion, cipherSuite, peerCertificates, localCertificates);
                } else {
                    handshake = null;
                }
            } finally {
                in.close();
            }
        }

        public Entry(Response response) {
            this.url = response.request().url().toString();
            this.varyHeaders = HttpHeaders.varyHeaders(response);
            this.requestMethod = response.request().method();
            this.protocol = response.protocol();
            this.code = response.code();
            this.message = response.message();
            this.responseHeaders = response.headers();
            this.handshake = response.handshake();
            this.sentRequestMillis = response.sentRequestAtMillis();
            this.receivedResponseMillis = response.receivedResponseAtMillis();
        }

        public void writeTo(DiskLruCache.Editor editor) throws IOException {
            BufferedSink sink = Okio.buffer(editor.newSink(ENTRY_METADATA));

            sink.writeUtf8(url)
                    .writeByte('\n');
            sink.writeUtf8(requestMethod)
                    .writeByte('\n');
            sink.writeDecimalLong(varyHeaders.size())
                    .writeByte('\n');
            for (int i = 0, size = varyHeaders.size(); i < size; i++) {
                sink.writeUtf8(varyHeaders.name(i))
                        .writeUtf8(": ")
                        .writeUtf8(varyHeaders.value(i))
                        .writeByte('\n');
            }

            sink.writeUtf8(new StatusLine(protocol, code, message).toString())
                    .writeByte('\n');
            sink.writeDecimalLong(responseHeaders.size() + 2)
                    .writeByte('\n');
            for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                sink.writeUtf8(responseHeaders.name(i))
                        .writeUtf8(": ")
                        .writeUtf8(responseHeaders.value(i))
                        .writeByte('\n');
            }
            sink.writeUtf8(SENT_MILLIS)
                    .writeUtf8(": ")
                    .writeDecimalLong(sentRequestMillis)
                    .writeByte('\n');
            sink.writeUtf8(RECEIVED_MILLIS)
                    .writeUtf8(": ")
                    .writeDecimalLong(receivedResponseMillis)
                    .writeByte('\n');

            if (isHttps()) {
                sink.writeByte('\n');
                sink.writeUtf8(handshake.cipherSuite().javaName())
                        .writeByte('\n');
                writeCertList(sink, handshake.peerCertificates());
                writeCertList(sink, handshake.localCertificates());
                // The handshake’s TLS version is null on HttpsURLConnection and on older cached responses.
                if (handshake.tlsVersion() != null) {
                    sink.writeUtf8(handshake.tlsVersion().javaName())
                            .writeByte('\n');
                }
            }
            sink.close();
        }

        private boolean isHttps() {
            return url.startsWith("https://");
        }

        private List<Certificate> readCertificateList(BufferedSource source) throws IOException {
            int length = readInt(source);
            if (length == -1) return Collections.emptyList(); // OkHttp v1.2 used -1 to indicate null.

            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                List<Certificate> result = new ArrayList<>(length);
                for (int i = 0; i < length; i++) {
                    String line = source.readUtf8LineStrict();
                    Buffer bytes = new Buffer();
                    bytes.write(ByteString.decodeBase64(line));
                    result.add(certificateFactory.generateCertificate(bytes.inputStream()));
                }
                return result;
            } catch (CertificateException e) {
                throw new IOException(e.getMessage());
            }
        }

        private void writeCertList(BufferedSink sink, List<Certificate> certificates)
                throws IOException {
            try {
                sink.writeDecimalLong(certificates.size())
                        .writeByte('\n');
                for (int i = 0, size = certificates.size(); i < size; i++) {
                    byte[] bytes = certificates.get(i).getEncoded();
                    String line = ByteString.of(bytes).base64();
                    sink.writeUtf8(line)
                            .writeByte('\n');
                }
            } catch (CertificateEncodingException e) {
                throw new IOException(e.getMessage());
            }
        }

        public boolean matches(Request request, Response response) {
            return url.equals(request.url().toString())
                    && requestMethod.equals(request.method())
                    && HttpHeaders.varyMatches(response, varyHeaders, request);
        }

        public Response response(DiskLruCache.Snapshot snapshot) {
            String contentType = responseHeaders.get("Content-Type");
            String contentLength = responseHeaders.get("Content-Length");
            Request cacheRequest = new Request.Builder()
                    .url(url)
                    .method(requestMethod, null)
                    .headers(varyHeaders)
                    .build();
            return new Response.Builder()
                    .request(cacheRequest)
                    .protocol(protocol)
                    .code(code)
                    .message(message)
                    .headers(responseHeaders)
                    .body(new RequestCache.RequestCacheResponseBody(snapshot, contentType, contentLength))
                    .handshake(handshake)
                    .sentRequestAtMillis(sentRequestMillis)
                    .receivedResponseAtMillis(receivedResponseMillis)
                    .build();
        }
    }

    private static int readInt(BufferedSource source) throws IOException {
        try {
            long result = source.readDecimalLong();
            String line = source.readUtf8LineStrict();
            if (result < 0 || result > Integer.MAX_VALUE || !line.isEmpty()) {
                throw new IOException("expected an int but was \"" + result + line + "\"");
            }
            return (int) result;
        } catch (NumberFormatException e) {
            throw new IOException(e.getMessage());
        }
    }

    private static class CacheResponseBody extends ResponseBody {
        private final DiskLruCache.Snapshot snapshot;
        private final BufferedSource bodySource;
        private final String contentType;
        private final String contentLength;

        public CacheResponseBody(final DiskLruCache.Snapshot snapshot,
                                 String contentType, String contentLength) {
            this.snapshot = snapshot;
            this.contentType = contentType;
            this.contentLength = contentLength;

            Source source = snapshot.getSource(ENTRY_BODY);
            bodySource = Okio.buffer(new ForwardingSource(source) {
                @Override public void close() throws IOException {
                    snapshot.close();
                    super.close();
                }
            });
        }

        @Override public MediaType contentType() {
            return contentType != null ? MediaType.parse(contentType) : null;
        }

        @Override public long contentLength() {
            try {
                return contentLength != null ? Long.parseLong(contentLength) : -1;
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        @Override public BufferedSource source() {
            return bodySource;
        }
    }

    private static class RequestCacheResponseBody extends ResponseBody {
        private final DiskLruCache.Snapshot snapshot;
        private final BufferedSource bodySource;
        private final String contentType;
        private final String contentLength;

        public RequestCacheResponseBody(final DiskLruCache.Snapshot snapshot,
                                 String contentType, String contentLength) {
            this.snapshot = snapshot;
            this.contentType = contentType;
            this.contentLength = contentLength;

            Source source = snapshot.getSource(ENTRY_BODY);
            bodySource = Okio.buffer(new ForwardingSource(source) {
                @Override public void close() throws IOException {
                    snapshot.close();
                    super.close();
                }
            });
        }

        @Override public MediaType contentType() {
            return contentType != null ? MediaType.parse(contentType) : null;
        }

        @Override public long contentLength() {
            try {
                return contentLength != null ? Long.parseLong(contentLength) : -1;
            } catch (NumberFormatException e) {
                return -1;
            }
        }

        @Override public BufferedSource source() {
            return bodySource;
        }
    }

}

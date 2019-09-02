package com.beyond.library.network.net.httpclient.cache;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.internal.Util;

/**
 * Created by linjinfa on 2017/6/8.
 * email 331710168@qq.com
 */
public class HeaderBuilder {

    private final List<String> namesAndValues = new ArrayList<>(20);

    /**
     * Add a header line without any validation. Only appropriate for headers from the remote peer
     * or cache.
     */
    HeaderBuilder addLenient(String line) {
        int index = line.indexOf(":", 1);
        if (index != -1) {
            return addLenient(line.substring(0, index), line.substring(index + 1));
        } else if (line.startsWith(":")) {
            // Work around empty header names and header names that start with a
            // colon (created by old broken SPDY versions of the response cache).
            return addLenient("", line.substring(1)); // Empty header name.
        } else {
            return addLenient("", line); // No header name.
        }
    }

    /** Add an header line containing a field name, a literal colon, and a value. */
    public HeaderBuilder add(String line) {
        int index = line.indexOf(":");
        if (index == -1) {
            throw new IllegalArgumentException("Unexpected header: " + line);
        }
        return add(line.substring(0, index).trim(), line.substring(index + 1));
    }

    /** Add a field with the specified value. */
    public HeaderBuilder add(String name, String value) {
        checkNameAndValue(name, value);
        return addLenient(name, value);
    }

    /**
     * Add a field with the specified value without any validation. Only appropriate for headers
     * from the remote peer or cache.
     */
    HeaderBuilder addLenient(String name, String value) {
        namesAndValues.add(name);
        namesAndValues.add(value.trim());
        return this;
    }

    public HeaderBuilder removeAll(String name) {
        for (int i = 0; i < namesAndValues.size(); i += 2) {
            if (name.equalsIgnoreCase(namesAndValues.get(i))) {
                namesAndValues.remove(i); // name
                namesAndValues.remove(i); // value
                i -= 2;
            }
        }
        return this;
    }

    /**
     * Set a field with the specified value. If the field is not found, it is added. If the field is
     * found, the existing values are replaced.
     */
    public HeaderBuilder set(String name, String value) {
        checkNameAndValue(name, value);
        removeAll(name);
        addLenient(name, value);
        return this;
    }

    private void checkNameAndValue(String name, String value) {
        if (name == null) throw new NullPointerException("name == null");
        if (name.isEmpty()) throw new IllegalArgumentException("name is empty");
        for (int i = 0, length = name.length(); i < length; i++) {
            char c = name.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                throw new IllegalArgumentException(Util.format(
                        "Unexpected char %#04x at %d in header name: %s", (int) c, i, name));
            }
        }
        if (value == null) throw new NullPointerException("value == null");
        for (int i = 0, length = value.length(); i < length; i++) {
            char c = value.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                throw new IllegalArgumentException(Util.format(
                        "Unexpected char %#04x at %d in %s value: %s", (int) c, i, name, value));
            }
        }
    }

    /** Equivalent to {@code build().get(name)}, but potentially faster. */
    public String get(String name) {
        for (int i = namesAndValues.size() - 2; i >= 0; i -= 2) {
            if (name.equalsIgnoreCase(namesAndValues.get(i))) {
                return namesAndValues.get(i + 1);
            }
        }
        return null;
    }

    public Headers build() {
        return Headers.of(namesAndValues.toArray(new String[namesAndValues.size()]));
    }

}

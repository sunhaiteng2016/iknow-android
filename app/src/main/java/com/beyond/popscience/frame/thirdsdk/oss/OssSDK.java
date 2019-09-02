package com.beyond.popscience.frame.thirdsdk.oss;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.beyond.library.util.L;
import com.beyond.popscience.frame.thirdsdk.ThirdSDKManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * OSS
 * Created by linjinfa on 2017/6/30.
 * email 331710168@qq.com
 */
public class OssSDK {

    private String bucketName = "ikow";
    private OSS oss;
    private Context context;
    /**
     * 存放成功的map path-->上传之后的url
     */
    private Map<String, String> successMap = new HashMap<>();
    /**
     * 失败的路径
     */
    private List<String> failurePathList = new ArrayList<>();
    /**
     * 需要上传的路径
     */
    private List<String> needUploadList = new ArrayList<>();
    /**
     *
     */
    private ThirdSDKManager.IUploadCallback iUploadCallback;
    /**
     *
     */
    private String errorCode;
    /**
     *
     */
    private String errorMsg;

    public OssSDK(Context context) {
        this.context = context;
        init();
    }

    /**
     * 初始化  OSS阿里云智能存储
     */
    private void init() {
        String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
        String accessKeyId = "LTAICGqfkFo5h33o";
        String accessKeySecret = "PfbU4cLZdk9PGJafNFhx1Q7XHL3WW6";
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);

        ClientConfiguration conf = new ClientConfiguration();
//        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
//        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(10); // 最大并发请求书，默认5个
//        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        oss = new OSSClient(context, endpoint, credentialProvider, conf);
    }

    /**
     * 上传图片文件
     */
    public void uploadImage(List<String> pathList, ThirdSDKManager.IUploadCallback iUploadCallback) {
        if (pathList == null || pathList.size() == 0) {
            return;
        }
        needUploadList.clear();
        needUploadList.addAll(pathList);

        this.iUploadCallback = iUploadCallback;
        successMap.clear();
        failurePathList.clear();

        for (String path : pathList) {
            File file = new File(path);
            String extensionName = getExtensionName(file.getName());
            String objectKey = "images/" + UUID.randomUUID() + "." + extensionName;
            // 构造上传请求
            PutObjectRequest put = new PutObjectRequest(bucketName, objectKey, path);
            OSSAsyncTask task = oss.asyncPutObject(put, new EXOSSCompletedCallback(path) {

                @Override
                public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult, Object targetObj) {
                    String url = "http://" + bucketName + ".oss-cn-shanghai.aliyuncs.com/" + putObjectRequest.getObjectKey();
                    successMap.put(targetObj.toString(), url);
                    callBack();
                }

                @Override
                public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1, Object targetObj) {
                    failurePathList.add(targetObj.toString());
                    // 请求异常
                    if (e != null) {
                        // 本地异常如网络异常等
                        e.printStackTrace();
                    }
                    if (e1 != null) {
                        // 服务异常
                        errorCode = e1.getErrorCode();
                        errorMsg = e1.getRawMessage();
                        L.v("ErrorCode============>" + e1.getErrorCode());
                        L.v("RequestId===========>" + e1.getRequestId());
                        L.v("HostId=============>" + e1.getHostId());
                        L.v("RawMessage===========>" + e1.getRawMessage());
                    }
                    callBack();
                }
            });
        }
    }

    /**
     * 回调
     */
    private void callBack() {
        if (iUploadCallback != null) {
            if (needUploadList.size() == (successMap.size() + failurePathList.size())) {    //上传结束
                if (successMap.size() > 0) {
                    iUploadCallback.onSuccess(successMap, failurePathList);
                } else {
                    iUploadCallback.onFailure(failurePathList, errorCode, errorMsg);
                }
            } /*else {
                iUploadCallback.onFailure(failurePathList, errorCode, errorMsg);
            }*/
        }
    }

    /**
     * @param iUploadCallback
     */
    public void setiUploadCallback(ThirdSDKManager.IUploadCallback iUploadCallback) {
        this.iUploadCallback = iUploadCallback;
    }

    /**
     * 获取文件扩展名
     */
    private String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     *
     */
    class EXOSSCompletedCallback implements OSSCompletedCallback<PutObjectRequest, PutObjectResult> {

        private Object targetObj;

        public EXOSSCompletedCallback(Object targetObj) {
            this.targetObj = targetObj;
        }

        @Override
        public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
            onSuccess(putObjectRequest, putObjectResult, targetObj);
        }

        public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult, Object targetObj) {

        }

        @Override
        public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1) {
            onFailure(putObjectRequest, e, e1, targetObj);
        }

        public void onFailure(PutObjectRequest putObjectRequest, ClientException e, ServiceException e1, Object targetObj) {

        }
    }

}

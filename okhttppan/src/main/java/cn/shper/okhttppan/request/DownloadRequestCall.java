package cn.shper.okhttppan.request;

import java.util.concurrent.TimeUnit;

import cn.shper.okhttppan.OkHttpRequest;
import cn.shper.okhttppan.callback.DownloadCallback;
import cn.shper.okhttppan.utils.Logger;
import okhttp3.OkHttpClient;

/**
 * Author shper
 * Description TODO
 * Version 0.1 16-6-12 C 创建
 */
public class DownloadRequestCall extends BaseRequestCall {

    private OkHttpClient downloadClient;

    public DownloadRequestCall(BaseRequest request) {
        super(request);
    }

    private void buildCall() {
        Logger.d("[TimeoutConfig - " + baseRequest.getRequestMethod() + "]" +
                " ConnectTimeout: " + baseRequest.getConnectTimeout() +
                " ReadTimeout: " + baseRequest.getReadTimeout() +
                " WriteTimeout: " + baseRequest.getWriteTimeout());
        request = baseRequest.buildRequest(null);
        downloadClient = OkHttpRequest.getInstance().getClient()
                .newBuilder()
                .connectTimeout(baseRequest.getConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(baseRequest.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(baseRequest.getWriteTimeout(), TimeUnit.SECONDS)
                .build();
        call = downloadClient.newCall(request);
    }

    public void execute(DownloadCallback callback) {
        buildCall();
        if (callback != null) {
            callback.onStart(request, getOkHttpRequest().getRequestId());
        }

        OkHttpRequest.getInstance().execute(this, callback);
    }

}

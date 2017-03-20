package cn.shper.okhttppan.requestcall;

import java.util.concurrent.TimeUnit;

import cn.shper.okhttppan.OkHttpPan;
import cn.shper.okhttppan.callback.DownloadCallback;
import cn.shper.okhttppan.request.BaseRequest;
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
        request = baseRequest.getRequest(null);
        downloadClient = OkHttpPan.getInstance().getClient()
                .newBuilder()
                .connectTimeout(baseRequest.getConnectTimeout(), TimeUnit.SECONDS)
                .readTimeout(baseRequest.getReadTimeout(), TimeUnit.SECONDS)
                .writeTimeout(baseRequest.getWriteTimeout(), TimeUnit.SECONDS)
                .build();
        call = downloadClient.newCall(request);
    }

    public void enqueue(DownloadCallback callback) {
        buildCall();
        if (callback != null) {
            callback.onStart(request);
        }

        OkHttpPan.getInstance().enqueue(this, callback);
    }

}

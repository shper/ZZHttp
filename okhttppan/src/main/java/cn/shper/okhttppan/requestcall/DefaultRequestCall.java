package cn.shper.okhttppan.requestcall;

import java.util.concurrent.TimeUnit;

import cn.shper.okhttppan.OkHttpPan;
import cn.shper.okhttppan.callback.HttpCallback;
import cn.shper.okhttppan.constant.HttpConstants;
import cn.shper.okhttppan.request.BaseRequest;
import cn.shper.okhttppan.utils.Logger;
import okhttp3.OkHttpClient;

/**
 * Author shper
 * Description 请求Call
 * Version 0.1 16-6-7 C 创建
 */
public class DefaultRequestCall extends BaseRequestCall {

    public DefaultRequestCall(BaseRequest request) {
        super(request);
    }

    private void buildCall() {
        request = baseRequest.getRequest(null);
        // 如果自定义了 Timeout 设置新的 Client
        if (baseRequest.getConnectTimeout() > 0 || baseRequest.getReadTimeout() > 0 || baseRequest.getWriteTimeout() > 0) {
            OkHttpClient newClient = OkHttpPan.getInstance().getClient()
                    .newBuilder()
                    .connectTimeout(baseRequest.getConnectTimeout() == 0 ? HttpConstants.Timeout.DEFAULT_CONNECT : baseRequest.getConnectTimeout(), TimeUnit.SECONDS)
                    .readTimeout(baseRequest.getReadTimeout() == 0 ? HttpConstants.Timeout.DEFAULT_READ : baseRequest.getReadTimeout(), TimeUnit.SECONDS)
                    .writeTimeout(baseRequest.getWriteTimeout() == 0 ? HttpConstants.Timeout.DEFAULT_WRITE : baseRequest.getWriteTimeout(), TimeUnit.SECONDS)
                    .build();
            // 如果自定义了 timeout
            Logger.d("[TimeoutConfig - " + baseRequest.getRequestMethod() + "]" +
                    " ConnectTimeout: " + newClient.connectTimeoutMillis() / 1000 +
                    " ReadTimeout: " + newClient.readTimeoutMillis() / 1000 +
                    " WriteTimeout: " + newClient.writeTimeoutMillis() / 1000);
            call = newClient.newCall(request);
        } else {
            call = OkHttpPan.getInstance().getClient().newCall(request);
        }
    }

    public <T> void execute(Class<T> clazz, HttpCallback callback) {
        buildCall();
        if (callback != null) {
            callback.onBefore(request);
        }

        OkHttpPan.getInstance().execute(this, clazz, callback);
    }
}

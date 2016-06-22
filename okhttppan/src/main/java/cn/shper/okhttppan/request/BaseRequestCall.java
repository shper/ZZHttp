package cn.shper.okhttppan.request;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Author shper
 * Version 0.1 16-6-7 C 创建
 */
public class BaseRequestCall {

    protected BaseRequest baseRequest;
    protected Request request;
    protected Call call;

    public BaseRequestCall(BaseRequest request) {
        this.baseRequest = request;
    }

    public Call getCall() {
        return call;
    }

    public Request getRequest() {
        return request;
    }

    public BaseRequest getOkHttpRequest() {
        return baseRequest;
    }

    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }
}

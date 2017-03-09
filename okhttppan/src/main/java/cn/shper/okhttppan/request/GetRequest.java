package cn.shper.okhttppan.request;

import cn.shper.okhttppan.callback.BaseCallback;
import cn.shper.okhttppan.constant.HttpConstants;
import cn.shper.okhttppan.requestcall.DefaultRequestCall;
import okhttp3.Request;

/**
 * Author shper
 * Description get 请求类
 * Version 0.1 16-6-7 C 创建
 */
public class GetRequest extends BaseRequest<GetRequest, DefaultRequestCall> {

    public GetRequest() {
        requestMethod = HttpConstants.Method.GET;
    }

    @Override
    public Request getRequest(BaseCallback callback) {
        return builder.get().build();
    }

}

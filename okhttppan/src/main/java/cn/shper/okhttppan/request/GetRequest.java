package cn.shper.okhttppan.request;

import cn.shper.okhttppan.callback.BaseCallback;
import okhttp3.Request;

/**
 * Author shper
 * Description get 请求类
 * Version 0.1 16-6-7 C 创建
 */
public class GetRequest extends BaseRequest {

    @Override
    Request buildRequest(BaseCallback callback) {
        return builder.get().build();
    }

}

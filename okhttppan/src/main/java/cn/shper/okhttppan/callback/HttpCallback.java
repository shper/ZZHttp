package cn.shper.okhttppan.callback;

import cn.shper.okhttppan.constant.HttpError;
import okhttp3.Request;

/**
 * Author shper
 * Description TODO
 * Version 0.1 16-6-7 C 创建
 */
public abstract class HttpCallback<T> extends BaseCallback {

    public void onBefore(Request request, int requestId) {
    }

    public abstract void onSuccess(T obj, int requestId);

    /**
     * 默认 Callback
     */
    public static HttpCallback DEFAULT = new HttpCallback() {
        @Override
        public void onSuccess(Object obj, int requestId) {
        }

        @Override
        public void onFail(HttpError error, int requestId) {
        }
    };

}

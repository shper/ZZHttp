package cn.shper.okhttppan.callback;

import cn.shper.okhttppan.exception.HttpError;
import okhttp3.Request;

/**
 * Author: Shper
 * Description: TODO
 * Version: 0.1 16-6-7 C 创建
 */
public abstract class HttpCallback<T> extends BaseCallback {

    public void onBefore(Request request) {
    }

    public abstract void onSuccess(T obj);

    /**
     * 默认 Callback
     */
    public static HttpCallback DEFAULT = new HttpCallback() {
        @Override
        public void onSuccess(Object obj) {
        }

        @Override
        public void onFail(HttpError error) {
        }
    };

}

package cn.shper.okhttppan.callback;

import cn.shper.okhttppan.exception.HttpError;
import okhttp3.Request;

/**
 * Author shper
 * Description
 * Version 0.1 16-6-11 C 创建
 */
public abstract class UploadCallback<T> extends BaseCallback {

    public abstract void onStart(Request request);

    public abstract void inProgress(float progress, long current, long total);

    public abstract void onComplete(T obj);

    /**
     * 默认 Callback
     */
    public static UploadCallback DEFAULT = new UploadCallback() {

        @Override
        public void onStart(Request request) {
        }

        @Override
        public void inProgress(float progress, long current, long total) {
        }

        @Override
        public void onComplete(Object obj) {
        }

        @Override
        public void onFail(HttpError error) {
        }
    };

}

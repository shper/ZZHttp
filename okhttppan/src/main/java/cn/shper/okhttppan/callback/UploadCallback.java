package cn.shper.okhttppan.callback;

import cn.shper.okhttppan.constant.HttpError;
import okhttp3.Request;

/**
 * Author shper
 * Description
 * Version 0.1 16-6-11 C 创建
 */
public abstract class UploadCallback<T> extends BaseCallback {

    public abstract void onStart(Request request, int requestId);

    public abstract void inProgress(float progress, long current, long total, int requestId);

    public abstract void onComplete(T obj, int requestId);

    /**
     * 默认 Callback
     */
    public static UploadCallback DEFAULT = new UploadCallback() {

        @Override
        public void onStart(Request request, int requestId) {
        }

        @Override
        public void inProgress(float progress, long current, long total, int requestId) {
        }

        @Override
        public void onComplete(Object obj, int requestId) {
        }

        @Override
        public void onFail(HttpError error, int requestId) {
        }
    };

}

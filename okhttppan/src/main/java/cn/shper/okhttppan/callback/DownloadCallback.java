package cn.shper.okhttppan.callback;

import java.io.File;

import cn.shper.okhttppan.exception.HttpError;
import okhttp3.Request;

/**
 * Author: Shper
 * Description: TODO
 * Version: 0.1 16-6-11 C 创建
 */
public abstract class DownloadCallback extends BaseCallback {

    public abstract void onStart(Request request);

    public abstract void inProgress(float progress, long current, long total);

    public abstract void onComplete(File file);

    /**
     * 默认 Callback
     */
    public static DownloadCallback DEFAULT = new DownloadCallback() {

        @Override
        public void onStart(Request request) {
        }

        @Override
        public void inProgress(float progress, long current, long total) {
        }

        @Override
        public void onComplete(File file) {
        }

        @Override
        public void onFail(HttpError error) {
        }
    };

}

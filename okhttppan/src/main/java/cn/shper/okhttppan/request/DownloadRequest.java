package cn.shper.okhttppan.request;

import android.text.TextUtils;

import cn.shper.okhttppan.callback.BaseCallback;
import cn.shper.okhttppan.constant.HttpConstants;
import cn.shper.okhttppan.exception.HttpRequestException;
import cn.shper.okhttppan.requestcall.DownloadRequestCall;
import okhttp3.Request;

/**
 * Author shper
 * Description TODO
 * Version 0.1 16-6-11 C 创建
 */
public class DownloadRequest extends BaseRequest<DownloadRequest, DownloadRequestCall> {

    protected String savePath;
    protected String saveFileName;

    public DownloadRequest() {
        requestMethod = HttpConstants.Method.DOWNLOAD;
        connectTimeout = HttpConstants.Timeout.DOWNLOAD_CONNECT;
        readTimeout = HttpConstants.Timeout.DOWNLOAD_READ;
        writeTimeout = HttpConstants.Timeout.DOWNLOAD_WRITE;
    }

    @Override
    public Request getRequest(BaseCallback callback) {
        return builder.get().build();
    }

    public DownloadRequest savePath(String savePath) {
        if (TextUtils.isEmpty(savePath)) {
            throw new HttpRequestException("The SavePath can't be empty!!!");
        }

        this.savePath = savePath;
        return this;
    }

    public DownloadRequest saveFileName(String saveFileName) {
        if (TextUtils.isEmpty(saveFileName)) {
            throw new HttpRequestException("The SaveFileName can't be empty!!!");
        }

        this.saveFileName = saveFileName;
        return this;
    }

}

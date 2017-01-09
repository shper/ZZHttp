package cn.shper.okhttppan.builder;

import android.text.TextUtils;

import cn.shper.okhttppan.BuildConfig;
import cn.shper.okhttppan.constant.HttpConstants;
import cn.shper.okhttppan.request.DownloadRequestCall;
import cn.shper.okhttppan.request.GetRequest;
import cn.shper.okhttppan.utils.Logger;

/**
 * Author shper
 * Description TODO
 * Version 0.1 16-6-11 C 创建
 */
public class DownloadBuilder extends BaseBuilder<DownloadBuilder> {

    protected String savePath;
    protected String saveFileName;

    public DownloadBuilder(String method) {
        requestMethod = method;
        connectTimeout = HttpConstants.Timeout.DOWNLOAD_CONNECT;
        readTimeout = HttpConstants.Timeout.DOWNLOAD_READ;
        writeTimeout = HttpConstants.Timeout.DOWNLOAD_WRITE;
    }

    @Override
    public DownloadRequestCall build() {
        // 检测 下载地址、存储地址、文件名 是否为空
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("url can't be empty!!!");
        }
        if (TextUtils.isEmpty(savePath)) {
            throw new NullPointerException("savePath can't be empty!!!");
        }
        if (TextUtils.isEmpty(saveFileName)) {
            throw new NullPointerException("saveFileName can't be empty!!!");
        }

        // 打印日志
        printLog("DOWNLOAD");
        if (params != null) {
            url = appendParams(url, params);
            // 打印拼接后的 URL
            if (BuildConfig.DEBUG) {
                Logger.d("DOWNLOAD - applyUrl: " + url);
            }
        }

        return (DownloadRequestCall) new GetRequest()
                .requestMethod(requestMethod)
                .url(url)
                .headers(headers)
                .params(params)
                .savePath(savePath)
                .saveFileName(saveFileName)
                .connectTimeout(connectTimeout)
                .writeTimeout(writeTimeout)
                .readTimeout(readTimeout)
                .tag(tag)
                .build();
    }

    public DownloadBuilder savePath(String savePath) {
        if (TextUtils.isEmpty(savePath)) {
            throw new NullPointerException("savePath can't be empty!!!");
        } else {
            this.savePath = savePath;
            return this;
        }
    }

    public DownloadBuilder saveFileName(String saveFileName) {
        if (TextUtils.isEmpty(saveFileName)) {
            throw new NullPointerException("fileName can't be empty!!!");
        } else {
            this.saveFileName = saveFileName;
            return this;
        }
    }

}

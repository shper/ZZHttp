package cn.shper.okhttppan.builder;

import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.shper.okhttppan.constant.HttpConstants;
import cn.shper.okhttppan.entity.FileInput;
import cn.shper.okhttppan.request.UpLoadRequest;
import cn.shper.okhttppan.request.UploadRequestCall;

/**
 * Author shper
 * Description TODO
 * Version 0.1 16-6-11 C 创建
 */
public class UploadBuilder extends BaseBuilder<UploadBuilder> {

    private List<FileInput> files = new ArrayList<>();

    public UploadBuilder(String method) {
        requestMethod = method;
        connectTimeout = HttpConstants.Timeout.UPLOAD_CONNECT;
        readTimeout = HttpConstants.Timeout.UPLOAD_READ;
        writeTimeout = HttpConstants.Timeout.UPLOAD_WRITE;
    }

    @Override
    public UploadRequestCall build() {
        // 检测 下载地址、存储地址、文件名 是否为空
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("url can't be empty!!!");
        }

        printLog("UPLOAD");
        return (UploadRequestCall) new UpLoadRequest()
                .requestMethod(requestMethod)
                .url(url)
                .headers(headers)
                .params(params)
                .files(files)
                .jsonStatusKey(jsonStatusKey)
                .jsonStatusSuccessValue(jsonStatusSuccessValue)
                .jsonDataKey(jsonDataKey)
                .connectTimeout(connectTimeout)
                .writeTimeout(writeTimeout)
                .readTimeout(readTimeout)
                .tag(tag)
                .build();
    }

    public UploadBuilder files(String key, Map<String, File> files) {
        for (String filename : files.keySet()) {
            this.files.add(new FileInput(key, filename, files.get(filename)));
        }
        return this;
    }

    public UploadBuilder addFile(String name, String filename, File file) {
        files.add(new FileInput(name, filename, file));
        return this;
    }

}

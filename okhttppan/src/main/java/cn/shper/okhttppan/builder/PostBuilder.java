package cn.shper.okhttppan.builder;

import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.shper.okhttppan.entity.FileInput;
import cn.shper.okhttppan.request.DefaultRequestCall;
import cn.shper.okhttppan.request.PostRequest;

/**
 * Author shper
 * Description TODO
 * Version 0.1 16-6-8 C 创建
 */
public class PostBuilder extends BaseBuilder<PostBuilder> {

    private List<FileInput> files = new ArrayList<>();

    public PostBuilder(String method) {
        requestMethod = method;
    }

    @Override
    public DefaultRequestCall build() {
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("url can't be empty!!!");
        }

        printLog("POST");
        return (DefaultRequestCall) new PostRequest()
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
                .requestId(requestId)
                .tag(tag)
                .build();
    }

    public PostBuilder files(String key, Map<String, File> files) {
        for (String filename : files.keySet()) {
            this.files.add(new FileInput(key, filename, files.get(filename)));
        }
        return this;
    }

    public PostBuilder addFile(String name, String filename, File file) {
        files.add(new FileInput(name, filename, file));
        return this;
    }

}

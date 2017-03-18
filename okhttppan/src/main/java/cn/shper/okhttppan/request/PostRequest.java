package cn.shper.okhttppan.request;

import android.text.TextUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.shper.okhttppan.callback.BaseCallback;
import cn.shper.okhttppan.constant.HttpConstants;
import cn.shper.okhttppan.entity.FileInput;
import cn.shper.okhttppan.requestcall.DefaultRequestCall;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Author shper
 * Description Post 请求类
 * Version 0.1 16-6-8 C 创建
 */
public class PostRequest extends BaseRequest<PostRequest, DefaultRequestCall> {

    private HashMap<String,String> bodys;
    protected List<FileInput> files;

    public PostRequest() {
        requestMethod = HttpConstants.Method.POST;
    }

    @Override
    public Request getRequest(BaseCallback callback) {
        return builder.post(buildRequestBody()).build();
    }

    public PostRequest body(String key,String value){
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return this;
        }

        if (null == this.bodys) {
            this.bodys = new HashMap<>();
        }

        this.bodys.put(key, value);
        return this;
    }

    public PostRequest bodys(HashMap<String,String> bodys){
        if (null == bodys || bodys.isEmpty()) {
            return this;
        }

        if (null == this.bodys) {
            this.bodys = new HashMap<>();
        }

        this.bodys.putAll(bodys);
        return this;
    }

    public PostRequest files(String key, Map<String, File> files) {
        for (String filename : files.keySet()) {
            this.files.add(new FileInput(key, filename, files.get(filename)));
        }
        return this;
    }

    public PostRequest addFile(String name, String filename, File file) {
        files.add(new FileInput(name, filename, file));
        return this;
    }

    private RequestBody buildRequestBody() {
        if (files == null || files.isEmpty()) {
            FormBody.Builder builder = new FormBody.Builder();
            addBodys(builder);
            return builder.build();
        } else {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            addBodys(builder);

            for (int i = 0; i < files.size(); i++) {
                FileInput fileInput = files.get(i);
                RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileInput.filename)), fileInput.file);
                builder.addFormDataPart(fileInput.key, fileInput.filename, fileBody);
            }
            return builder.build();
        }
    }

    private void addBodys(MultipartBody.Builder builder) {
        if (bodys != null && !bodys.isEmpty()) {
            for (String key : bodys.keySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, String.valueOf(bodys.get(key))));
            }
        }
    }

    private void addBodys(FormBody.Builder builder) {
        if (bodys != null && !bodys.isEmpty()) {
            for (String key : bodys.keySet()) {
                builder.add(key, String.valueOf(bodys.get(key)));
            }
        }
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;
        try {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

}

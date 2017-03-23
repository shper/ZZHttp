package cn.shper.okhttppan.request;

import android.text.TextUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import cn.shper.okhttppan.callback.BaseCallback;
import cn.shper.okhttppan.constant.HttpConstants;
import cn.shper.okhttppan.entity.FileInput;
import cn.shper.okhttppan.exception.HttpRequestException;
import cn.shper.okhttppan.requestcall.UploadRequestCall;
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
public class UpLoadRequest extends BaseRequest<UpLoadRequest, UploadRequestCall> {

    private List<FileInput> files;

    public UpLoadRequest() {
        requestMethod = HttpConstants.Method.UPLOAD;
        connectTimeout = HttpConstants.Timeout.UPLOAD_CONNECT;
        readTimeout = HttpConstants.Timeout.UPLOAD_READ;
        writeTimeout = HttpConstants.Timeout.UPLOAD_WRITE;
    }

    public UpLoadRequest files(String key, Map<String, File> files) {
        if (TextUtils.isEmpty(key) || null == files || files.isEmpty()) {
            throw new HttpRequestException("The files are can't be empty!!!");
        }

        for (String filename : files.keySet()) {
            this.files.add(new FileInput(key, filename, files.get(filename)));
        }

        return this;
    }

    public UpLoadRequest addFile(String name, String fileName, File file) {

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(fileName) || null == file) {
            throw new HttpRequestException("The name or fileName or file can't be empty!!!");
        }

        files.add(new FileInput(name, fileName, file));
        return this;
    }

    @Override
    public Request getRequest(BaseCallback callback) {
        return builder.post(new UploadRequestBody(buildRequestBody(), callback)).build();
    }

    private RequestBody buildRequestBody() {
        if (null == files || files.isEmpty()) {
            FormBody.Builder builder = new FormBody.Builder();
            addParams(builder);
            return builder.build();
        } else {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            addParams(builder);

            for (int i = 0; i < files.size(); i++) {
                FileInput fileInput = files.get(i);
                RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileInput.filename)), fileInput.file);
                builder.addFormDataPart(fileInput.key, fileInput.filename, fileBody);
            }
            return builder.build();
        }
    }

    private void addParams(MultipartBody.Builder builder) {
        if (null != params && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, String.valueOf(params.get(key))));
            }
        }
    }

    private void addParams(FormBody.Builder builder) {
        if (null != params && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.add(key, String.valueOf(params.get(key)));
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

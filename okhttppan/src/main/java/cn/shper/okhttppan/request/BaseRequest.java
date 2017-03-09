package cn.shper.okhttppan.request;

import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Set;

import cn.shper.okhttppan.callback.BaseCallback;
import cn.shper.okhttppan.constant.HttpConstants;
import cn.shper.okhttppan.requestcall.BaseRequestCall;
import cn.shper.okhttppan.requestcall.DefaultRequestCall;
import cn.shper.okhttppan.requestcall.DownloadRequestCall;
import cn.shper.okhttppan.requestcall.UploadRequestCall;
import cn.shper.okhttppan.utils.Logger;
import okhttp3.Headers;
import okhttp3.Request;

/**
 * Author shper
 * Description 基础请求类
 * Version 0.1 16-6-7 C 创建
 */
public abstract class BaseRequest<T extends BaseRequest,R extends BaseRequestCall> {

    protected String requestMethod;

    protected String url;
    protected Object tag;
    protected HashMap<String, String> params;
    protected HashMap<String, String> headers;

    protected String jsonStatusKey;
    protected String jsonStatusSuccessValue;
    protected String jsonDataKey;
    protected Boolean forceLogin;

    protected String savePath;
    protected String saveFileName;

    protected int connectTimeout;
    protected int readTimeout;
    protected int writeTimeout;

    protected Request.Builder builder = new Request.Builder();

    public T requestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
        return (T) this;
    }

    public T url(String url) {
        this.url = url;
        return (T) this;
    }

    public T headers(HashMap<String, String> headers) {
        this.headers = headers;
        return (T) this;
    }

    public T params(HashMap<String, String> params) {
        this.params = params;
        return (T) this;
    }

    public T jsonStatusKey(String jsonStatusKey) {
        this.jsonStatusKey = jsonStatusKey;
        return (T) this;
    }

    public T jsonStatusSuccessValue(String jsonStatusSuccessValue) {
        this.jsonStatusSuccessValue = jsonStatusSuccessValue;
        return (T) this;
    }

    public T jsonDataKey(String jsonDataKey) {
        this.jsonDataKey = jsonDataKey;
        return (T) this;
    }

    public T forceLogin(Boolean forceLogin) {
        this.forceLogin = forceLogin;
        return (T) this;
    }

    public T tag(Object tag) {
        this.tag = tag;
        return (T) this;
    }

    public T savePath(String savePath) {
        this.savePath = savePath;
        return (T) this;
    }

    public T saveFileName(String saveFileName) {
        this.saveFileName = saveFileName;
        return (T) this;
    }

    public T connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return (T) this;
    }

    public T readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return (T) this;
    }

    public T writeTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
        return (T) this;
    }

    public R build() {
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("url can't be empty!!!");
        }

        if (HttpConstants.Method.DOWNLOAD.equals(requestMethod)) {
            if (TextUtils.isEmpty(savePath)) {
                throw new NullPointerException("savePath can't be empty!!!");
            }
            if (TextUtils.isEmpty(saveFileName)) {
                throw new NullPointerException("saveFileName can't be empty!!!");
            }
        }

        // 打印日志
        printLog(requestMethod);
        if (params != null) {
            url = appendParams(url, params);
            // 打印拼接后的 URL
            Logger.d("applyUrl: " + url);
        }

        builder.url(url).tag(tag);
        appendHeaders();

        switch (requestMethod) {
            case HttpConstants.Method.GET:
            case HttpConstants.Method.POST:
                return (R) new DefaultRequestCall(this);
            case HttpConstants.Method.DOWNLOAD:
                return (R) new DownloadRequestCall(this);
            case HttpConstants.Method.UPLOAD:
                return (R) new UploadRequestCall(this);
        }
        return (R) new DefaultRequestCall(this);
    }

    /**
     * 添加头部
     */
    private void appendHeaders() {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers == null || headers.isEmpty()) {
            return;
        }
        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }

    /**
     * 拼接参数
     */
    String appendParams(String url, HashMap<String, String> params) {
        if (TextUtils.isEmpty(url) || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }

    // debug 的时候打印日志
    void printLog(String tag) {
        Logger.i(tag + " - url: " + url);
        if (params != null && params.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            for (String key : params.keySet()) {
                buffer.append(key + " : " + params.get(key) + ", ");
            }
            Logger.i(tag + " - params: " + buffer.toString());
        }
        if (headers != null && headers.size() > 0) {
            StringBuffer buffer = new StringBuffer();
            for (String key : headers.keySet()) {
                buffer.append(key + " : " + headers.get(key) + ", ");
            }
            Logger.i(tag + " - headers: " + buffer.toString());
        }
    }

    /**
     * 创建 Request
     *
     * @param callback
     */
    public abstract Request getRequest(BaseCallback callback);

    public String getJsonStatusKey() {
        return jsonStatusKey;
    }

    public String getJsonStatusSuccessValue() {
        return jsonStatusSuccessValue;
    }

    public String getJsonDataKey() {
        return jsonDataKey;
    }

    public Boolean getForceLogin() {
        return forceLogin;
    }

    public String getSavePath() {
        return savePath;
    }

    public String getSaveFileName() {
        return saveFileName;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

}

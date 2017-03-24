package cn.shper.okhttppan.request;

import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Set;

import cn.shper.okhttppan.callback.BaseCallback;
import cn.shper.okhttppan.config.ResponseParser;
import cn.shper.okhttppan.constant.HttpConstants;
import cn.shper.okhttppan.exception.HttpRequestException;
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
public abstract class BaseRequest<T extends BaseRequest, R extends BaseRequestCall> {

    protected String requestMethod;
    // 自定义 Response 解析器
    protected ResponseParser requestResponseParser;

    protected String url;
    protected Object tag;
    protected HashMap<String, String> params;
    protected HashMap<String, String> headers;

    protected Boolean forceLogin;

    protected String savePath;
    protected String saveFileName;

    protected int connectTimeout;
    protected int readTimeout;
    protected int writeTimeout;

    protected Request.Builder builder = new Request.Builder();

    public T responseParser(ResponseParser responseParser) {
        if (null == responseParser) {
            throw new HttpRequestException("The responseParser can't be empty!!!");
        }

        this.requestResponseParser = responseParser;
        return (T) this;
    }

    public T requestMethod(String requestMethod) {
        if (TextUtils.isEmpty(requestMethod)) {
            return (T) this;
        }

        this.requestMethod = requestMethod;
        return (T) this;
    }

    public T url(String url) {
        if (TextUtils.isEmpty(url)) {
            throw new HttpRequestException("The URL can't be empty!!!");
        }

        this.url = url;
        return (T) this;
    }

    public T header(String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return (T) this;
        }

        if (null == this.headers) {
            this.headers = new HashMap<>();
        }

        this.headers.put(key, value);
        return (T) this;
    }

    public T headers(HashMap<String, String> headers) {
        if (null == headers || headers.isEmpty()) {
            return (T) this;
        }

        if (null == this.headers) {
            this.headers = new HashMap<>();
        }

        this.headers.putAll(headers);
        return (T) this;
    }

    public T param(String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return (T) this;
        }

        if (null == this.params) {
            this.params = new HashMap<>();
        }

        this.params.put(key, value);
        return (T) this;
    }

    public T params(HashMap<String, String> params) {
        if (null == params || params.isEmpty()) {
            return (T) this;
        }

        if (null == this.params) {
            this.params = new HashMap<>();
        }

        this.params.putAll(params);
        return (T) this;
    }

    public T forceLogin(Boolean forceLogin) {
        this.forceLogin = forceLogin;
        return (T) this;
    }

    public T tag(Object tag) {
        if (null == tag) {
            throw new HttpRequestException("The Tag can't be empty!!!");
        }

        this.tag = tag;
        return (T) this;
    }

    public T connectTimeout(int connectTimeout) {
        if (connectTimeout < 1) {
            return (T) this;
        }

        this.connectTimeout = connectTimeout;
        return (T) this;
    }

    public T readTimeout(int readTimeout) {
        if (readTimeout < 1) {
            return (T) this;
        }

        this.readTimeout = readTimeout;
        return (T) this;
    }

    public T writeTimeout(int writeTimeout) {
        if (writeTimeout < 1) {
            return (T) this;
        }

        this.writeTimeout = writeTimeout;
        return (T) this;
    }

    public R build() {
        if (TextUtils.isEmpty(url)) {
            throw new HttpRequestException("The URL can't be empty!!!");
        }

        if (HttpConstants.Method.DOWNLOAD.equals(requestMethod)) {
            if (TextUtils.isEmpty(savePath)) {
                throw new HttpRequestException("The savePath can't be empty!!!");
            }
            if (TextUtils.isEmpty(saveFileName)) {
                throw new HttpRequestException("The saveFileName can't be empty!!!");
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

    public ResponseParser getRequestResponseParser() {
        return requestResponseParser;
    }

}

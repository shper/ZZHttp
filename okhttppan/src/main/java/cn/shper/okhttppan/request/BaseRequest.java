package cn.shper.okhttppan.request;

import java.util.HashMap;

import cn.shper.okhttppan.callback.BaseCallback;
import cn.shper.okhttppan.constant.HttpConstants;
import okhttp3.Headers;
import okhttp3.Request;

/**
 * Author shper
 * Description 基础请求类
 * Version 0.1 16-6-7 C 创建
 */
public abstract class BaseRequest<T extends BaseRequest> {

    protected String url;
    protected Object tag;
    protected HashMap<String, Object> params;
    protected HashMap<String, String> headers;
    protected int requestId;
    protected String requestMethod;

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

    public T params(HashMap<String, Object> params) {
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

    public T requestId(int requestId) {
        this.requestId = requestId;
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

    public BaseRequestCall build() {
        builder.url(url).tag(tag);
        appendHeaders();

        switch (requestMethod) {
            case HttpConstants.Method.GET:
            case HttpConstants.Method.POST:
                return new DefaultRequestCall(this);
            case HttpConstants.Method.DOWNLOAD:
                return new DownloadRequestCall(this);
            case HttpConstants.Method.UPLOAD:
                return new UploadRequestCall(this);
        }
        return new DefaultRequestCall(this);
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
     * 创建 Request
     * @param callback
     */
    abstract Request buildRequest(BaseCallback callback);

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

    public int getRequestId() {
        return requestId;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

}

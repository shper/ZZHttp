package cn.shper.okhttppan.builder;

import android.net.Uri;

import java.util.HashMap;
import java.util.Set;

import cn.shper.okhttppan.BuildConfig;
import cn.shper.okhttppan.request.BaseRequestCall;
import cn.shper.okhttppan.utils.Logger;

/**
 * Author shper
 * Description TODO
 * Version 0.1 16-6-7 C 创建
 */
public abstract class BaseBuilder<T extends BaseBuilder> {

    protected String requestMethod;

    protected String url;
    protected HashMap<String, String> headers;
    protected HashMap<String, Object> params;
    protected Object tag;

    protected String jsonStatusKey;
    protected String jsonStatusSuccessValue;
    protected String jsonDataKey;

    protected int connectTimeout;
    protected int readTimeout;
    protected int writeTimeout;

    public T params(HashMap<String, Object> params) {
        if (null == this.params) {
            this.params = params;
        } else {
            this.params.putAll(params);
        }
        return (T) this;
    }

    public T params(String key, Object val) {
        if (null == params) {
            params = new HashMap<>();
        }
        params.put(key, val);
        return (T) this;
    }

    public T tag(Object tag) {
        this.tag = tag;
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

    public T header(String key, String value) {
        if (this.headers == null) {
            headers = new HashMap<>();
        }
        headers.put(key, value);
        return (T) this;
    }

    public T token(String key, String value) {
        if (null == this.params) {
            this.params = new HashMap<>();
        }
        this.params.put(key, value);
        return (T) this;
    }

    public T jsonStatusKey(String statusKey) {
        this.jsonStatusKey = statusKey;
        return (T) this;
    }

    public T jsonStatusSuccessValue(String successValue) {
        this.jsonStatusSuccessValue = successValue;
        return (T) this;
    }

    public T jsonDataKey(String dataKey) {
        this.jsonDataKey = dataKey;
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

    public abstract BaseRequestCall build();

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
     * 拼接参数
     */
    String appendParams(String url, HashMap<String, Object> params) {
        if (url == null || params == null || params.isEmpty()) {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            builder.appendQueryParameter(key, String.valueOf(params.get(key)));
        }
        return builder.build().toString();
    }
}

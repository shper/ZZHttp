package cn.shper.okhttppan.config;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cn.shper.okhttppan.constant.HttpConstants;
import cn.shper.okhttppan.exception.HttpClientConfigException;
import cn.shper.okhttppan.utils.HttpsCertUtils;
import okhttp3.Dns;
import okhttp3.Interceptor;

/**
 * Author: Shper
 * Description: OkHttpPan 配置类
 * Version: V0.1 2017/1/9
 */
public class OkHttpPanConfig {

    // 返回的 Json 正常时的标识Key
    public String jsonStatusKey;
    // 返回的 Json 正常时的成功Value
    public String jsonStatusSuccessValue;
    // 返回的 Json 正常时，数据标识Key
    public String jsonDataKey;
    // 返回的 Json 失败时的标识Key
    public String jsonFailedKey;
    // 网络连接超时时间
    public int connectTimeout = HttpConstants.Timeout.DEFAULT_CONNECT;
    // 网络读取超时时间
    public int readTimeout = HttpConstants.Timeout.DEFAULT_READ;
    // 网络写超时时间
    public int writeTimeout = HttpConstants.Timeout.DEFAULT_WRITE;
    // HTTPS SSL 证书
    public HttpsCertUtils.SSLParams sslParams;
    // HttpDns
    public Dns dns;
    // 拦截器
    public List<Interceptor> interceptors;
    public List<Interceptor> netWorkInterceptors;

    public static class Builder {

        private OkHttpPanConfig config;

        public Builder() {
            this.config = new OkHttpPanConfig();
        }

        public Builder jsonStatusKey(String jsonStatusKey) {
            if (TextUtils.isEmpty(jsonStatusKey)) {
                throw new HttpClientConfigException("The JsonStatusKey can't be Empty!!!");
            }

            config.jsonStatusKey = jsonStatusKey;
            return this;
        }

        public Builder jsonStatusSuccessValue(String jsonStatusSuccessValue) {
            if (TextUtils.isEmpty(jsonStatusSuccessValue)) {
                throw new HttpClientConfigException("The JsonStatusSuccessValue can't be Empty!!!");
            }

            config.jsonStatusSuccessValue = jsonStatusSuccessValue;
            return this;
        }

        public Builder jsonDataKey(String jsonDataKey) {
            if (TextUtils.isEmpty(jsonDataKey)) {
                throw new HttpClientConfigException("The JsonDataKey can't be Empty!!!");
            }

            config.jsonDataKey = jsonDataKey;
            return this;
        }

        public Builder jsonFailedKey(String jsonFailedKey) {
            if (TextUtils.isEmpty(jsonFailedKey)) {
                throw new HttpClientConfigException("The JsonFailedKey can't be Empty!!!");
            }

            config.jsonFailedKey = jsonFailedKey;
            return this;
        }

        public Builder connectTimeout(int connectTimeout) {
            if (connectTimeout < 0) {
                throw new HttpClientConfigException("The connectTimeout can't be less than 0!!!");
            }

            config.connectTimeout = connectTimeout;
            return this;
        }

        public Builder readTimeout(int readTimeout) {
            if (readTimeout < 0) {
                throw new HttpClientConfigException("The readTimeout can't be less than 0!!!");
            }

            config.readTimeout = readTimeout;
            return this;
        }

        public Builder writeTimeout(int writeTimeout) {
            if (writeTimeout < 0) {
                throw new HttpClientConfigException("The writeTimeout can't be less than 0!!!");
            }

            config.writeTimeout = writeTimeout;
            return this;
        }

        public Builder sslParams(HttpsCertUtils.SSLParams sslParams) {
            if (null == sslParams || null == sslParams.sSLSocketFactory || null == sslParams.trustManager) {
                throw new HttpClientConfigException("The sslParams can't be empty!!!");
            }

            config.sslParams = sslParams;
            return this;
        }

        public Builder dns(Dns dns) {
            if (null == dns) {
                throw new HttpClientConfigException("The dns can't be empty!!!");
            }

            config.dns = dns;
            return this;
        }

        public Builder interceptors(List<Interceptor> interceptors) {
            if (null == interceptors || interceptors.isEmpty()) {
                throw new HttpClientConfigException("The interceptors can't be empty!!!");
            }

            config.interceptors = interceptors;
            return this;
        }

        public Builder interceptor(Interceptor interceptor) {
            if (null == interceptor) {
                throw new HttpClientConfigException("The interceptor can't be empty!!!");
            }

            if (null == config.interceptors) {
                config.interceptors = new ArrayList<>();
            }

            config.interceptors.add(interceptor);
            return this;
        }

        public Builder netWorkInterceptors(List<Interceptor> netWorkInterceptors) {
            if (null == netWorkInterceptors || netWorkInterceptors.isEmpty()) {
                throw new HttpClientConfigException("The netWorkInterceptors can't be empty!!!");
            }

            config.netWorkInterceptors = netWorkInterceptors;
            return this;
        }

        public Builder netWorkInterceptor(Interceptor netWorkInterceptor) {
            if (null == netWorkInterceptor) {
                throw new HttpClientConfigException("The netWorkInterceptor can't be empty!!!");
            }

            if (null == config.netWorkInterceptors) {
                config.netWorkInterceptors = new ArrayList<>();
            }

            config.netWorkInterceptors.add(netWorkInterceptor);
            return this;
        }

        public OkHttpPanConfig Build() {
            return config;
        }

    }
}

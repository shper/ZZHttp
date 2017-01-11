package cn.shper.okhttppan.entity;

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
    public int connectTimeout;
    // 网络读取超时时间
    public int readTimeout;
    // 网络写超时时间
    public int writeTimeout;

    public static class Builder {

        private OkHttpPanConfig config;

        public Builder() {
            this.config = new OkHttpPanConfig();
        }

        public Builder jsonStatusKey(String jsonStatusKey) {
            config.jsonStatusKey = jsonStatusKey;
            return this;
        }

        public Builder jsonStatusSuccessValue(String jsonStatusSuccessValue) {
            config.jsonStatusSuccessValue = jsonStatusSuccessValue;
            return this;
        }

        public Builder jsonDataKey(String jsonDataKey) {
            config.jsonDataKey = jsonDataKey;
            return this;
        }

        public Builder jsonFailedKey(String jsonFailedKey){
            config.jsonFailedKey = jsonFailedKey;
            return this;
        }

        public Builder connectTimeout(int connectTimeout) {
            config.connectTimeout = connectTimeout;
            return this;
        }

        public Builder readTimeout(int readTimeout) {
            config.readTimeout = readTimeout;
            return this;
        }

        public Builder writeTimeout(int writeTimeout) {
            config.writeTimeout = writeTimeout;
            return this;
        }

        public OkHttpPanConfig Build() {
            return config;
        }

    }
}

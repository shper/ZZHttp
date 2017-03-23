package cn.shper.okhttppan.config;

/**
 * Description: TODO
 * Version: V0.1 2017/3/23
 */
public interface ResponseParser {

    <T> T parseResponse(String responseBody, Class<T> clazz);

}

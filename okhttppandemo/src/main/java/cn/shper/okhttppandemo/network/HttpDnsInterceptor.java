package cn.shper.okhttppandemo.network;

import java.io.IOException;

import cn.shper.okhttppandemo.util.Logger;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description: TODO
 * Version: V0.1 2017/3/22
 */
public class HttpDnsInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originRequest = chain.request();
        HttpUrl httpUrl = originRequest.url();

        Logger.d(String.format("url: %1$s; host: %2$s", httpUrl.toString(), httpUrl.host()));
        Logger.d("socket: " + chain.connection().socket().toString());

        return chain.proceed(originRequest);
    }

}

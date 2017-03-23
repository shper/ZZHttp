package cn.shper.okhttppan;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import cn.shper.okhttppan.callback.BaseCallback;
import cn.shper.okhttppan.callback.DownloadCallback;
import cn.shper.okhttppan.callback.HttpCallback;
import cn.shper.okhttppan.callback.UploadCallback;
import cn.shper.okhttppan.config.OkHttpPanConfig;
import cn.shper.okhttppan.config.ResponseParser;
import cn.shper.okhttppan.constant.HttpConstants;
import cn.shper.okhttppan.exception.HttpError;
import cn.shper.okhttppan.request.DownloadRequest;
import cn.shper.okhttppan.request.GetRequest;
import cn.shper.okhttppan.request.PostRequest;
import cn.shper.okhttppan.request.UpLoadRequest;
import cn.shper.okhttppan.requestcall.BaseRequestCall;
import cn.shper.okhttppan.utils.Logger;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Author: Shper
 * Description: OkHttp封装类
 * Version: 0.1 16-6-7 C 创建
 * <p>
 * <p>
 * 使用列子：
 * OkHttpPan.post() or .get() or .download() or .upload()        // [必选] post 、get 、download 、upload 请求
 * .url(<$URL>)                                                  // [必选] 请求 的 URL
 * .params("XXO", xxo)                                           // [可选] 请求参数（可多个）
 * .files("Key", Map<String, File> files)                        // [可选] POST 专用，使用此标签 上传文件
 * .addFile(“name”, “filename”, File file)                      // [可选] POST 专用，使用此标签 上传文件
 * .savePath(String savePath)                                    // [可选] DOWNLOAD 专用，使用此标签 存储文件地址
 * .saveFileName(String saveFileName)                            // [可选] DOWNLOAD 专用，使用此标签 存储文件名
 * .responseParser(responseParser)                               // [可选] 自定义 response 解析器
 * .connectTimeout(int)                                          // [可选] 自定义连接超时时间
 * .readTimeout(int)                                             // [可选] 自定义数据读取超时时间
 * .writeTimeout(int)                                            // [可选] 自定义数据写入超时时间
 * .token(key,value)                                             // [可选] 使用此标签 请求中带 token
 * .build().enqueue(<$Entity>.class, callback);
 */
public class OkHttpPan {

    // 是否开启 debug 日志
    public boolean isDebug = false;

    // Response 解析器
    ResponseParser responseParser;

    private volatile static OkHttpPan instance;
    private static OkHttpClient defaultClient;
    private static Handler respHandler;

    private OkHttpPan() {
    }

    /**
     * tips:此方式必须在 application 中实现
     */
    public static void initialization() {
        initialization(new OkHttpPanConfig.Builder()
                .connectTimeout(HttpConstants.Timeout.DEFAULT_CONNECT)
                .readTimeout(HttpConstants.Timeout.DEFAULT_READ)
                .writeTimeout(HttpConstants.Timeout.DEFAULT_WRITE)
                .Build());
    }

    public static void initialization(OkHttpPanConfig config) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (null != config) {
            if (config.connectTimeout > 0) {
                builder.connectTimeout(config.connectTimeout, TimeUnit.SECONDS);
            }
            if (config.readTimeout > 0) {
                builder.readTimeout(config.readTimeout, TimeUnit.SECONDS);
            }
            if (config.writeTimeout > 0) {
                builder.writeTimeout(config.writeTimeout, TimeUnit.SECONDS);
            }
            if (null != config.sslParams) {
                builder.sslSocketFactory(config.sslParams.sSLSocketFactory, config.sslParams.trustManager);
            }
            if (null != config.dns) {
                builder.dns(config.dns);
            }
            if (null != config.interceptors && !config.interceptors.isEmpty()) {
                for (Interceptor interceptor : config.interceptors) {
                    builder.addInterceptor(interceptor);
                }
            }
            if (null != config.netWorkInterceptors && !config.netWorkInterceptors.isEmpty()) {
                for (Interceptor interceptor : config.netWorkInterceptors) {
                    builder.addNetworkInterceptor(interceptor);
                }
            }
        }

        defaultClient = builder.build();
        // 初始化Handler
        respHandler = new Handler(Looper.getMainLooper());

        // 初始化 OkHttpPan
        getInstance();

        // Response 解析器
        getInstance().responseParser = config.responseParser;

        Logger.i("OkHttpPan Initialized");
    }

    /**
     * 是否开启 debug 模式
     */
    public static void setDebug(boolean isDebug) {
        getInstance().isDebug = isDebug;
    }

    public static boolean isDebugged() {
        return getInstance().isDebug;
    }

    /**
     * 返回单例实例
     */
    public static OkHttpPan getInstance() {
        if (null == instance) {
            synchronized (OkHttpPan.class) {
                if (null == instance) {
                    instance = new OkHttpPan();
                }
            }
        }
        return instance;
    }

    public OkHttpClient getClient() {
        return defaultClient;
    }

    public Handler getRespHandler() {
        return respHandler;
    }

    public static GetRequest get() {
        return new GetRequest();
    }

    public static PostRequest post() {
        return new PostRequest();
    }

    public static DownloadRequest download() {
        return new DownloadRequest();
    }

    public static UpLoadRequest upload() {
        return new UpLoadRequest();
    }


    public <T> T execute(BaseRequestCall requestCall, Class<T> clazz) throws IOException {
        Response response = requestCall.getCall().execute();

        if (null == response || !response.isSuccessful() || null == response.body()) {
            return null;
        }

        return parseResponse(response, clazz);
    }

    /**
     * 执行 get post 请求
     */
    public <T> void enqueue(BaseRequestCall requestCall, final Class<T> clazz, final BaseCallback<T> callback) {
        // 获取 请求参数
        final String requestMethod = requestCall.getOkHttpRequest().getRequestMethod();

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final HttpError error = createHttpError(call, e);
                // 发送失败回调消息
                sendDefaultFailResultCallback(error, callback);
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                if (call.isCanceled() || !response.isSuccessful() || null == response.body()) {
                    Logger.e(OkHttpPan.class.getName() + "请求被取消");
                    // 发送失败回调消息
                    sendDefaultFailResultCallback(new HttpError(HttpError.ERR_CODE_CANCELED), callback);
                    return;
                }

                // 发送成功回调消息
                sendDefaultSuccessResultCallback(parseResponse(response, clazz), requestMethod, callback);
            }
        });
    }

    /**
     * 执行 download 请求
     */
    public void enqueue(BaseRequestCall requestCall, DownloadCallback callback) {
        if (callback == null) {
            callback = DownloadCallback.DEFAULT;
        }
        final DownloadCallback finalCallback = callback;

        // 获取 请求参数
        final String savePath = requestCall.getOkHttpRequest().getSavePath();
        final String saveFileName = requestCall.getOkHttpRequest().getSaveFileName();

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final HttpError error = createHttpError(call, e);
                Logger.e("[Http]" + OkHttpPan.class.getName() + "Error code = " +
                        error.getCode() + ", Error msg : " + error.getMessage());

                // 发送失败回调消息
                respHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        finalCallback.onFail(error);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (call.isCanceled()) {
                    Logger.e(OkHttpPan.class.getName() + "请求被取消");

                    // 发送失败回调消息
                    respHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            finalCallback.onFail(new HttpError(HttpError.ERR_CODE_CANCELED));
                        }
                    });
                    return;
                }

                // http请求失败
                if (!response.isSuccessful()) {
                    final String msg = response.message();
                    final int code = response.code();
                    Logger.d(code + ", " + msg);

                    // 发送失败回调消息
                    respHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            finalCallback.onFail(new HttpError(code, msg));
                        }
                    });

                    return;
                }

                // 发送成功回调消息
                sendDownloadSuccessResultCallback(response, savePath, saveFileName, finalCallback);
            }
        });
    }

    /**
     * 获取服务器数据 失败
     */

    private void sendDefaultFailResultCallback(final HttpError error, final BaseCallback callback) {
        if (callback == null) {
            return;
        }
        // 提交给UI线程
        respHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFail(error);
            }
        });
    }

    /**
     * 获取服务端数据 成功
     */
    private <T> void sendDefaultSuccessResultCallback(final T data, final String requestMethod,
                                                      final BaseCallback<T> callback) {
        if (null == callback) {
            return;
        }

        respHandler.post(new Runnable() {
            @Override
            public void run() {
                Logger.i("[Http] - 数据获取成功");
                if (HttpConstants.Method.UPLOAD.equals(requestMethod) && callback instanceof UploadCallback) {
                    ((UploadCallback<T>) callback).onComplete(data);
                } else if (callback instanceof HttpCallback) {
                    ((HttpCallback<T>) callback).onSuccess(data);
                }

            }
        });
    }

    /**
     * 下载完成 数据处理
     */
    private void sendDownloadSuccessResultCallback(Response response, String destFileDir, String destFileName,
                                                   final DownloadCallback callback) {
        try {
            InputStream is = null;
            byte[] buf = new byte[2048];
            int len;
            FileOutputStream fos = null;
            try {
                is = response.body().byteStream();
                final long total = response.body().contentLength();
                long current = 0;
                File dir = new File(destFileDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                final File file = new File(dir, destFileName);
                fos = new FileOutputStream(file);
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    current += len;
                    final long finalCurrent = current;
                    respHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.inProgress(finalCurrent * 1.0f / total, finalCurrent, total);
                            Logger.d("current: " + finalCurrent + " total:" + total);
                        }
                    });
                }
                fos.flush();
                // 存储完成
                respHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onComplete(file);
                    }
                });
            } finally {
                try {
                    response.body().close();
                    if (is != null) is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (fos != null) fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("[Http] - " + OkHttpPan.class.getName() + " code = " +
                    response.code() + ", response msg : " + response.message());
            final HttpError error = new HttpError(HttpError.ERR_CODE_UNKNOWN,
                    e.getClass().getName() + " : " + e.getMessage());
            // 发送失败回调消息
            respHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onFail(error);
                }
            });
        }
    }

    private <T> T parseResponse(Response response, Class<T> clazz) throws IOException {
        String responseBody = response.body().string();
        response.body().close();

        // debug 模式下 json 打印
        if (isDebug && !TextUtils.isEmpty(responseBody)) {
            int maxLogChars = 4096 / 3 - 1;
            for (int i = 0; i < responseBody.length(); i += maxLogChars) {
                int end = i + maxLogChars;
                if (end > responseBody.length()) {
                    end = responseBody.length();
                }
                Logger.d("respStr = " + responseBody.substring(i, end).trim());
            }
        }

        if (null != responseParser) {
            return responseParser.parseResponse(responseBody, clazz);
        }

        // 未设置 ResponseParser 解析器时 默认使用 Gson 解析
        if (null == clazz || String.class.equals(clazz) || TextUtils.isEmpty(responseBody)) {
            return (T) responseBody;
        }

        return JSONObject.parseObject(responseBody, clazz);
    }

    private HttpError createHttpError(Call call, IOException e) {
        // 异常时，默认提示
        int code = HttpError.ERR_CODE_SERVER_ERROR;
        String msg = (e.getClass().getName() + " : " + e.getMessage());
        // 判断网络问题
        if (e instanceof UnknownHostException) {
            code = HttpError.ERR_CODE_NETWORK_ERROR;
        } else if (e instanceof ConnectTimeoutException) {
            code = HttpError.ERR_CODE_CONNECT_TIMEOUT;
        } else if (e instanceof SocketTimeoutException) {
            code = HttpError.ERR_CODE_SOCKET_TIMEOUT;
        }
        Logger.e(HttpError.getMessage(code) + ":[" + code + "]" + " - " + call.request().url().toString());
        return new HttpError(code, msg);
    }

    /**
     * 取消请求
     *
     * @param tag
     */
    public static void cancel(Object tag) {
        for (Call call : defaultClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : defaultClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

}

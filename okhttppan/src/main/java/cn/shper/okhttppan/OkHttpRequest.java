package cn.shper.okhttppan;

import android.os.Handler;
import android.os.Looper;

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

import cn.shper.okhttppan.builder.DownloadBuilder;
import cn.shper.okhttppan.builder.GetBuilder;
import cn.shper.okhttppan.builder.PostBuilder;
import cn.shper.okhttppan.builder.UploadBuilder;
import cn.shper.okhttppan.callback.BaseCallback;
import cn.shper.okhttppan.callback.DownloadCallback;
import cn.shper.okhttppan.callback.HttpCallback;
import cn.shper.okhttppan.callback.UploadCallback;
import cn.shper.okhttppan.constant.HttpConstants;
import cn.shper.okhttppan.constant.HttpError;
import cn.shper.okhttppan.request.BaseRequestCall;
import cn.shper.okhttppan.utils.Logger;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Author shper
 * Description OkHttp封装类
 * Version 0.1 16-6-7 C 创建
 * <p>
 * <p>
 * 使用列子：
 * OkHttpRequest.post() or .get() or .download() or .upload()   // [必选] post 、get 、download 、upload 请求
 * .url(<$URL>)                                                  // [必选] 请求 的 URL
 * .params("XXO", xxo)                                           // [可选] 请求参数（可多个）
 * .files("Key", Map<String, File> files)                        // [可选] POST 专用，使用此标签 上传文件
 * .addFile(“name”, “filename”, File file)                       // [可选] POST 专用，使用此标签 上传文件
 * .savePath(String savePath)                                    // [可选] DOWNLOAD 专用，使用此标签 存储文件地址
 * .saveFileName(String saveFileName)                            // [可选] DOWNLOAD 专用，使用此标签 存储文件名
 * .jsonStatusKey("status")                                      // [可选] 自定义解析返回json中的状态字段；默认值：status
 * .jsonStatusSuccessValue("1")                                  // [可选] 自定义解析返回json中的成功标识符；默认值：1
 * .jsonDataKey("data")                                          // [可选] 自定义解析返回json中的数据标识符；默认值：data
 * .connectTimeout(int)                                          // [可选] 自定义连接超时时间
 * .readTimeout(int)                                             // [可选] 自定义数据读取超时时间
 * .writeTimeout(int)                                            // [可选] 自定义数据写入超时时间
 * .token(key,value)                                             // [可选] 使用此标签 请求中带 token
 * .build().execute(<$Entity>.class, callback);
 */
public class OkHttpRequest {

    private static OkHttpClient defaultClient;
    private static OkHttpRequest instance;
    private static Handler respHandler;

    private OkHttpRequest() {
    }

    /**
     * tips:此方式必须在 application 中实现
     */
    public static void initClient() {
        // 设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(HttpConstants.Timeout.DEFAULT_CONNECT, TimeUnit.SECONDS)
                .readTimeout(HttpConstants.Timeout.DEFAULT_READ, TimeUnit.SECONDS)
                .writeTimeout(HttpConstants.Timeout.DEFAULT_WRITE, TimeUnit.SECONDS);
        defaultClient = builder.build();
        // 初始化Handler
        respHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpRequest getInstance() {
        if (null == instance) {
            synchronized (OkHttpRequest.class) {
                if (null == instance) {
                    instance = new OkHttpRequest();
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

    public static GetBuilder get() {
        return new GetBuilder(HttpConstants.Method.GET);
    }

    public static PostBuilder post() {
        return new PostBuilder(HttpConstants.Method.POST);
    }

    public static DownloadBuilder download() {
        return new DownloadBuilder(HttpConstants.Method.DOWNLOAD);
    }

    public static UploadBuilder upload() {
        return new UploadBuilder(HttpConstants.Method.UPLOAD);
    }

    // 执行 get post 请求
    public <T> void execute(BaseRequestCall requestCall, final Class<T> clazz, final BaseCallback callback) {
        // 获取 请求参数
        final int id = requestCall.getOkHttpRequest().getRequestId();
        final String jsonStatusKey = requestCall.getOkHttpRequest().getJsonStatusKey();
        final String jsonStatusSuccessValue = requestCall.getOkHttpRequest().getJsonStatusSuccessValue();
        final String jsonDataKey = requestCall.getOkHttpRequest().getJsonDataKey();
        final String requestMethod = requestCall.getOkHttpRequest().getRequestMethod();

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final HttpError error = createHttpError(call, e);
                // 发送失败回调消息
                sendDefaultFailResultCallback(error, callback, id);
            }

            @Override
            public void onResponse(final Call call, final Response response) {
                try {
                    if (call.isCanceled()) {
                        Logger.e(OkHttpRequest.class.getName() + "请求被取消");
                        // 发送失败回调消息
                        sendDefaultFailResultCallback(new HttpError(HttpError.ERR_CODE_CANCELED), callback, id);
                        return;
                    }
                    // 发送成功回调消息
                    sendDefaultSuccessResultCallback(response, clazz, callback, id, jsonStatusKey,
                            jsonStatusSuccessValue, jsonDataKey, requestMethod);
                } catch (Exception e) {
                    Logger.e(OkHttpRequest.class.getName() + "http code = " + response.code() +
                            ", response msg : " + response.message());
                    // 发送失败回调消息
                    sendDefaultFailResultCallback(new HttpError(HttpError.ERR_CODE_UNKNOWN), callback, id);
                }
            }
        });
    }

    // 执行 download 请求
    public void execute(BaseRequestCall requestCall, DownloadCallback callback) {
        if (callback == null) {
            callback = DownloadCallback.DEFAULT;
        }
        final DownloadCallback finalCallback = callback;

        // 获取 请求参数
        final int requestId = requestCall.getOkHttpRequest().getRequestId();
        final String savePath = requestCall.getOkHttpRequest().getSavePath();
        final String saveFileName = requestCall.getOkHttpRequest().getSaveFileName();

        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                final HttpError error = createHttpError(call, e);
                // 发送失败回调消息
                respHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        finalCallback.onFail(error, requestId);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (call.isCanceled()) {
                        Logger.e(OkHttpRequest.class.getName() + "请求被取消");
                        // 发送失败回调消息
                        respHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                finalCallback.onFail(new HttpError(HttpError.ERR_CODE_CANCELED), requestId);
                            }
                        });
                        return;
                    }
                    // 发送成功回调消息
                    sendDownloadSuccessResultCallback(response, savePath, saveFileName, requestId, finalCallback);
                } catch (Exception e) {
                    Logger.e("[Http]" + OkHttpRequest.class.getName() + "http code = " +
                            response.code() + ", response msg : " + response.message());
                    final HttpError error = new HttpError(HttpError.ERR_CODE_UNKNOWN);
                    // 发送失败回调消息
                    respHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            finalCallback.onFail(error, requestId);
                        }
                    });
                }
            }
        });
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

    // 获取数据失败
    private void sendDefaultFailResultCallback(final HttpError error, final BaseCallback callback, final int id) {
        if (callback == null) {
            return;
        }
        // 提交给UI线程
        respHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFail(error, id);
            }
        });
    }

    // 获取数据成功
    private <T> void sendDefaultSuccessResultCallback(Response response, Class<T> clazz,
                                                      final BaseCallback callback, final int requestId,
                                                      final String jsonStatusKey, final String jsonStatusSuccessValue,
                                                      final String jsonDataKey, final String requestMethod) {
        try {
            final Result result = new Result();
            // http请求失败
            if (!response.isSuccessful()) {
                String msg = response.message();
                int code = response.code();
                if (BuildConfig.DEBUG) {
                    Logger.d(code + ", " + msg);
                }
                result.success = false;
                result.error = new HttpError(code, msg);
            } else { // http请求成功
                String respStr = response.body().string();
                Logger.d("requestId = " + requestId + ", respStr = " + respStr);
                response.body().close();
                // log 打印
                if (BuildConfig.DEBUG) {
                    int maxLogChars = 4096 / 3 - 1;
                    for (int i = 0; i < respStr.length(); i += maxLogChars) {
                        int end = i + maxLogChars;
                        if (end > respStr.length()) end = respStr.length();
                        Logger.d(respStr.substring(i, end).trim());
                    }
                }
                JSONObject json = JSONObject.parseObject(respStr);
                if (json != null && json.containsKey(jsonStatusKey)) {
                    Object status = json.get(jsonStatusKey);
                    // 返回数据正常
                    if (jsonStatusSuccessValue.equals(String.valueOf(status))) {
                        // 有获取数据
                        if (json.containsKey(jsonDataKey)) {
                            result.success = true;
                            Object data = json.get(jsonDataKey);
                            if (data != null) {
                                if (clazz == null) { // 不需要解析，由调用方自己处理
                                    result.data = data;
                                } else {
                                    if (data instanceof JSONArray) {
                                        JSONArray dataArray = (JSONArray) data;
                                        result.data = JSON.parseArray(dataArray.toString(), clazz);
                                    } else if (data instanceof JSONObject) {
                                        JSONObject dataObj = (JSONObject) data;
                                        result.data = JSON.parseObject(dataObj.toString(), clazz);
                                    }
                                }
                            }
                        } else { // 无数据
                            result.success = false;
                            result.error = new HttpError(HttpError.ERR_CODE_DATA_EMPTY);
                        }
                    } else if ("-100".equals(String.valueOf(status))) {
                        result.success = false;
                        result.error = new HttpError(Integer.parseInt(String.valueOf(status)),
                                String.valueOf(json.get("failed")));
                    } else if (String.valueOf(HttpError.ERR_CODE_LOGING_TIMEOUT).equals(String.valueOf(status))) { // 登录超时
                        result.success = false;
                        result.error = new HttpError(HttpError.ERR_CODE_LOGING_TIMEOUT);
                    } else { // 操作失败
                        result.success = false;
                        result.error = new HttpError(json.containsKey("failed") ?
                                HttpError.ERR_CODE_FAILED : HttpError.ERR_CODE_UNKNOWN,
                                json.containsKey("failed") ? String.valueOf(json.get("failed")) :
                                        HttpError.ERR_CODE_UNKNOWN_MSG);
                    }
                } else { // 无数据
                    result.success = false;
                    result.error = new HttpError(HttpError.ERR_CODE_DATA_EMPTY);
                }
            }
            // 返回数据
            respHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        if (result.success) {
                            if (HttpConstants.Method.UPLOAD.equals(requestMethod) && callback instanceof UploadCallback) {
                                ((UploadCallback) callback).onComplete(result.data, requestId);
                            } else if (callback instanceof HttpCallback) {
                                ((HttpCallback) callback).onSuccess(result.data, requestId);
                            }
                        } else {
                            callback.onFail(result.error, requestId);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("[Http] - " + OkHttpRequest.class.getName() + " code = " +
                    response.code() + ", response msg : " + response.message());
            sendDefaultFailResultCallback(new HttpError(HttpError.ERR_CODE_UNKNOWN,
                    e.getClass().getName() + " : " + e.getMessage()), callback, requestId);
        }
    }

    // 下载完成 数据处理
    private void sendDownloadSuccessResultCallback(Response response, String destFileDir, String destFileName,
                                                   final int requestId, final DownloadCallback callback) {
        try {
            final Result result = new Result();
            // http请求失败
            if (!response.isSuccessful()) {
                String msg = response.message();
                int code = response.code();
                if (BuildConfig.DEBUG) {
                    Logger.d(code + ", " + msg);
                }
                result.success = false;
                result.error = new HttpError(code, msg);
            } else { // http请求成功 存储文件
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
                                callback.inProgress(finalCurrent * 1.0f / total, finalCurrent, total, requestId);
                                Logger.d("[SHPER - requestId: " + requestId + "] current: " + finalCurrent + " total:" + total);
                            }
                        });
                    }
                    fos.flush();
                    // 存储完成
                    respHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onComplete(file, requestId);
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("[Http] - " + OkHttpRequest.class.getName() + " code = " +
                    response.code() + ", response msg : " + response.message());
            final HttpError error = new HttpError(HttpError.ERR_CODE_UNKNOWN,
                    e.getClass().getName() + " : " + e.getMessage());
            // 发送失败回调消息
            respHandler.post(new Runnable() {
                @Override
                public void run() {
                    callback.onFail(error, requestId);
                }
            });
        }
    }

    private static class Result {
        public boolean success;
        public HttpError error;
        public Object data;
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

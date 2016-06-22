package cn.shper.okhttppan.request;

import java.io.IOException;

import cn.shper.okhttppan.OkHttpRequest;
import cn.shper.okhttppan.callback.BaseCallback;
import cn.shper.okhttppan.callback.UploadCallback;
import cn.shper.okhttppan.utils.Logger;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Author shper
 * Description TODO
 * Version 0.1 16-6-12 C 创建
 */
public class UploadRequestBody extends RequestBody {

    private UploadCallback callback;
    private BufferedSink bufferedSink;
    private RequestBody requestBody;
    private int requestId;

    public UploadRequestBody(RequestBody requestBody, BaseCallback callback, int requestId) {
        this.requestBody = requestBody;
        if (callback != null && callback instanceof UploadCallback) {
            this.callback = (UploadCallback) callback;
        } else {
            this.callback = UploadCallback.DEFAULT;
        }
        this.requestId = requestId;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(startSink(sink));
        }
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private Sink startSink(Sink sink) {
        return new ForwardingSink(sink) {
            long current = 0L;
            long contentLength = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    contentLength = contentLength();
                }
                current += byteCount;
                final long finalCurrent = current;
                OkHttpRequest.getInstance().getRespHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        callback.inProgress(finalCurrent * 1.0f / contentLength, finalCurrent, contentLength, requestId);
                        Logger.d("[requestId: " + requestId + "] current: " + finalCurrent + " total:" + contentLength);
                    }
                });
            }
        };
    }

}

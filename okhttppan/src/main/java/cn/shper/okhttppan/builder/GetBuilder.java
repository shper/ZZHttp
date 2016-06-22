package cn.shper.okhttppan.builder;

import android.text.TextUtils;

import cn.shper.okhttppan.request.DefaultRequestCall;
import cn.shper.okhttppan.request.GetRequest;
import cn.shper.okhttppan.utils.Logger;

/**
 * Author shper
 * Description TODO
 * Version 0.1 16-6-7 C 创建
 */
public class GetBuilder extends BaseBuilder<GetBuilder> {

    public GetBuilder(String method) {
        requestMethod = method;
    }

    @Override
    public DefaultRequestCall build() {
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("url can't be empty!!!");
        }

        // 打印日志
        printLog("GET");
        if (params != null) {
            url = appendParams(url, params);
            // 打印拼接后的 URL
            Logger.d("GET - applyUrl: " + url);
        }

        return (DefaultRequestCall) new GetRequest()
                .requestMethod(requestMethod)
                .url(url)
                .headers(headers)
                .params(params)
                .jsonStatusKey(jsonStatusKey)
                .jsonStatusSuccessValue(jsonStatusSuccessValue)
                .jsonDataKey(jsonDataKey)
                .requestId(requestId)
                .tag(tag)
                .build();
    }

}

package cn.shper.okhttppandemo.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.shper.okhttppan.config.ResponseParser;

/**
 * Description: TODO
 * Version: V0.1 2017/3/23
 */
public class MResponseParser implements ResponseParser {

    @Override
    public <T> T parseResponse(String responseBody, Class<T> clazz) {

        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        if (jsonObject.containsKey("weatherinfo")) {
            Object data = jsonObject.get("weatherinfo");

            if (data instanceof JSONArray) {
                JSONArray dataArray = (JSONArray) data;
                return (T) JSON.parseArray(dataArray.toString(), clazz);
            } else if (data instanceof JSONObject) {
                JSONObject dataObj = (JSONObject) data;
                return JSON.parseObject(dataObj.toString(), clazz);
            }
        }

        return (T) responseBody;
    }

}

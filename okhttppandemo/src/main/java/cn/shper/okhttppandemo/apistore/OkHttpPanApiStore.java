package cn.shper.okhttppandemo.apistore;

import cn.shper.okhttppan.OkHttpPan;
import cn.shper.okhttppan.callback.HttpCallback;
import cn.shper.okhttppandemo.entity.WeatherInfo;

/**
 * Author: Shper
 * Version: V0.1 2017/1/11
 */
public class OkHttpPanApiStore {

    private static final String WEATHER_URL = "http://www.weather.com.cn/adat/sk/";

    /**
     * 获取天气详情
     */
    public static void getWeather(String cityId, HttpCallback<WeatherInfo> callback) {
        OkHttpPan.get().url(WEATHER_URL + cityId + ".html")
                .jsonDataKey("weatherinfo")
                .build().enqueue(WeatherInfo.class, callback);
    }

    /**
     * 获取天气原始Json数据
     */
    public static void getWeatherJson(String cityId, HttpCallback callback){
        OkHttpPan.get().url(WEATHER_URL + cityId + ".html")
                .build().enqueue(null, callback);
    }

    public static void get12306(HttpCallback<String> callback){
        OkHttpPan.get().url("https://kyfw.12306.cn/otn/")
                .build().enqueue(String.class, callback);
    }

}

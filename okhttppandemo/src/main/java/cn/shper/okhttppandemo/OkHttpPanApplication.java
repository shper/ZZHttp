package cn.shper.okhttppandemo;

import android.app.Application;

import cn.shper.okhttppan.OkHttpPan;
import cn.shper.okhttppan.entity.OkHttpPanConfig;
import cn.shper.okhttppandemo.network.HttpDns;

/**
 * Author: Shper
 * Description: OkHttpPan Application 类
 * Version: V0.1 2017/1/11
 */
public class OkHttpPanApplication extends Application {

    private static OkHttpPanApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        OkHttpPanConfig config = new OkHttpPanConfig.Builder()
                .dns(new HttpDns())
                .Build();

        // 初始化 OkHttpPan
        OkHttpPan.initialization(config);
        if (BuildConfig.DEBUG) {
            OkHttpPan.setDebug(true);
        }
    }

    public static OkHttpPanApplication getInstance() {
        return instance;
    }

}

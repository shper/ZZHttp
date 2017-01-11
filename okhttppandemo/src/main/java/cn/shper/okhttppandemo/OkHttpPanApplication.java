package cn.shper.okhttppandemo;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import cn.shper.okhttppan.OkHttpPan;

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

        // 初始化 OkHttpPan
        OkHttpPan.initialization();
        OkHttpPan.setDebug(true);
    }

    public static OkHttpPanApplication getInstance() {
        return instance;
    }

}

package cn.shper.okhttppandemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.shper.okhttppan.callback.HttpCallback;
import cn.shper.okhttppan.exception.HttpError;
import cn.shper.okhttppandemo.R;
import cn.shper.okhttppandemo.apistore.OkHttpPanApiStore;
import cn.shper.okhttppandemo.entity.BaseResponse;
import cn.shper.okhttppandemo.entity.WeatherInfo;
import cn.shper.okhttppandemo.util.Logger;

/**
 * Author: Shper
 * Version: V0.1 2017/1/11
 */
public class OkHttpPanActivity extends AppCompatActivity {

    @BindView(R.id.get_weather_btn)
    Button getWeatherBtn;
    @BindView(R.id.weather_info_txt)
    TextView weatherInfoTxt;
    @BindView(R.id.weather_info_json_txt)
    TextView weatherInfoJsonTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http_pan);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.get_weather_btn)
    public void onGetWeatherClick() {
        clearTextDate();

//        OkHttpPanApiStore.getWeatherBase("101210101", new HttpCallback<BaseResponse>() {
//            @Override
//            public void onSuccess(BaseResponse baseResponse) {
//                if (null == baseResponse || null == baseResponse.getWeatherinfo()) {
//                    return;
//                }
//                weatherInfoTxt.setText(baseResponse.getWeatherinfo().toString());
//            }
//
//            @Override
//            public void onFail(HttpError error) {
//                weatherInfoTxt.setText(error.getMessage());
//            }
//        });

//        OkHttpPanApiStore.getWeather("101210101", new HttpCallback<WeatherInfo>() {
//            @Override
//            public void onSuccess(WeatherInfo weatherInfo) {
//                if (null == weatherInfo) {
//                    return;
//                }
//                weatherInfoTxt.setText(weatherInfo.toString());
//            }
//
//            @Override
//            public void onFail(HttpError error) {
//                weatherInfoTxt.setText(error.getMessage());
//            }
//        });

        OkHttpPanApiStore.getWeatherString("101210101", new HttpCallback<String>() {
            @Override
            public void onSuccess(String str) {
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                weatherInfoTxt.setText(str);
            }

            @Override
            public void onFail(HttpError error) {
                weatherInfoTxt.setText(error.getMessage());
            }
        });

//        OkHttpPanApiStore.getWeatherJson("101210101", new HttpCallback<String>() {
//            @Override
//            public void onSuccess(final String string) {
//                weatherInfoJsonTxt.setText(string);
//            }
//
//            @Override
//            public void onFail(HttpError error) {
//                weatherInfoJsonTxt.setText(error.getMessage());
//            }
//
//        });

//        OkHttpPanApiStore.get12306(new HttpCallback<String>() {
//            @Override
//            public void onFail(HttpError error) {
//                Logger.e(error.getMessage());
//            }
//
//            @Override
//            public void onSuccess(String obj) {
//                Logger.d(obj);
//            }
//        });

    }

    /**
     * 清除数据
     */
    private void clearTextDate() {
        weatherInfoTxt.setText("");
        weatherInfoJsonTxt.setText("");
    }

}

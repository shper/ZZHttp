package cn.shper.okhttppandemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.shper.okhttppan.callback.HttpCallback;
import cn.shper.okhttppan.constant.HttpError;
import cn.shper.okhttppandemo.R;
import cn.shper.okhttppandemo.apistore.OkHttpPanApiStore;
import cn.shper.okhttppandemo.entity.WeatherInfo;

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

        OkHttpPanApiStore.getWeather("101210101", new HttpCallback<WeatherInfo>() {
            @Override
            public void onSuccess(WeatherInfo weatherInfo) {
                weatherInfoTxt.setText(weatherInfo.toString());
            }

            @Override
            public void onFail(HttpError error) {
                weatherInfoTxt.setText(error.getMessage());
            }
        });


        OkHttpPanApiStore.getWeatherJson("101210101", new HttpCallback() {
            @Override
            public void onSuccess(final Object object) {
                weatherInfoJsonTxt.setText(object.toString());
            }

            @Override
            public void onFail(HttpError error) {
                weatherInfoJsonTxt.setText(error.getMessage());
            }

        });
    }

    /**
     * 清除数据
     */
    private void clearTextDate() {
        weatherInfoTxt.setText("");
        weatherInfoJsonTxt.setText("");
    }

}

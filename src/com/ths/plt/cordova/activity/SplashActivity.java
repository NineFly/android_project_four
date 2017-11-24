package com.ths.plt.cordova.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ths.plt.cordova.R;

/**
 * Created by ruaho on 2017/11/24.
 * 闪屏页
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

    }
}

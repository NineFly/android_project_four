package com.ths.plt.cordova.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import com.ths.plt.cordova.R;

/**
 * Created by ruaho on 2017/11/24.
 * 闪屏页
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

    }
}

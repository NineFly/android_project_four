package com.ths.plt.cordova.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Window;

import com.ths.plt.cordova.R;

/**
 * Created by ruaho on 2017/11/24.
 * 闪屏页
 */
public class SplashActivity extends BaseActivity {

    private Handler os = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);


        // TODO: 2017/11/24 延迟进入主界面
        os.postDelayed(new Runnable() {
            @Override
            public void run() {
                //进入主页面
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 1500);
    }
}

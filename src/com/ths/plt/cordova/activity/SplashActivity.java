package com.ths.plt.cordova.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

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

        RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(1000);
        rootLayout.startAnimation(animation);

        os.postDelayed(new Runnable() {
            @Override
            public void run() {
                //进入主页面
                startActivity(new Intent(SplashActivity.this, AndyViewPagerActivity.class));
                finishNoTransition();
            }
        }, 1500);
    }
}

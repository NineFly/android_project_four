package com.ths.plt.cordova.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ths.plt.cordova.R;
import com.ths.plt.cordova.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruaho on 2017/11/22.
 * 介绍页
 */
public class AndyViewPagerActivity extends Activity
        implements View.OnClickListener,
        ViewPager.OnPageChangeListener {
    private ViewPager vp;
    // 引导图片资源
    private static final int[] pics = {R.drawable.one,
            R.drawable.two, R.drawable.three, R.drawable.four};

    // 底部小点图片
    private ImageView[] dots;

    // 记录当前选中位置
    private int currentIndex;
    int curPage;
    Boolean about;
    private Button button;

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.viewpage);
        hideVirtualButtons();

        initView();
        initData();
        initEvent();
    }

    private void initView() {
        vp = (ViewPager) findViewById(R.id.viewpager);
        button = (Button) findViewById(R.id.button);
        // 初始化底部小点
        initDots();

    }

    private void initData() {
        about = getIntent().getBooleanExtra("about", false);

        List<View> views = new ArrayList<View>();
        @SuppressWarnings("deprecation")
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        // 初始化引导图片列表
        for (int pic : pics) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(pic);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            views.add(iv);
        }

        // 初始化Adapter
        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(views);
        vp.setAdapter(vpAdapter);
    }

    private void initEvent() {
        // 绑定回调
        vp.setOnPageChangeListener(this);
        vp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (top.getVisibility() == View.VISIBLE) {
//                    top.setVisibility(View.GONE);
//                } else if (top.getVisibility() == View.GONE) {
//                    top.setVisibility(View.VISIBLE);
//                }
            }
        });
        // button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                Intent intent = new Intent(AndyViewPagerActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();

            }
        });
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[pics.length];

        // 循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);
    }

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }

        vp.setCurrentItem(position);
    }

    private void setCurDot(int positon) {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }

        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = positon;
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {
        Log.d("TestActivity", arg0 + "");
        curPage = arg0;
    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        Log.d("Test", arg0 + "," + arg1 + "," + arg2);
        if (arg0 == pics.length - 1 && curPage == 1) {
            if (getIntent().getBooleanExtra("aa", false)) {
//                first();
                return;
            }
            finish();
        }
    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状态
        setCurDot(arg0);
//        if (arg0 != pics.length - 1) {
//            button.setVisibility(View.GONE);
//            ll.setVisibility(View.VISIBLE);
//        } else {
//            button.setVisibility(View.VISIBLE);
//            ll.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        setCurView(position);
        setCurDot(position);
    }

    private void hideVirtualButtons() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }
}
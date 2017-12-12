package com.ths.plt.cordova.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ths.plt.cordova.R;
import com.ths.plt.cordova.utils.ToastUtils;

import java.util.List;

/**
 * Created by ruaho on 2017/11/24.
 * Activity基类
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Android 判断app是否在前台还是在后台运行，直接看代码，可直接使用。
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses != null) {
            for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
				GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
				 */
                    Log.i(context.getPackageName(), "此appimportace ="
                            + appProcess.importance
                            + ",context.getClass().getName()="
                            + context.getClass().getName());
                    if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        Log.i(context.getPackageName(), "处于后台"
                                + appProcess.processName);
                        return true;
                    } else {
                        Log.i(context.getPackageName(), "处于前台"
                                + appProcess.processName);
                        return false;
                    }
                }
            }
        }

        return false;
    }

    /**
     * 判断当前应用是否是本应用
     */
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
    /**
     * 返回
     */
    public void back(View view) {
        finish();
    }

    private ProgressDialog _loadingDlg = null;

    protected final Object lock = new Object();

    /**
     * 显示装载中对话框
     *
     * @param msg 提示消息内容
     */
    public void showLoadingDlg(final String msg) {
        if (BaseActivity.this.isFinishing()) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    if (_loadingDlg == null) {
                        _loadingDlg = new ProgressDialog(BaseActivity.this);
                        _loadingDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        _loadingDlg.setIcon(R.drawable.ajax_loader);
                        _loadingDlg.setCancelable(true);
                    }

                    if (com.ths.plt.cordova.utils.StringUtils.isNotEmpty(msg)) {
                        _loadingDlg.setMessage(msg);
                    } else {
                        _loadingDlg.setMessage(getResources().getString(R.string.please_wait));
                    }

                    if (!_loadingDlg.isShowing()) {
                        _loadingDlg.show();
                    }
                }
            }
        });
    }

    /**
     * 隐藏加载中对话框
     */
    public void cancelLoadingDlg() {
        try {
            if (BaseActivity.this.isFinishing()) {
                return;
            }
            runOnUiThread(new Runnable() {
                public void run() {
                    if (_loadingDlg != null) {
                        if (_loadingDlg.isShowing()) {
                            _loadingDlg.cancel();
                        }
                    }
                }
            });
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    /**
     * 增加长时间的提示消息
     *
     * @param msg 提示消息
     */
    public void showLongMsg(final String msg) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.longMsg(msg);
                }
            });
        } catch (Exception ignored) {
        }
    }

    /**
     * 增加短时间的提示消息
     *
     * @param msg 提示消息
     */
    public void showShortMsg(final String msg) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.shortMsg(msg);
                }
            });
        } catch (Exception ignored) {
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.translate_to_right, R.anim.translate_to_right_hide);//activity间跳转动画
    }

    /**
     * 不带动画的finish
     */
    public void finishNoTransition() {
        super.finish();
        overridePendingTransition(0, 0);//activity间跳转动画
    }

}

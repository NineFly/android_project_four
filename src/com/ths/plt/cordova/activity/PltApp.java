package com.ths.plt.cordova.activity;

import android.app.Application;
import com.ths.plt.cordova.utils.PltAppUtil;
import es.dmoral.toasty.Toasty;

/**
 * Created by ruaho on 2017/11/24.
 * app类
 */
public class PltApp extends Application {

    private static PltHandler sHandler = null;

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化吐司参数
        Toasty.Config.getInstance().setTextSize(14).apply();
        //初始化 PltAppUtil
        PltAppUtil.setContext(this);
        if (sHandler == null) {
            sHandler = new PltHandler(getApplicationContext());
        }
        PltAppUtil.setHandler(sHandler);
        //

    }
}

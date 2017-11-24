package com.ths.plt.cordova.utils;

import android.content.Context;
import android.os.Handler;

/**
 * Created by ruaho on 2017/11/24.
 *
 */
public class PltAppUtil {
    private static Context context = null;

    public static void setContext(Context c){
        context = c;
    }

    public static Context getAppContext(){
        return context;
    }

    private static Handler handler = null;

    public static void setHandler(Handler mhandler){
        handler = mhandler;
    }

    public static Handler getHandler(){
        return handler;
    }

}

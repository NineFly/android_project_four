package com.ths.plt.cordova.activity;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
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
        //子线程初始化
        new Thread(new Runnable() {
            @Override
            public void run() {
                initImageLoader(getApplicationContext());
            }
        }).start();

    }

    private void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY);
        config.denyCacheImageMultipleSizesInMemory();
        config.memoryCacheSize((int) Runtime.getRuntime().maxMemory() / 4);
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 100 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        //修改连接超时时间5秒，下载超时时间5秒
        config.imageDownloader(new BaseImageDownloader(this, 5 * 1000, 5 * 1000));
        //		config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
}

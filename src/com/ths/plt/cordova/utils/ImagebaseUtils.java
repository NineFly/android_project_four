package com.ths.plt.cordova.utils;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.ths.plt.cordova.R;

public class ImagebaseUtils {
    //最大宽高：原图显示也得处理
    public static final int SCALE_IMAGE_MAX_WIDTH_OR_HEIGHT = 1080;//暂时改为1080；等王艳静弄完了，就
    public static final int SCALE_IMAGE_MAX_WIDTH = 1080;
    public static final int SCALE_IMAGE_MAX_HEIGHT = 1920;
    public static final int SCALE_IMAGE_WIDTH = 640;
    public static final int SCALE_IMAGE_HEIGHT = 960;
    public static final String SCALE_SIZE800 = "800"; //px
    public static final String SCALE_SIZE200 = "200"; //px
    public static final String SCALE_SIZE150 = "150"; //px
    public static final String SCALE_SIZE100 = "90"; //px
    public static final String SCALE_SIZE80 = "80"; //px

    public static DisplayImageOptions getDefaultOptions() {
        return defaultOptions;
    }

    //图片默认设置
    private static DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .showImageForEmptyUri(R.drawable.default_image)
            .showImageOnFail(R.drawable.default_image)
            .showImageOnLoading(R.drawable.default_image)
            .considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

    public static DisplayImageOptions getNullOptions() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }
}

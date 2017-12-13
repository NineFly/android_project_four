package com.ths.plt.cordova.imageloader;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ths.plt.cordova.helper.StorageHelper;
import com.ths.plt.cordova.utils.ImagebaseUtils;
import com.ths.plt.cordova.utils.PltAppUtil;

import java.io.File;

/**
 * Created by well on 15/6/1.
 */
public class ImageLoaderManager {

    private static ImageLoaderManager instance = new ImageLoaderManager();

    //会话图片加载对象
    private RuahoImageLoader chatImgLoader = null;
    //默认的图片加载对象
    private RuahoImageLoader defaultImgLoader = null;

    private ImageLoaderManager() {

    }

    public static ImageLoaderManager getInstance() {
        return instance;
    }


    /**
     * 重置图片管理对象
     */
    public void resetImageLoader() {
        destoryImageLoader(defaultImgLoader);
        destoryImageLoader(chatImgLoader);
    }

    /**
     * 获取会话图片加载对象
     *
     * @return
     */
    public RuahoImageLoader getChatImgLoader() {
        if (null == chatImgLoader) {
            RuahoImageLoader imageLoader = new RuahoImageLoader();
            initImageLoader(imageLoader, StorageHelper.getInstance().getFilePath());
            chatImgLoader = imageLoader;
        }
        return chatImgLoader;
    }

    /**
     * 获取默认图片加载对象
     *
     * @return
     */
    public ImageLoader getDefaultImgLoader() {
        if (null == defaultImgLoader) {
            RuahoImageLoader imageLoader = new RuahoImageLoader();
            initImageLoader(imageLoader, StorageHelper.getInstance().getImagePath());
            defaultImgLoader = imageLoader;
        }
        return defaultImgLoader;
    }

    public ImageLoader getImageLoader(String appId) {
        RuahoImageLoader imageLoader = new RuahoImageLoader();
        String filePath = StorageHelper.getInstance().getFilePath().toString()  + "/images";
        initImageLoader(imageLoader, new File(filePath));
        return imageLoader;
    }

    public ImageLoader getMomentsImgLoader() {
        return getChatImgLoader();
    }


    private void initImageLoader(RuahoImageLoader imageLoader, File storagePath) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(PltAppUtil.getAppContext());
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        //设置缓存为弱缓存，避免内存溢出
        config.memoryCache(new LRULimitedMemoryCache(5 * 1024 * 1024));
        config.memoryCacheSize(5 * 1024 * 1024);
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        //根据cpu数量来设置线程数
        config.threadPoolSize(Runtime.getRuntime().availableProcessors() + 1);
        config.diskCacheSize(50 * 1024 * 1024); // 50 MB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.defaultDisplayImageOptions(ImagebaseUtils.getDefaultOptions());
        config.diskCache(new RhImageDiskCache(storagePath));
        config.writeDebugLogs(); // Remove for release app
        config.imageDownloader(new RhImageDownloader(PltAppUtil.getAppContext()));
        // Initialize ImageLoader with configuration.
        imageLoader.init(config.build());
    }

    private void destoryImageLoader(ImageLoader imageLoader) {
        if (null != imageLoader) {
            imageLoader.destroy();
        }
        imageLoader = null;
    }


    /**
     * Created by well on 15/6/1.
     */
    public class RuahoImageLoader extends ImageLoader {
        public RuahoImageLoader() {
            super();
        }
    }

}



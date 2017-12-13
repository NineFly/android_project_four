package com.ths.plt.cordova.imageloader;


import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.IoUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Singletone for image loading and displaying at {@link ImageView ImageViews}<br />
 * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before any other method.
 * <p>
 * example: <code>
 * ImageLoaderService.getInstance().displayImage(url, imageView, ImageUtils.getNullOptions(),
 * new SimpleImageLoadingListener() {
 *
 * @Override public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
 * doSinglePicPix(loadedImage, imageView);
 * super.onLoadingComplete(imageUri, view, loadedImage);
 * }
 * }, ImageLoaderParam.getColleaguemageImageParam());
 * </code>
 */
public class ImageLoaderService {

    private static ImageLoaderService instance = new ImageLoaderService();

    private ImageLoaderService() {

    }

    public static ImageLoaderService getInstance() {
        return instance;
    }

    /**
     * Adds display image task to execution pool. Image will be set to ImageAware when it's turn. <br/>
     * Default {@linkplain DisplayImageOptions display image options} from {@linkplain ImageLoaderConfiguration
     * configuration} will be used.<br />
     * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
     *
     * @param uri        Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param imageAware {@linkplain ImageAware Image aware view}
     *                   which should display image
     * @throws IllegalStateException    if {@link #init(ImageLoaderConfiguration)} method wasn't called before
     * @throws IllegalArgumentException if passed <b>imageAware</b> is null
     */
    public void displayImage(String uri, ImageAware imageAware, ImageLoaderParam param) {
        displayImage(uri, imageAware, null, null, null, param);
    }

    /**
     * Adds display image task to execution pool. Image will be set to ImageAware when it's turn.<br />
     * Default {@linkplain DisplayImageOptions display image options} from {@linkplain ImageLoaderConfiguration
     * configuration} will be used.<br />
     * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
     *
     * @param uri        Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param imageAware {@linkplain ImageAware Image aware view}
     *                   which should display image
     * @param listener   {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires events on
     *                   UI thread if this method is called on UI thread.
     * @throws IllegalStateException    if {@link #init(ImageLoaderConfiguration)} method wasn't called before
     * @throws IllegalArgumentException if passed <b>imageAware</b> is null
     */
    public void displayImage(String uri, ImageAware imageAware, ImageLoadingListener listener, ImageLoaderParam param) {
        displayImage(uri, imageAware, null, listener, null, param);
    }

    /**
     * Adds display image task to execution pool. Image will be set to ImageAware when it's turn.<br />
     * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
     *
     * @param uri        Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param imageAware {@linkplain ImageAware Image aware view}
     *                   which should display image
     * @param options    {@linkplain DisplayImageOptions Options} for image
     *                   decoding and displaying. If <b>null</b> - default display image options
     *                   {@linkplain ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions)
     *                   from configuration} will be used.
     * @throws IllegalStateException    if {@link #init(ImageLoaderConfiguration)} method wasn't called before
     * @throws IllegalArgumentException if passed <b>imageAware</b> is null
     */
    public void displayImage(String uri, ImageAware imageAware, DisplayImageOptions options, ImageLoaderParam param) {
        displayImage(uri, imageAware, options, null, null, param);
    }

    /**
     * Adds display image task to execution pool. Image will be set to ImageAware when it's turn.<br />
     * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
     *
     * @param uri        Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param imageAware {@linkplain ImageAware Image aware view}
     *                   which should display image
     * @param options    {@linkplain DisplayImageOptions Options} for image
     *                   decoding and displaying. If <b>null</b> - default display image options
     *                   {@linkplain ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions)
     *                   from configuration} will be used.
     * @param listener   {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires events on
     *                   UI thread if this method is called on UI thread.
     * @throws IllegalStateException    if {@link #init(ImageLoaderConfiguration)} method wasn't called before
     * @throws IllegalArgumentException if passed <b>imageAware</b> is null
     */
    public void displayImage(String uri, ImageAware imageAware, DisplayImageOptions options,
                             ImageLoadingListener listener, ImageLoaderParam param) {
        displayImage(uri, imageAware, options, listener, null, param);
    }

    /**
     * Adds display image task to execution pool. Image will be set to ImageAware when it's turn.<br />
     * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
     *
     * @param uri              Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param imageAware       {@linkplain ImageAware Image aware view}
     *                         which should display image
     * @param options          {@linkplain DisplayImageOptions Options} for image
     *                         decoding and displaying. If <b>null</b> - default display image options
     *                         {@linkplain ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions)
     *                         from configuration} will be used.
     * @param listener         {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires
     *                         events on UI thread if this method is called on UI thread.
     * @param progressListener {@linkplain ImageLoadingProgressListener
     *                         Listener} for image loading progress. Listener fires events on UI thread if this method
     *                         is called on UI thread. Caching on disk should be enabled in
     *                         {@linkplain DisplayImageOptions options} to make
     *                         this listener work.
     * @throws IllegalStateException    if {@link #init(ImageLoaderConfiguration)} method wasn't called before
     * @throws IllegalArgumentException if passed <b>imageAware</b> is null
     */
    public void displayImage(String uri, ImageAware imageAware, DisplayImageOptions options,
                             final ImageLoadingListener listener, ImageLoadingProgressListener progressListener, ImageLoaderParam param) {
        uri = changeUri(param, uri);
        param.getLoader().displayImage(uri, imageAware, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                //处理用户的头像；

                if (listener != null) {
                    listener.onLoadingStarted(imageUri, view);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (listener != null) {
                    listener.onLoadingFailed(imageUri, view, failReason);
                }

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (listener != null) {
                    listener.onLoadingComplete(imageUri, view, loadedImage);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if (listener != null) {
                    listener.onLoadingCancelled(imageUri, view);
                }
            }
        }, progressListener);
    }

    /**
     * Adds display image task to execution pool. Image will be set to ImageView when it's turn. <br/>
     * Default {@linkplain DisplayImageOptions display image options} from {@linkplain ImageLoaderConfiguration
     * configuration} will be used.<br />
     * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
     *
     * @param uri       Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param imageView {@link ImageView} which should display image
     * @throws IllegalStateException    if {@link #init(ImageLoaderConfiguration)} method wasn't called before
     * @throws IllegalArgumentException if passed <b>imageView</b> is null
     */
    public void displayImage(String uri, ImageView imageView, ImageLoaderParam param) {
        displayImage(uri, new ImageViewAware(imageView), null, null, null, param);
    }

    /**
     * Adds display image task to execution pool. Image will be set to ImageView when it's turn.<br />
     * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
     *
     * @param uri       Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param imageView {@link ImageView} which should display image
     * @param options   {@linkplain DisplayImageOptions Options} for image
     *                  decoding and displaying. If <b>null</b> - default display image options
     *                  {@linkplain ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions)
     *                  from configuration} will be used.
     * @throws IllegalStateException    if {@link #init(ImageLoaderConfiguration)} method wasn't called before
     * @throws IllegalArgumentException if passed <b>imageView</b> is null
     */
    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options, ImageLoaderParam param) {
        displayImage(uri, new ImageViewAware(imageView), options, null, null, param);
    }

    /**
     * Adds display image task to execution pool. Image will be set to ImageView when it's turn.<br />
     * Default {@linkplain DisplayImageOptions display image options} from {@linkplain ImageLoaderConfiguration
     * configuration} will be used.<br />
     * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
     *
     * @param uri       Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param imageView {@link ImageView} which should display image
     * @param listener  {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires events on
     *                  UI thread if this method is called on UI thread.
     * @throws IllegalStateException    if {@link #init(ImageLoaderConfiguration)} method wasn't called before
     * @throws IllegalArgumentException if passed <b>imageView</b> is null
     */
    public void displayImage(String uri, ImageView imageView, ImageLoadingListener listener, ImageLoaderParam param) {
        displayImage(uri, new ImageViewAware(imageView), null, listener, null, param);
    }

    /**
     * Adds display image task to execution pool. Image will be set to ImageView when it's turn.<br />
     * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
     *
     * @param uri       Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param imageView {@link ImageView} which should display image
     * @param options   {@linkplain DisplayImageOptions Options} for image
     *                  decoding and displaying. If <b>null</b> - default display image options
     *                  {@linkplain ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions)
     *                  from configuration} will be used.
     * @param listener  {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires events on
     *                  UI thread if this method is called on UI thread.
     * @throws IllegalStateException    if {@link #init(ImageLoaderConfiguration)} method wasn't called before
     * @throws IllegalArgumentException if passed <b>imageView</b> is null
     */
    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options,
                             ImageLoadingListener listener, ImageLoaderParam param) {
        displayImage(uri, imageView, options, listener, null, param);
    }

    /**
     * Adds display image task to execution pool. Image will be set to ImageView when it's turn.<br />
     * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
     *
     * @param uri              Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param imageView        {@link ImageView} which should display image
     * @param options          {@linkplain DisplayImageOptions Options} for image
     *                         decoding and displaying. If <b>null</b> - default display image options
     *                         {@linkplain ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions)
     *                         from configuration} will be used.
     * @param listener         {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires
     *                         events on UI thread if this method is called on UI thread.
     * @param progressListener {@linkplain ImageLoadingProgressListener
     *                         Listener} for image loading progress. Listener fires events on UI thread if this method
     *                         is called on UI thread. Caching on disk should be enabled in
     *                         {@linkplain DisplayImageOptions options} to make
     *                         this listener work.
     * @throws IllegalStateException    if {@link #init(ImageLoaderConfiguration)} method wasn't called before
     * @throws IllegalArgumentException if passed <b>imageView</b> is null
     */
    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options,
                             final ImageLoadingListener listener, ImageLoadingProgressListener progressListener, ImageLoaderParam param) {
        displayImage(uri, new ImageViewAware(imageView), options, listener, progressListener, param);
    }

    /**
     * Adds load image task to execution pool. Image will be returned with
     * {@link ImageLoadingListener#onLoadingComplete(String, android.view.View, Bitmap)} callback}.
     * <br />
     * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
     *
     * @param uri      Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param listener {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires events on UI
     *                 thread if this method is called on UI thread.
     * @throws IllegalStateException if {@link #init(ImageLoaderConfiguration)} method wasn't called before
     */
    public void loadImage(String uri, ImageLoadingListener listener, ImageLoaderParam param) {
        loadImage(uri, null, null, listener, null, param);
    }

    /**
     * Adds load image task to execution pool. Image will be returned with
     * {@link ImageLoadingListener#onLoadingComplete(String, android.view.View, Bitmap)} callback}.
     * <br />
     * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
     *
     * @param uri             Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param targetImageSize Minimal size for {@link Bitmap} which will be returned in
     *                        {@linkplain ImageLoadingListener#onLoadingComplete(String, android.view.View,
     *                        Bitmap)} callback}. Downloaded image will be decoded
     *                        and scaled to {@link Bitmap} of the size which is <b>equal or larger</b> (usually a bit
     *                        larger) than incoming targetImageSize.
     * @param listener        {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires
     *                        events on UI thread if this method is called on UI thread.
     * @throws IllegalStateException if {@link #init(ImageLoaderConfiguration)} method wasn't called before
     */
    public void loadImage(String uri, ImageSize targetImageSize, ImageLoadingListener listener, ImageLoaderParam param) {
        loadImage(uri, targetImageSize, null, listener, null, param);
    }

    /**
     * Adds load image task to execution pool. Image will be returned with
     * {@link ImageLoadingListener#onLoadingComplete(String, android.view.View, Bitmap)} callback}.
     * <br />
     * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
     *
     * @param uri      Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param options  {@linkplain DisplayImageOptions Options} for image
     *                 decoding and displaying. If <b>null</b> - default display image options
     *                 {@linkplain ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions) from
     *                 configuration} will be used.<br />
     * @param listener {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires events on UI
     *                 thread if this method is called on UI thread.
     * @throws IllegalStateException if {@link #init(ImageLoaderConfiguration)} method wasn't called before
     */
    public void loadImage(String uri, DisplayImageOptions options, ImageLoadingListener listener, ImageLoaderParam param) {
        loadImage(uri, null, options, listener, null, param);
    }

    /**
     * Adds load image task to execution pool. Image will be returned with
     * {@link ImageLoadingListener#onLoadingComplete(String, android.view.View, Bitmap)} callback}.
     * <br />
     * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
     *
     * @param uri             Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param targetImageSize Minimal size for {@link Bitmap} which will be returned in
     *                        {@linkplain ImageLoadingListener#onLoadingComplete(String, android.view.View,
     *                        Bitmap)} callback}. Downloaded image will be decoded
     *                        and scaled to {@link Bitmap} of the size which is <b>equal or larger</b> (usually a bit
     *                        larger) than incoming targetImageSize.
     * @param options         {@linkplain DisplayImageOptions Options} for image
     *                        decoding and displaying. If <b>null</b> - default display image options
     *                        {@linkplain ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions)
     *                        from configuration} will be used.<br />
     * @param listener        {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires
     *                        events on UI thread if this method is called on UI thread.
     * @throws IllegalStateException if {@link #init(ImageLoaderConfiguration)} method wasn't called before
     */
    public void loadImage(String uri, ImageSize targetImageSize, DisplayImageOptions options,
                          ImageLoadingListener listener, ImageLoaderParam param) {
        loadImage(uri, targetImageSize, options, listener, null, param);
    }

    /**
     * Adds load image task to execution pool. Image will be returned with
     * {@link ImageLoadingListener#onLoadingComplete(String, android.view.View, Bitmap)} callback}.
     * <br />
     * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
     *
     * @param uri              Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param targetImageSize  Minimal size for {@link Bitmap} which will be returned in
     *                         {@linkplain ImageLoadingListener#onLoadingComplete(String, android.view.View,
     *                         Bitmap)} callback}. Downloaded image will be decoded
     *                         and scaled to {@link Bitmap} of the size which is <b>equal or larger</b> (usually a bit
     *                         larger) than incoming targetImageSize.
     * @param options          {@linkplain DisplayImageOptions Options} for image
     *                         decoding and displaying. If <b>null</b> - default display image options
     *                         {@linkplain ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions)
     *                         from configuration} will be used.<br />
     * @param listener         {@linkplain ImageLoadingListener Listener} for image loading process. Listener fires
     *                         events on UI thread if this method is called on UI thread.
     * @param progressListener {@linkplain ImageLoadingProgressListener
     *                         Listener} for image loading progress. Listener fires events on UI thread if this method
     *                         is called on UI thread. Caching on disk should be enabled in
     *                         {@linkplain DisplayImageOptions options} to make
     *                         this listener work.
     * @throws IllegalStateException if {@link #init(ImageLoaderConfiguration)} method wasn't called before
     */
    public void loadImage(String uri, ImageSize targetImageSize, DisplayImageOptions options,
                          ImageLoadingListener listener, ImageLoadingProgressListener progressListener, ImageLoaderParam param) {
        uri = changeUri(param, uri);
        param.getLoader().loadImage(uri, targetImageSize, options, listener, progressListener);
    }

    /**
     * Loads and decodes image synchronously.<br />
     * Default display image options
     * {@linkplain ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions) from
     * configuration} will be used.<br />
     * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
     *
     * @param uri Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @return Result image Bitmap. Can be <b>null</b> if image loading/decoding was failed or cancelled.
     * @throws IllegalStateException if {@link #init(ImageLoaderConfiguration)} method wasn't called before
     */
    public Bitmap loadImageSync(String uri, ImageLoaderParam param) {
        return loadImageSync(uri, null, null, param);
    }

    /**
     * Loads and decodes image synchronously.<br />
     * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
     *
     * @param uri     Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param options {@linkplain DisplayImageOptions Options} for image
     *                decoding and scaling. If <b>null</b> - default display image options
     *                {@linkplain ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions) from
     *                configuration} will be used.
     * @return Result image Bitmap. Can be <b>null</b> if image loading/decoding was failed or cancelled.
     * @throws IllegalStateException if {@link #init(ImageLoaderConfiguration)} method wasn't called before
     */
    public Bitmap loadImageSync(String uri, DisplayImageOptions options, ImageLoaderParam param) {
        return loadImageSync(uri, null, options, param);
    }

    /**
     * Loads and decodes image synchronously.<br />
     * Default display image options
     * {@linkplain ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions) from
     * configuration} will be used.<br />
     * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
     *
     * @param uri             Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param targetImageSize Minimal size for {@link Bitmap} which will be returned. Downloaded image will be decoded
     *                        and scaled to {@link Bitmap} of the size which is <b>equal or larger</b> (usually a bit
     *                        larger) than incoming targetImageSize.
     * @return Result image Bitmap. Can be <b>null</b> if image loading/decoding was failed or cancelled.
     * @throws IllegalStateException if {@link #init(ImageLoaderConfiguration)} method wasn't called before
     */
    public Bitmap loadImageSync(String uri, ImageSize targetImageSize, ImageLoaderParam param) {
        return loadImageSync(uri, targetImageSize, null, param);
    }

    /**
     * Loads and decodes image synchronously.<br />
     * <b>NOTE:</b> {@link #init(ImageLoaderConfiguration)} method must be called before this method call
     *
     * @param uri             Image URI (i.e. "http://site.com/image.png", "file:///mnt/sdcard/image.png")
     * @param targetImageSize Minimal size for {@link Bitmap} which will be returned. Downloaded image will be decoded
     *                        and scaled to {@link Bitmap} of the size which is <b>equal or larger</b> (usually a bit
     *                        larger) than incoming targetImageSize.
     * @param options         {@linkplain DisplayImageOptions Options} for image
     *                        decoding and scaling. If <b>null</b> - default display image options
     *                        {@linkplain ImageLoaderConfiguration.Builder#defaultDisplayImageOptions(DisplayImageOptions)
     *                        from configuration} will be used.
     * @return Result image Bitmap. Can be <b>null</b> if image loading/decoding was failed or cancelled.
     * @throws IllegalStateException if {@link #init(ImageLoaderConfiguration)} method wasn't called before
     */
    public Bitmap loadImageSync(String uri, ImageSize targetImageSize, DisplayImageOptions options, ImageLoaderParam param) {
        uri = changeUri(param, uri);
        return param.getLoader().loadImageSync(uri, targetImageSize, options);
    }


    /**
     * 保存图片
     *
     * @param imageUri    - 图片的远程url
     * @param imageStream - 该函数不回关闭
     * @param listener    - listener
     * @param param       - image loader param
     * @return
     * @throws IOException
     */
    public boolean save(String imageUri, InputStream imageStream, IoUtils.CopyListener listener, ImageLoaderParam param) throws IOException {
        imageUri = changeUri(param, imageUri);
        return param.getLoader().getDiskCache().save(imageUri, imageStream, listener);
    }

    /**
     * 保存图片
     *
     * @param imageUri - 图片的远程url
     * @param bitmap   - bitmap
     * @param param    - image loader param
     * @return
     * @throws IOException
     */
    public boolean save(String imageUri, Bitmap bitmap, ImageLoaderParam param) throws IOException {
        imageUri = changeUri(param, imageUri);
        return param.getLoader().getDiskCache().save(imageUri, bitmap);
    }



    /**
     * 取消图片显示任务
     *
     * @param imageView - {@link ImageView} which should display image
     * @param param     - imageloader param
     */
    public void cancelDisplayTask(ImageView imageView, ImageLoaderParam param) {
        param.getLoader().cancelDisplayTask(imageView);
    }

    /**
     * 删除图片缓存
     *
     * @param imageUri - 图片url
     * @param param    - imageloader param
     */
    public void removeImage(String imageUri, ImageLoaderParam param) {
        imageUri = changeUri(param, imageUri);
        MemoryCacheUtils.removeFromCache(imageUri, param.getLoader().getMemoryCache());
        DiskCacheUtils.removeFromCache(imageUri, param.getLoader().getDiskCache());
    }


    private String changeUri(ImageLoaderParam param, String uri) {
        if (uri.startsWith("http") || uri.startsWith("https")) {
            if (null != param.getCategory() && 0 < param.getCategory().length()) {
                String categoryKey = "IMG_CATEGORY=";
                if (1 < uri.indexOf("?")) {
                    uri = uri + "&" + categoryKey + param.getCategory();
                } else {
                    uri = uri + "?" + categoryKey + param.getCategory();
                }
            }
        }
        return uri;
    }
}

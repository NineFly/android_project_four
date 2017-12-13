package com.ths.plt.cordova.imageloader;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by well on 15/6/1.
 *
 */
public class ImageLoaderParam {
	public static final String FILE__COLLEAGUE = "S__COLLEAGUE";
	public static final String FILE__TASK = "S__TASK";
	public static final String FILE__FAVORITES = "S__FAVORITES";
    public static final String FILE_MAIL="cc_open_mail";
    public static final String app_xwalkcore="app_xwalkcore";
    private String category = "";

    private ImageLoader loader = null;

	private ImageLoaderParam() {}

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return this.category;
    }

    public void setLoader(ImageLoader loader) {
        this.loader = loader;
    }

    public ImageLoader getLoader() {
        return this.loader;
    }




/**
 *
 * 获取默认图片加载参数
 *
 **/
    public static ImageLoaderParam getDefaultImageParam(){
        ImageLoaderParam param = new ImageLoaderParam();
        param.setLoader(ImageLoaderManager.getInstance().getDefaultImgLoader());
        return param;
    }


    /**
     * 获取应用加载参数
     */
    public static ImageLoaderParam getAppParam(String appId) {
        ImageLoaderParam param = new ImageLoaderParam();
        param.setLoader(ImageLoaderManager.getInstance().getImageLoader(appId));
        return param;
    }

    /**
 *
 * 获取头像加载参数
 *
 */
    public static ImageLoaderParam getIconImageParam() {
        ImageLoaderParam param = new ImageLoaderParam();
        param.setLoader(ImageLoaderManager.getInstance().getDefaultImgLoader());
        return param;
    }


/**
 *
 * 获取同事圈图片加载参数
 *
 **/
    public static ImageLoaderParam  getMomentsImageParam(){
        ImageLoaderParam param = new ImageLoaderParam();
        param.setLoader(ImageLoaderManager.getInstance().getChatImgLoader());
        param.setCategory(FILE__COLLEAGUE);
        return param;
    }

	/**
	 *
	 * 获取任务圈图片加载参数
	 *
	 **/
	public static ImageLoaderParam  getTaskImageParam(){
		ImageLoaderParam param = new ImageLoaderParam();
		param.setLoader(ImageLoaderManager.getInstance().getChatImgLoader());
		param.setCategory(FILE__TASK);
		return param;
	}

    /**
     *
     * 获取对应servId的大图缓存参数
     *
     **/
    public static ImageLoaderParam  getServImageParam(String servId){
        ImageLoaderParam param = new ImageLoaderParam();
        param.setLoader(ImageLoaderManager.getInstance().getChatImgLoader());
        param.setCategory(servId);
        return param;
    }
/**
 *
 * 获取收藏图片加载参数
 *
 **/
    public static ImageLoaderParam  getFavoritesImageParam(){
        ImageLoaderParam param = new ImageLoaderParam();
        param.setLoader(ImageLoaderManager.getInstance().getChatImgLoader());
        param.setCategory(FILE__FAVORITES);
        return param;
    }

/**
 *
 * 获取会话图片加载参数
 * @param fullId 会话fullID
 *
 **/
    public static ImageLoaderParam getChatImageParam(String fullId){
        ImageLoaderParam param = new ImageLoaderParam();
        param.setLoader(ImageLoaderManager.getInstance().getChatImgLoader());
        param.setCategory(fullId);
        return param;
    }

}

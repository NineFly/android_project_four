package com.ths.plt.cordova.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;

import com.ths.plt.cordova.utils.PltAppUtil;
import com.ths.plt.cordova.utils.ToastUtils;

import java.io.File;
import java.math.BigDecimal;

public class StorageHelper {
    public static String pathPrefix;
    public static final String historyPathName = "/service/";
    public static final String imagePathName = "/image/";
    public static final String voicePathName = "/voice/";
    public static final String filePathName = "/file/";
    public static final String videoPathName = "/video/";
    public static final String databasePathName = "/database/";
    public static final String webappName = "/www/";
    public static final String logPathName = "log/";
    public static final String netdiskDownloadPathName = "/netdisk/";
    public static final String meetingPathName = "/meeting/";
    private static File storageDir = null;
    private static StorageHelper instance = null;
    private File voicePath = null;
    private File imagePath = null;
    private File faceRecogPath = null;
    private File historyPath = null;
    private File videoPath = null;
    private File filePath = null;
    private File basePath = null;
    private File databasePath = null;
    private File webappPath = null;
    private File logPath = null;
    private File logsPath = null;
    private File ziplogPath1 = null;
    public static final String logsPathName = "logs/";

    public static final String logPathName_zip = "ziplog/";
    /**
     *
     * @return 压缩文件的去处
     */
    public File getZipLogPath(String uploadDate) {
        //先建文件夹 ， 再新建文件
        File ziplogPath = new File(getZipLogPathDir().getAbsolutePath(),uploadDate+".log.zip");
        return ziplogPath;
    }

    /**
     *
     * @return 压缩文件的去处
     */
    public File getZipDbPath() {
        //先建文件夹 ， 再新建文件
        File ziplogPath = new File(getZipLogPathDir().getAbsolutePath(), ".db.zip");
        return ziplogPath;
    }

    /**
     *
     * @return 压缩文件存放目录
     */
    public File getZipLogPathDir() {
        if(ziplogPath1 == null) {
            String str = pathPrefix + logPathName_zip;
            ziplogPath1 = new File(getStorageDir(PltAppUtil.getAppContext()), str);
            if(!ziplogPath1.exists()) {
                ziplogPath1.mkdirs();
            }
        }
        return ziplogPath1;
    }

    /**
     *
     * @return 系统普通日志文件目录，如果目录不存在，则创建目录。
     */
    public File getLogsPath() {
        if(logsPath == null) {
            String str = pathPrefix + logsPathName;
            logsPath = new File(getStorageDir(PltAppUtil.getAppContext()), str);
            if(!logsPath.exists()) {
                logPath.mkdirs();
            }
        }

        return logsPath;
    }

    private static final String defaultWebappPath = "/android_asset/www/";

    private StorageHelper() {
        Context context = PltAppUtil.getAppContext();
        pathPrefix = "/Android/data/" + context.getPackageName() + "/";
    }

    public static StorageHelper getInstance() {
        if (instance == null)
            instance = new StorageHelper();
        return instance;
    }

    /**
     * 初始化 <br>
     * 请在登录成功后调用
     */
    public void init() {
        imagePath = null;
        voicePath = null;
        filePath = null;
        videoPath = null;
        databasePath = null;
        faceRecogPath = null;
    }

    /**
     * 获取图片存储路径
     *
     * @return
     */
    public File getImagePath() {
        if (imagePath == null || 0 == imagePath.length()) {
            Context context = PltAppUtil.getAppContext();
            String userCode = "123";
            String servName = getServName();
            this.imagePath = generateImagePath(servName, userCode, context);
            if (!(this.imagePath.exists())) {
                this.imagePath.mkdirs();
            }
        }
        return this.imagePath;
    }

    /**
     * 获取图片存储路径
     *
     * @return
     */
    public File getFaceRecogPath() {
        if (faceRecogPath == null || 0 == faceRecogPath.length()) {
            Context context = PltAppUtil.getAppContext();
            String userCode = "123";
            String servName = getServName();
            this.faceRecogPath = generateImagePath(servName, userCode, context);
            if (!(this.faceRecogPath.exists())) {
                this.faceRecogPath.mkdirs();
            }
        }
        return this.faceRecogPath;
    }

    //取得gif图片缓存
    public String getGifPath() {
        String localFilePath = getImagePath().getAbsolutePath() + "/"+"mygif" ;
        File file=new File(localFilePath);
        if (!file.exists()){
            file.mkdirs();
        }
        return localFilePath;
    }
    /**
     * 取得根路径/storage/sdcard0/Android/data/com.ruaho.function/
     */
    public File getStoragePath() {
        Context context = PltAppUtil.getAppContext();
        return new File(getStorageDir(context), pathPrefix);
    }

    /**
     * 取得真实的相册地址，拍照用
     */
    public File getRealCameraPath() {
        Context context = PltAppUtil.getAppContext();
        if (!new File(getStorageDir(context), "/DCIM/Camera/").exists()){
            new File(getStorageDir(context), "/DCIM/Camera/").mkdirs();
        }
        return new File(getStorageDir(context), "/DCIM/Camera/");
    }

    /**
     * 是否包下的文件
     */
    public static boolean isStoragePath(String filePath) {
        boolean result = false;

        if (!TextUtils.isEmpty(filePath)
                && filePath.startsWith(getInstance().getStoragePath().getAbsolutePath())) {
            result = true;
        }

        return result;
    }

    /**
     * 获取聊天图片存储路径
     *
     * @param fullId 会话ID
     * @return
     */
    public File getChatImagePath(String fullId) {
        File imagePath = getImagePath();
        if (null != fullId && 0 < fullId.length()) {
            String chatImagePath = imagePath.getAbsolutePath() + "/" + fullId;
            imagePath = new File(chatImagePath);
            if (!imagePath.exists()) {
                imagePath.mkdirs();
            }
        }
        return imagePath;
    }

    /**
     * 获取聊天背景图片的路径
     *
     * @return
     */
    public File getBackImagePath() {
        File imagePath = getImagePath();
        String backImagePath = imagePath.getAbsolutePath() + "/" + "background";
        imagePath = new File(backImagePath);
        if (!imagePath.exists()) {
            imagePath.mkdirs();
        }
        return imagePath;
    }


    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void DeleteFile(final Context context, File file) {
        if (file.exists() == false) {
            ToastUtils.shortMsg("文件夹不存在");
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(context, f);
                }
                file.delete();
            }
        }
        ToastUtils.shortMsg("删除成功");
    }

    /**
     * 获取数据库存储路径
     *
     * @return
     */
    public File getDatabasePath() {
        if (databasePath == null || 0 == databasePath.length()) {
            Context context = PltAppUtil.getAppContext();
            // TODO: 2017/7/8 缺一个用户UserCode
            String userCode = "123";
            String servName = getServName();
            this.databasePath = generateDatabasePath(servName, userCode, context);
            if (!(this.databasePath.exists())) {
                this.databasePath.mkdirs();
            }
        }
        return this.databasePath;
    }


    /**
     * 改变系统webapp资源文件夹配置,
     *
     * @param useDefault - 是否使用默认路径. <br>
     *                   默认路径: file:///android_asset/www/
     *                   自定义路径: file:/storage/data/com.ruaho.function/www/
     */
    public void changeWebappPath(boolean useDefault) {
        SharedPreferences sharedPreferences = PltAppUtil.getAppContext().getSharedPreferences("defaultWebapp", Context.MODE_PRIVATE); //私有数据
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("defaultWebapp", useDefault);
        editor.commit();//提交修改
    }

    /**
     * 获取文件夹大小
     */
    public long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                //如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * file大小转换
     *
     * @param size file所占空间的long值
     */
    public String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    public File getCurrentWebappPath() {
        SharedPreferences sharedPreferences = PltAppUtil.getAppContext().getSharedPreferences("defaultWebapp", Context.MODE_PRIVATE); //私有数据
        boolean isDefault = sharedPreferences.getBoolean("defaultWebapp", true);
        if (isDefault) {
            return new File(defaultWebappPath);
        }

        if (webappPath == null || 0 == webappPath.length()) {
            Context context = PltAppUtil.getAppContext();
            // TODO: 2017/7/8 缺一个用户UserCode
            String userCode = "123";
            String servName = getServName();
            this.webappPath = generateWebSourcePath(servName, userCode, context);
            if (!(this.webappPath.exists())) {
                this.webappPath.mkdirs();
            }
        }
        return this.webappPath;
    }


    /**
     * 声音文件存储路径
     *
     * @return
     */
    public File getVoicePath() {
        if (voicePath == null || 0 == voicePath.length()) {
            Context context = PltAppUtil.getAppContext();
            // TODO: 2017/7/8 缺一个用户UserCode
            String userCode = "123";
            String servName = getServName();
            this.voicePath = generateVoicePath(servName, userCode, context);
            if (!(this.voicePath.exists()))
                this.voicePath.mkdirs();

        }
        return this.voicePath;
    }

    /**
     * 文件存储路径
     *
     * @return
     */
    public File getFilePath() {
        if (filePath == null || 0 == filePath.length()) {
            Context context = PltAppUtil.getAppContext();
            // TODO: 2017/7/8 缺一个用户UserCode
            String userCode = "123";
            String servName = getServName();
            this.filePath = generateFiePath(servName, userCode, context);
            if (!this.filePath.exists()) {
                this.filePath.mkdirs();
            }
        }
        return this.filePath;
    }
    public File getBasePath(){
        if (basePath == null || 0 == basePath.length()) {
            Context context = PltAppUtil.getAppContext();
            // TODO: 2017/7/8 缺一个用户UserCode
            String userCode = "123";
            String servName = getServName();
            this.basePath = generateBasePath(servName, userCode, context);
            if (!this.basePath.exists()) {
                this.basePath.mkdirs();
            }
        }
        return this.basePath;
    }
    /**
     * 视频文件存储路径
     *
     * @return
     */
    public File getVideoPath() {
        if (videoPath == null || 0 == videoPath.length()) {
            Context context = PltAppUtil.getAppContext();
            // TODO: 2017/7/8 缺一个用户UserCode
            String userCode = "123";
            String servName = getServName();
            this.videoPath = generateVideoPath(servName, userCode, context);
            if (!(this.videoPath.exists()))
                this.videoPath.mkdirs();
        }
        return this.videoPath;
    }

    /**
     * @return 系统崩溃日志文件目录，如果目录不存在，则创建目录。
     */
    public File getLogPath() {
        if (logPath == null) {
            String str = pathPrefix + logPathName;
            logPath = new File(getStorageDir(PltAppUtil.getAppContext()), str);
            if (!logPath.exists()) {
                logPath.mkdirs();
            }
        }

        return logPath;
    }

    /**
     * 历史文件存储路径
     *
     * @return
     */
    public File getHistoryPath() {
        if (historyPath == null || 0 == historyPath.length()) {
            Context context = PltAppUtil.getAppContext();
            // TODO: 2017/7/8 缺一个用户UserCode
            String userCode = "123";
            String servName = getServName();
            this.historyPath = generateHistoryPath(servName, userCode, context);
            if (!(this.historyPath.exists()))
                this.historyPath.mkdirs();
        }

        return this.historyPath;
    }

    /**
     * 清除本地存储
     */
    public void clearStorage() {
        //TODO
    }

    private static File getStorageDir(Context paramContext) {
        if (storageDir == null) {
            File localFile = Environment.getExternalStorageDirectory();
            if (localFile.exists())
                return localFile;
            storageDir = paramContext.getFilesDir();
        }
        return storageDir;
    }

    private static File generateImagePath(String servName, String userCode, Context paramContext) {
        String str = null;
        if (servName == null)
            str = pathPrefix + userCode + imagePathName;
        else
            str = pathPrefix + servName + "/" + userCode + imagePathName;
        return new File(getStorageDir(paramContext), str);
    }

    private static File generateDatabasePath(String servName, String userCode, Context paramContext) {
        String str = null;
        if (servName == null)
            str = pathPrefix + userCode + databasePathName;
        else
            str = pathPrefix + servName + "/" + userCode + databasePathName;
        return new File(getStorageDir(paramContext), str);
    }

    private static File generateWebSourcePath(String servName, String userCode, Context paramContext) {
        String str = pathPrefix + webappName;
        return new File(getStorageDir(paramContext), str);
    }


    private static File generateVoicePath(String servName, String userCode, Context paramContext) {
        String str = null;
        if (servName == null)
            str = pathPrefix + userCode + filePathName;
        else
            str = pathPrefix + servName + "/" + userCode + filePathName;
        return new File(getStorageDir(paramContext), str);
    }

    private static File generateFiePath(String servName, String userCode, Context paramContext) {
        String str = null;
        if (servName == null)
            str = pathPrefix + userCode + filePathName;
        else
            str = pathPrefix + servName + "/" + userCode + filePathName;
        return new File(getStorageDir(paramContext), str);
    }
    private static File generateBasePath(String servName, String userCode, Context paramContext) {
        String str = null;
        if (servName == null)
            str = pathPrefix + userCode ;
        else
            str = pathPrefix + servName + "/" + userCode ;
        return new File(getStorageDir(paramContext), str);
    }

    private static File generateVideoPath(String servName, String userCode, Context paramContext) {
        String str = null;
        if (servName == null)
            str = pathPrefix + userCode + videoPathName;
        else
            str = pathPrefix + servName + "/" + userCode + videoPathName;
        return new File(getStorageDir(paramContext), str);
    }

    private static File generateHistoryPath(String servName, String paramString2, Context paramContext) {
        String str = null;
        if (servName == null)
            str = pathPrefix + paramString2 + "/service/";
        else
            str = pathPrefix + servName + "/" + paramString2 + "/service/";
        return new File(getStorageDir(paramContext), str);
    }

    private static File generateMessagePath(String servName, String userCode, Context paramContext) {
        File localFile = new File(generateHistoryPath(servName, userCode, paramContext), userCode + File.separator + "Msg.db");
        return localFile;
    }

    public static File getTempPath() {
        return Environment.getDownloadCacheDirectory();
    }

    private static String getServName() {
        String servName = "123";
        //String servName = ServiceContext.getHttpServer().replace("://", "");
        return servName;
    }
}

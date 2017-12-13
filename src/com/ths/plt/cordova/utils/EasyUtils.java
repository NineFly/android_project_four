package com.ths.plt.cordova.utils;

import android.app.ActivityManager;
import android.content.Context;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.Security;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;

public class EasyUtils
{
  private static final String TAG = "EasyUtils";
  private static Hashtable<String, String> resourceTable = new Hashtable();

  public static boolean isAppRunningForeground(Context paramContext)
  {
    ActivityManager localActivityManager = (ActivityManager)paramContext.getSystemService("activity");
    List localList = localActivityManager.getRunningTasks(1);
    return paramContext.getPackageName().equalsIgnoreCase(((ActivityManager.RunningTaskInfo)localList.get(0)).baseActivity.getPackageName());
  }

  public static String getTopActivityName(Context paramContext)
  {
    ActivityManager localActivityManager = (ActivityManager)paramContext.getSystemService("activity");
    List localList = localActivityManager.getRunningTasks(2147483647);
    return ((ActivityManager.RunningTaskInfo)localList.get(0)).topActivity.getClassName();
  }

  public static String getTimeStamp()
  {
    Date localDate = new Date(System.currentTimeMillis());
    SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    return localSimpleDateFormat.format(localDate);
  }

  public static boolean writeToZipFile(byte[] paramArrayOfByte, String paramString)
  {
    FileOutputStream localFileOutputStream = null;
    GZIPOutputStream localGZIPOutputStream = null;
    try
    {
      localFileOutputStream = new FileOutputStream(paramString);
      localGZIPOutputStream = new GZIPOutputStream(new BufferedOutputStream(localFileOutputStream));
      localGZIPOutputStream.write(paramArrayOfByte);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      return false;
    }
    finally
    {
      if (localGZIPOutputStream != null)
        try
        {
          localGZIPOutputStream.close();
        }
        catch (IOException localIOException3)
        {
          localIOException3.printStackTrace();
        }
      if (localFileOutputStream != null)
        try
        {
          localFileOutputStream.close();
        }
        catch (IOException localIOException4)
        {
          localIOException4.printStackTrace();
        }
    }

    return true;
  }

  public static String getAppResourceString(Context paramContext, String paramString)
  {
    String str = (String)resourceTable.get(paramString);
    if (str != null)
      return str;
    int i = paramContext.getResources().getIdentifier(paramString, "string", paramContext.getPackageName());
    str = paramContext.getString(i);
    if (str != null)
      resourceTable.put(paramString, str);
    return str;
  }

  public static String convertByteArrayToString(byte[] paramArrayOfByte)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (byte b : paramArrayOfByte)
      localStringBuffer.append(String.format("0x%02X", new Object[] { Byte.valueOf(b) }));
    return localStringBuffer.toString();
  }

    /**
     * 获取 sllcontext
     * @return
     */
    public static SSLContext getSSLContext() {
        SSLContext context = null;
        try {
             context = EasyUtils.createSSLContext(null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context;
    }

    private static SSLContext createSSLContext(String keyStoreFilePath,
                                               String keyStoreFilePassword) throws Exception {

        // disableSslVerification
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs,
                                           String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs,
                                           String authType) {
            }

            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] arg0, String arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] arg0, String arg1) {
                // TODO Auto-generated method stub

            }
        } };

        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        // ssl context init
        String algorithm = Security
                .getProperty("ssl.KeyManagerFactory.algorithm");
        if (algorithm == null) {
            algorithm = "SunX509";
        }

        SSLContext serverContext = null;
        if (null != keyStoreFilePath && null != keyStoreFilePassword ) {
            System.setProperty("javax.net.ssl.trustStore", keyStoreFilePath);
            //使用本地证书
            KeyStore ks = KeyStore.getInstance("JKS");
            FileInputStream fin = new FileInputStream(keyStoreFilePath);
            ks.load(fin, keyStoreFilePassword.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
            kmf.init(ks, keyStoreFilePassword.toCharArray());
            serverContext = SSLContext.getInstance("TLS");
            serverContext.init(kmf.getKeyManagers(), trustAllCerts, new java.security.SecureRandom());
        } else {
            serverContext = SSLContext.getInstance("SSL");
            serverContext.init(null, trustAllCerts, new java.security.SecureRandom());
        }

        return serverContext;
    }
}

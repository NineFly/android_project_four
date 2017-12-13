package com.ths.plt.cordova.imageloader;

import android.content.Context;
import android.net.Uri;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.ths.plt.cordova.utils.EasyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

/**
 * Provides retrieving of {@link InputStream} of image by URI from network or file system or app resources.<br />
 * {@link URLConnection} is used to retrieve image stream from network.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.0
 */
public class RhImageDownloader extends BaseImageDownloader {

    public RhImageDownloader(Context context) {
        super(context);
    }

    /**
     * Create {@linkplain HttpURLConnection HTTP connection} for incoming URL
     *
     * @param url   URL to connect to
     * @param extra Auxiliary object which was passed to {@link DisplayImageOptions.Builder#extraForDownloader(Object)
     *              DisplayImageOptions.extraForDownloader(Object)}; can be null
     * @return {@linkplain HttpURLConnection Connection} for incoming URL. Connection isn't established so it still configurable.
     * @throws IOException if some I/O error occurs during network request or if no InputStream could be created for
     *                     URL.
     */
    protected HttpURLConnection createConnection(String url, Object extra) throws IOException {
        String encodedUrl = Uri.encode(url, ALLOWED_URI_CHARS);
//        HttpURLConnection conn = (HttpURLConnection) new URL(encodedUrl).openConnection();

        HttpURLConnection conn = (HttpURLConnection) new URL(encodedUrl).openConnection();
        //支持https
        if (conn instanceof HttpsURLConnection) {
            SSLContext context = EasyUtils.getSSLContext();
            if (context != null) {
                ((HttpsURLConnection) conn).setSSLSocketFactory(context.getSocketFactory());
            }
        }

        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeout);
        return conn;
    }

}


package com.ths.plt.cordova.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eralp.circleprogressview.CircleProgressView;
import com.shizhefei.view.largeimage.LargeImageView;
import com.squareup.picasso.Picasso;
import com.ths.plt.cordova.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 图片预览界面
 */
public class PhotoActivity extends Activity {
    private PhotoViewAttacher mAttacher;

    private ImageView photo;
    private String imageUrl;

    private ImageButton closeBtn;
    private ImageButton shareBtn;

    private TextView titleTxt;
    protected ViewPager mViewPager;
    private JSONObject options;
    private int shareBtnVisibility;
    protected PagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        initView();
        initData();
        initEvent();

    }
    private void initView() {
        closeBtn = (ImageButton) findViewById(R.id.closeBtn);
        shareBtn = (ImageButton) findViewById(R.id.shareBtn);

        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);

        photo = (ImageView) findViewById(R.id.photoView);
        mAttacher = new PhotoViewAttacher(photo);

        titleTxt = (TextView) findViewById(R.id.titleTxt);
    }

    private void initData() {
        try {
            options = new JSONObject(this.getIntent().getStringExtra("options"));
            shareBtnVisibility = options.getBoolean("share") ? View.VISIBLE : View.INVISIBLE;
        } catch (JSONException exception) {
            shareBtnVisibility = View.VISIBLE;
        }
        shareBtn.setVisibility(shareBtnVisibility);

        // Change the Activity Title
        String actTitle = this.getIntent().getStringExtra("title");
        if (!actTitle.equals("")) {
            titleTxt.setText(actTitle);
        }

        imageUrl = this.getIntent().getStringExtra("url");

        // 设置当前页面前后，各预加载1个Page。
        mViewPager.setOffscreenPageLimit(1);
        adapter = new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = View.inflate(container.getContext(), R.layout.row_imageview, null);
                view.setId(position); //用于查找此View
                container.addView(view);
                loadBigImage(position, view);
//                if (position == 0) {
//                    handleCurrentPosition(position);
//                }
//                Log.i(TAG, "instantiateItem: end" + System.currentTimeMillis());
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
//                return fileUrls.length;
                return 1;
            }
        };
        mViewPager.setAdapter(adapter);
        // TODO: 2017/11/22 待完成 
        mViewPager.setVisibility(View.GONE);
        loadImage();
    }

    private class ViewHolder {
        View loadingView;
        //        TextView loadingMsgview;
        CircleProgressView loadingViewImage;
        TextView loadingFailedMsgview;
        LargeImageView imageView;
//        GifImageView gifImageView;
        String imageUrl;
        int position;
    }
    public static ConcurrentHashMap map = new ConcurrentHashMap();

    /**
     * 加载原图
     */
    private void loadBigImage(final int position, View view) {
        if (map == null) {
            map = new ConcurrentHashMap();
        }
        ViewHolder tempHolder = (ViewHolder) view.getTag();
        if (tempHolder == null) {
            tempHolder = new ViewHolder();
            view.setTag(tempHolder);
            tempHolder.position = position;

            //正在加载图片提示
            tempHolder.loadingView = view.findViewById(R.id.loadingView);
            tempHolder.loadingFailedMsgview = (TextView) view.findViewById(R.id.loadingView_msg);
            tempHolder.loadingViewImage = (CircleProgressView) view.findViewById(R.id.loadingView_loading);

            //图片展示的2中方式
            tempHolder.imageView = (LargeImageView) view.findViewById(R.id.touchImageView);

            // 单击关闭图片预览
            tempHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            });

        }

//        final String imgUrl = fileUrls[position];
//        tempHolder.imageView.setOnLongClickListener(imglongListener);


//        tempHolder.imageUrl = imgUrl;
//        if (ImagebaseUtils.isGif(imgUrl)) {
//            showGifImage(position, tempHolder, imgUrl);
//        } else {
//            showOtherImage(position, tempHolder, imgUrl);
//        }
    }

    private void initEvent() {
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri bmpUri = getLocalBitmapUri(photo);

                if (bmpUri != null) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);

                    sharingIntent.setType("image/*");
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);

                    startActivity(Intent.createChooser(sharingIntent, "Share"));
                }
            }
        });
    }

    private void loadImage() {
        if (imageUrl.startsWith("http")) {
            Picasso.with(this)
                    .load(imageUrl)
                    .fit()
                    .centerInside()
                    .into(photo, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            hideLoadingAndUpdate();
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(PhotoActivity.this, "Error loading image.", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
        } else if (imageUrl.startsWith("data:image")) {
            String base64String = imageUrl.substring(imageUrl.indexOf(",") + 1);
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,
                    decodedString.length);
            photo.setImageBitmap(decodedByte);

            hideLoadingAndUpdate();
        } else {
            photo.setImageURI(Uri.parse(imageUrl));

            hideLoadingAndUpdate();
        }
    }

    private void hideLoadingAndUpdate() {
        photo.setVisibility(View.VISIBLE);
        shareBtn.setVisibility(shareBtnVisibility);
        mAttacher.update();
    }

    public Uri getLocalBitmapUri(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;

        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS
                    ), "share_image_" + System.currentTimeMillis() + ".png");

            file.getParentFile().mkdirs();

            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

}

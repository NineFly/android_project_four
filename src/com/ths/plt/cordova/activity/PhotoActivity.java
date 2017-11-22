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
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.ths.plt.cordova.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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

    private JSONObject options;
    private int shareBtnVisibility;

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
        loadImage();
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

    /**
     * Hide Loading when showing the photo. Update the PhotoView Attacher
     */
    private void hideLoadingAndUpdate() {
        photo.setVisibility(View.VISIBLE);
        shareBtn.setVisibility(shareBtnVisibility);
        mAttacher.update();
    }

    /**
     * Create Local Image due to Restrictions
     *
     * @param imageView
     * @return
     */
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

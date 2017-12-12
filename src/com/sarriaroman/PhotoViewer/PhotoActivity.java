package com.sarriaroman.PhotoViewer;

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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ths.plt.cordova.R;
import com.ths.plt.cordova.utils.ImagebaseUtils;
import com.ths.plt.cordova.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoActivity extends Activity implements
		ViewPager.OnPageChangeListener {
	private ImageView photo;

	private ImageButton closeBtn;
	private ImageButton shareBtn;
	private PhotoViewAttacher mAttacher;

	private TextView titleTxt;
	protected ViewPager mViewPager;
	private JSONObject options;
	private int shareBtnVisibility;
	protected ThisPageAdapter adapter;
	public static ConcurrentHashMap map = new ConcurrentHashMap();

	public static final String INDEX = "INDEX";
	public static final String FILE_URL = "FILE_URL";

	protected String[] fileUrls; //原图URL
	int currentItem = 0;//用户点击的是第几个，而不是当前viewpager的currentItem

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
		Intent intent = this.getIntent();
		try {
			options = new JSONObject(intent.getStringExtra("options"));
			shareBtnVisibility = options.getBoolean("share") ? View.VISIBLE : View.INVISIBLE;
		} catch (JSONException exception) {
			shareBtnVisibility = View.VISIBLE;
		}
		shareBtn.setVisibility(shareBtnVisibility);

		// Change the Activity Title
		String actTitle = intent.getStringExtra("title");
		if (!actTitle.equals("")) {
			titleTxt.setText(actTitle);
		}

//		String imageUrl = intent.getStringExtra("url");
        fileUrls = intent.getStringArrayExtra(FILE_URL); //原图URL
//		fileUrls = new String[]{"1","2","3"}; //原图URL
		if (currentItem >= fileUrls.length) {
			currentItem = 0;
		}

		mViewPager.setOffscreenPageLimit(1);
		mViewPager.setCurrentItem(currentItem);
		showPageNumber(currentItem + 1);
		// 设置当前页面前后，各预加载1个Page。
		adapter = new ThisPageAdapter();
		mViewPager.setAdapter(adapter);

		// TODO: 2017/11/22 待完成
//		mViewPager.setVisibility(View.GONE);
//		loadImage(imageUrl);
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
		mViewPager.setOnPageChangeListener(this);
	}

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
			tempHolder.imageView = (ImageView) view.findViewById(R.id.touchImageView);

			// 单击关闭图片预览
			tempHolder.imageView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
				}
			});

		}

		final String imgUrl = fileUrls[position];
		loadImage(imgUrl,tempHolder);
//        tempHolder.imageView.setOnLongClickListener(imglongListener);


//        tempHolder.imageUrl = imgUrl;
//        if (ImagebaseUtils.isGif(imgUrl)) {
//            showGifImage(position, tempHolder, imgUrl);
//        } else {
//            showOtherImage(position, tempHolder, imgUrl);
//        }
		// TODO: 2017/11/24 加载图片
	}

	private void loadImage(final String imgUrl,final ViewHolder tempHolder) {
			if (imgUrl.startsWith("http")) {
			//请求网络下载并显示图片
				ImageLoader.getInstance().displayImage(imgUrl,tempHolder.imageView,
					ImagebaseUtils.getNullOptions(),new ImageLoadingListener(){
						@Override
						public void onLoadingStarted(String imageUri, View view) {
						}

						@Override
						public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
							Toast.makeText(PhotoActivity.this,
									"Error loading image.",
									Toast.LENGTH_LONG).show();
							finish();
						}

						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
							tempHolder.imageView.setTag(bitmap);
							hideLoadingAndUpdate();
						}

						@Override
						public void onLoadingCancelled(String imageUri, View view) {

						}
					});

//			Picasso.with(this)
//					.load(imgUrl)
//					.fit()
//					.centerInside()
//					.into(photo, new com.squareup.picasso.Callback() {
//						@Override
//						public void onSuccess() {
//							hideLoadingAndUpdate();
//						}
//
//						@Override
//						public void onError() {
//							Toast.makeText(PhotoActivity.this,
//									"Error loading image.",
//									Toast.LENGTH_LONG).show();
//							finish();
//						}
//					});
		} else if (imgUrl.startsWith("data:image")) {
			String base64String = imgUrl.substring(imgUrl.indexOf(",") + 1);
			byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0,
					decodedString.length);
				tempHolder.imageView.setImageBitmap(decodedByte);

			hideLoadingAndUpdate();
		} else {
				tempHolder.imageView.setImageURI(Uri.parse(imgUrl));

			hideLoadingAndUpdate();
		}
	}

	private void hideLoadingAndUpdate() {
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

	/**
	 * 显示当前页面信息
	 */
	private void showPageNumber(int curPage) {
		// TODO: 2017/11/24 总共有多少张图片,当前显示哪一张图片
//        totalPage.setText("/" + fileUrls.length);
//        currentPage.setText("" + curPage);
	}

	private class ViewHolder {
		View loadingView;
		//        TextView loadingMsgview;
		CircleProgressView loadingViewImage;
		TextView loadingFailedMsgview;
		ImageView imageView;
		//        GifImageView gifImageView;
		String imageUrl;
		int position;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		showPageNumber(position + 1);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
	}

	private class ThisPageAdapter extends PagerAdapter{
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = View.inflate(container.getContext(), R.layout.row_imageview, null);
			view.setId(position); //用于查找此View
			container.addView(view);
			loadBigImage(position, view);
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
			return fileUrls.length;
		}
	}

}

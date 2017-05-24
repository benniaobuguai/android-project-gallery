package com.wo2b.wrapper.component.image;

import java.util.ArrayList;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.SaveImageOptions;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.WindowCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ViewSwitcher.ViewFactory;

import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.BaseFragmentActivity;
import com.wo2b.wrapper.view.XImageSwitcher;

/**
 * 图片自动播放器
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 */
public class ImageAutoPlayImageSwitcherActivity extends BaseFragmentActivity implements View.OnClickListener,ViewFactory
{

	public static final int ACTIONBAR_HIDE_DELAYED = 1 * 1000;
	public static final int SCROLL_PERIOD_DEFAULT = 5 * 1000;
	public static final ImageView.ScaleType IMAGEVIEW_SCALETYPE = ImageView.ScaleType.FIT_XY;
	
	public static final String EXTRA_IMAGE_LIST = "image_list";
	public static final String EXTRA_TITLE = "title";
	public static final String EXTRA_DIRECTORY = "directory";
	public static final String EXTRA_PERIOD = "period";
	public static final String EXTRA_POSITION = "position";
	public static final String EXTRA_CACHE_ON_DISC = "cache_on_disc";
	public static final String EXTRA_IMAGEVIEW_SCALETYPE = "imageview_scaletype";
	
	private XImageSwitcher imageSwitcher;

	private String mTitle; // 标题
	private String mCacheDir; // 图片缓存目录
	private int mPeriod = SCROLL_PERIOD_DEFAULT; // 图片切换周期
	private int mPosition = 0; // 当前图片所处于位置
	private boolean mCacheOnDisc = true; // 是否缓存本地
	private ArrayList<String> mImageList = new ArrayList<String>(); // 图片地址集合
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wrapper_cn_image_autoplay);

		Intent intent = getIntent();
		mImageList = intent.getStringArrayListExtra(EXTRA_IMAGE_LIST);
		mTitle = intent.getStringExtra(EXTRA_TITLE);
		mCacheDir = intent.getStringExtra(EXTRA_DIRECTORY);
		mPeriod = intent.getIntExtra(EXTRA_PERIOD, SCROLL_PERIOD_DEFAULT);
		mPosition = intent.getIntExtra(EXTRA_POSITION, 0);
		// 默认进行图片缓存
		mCacheOnDisc = intent.getBooleanExtra(EXTRA_CACHE_ON_DISC, true);
		if (mPeriod <= 0)
		{
			mPeriod = SCROLL_PERIOD_DEFAULT;
		}
		if (TextUtils.isEmpty(mCacheDir))
		{
			// 没有地址不进行缓存, 后续看看有没有需要考虑没有地址使用默认地址的情况
			// 原则上, 在设计时, 都提供存储地址应该比较合理
			mCacheOnDisc = false;
		}

		if (TextUtils.isEmpty(mTitle))
		{
			int titleId = getResources().getIdentifier("app_name", "string", this.getPackageName());
			mTitle = getString(titleId);
		}
		getSupportActionBar().show();
		getSupportActionBar().setTitle(mTitle);
		//getSupportActionBar().setSubtitle(title + "sub");
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg_translucent));
		
		getUiHandler().postDelayed(new Runnable()
		{

			@Override
			public void run()
			{
				getSupportActionBar().hide();
			}
		}, ACTIONBAR_HIDE_DELAYED);
		
		
		SaveImageOptions saveOptions = new SaveImageOptions.Builder()
			.medule("Image_AutoPlay")
			.extraDir(mCacheDir)
			.build();

		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.warn_image_empty)
			.showImageOnFail(R.drawable.warn_image_error)
			.cacheInMemory(true)
			.cacheOnDisc(mCacheOnDisc)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.saveImageOptions(saveOptions)
			.build();

		imageSwitcher = (XImageSwitcher) findViewById(R.id.imageSwitcher);
		imageSwitcher.setFactory(this);
		imageSwitcher.setOnClickListener(this);
		imageSwitcher.addImagePath(mImageList);
		imageSwitcher.setDisplayImageOptions(displayImageOptions);
		imageSwitcher.startAutoScroll(mPeriod, mPosition);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		imageSwitcher.resumeScroll();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		imageSwitcher.stopScroll();
	}
	
	@Override
	public void finish()
	{
		super.finish();

		// Activity切换动画使用淡入淡出的效果
		this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.imageSwitcher)
		{
			if (getSupportActionBar().isShowing())
			{
				getSupportActionBar().hide();
			}
			else
			{
				getSupportActionBar().show();
			}
		}
	}

	@Override
	public View makeView()
	{
		final ImageView imageView = new ImageView(this);
		imageView.setBackgroundColor(0xff000000);
		// imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		// FIXME:提供外层进行设置,或者是提供设置.
		// imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		// imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		// imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		return imageView;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.cn_image_period_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == R.id.period_fast)
		{
			changePeriod(5 * 1000);
		}
		else if (item.getItemId() == R.id.period_normal)
		{
			changePeriod(8 * 1000);
		}
		else if (item.getItemId() == R.id.period_slow)
		{
			changePeriod(12 * 1000);
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * 切换自动播放周期
	 * 
	 * @param period 切换周期
	 * @param position 播放位置
	 */
	private void changePeriod(int period)
	{
		mPeriod = period;
		imageSwitcher.changeScrollPeriod(period);

		getSupportActionBar().hide();
	}

	/**
	 * 启动图片自动播放,起始位置为0,播放周期取默认值{@link #SCROLL_PERIOD_DEFAULT}
	 * 
	 * @param context 上下文
	 * @param imageList 图片列表
	 * @param title 标题/相册名
	 * @param cacheDir 图片缓存目录
	 */
	public static void startImageAutoPlay(Activity activity, ArrayList<String> imageList, String title, String cacheDir)
	{
		startImageAutoPlay(activity, imageList, title, cacheDir, 0);
	}

	/**
	 * 启动图片自动播放,播放周期取默认值{@link #SCROLL_PERIOD_DEFAULT}
	 * 
	 * @param context 上下文
	 * @param imageList 图片列表
	 * @param title 标题/相册名
	 * @param cacheDir 图片缓存目录
	 * @param position 起始位置
	 */
	public static void startImageAutoPlay(Activity activity, ArrayList<String> imageList, String title, String cacheDir,
			int position)
	{
		startImageAutoPlay(activity, imageList, title, cacheDir, true, position, 0);
	}

	/**
	 * 启动图片自动播放,播放周期取默认值{@link #SCROLL_PERIOD_DEFAULT}
	 * 
	 * @param context 上下文
	 * @param imageList 图片列表
	 * @param title 标题/相册名
	 * @param cacheDir 图片缓存目录
	 * @param cacheOnDisc 是否需要缓存
	 * @param position 起始位置
	 */
	public static void startImageAutoPlay(Activity activity, ArrayList<String> imageList, String title, String cacheDir,
			boolean cacheOnDisc, int position)
	{
		startImageAutoPlay(activity, imageList, title, cacheDir, cacheOnDisc, position, 0);
	}

	/**
	 * 启动图片自动播放
	 * 
	 * @param context 上下文
	 * @param imageList 图片列表
	 * @param title 标题/相册名
	 * @param cacheDir 图片缓存目录
	 * @param cacheOnDisc 是否需要缓存
	 * @param position 起始位置
	 * @param scrollPeriod 图片切换周期
	 */
	public static void startImageAutoPlay(Activity activity, ArrayList<String> imageList, String title, String cacheDir,
			boolean cacheOnDisc, int position, int scrollPeriod)
	{
		Intent intent = new Intent();
		intent.setClass(activity, ImageAutoPlayImageSwitcherActivity.class);
		intent.putStringArrayListExtra(EXTRA_IMAGE_LIST, imageList);
		intent.putExtra(EXTRA_TITLE, title);
		intent.putExtra(EXTRA_DIRECTORY, cacheDir);
		intent.putExtra(EXTRA_POSITION, position);
		intent.putExtra(EXTRA_PERIOD, scrollPeriod);
		intent.putExtra(EXTRA_CACHE_ON_DISC, cacheOnDisc);
		activity.startActivity(intent);
		
		// Activity切换动画使用淡入淡出的效果
		activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

}

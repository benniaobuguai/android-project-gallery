package com.wo2b.gallery.ui.image;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.SaveImageOptions;
import opensource.component.imageloader.core.display.RoundedBitmapDisplayer;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.opencdk.view.swiperefresh.RecyclerViewAdapter;
import com.opencdk.view.swiperefresh.RecyclerViewAdapter.OnItemClickListener;
import com.opencdk.view.swiperefresh.RecyclerViewAdapter.RecyclerViewHolder;
import com.opencdk.view.swiperefresh.SwipeRefreshRecyclerGridLayout;
import com.wo2b.gallery.R;
import com.wo2b.gallery.business.base.DatabaseHelper;
import com.wo2b.gallery.business.image.Module;
import com.wo2b.gallery.business.image.PhotoBiz;
import com.wo2b.gallery.global.AppCacheFactory.ExtraDir;
import com.wo2b.gallery.global.GIntent;
import com.wo2b.gallery.model.image.AlbumInfo;
import com.wo2b.gallery.model.image.PhotoInfo;
import com.wo2b.gallery.model.image.PhotoInfoSet;
import com.wo2b.gallery.ui.global.ActivityBridge;
import com.wo2b.wrapper.component.common.CommentActivity;
import com.wo2b.wrapper.component.security.SecurityTu123;

/**
 * Image Grid List Preview.
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * 
 */
public class ImageGridActivity extends SwipeRefreshGridBaseActivity
{

	private RecyclerViewListAdapter<PhotoInfo> mImageAdapter = null;
	private PhotoInfoSet mPhotoInfoSet = null;
	private AlbumInfo mAlbumInfo = null;
	private List<PhotoInfo> mPhotoInfos = new ArrayList<PhotoInfo>();
	
	private PhotoBiz mPhotoBiz = null;
	
	private static final int MSG_LOAD_PHOTOS = 1;
	
	// ---------------------------------------------------------------------
	// ---------------------------------------------------------------------
	// ClientContext flags (see flags variable).
	// <-------> <--> <---------------------->
	// 0100 0000 0000 0000 0000 0000 0000 0000
	// 注: 第一位为符号位.
	// 举例: (0x40000000 | 0x8000000) 表示显示标题并且可编辑.
	private int flags;
	
	/** 标题显示标识 */
	public static final int FLAG_TITLE_DISPLAY = 0x40000000;
	/** 可编辑标识 */
	public static final int FLAG_EDIT_SUPPORT = 0x8000000;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_grid_list);
		initView();
		setDefaultValues();
		bindEvents();
	}
	
	@Override
	protected void initView()
	{
		mRecyclerGridLayout = (SwipeRefreshRecyclerGridLayout) findViewById(R.id.pullToRefreshGridView);
		Intent intent = getIntent();
		mAlbumInfo = (AlbumInfo) intent.getSerializableExtra(GIntent.EXTRA_ALBUM);
		
		switch (Module.valueOf(mAlbumInfo.getModule()))
		{
			case A:
			{
				addFlags(FLAG_TITLE_DISPLAY);
				break;
			}
			case H:
			{
				
				break;
			}
			default:
			{
				
				break;
			}
		}
		
		setActionBarTitle(getString(R.string.name_number, mAlbumInfo.getName(), mAlbumInfo.getPicnum()));
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
//		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
//		{
//			// 横屏
//		}
//		else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
//		{
//			// 竖屏
//		}

		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void setDefaultValues()
	{
		mPhotoBiz = new PhotoBiz(DatabaseHelper.getDatabaseHelper(mContext));
		
		final String encodeAlbumName = SecurityTu123.encodeText(mAlbumInfo.getName());

		mSaveOptions = new SaveImageOptions.Builder()
			.medule("IMAGE_GRID_LIST")
			.extraDir(ExtraDir.IMAGE + encodeAlbumName)
			.build();
		
		mOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.warn_image_loading)
			.showImageForEmptyUri(R.drawable.warn_image_empty)
			.showImageOnFail(R.drawable.warn_image_error)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.displayer(new RoundedBitmapDisplayer(10))
			.saveImageOptions(mSaveOptions)
			.build();
		
		getSubHandler().sendEmptyMessage(MSG_LOAD_PHOTOS);
	}
	
	@Override
	protected void bindEvents()
	{
		mRecyclerGridLayout.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(RecyclerViewHolder holder, View view, int position)
			{
				startImageViewerActivity(position);
			}
		});
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.common_comment, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.comment:
			{
				// 评论
				CommentActivity.entryComment(this, mAlbumInfo.getModule(), mAlbumInfo.getAlbumid(),
				        mAlbumInfo.getName());
				return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}
	
	
	// -----------------------------------------------------------------------------------
	public int getFlags()
	{
		return flags;
	}
	
	public ImageGridActivity setFlags(int flags)
	{
		this.flags = flags;
		return this;
	}
	
	public ImageGridActivity addFlags(int flags)
	{
		this.flags |= flags;
		return this;
	}
	
	/**
	 * 进入播放界面
	 * 
	 * @param position
	 */
	private void startImageViewerActivity(int position)
	{
		ActivityBridge.gotoImageViewerActivity(this, mPhotoInfoSet, position, mSaveOptions.getExtraDir());
	}
	
	@Override
	protected boolean uiHandlerCallback(Message msg)
	{
		switch (msg.what)
		{
			case MSG_LOAD_PHOTOS:
			{
				mImageAdapter = new RecyclerViewListAdapter<PhotoInfo>(new WeakReference(this), mPhotoInfos);
				mRecyclerGridLayout.setAdapter(mImageAdapter);
				
				break;
			}
		}
		
		return super.uiHandlerCallback(msg);
	}
	
	@Override
	protected boolean subHandlerCallback(Message msg)
	{
		switch (msg.what)
		{
			case MSG_LOAD_PHOTOS:
			{
				mPhotoInfoSet = new PhotoInfoSet();
				mPhotoInfoSet.setRet(0);
				mPhotoInfoSet.setData(mPhotoBiz.queryByAlbumid(mAlbumInfo.getAlbumid()));
				if (mPhotoInfoSet.getRet() == 0)
				{
					mPhotoInfoSet.setAlbumname(mAlbumInfo.getName());
					mPhotoInfos = mPhotoInfoSet.getData();

					getUiHandler().sendEmptyMessage(MSG_LOAD_PHOTOS);
				}

				return true;
			}
		}

		return super.subHandlerCallback(msg);
	}
	
	public void realOnBindViewHolder(RecyclerViewAdapter.RecyclerViewHolder holder, int position)
	{
		ImageView imageView = holder.findViewById(R.id.image);
		LinearLayout desc_line = holder.findViewById(R.id.desc_line);
		
		PhotoInfo photoInfo = mImageAdapter.getItem(position);
		if ((FLAG_TITLE_DISPLAY & getFlags()) == FLAG_TITLE_DISPLAY)
		{
			desc_line.setVisibility(View.VISIBLE);
			
			TextView desc = holder.findViewById(R.id.desc);
			desc.setText(photoInfo.getName());
		}
		else
		{
			desc_line.setVisibility(View.GONE);
		}
		
		mImageLoader.displayImage(photoInfo.getLargeUrl(), imageView, mOptions);
	}
	
	public View realInflate(ViewGroup parent, int viewType)
	{
		return LayoutInflater.from(getContext()).inflate(R.layout.image_grid_list_item, parent, false);
	}

	public static class RecyclerViewListAdapter<ITEM> extends RecyclerViewAdapter<ITEM>
	{
		
		private WeakReference<ImageGridActivity> mWeakReference;
		
		public RecyclerViewListAdapter(WeakReference<ImageGridActivity> weakReference, List<ITEM> items)
		{
			super(weakReference.get(), items);
			this.mWeakReference = weakReference;
		}
		
		@Override
		public void onBindViewHolder(RecyclerViewAdapter.RecyclerViewHolder holder, int position)
		{
			mWeakReference.get().realOnBindViewHolder(holder, position);
		}
		
		@Override
		public View inflate(ViewGroup parent, int viewType)
		{
			return mWeakReference.get().realInflate(parent, viewType);
		}
		
	}

}
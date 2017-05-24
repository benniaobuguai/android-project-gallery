package com.wo2b.gallery.ui.uc;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.SaveImageOptions;
import opensource.component.imageloader.core.display.RoundedBitmapDisplayer;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.opencdk.view.Transparency;
import com.opencdk.view.swiperefresh.RecyclerViewAdapter;
import com.opencdk.view.swiperefresh.RecyclerViewAdapter.OnItemClickListener;
import com.opencdk.view.swiperefresh.RecyclerViewAdapter.RecyclerViewHolder;
import com.opencdk.view.swiperefresh.SwipeRefreshRecyclerGridLayout;
import com.wo2b.gallery.R;
import com.wo2b.gallery.business.base.UserDatabaseHelper;
import com.wo2b.gallery.business.image.MyFavoritesBiz;
import com.wo2b.gallery.global.AppCacheFactory.ExtraDir;
import com.wo2b.gallery.model.image.AlbumInfo;
import com.wo2b.gallery.model.image.MyFavorites;
import com.wo2b.gallery.model.image.PhotoInfo;
import com.wo2b.gallery.model.image.PhotoInfoSet;
import com.wo2b.gallery.ui.global.ActivityBridge;
import com.wo2b.gallery.ui.image.ImageHelper;
import com.wo2b.gallery.ui.image.SwipeRefreshGridBaseActivity;
import com.wo2b.gallery.ui.widget.PopupWindow4Delete;
import com.wo2b.gallery.ui.widget.PopupWindow4Delete.OnPopupDeleteClickListener;

/**
 * 我的最爱
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2015-4-12
 */
public class MyFavoritesActivity extends SwipeRefreshGridBaseActivity
{
	
	private RecyclerViewListAdapter<MyFavorites> mImageAdapter = null;
	private PhotoInfoSet mPhotoInfoSet;
	private AlbumInfo mAlbumInfo;
	private List<MyFavorites> mPhotoInfos = new ArrayList<MyFavorites>();
	
	private static final int MSG_LOAD_PHOTOS = 1;
	
	private MyFavoritesBiz mMyFavoritesBiz;
	
	private PopupWindow mEditPopupMenu;
	private boolean isSelectAll = false;
	
	private boolean mEditMode;
	
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
		
		mAlbumInfo = new AlbumInfo();
		mAlbumInfo.setAlbumid("my_favorites_albumid");
		mAlbumInfo.setName(getString(R.string.my_favorites));
		
		displayTitle(mAlbumInfo.getName(), mAlbumInfo.getPicnum());
	}
	
	private void displayTitle(String album, int number)
	{
		getSupportActionBar().setTitle(getString(R.string.name_number, album, number));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.common_edit, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onActionBarEditClick()
	{
		if (mEditMode)
		{
			entryViewMode();
		}
		else
		{
			entryEditMode();
		}
		
		mImageAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void setDefaultValues()
	{
		mMyFavoritesBiz = new MyFavoritesBiz(UserDatabaseHelper.getUserDatabaseHelper(mContext));
		
		mSaveOptions = new SaveImageOptions.Builder()
			.medule("MY_FAVORITES")
			.extraDir(ExtraDir.USERS + mAlbumInfo.getName())
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
	protected void onPause()
	{
		super.onPause();
		if (mEditPopupMenu != null && mEditPopupMenu.isShowing())
		{
			// 避免PopupWindow的泄漏, has leaked window android.widget.PopupWindow$PopupViewContainer
			dismissBottomPopupMenu(mEditPopupMenu);
		}
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}
	
	/**
	 * 选择单个
	 * 
	 * @param position
	 */
	private void selectSingle(int position)
	{
		if (selectAble())
		{
			mImageAdapter.getItem(position).setSelected(!mImageAdapter.getItem(position).isSelected());
		}
	}
	
	/**
	 * 选择全部
	 */
	private void selectAll()
	{
		if (selectAble())
		{
			final int count = mImageAdapter.getItemCount();
			
			for (int position = 0; position < count; position++)
			{
				mImageAdapter.getItem(position).setSelected(true);
			}
			
			mImageAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 取消选择全部
	 */
	private void selectNone()
	{
		if (selectAble())
		{
			final int count = mImageAdapter.getItemCount();
			for (int position = 0; position < count; position++)
			{
				mImageAdapter.getItem(position).setSelected(false);
			}
			
			mImageAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * 是否可选择
	 * 
	 * @return
	 */
	private boolean selectAble()
	{
		if (mImageAdapter != null && mImageAdapter.getItemCount() > 0)
		{
			return true;
		}
		
		return false;
	}
	
	private boolean deleteFavorites()
	{
		int count = mImageAdapter.getItemCount();
		for (int position = 0; position < count; position++)
		{
			if (mImageAdapter.getItem(position).isSelected())
			{				
				final MyFavorites favourite = (MyFavorites) mImageAdapter.getItem(position);
				mMyFavoritesBiz.delete(favourite);
				
				mPhotoInfos.remove(position);
				mImageAdapter.notifyItemRemoved(position);
				position--;
				count--;
				
				// 开启新线程进行删除
				new Thread(new Runnable()
				{
					
					@Override
					public void run()
					{
						File file = ImageHelper.getCacheFile(mImageLoader.getDiscCache().getExtraDir().toString() + "/"
						        + mSaveOptions.getExtraDir(), favourite.getLargeUrl());
						if (file.exists())
						{
							file.delete();
						}
					}
				}, "Favourite-Delete").start();
			}
		}
		
		//entryViewMode();
		
		// getSubHandler().sendEmptyMessage(MSG_LOAD_PHOTOS);
		
		return true;
	}
	
	@Override
	protected void bindEvents()
	{
		mRecyclerGridLayout.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(RecyclerViewHolder holder, View view, int position)
			{
				if (mEditMode)
				{
					MyFavorites mf = mImageAdapter.getItem(position);
					mImageAdapter.getItem(position).setSelected(!mf.isSelected());
					mImageAdapter.notifyItemChanged(position);
				}
				else
				{
					startImagePagerActivity(position);
				}
			}
		});
	}
	
	/**
	 * 退出编辑模式
	 */
	private void entryViewMode()
	{
		this.mEditMode = false;
		dismissBottomPopupMenu(mEditPopupMenu);
	}
	
	/**
	 * 进入编辑模式
	 */
	private void entryEditMode()
	{
		this.mEditMode = true;
		
		// 弹出CommandListPopupWindow
		if (mEditPopupMenu == null)
		{
			mEditPopupMenu = new PopupWindow4Delete.Builder(getContext()).setTransparency(Transparency.LEVEL_1)
		        .setOutsideTouchable(false).setOnPopupDeleteClickListener(new OnPopupDeleteClickListener()
		        {
			        
			        @Override
			        public void onSeleteAll(TextView v)
			        {
				        if (!isSelectAll)
				        {
					        selectAll();
					        isSelectAll = true;
				        }
				        else
				        {
					        selectNone();
					        isSelectAll = false;
				        }
			        }
			        
			        @Override
			        public void onDelete()
			        {
				        deleteFavorites();
			        }
		        }).create();
		}
		
		showBottomPopupMenu(mEditPopupMenu, R.color.white);
	}
	
	private void startImagePagerActivity(int position)
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
				mImageAdapter = new RecyclerViewListAdapter<MyFavorites>(new WeakReference(this),
				        R.layout.image_grid_list_item, mPhotoInfos);
				
				mRecyclerGridLayout.setAdapter(mImageAdapter);
				
				displayTitle(mAlbumInfo.getName(), mPhotoInfos.size());
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
				mPhotoInfos = mMyFavoritesBiz.queryForAll();
				
				mPhotoInfoSet = new PhotoInfoSet();
				mPhotoInfoSet.setAlbumname(mAlbumInfo.getName());
				mPhotoInfoSet.setRet(0);
				mPhotoInfoSet.setData(favorites2PhotoInfo(mPhotoInfos));
				
				getUiHandler().sendEmptyMessage(MSG_LOAD_PHOTOS);
				return true;
			}
		}
		
		return super.subHandlerCallback(msg);
	}
	
	private List<PhotoInfo> favorites2PhotoInfo(List<MyFavorites> favorites)
	{
		List<PhotoInfo> photoInfos = new ArrayList<PhotoInfo>();
		if (favorites != null && !favorites.isEmpty())
		{
			int count = favorites.size();
			PhotoInfo photoInfo = null;
			for (int i = 0; i < count; i++)
			{
				photoInfo = favorites.get(i);
				photoInfos.add(photoInfo);
			}
		}
		
		return photoInfos;
	}
	
	public void realOnBindViewHolder(RecyclerViewAdapter.RecyclerViewHolder holder, int position)
	{
		MyFavorites photoInfo = mImageAdapter.getItem(position);
		
		ImageView imageView = holder.findViewById(R.id.image);
		ImageView tag = holder.findViewById(R.id.tag);
		
		if (mEditMode)
		{
			tag.setVisibility(View.VISIBLE);
			if (photoInfo.isSelected())
			{
				tag.setImageResource(R.drawable.checkbox_round_checked);
			}
			else
			{
				tag.setImageResource(R.drawable.checkbox_round_normal);
			}
		}
		else
		{
			tag.setVisibility(View.GONE);
		}
		
		mImageLoader.displayImage(photoInfo.getLargeUrl(), imageView, mOptions);
	}
	
	public static class RecyclerViewListAdapter<ITEM> extends RecyclerViewAdapter<ITEM>
	{
		
		private WeakReference<MyFavoritesActivity> mWeakReference;
		
		public RecyclerViewListAdapter(WeakReference<MyFavoritesActivity> weakReference, int layoutId, List<ITEM> items)
		{
			super(weakReference.get(), layoutId, items);
			this.mWeakReference = weakReference;
		}
		
		@Override
		public void onBindViewHolder(RecyclerViewAdapter.RecyclerViewHolder holder, int position)
		{
			mWeakReference.get().realOnBindViewHolder(holder, position);
		}
		
	}
	
}
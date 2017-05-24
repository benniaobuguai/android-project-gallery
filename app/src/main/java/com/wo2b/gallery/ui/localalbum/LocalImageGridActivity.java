package com.wo2b.gallery.ui.localalbum;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.SaveImageOptions;
import opensource.component.imageloader.core.display.RoundedBitmapDisplayer;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.opencdk.view.swiperefresh.RecyclerViewAdapter;
import com.opencdk.view.swiperefresh.RecyclerViewAdapter.OnItemClickListener;
import com.opencdk.view.swiperefresh.RecyclerViewAdapter.RecyclerViewHolder;
import com.opencdk.view.swiperefresh.SwipeRefreshRecyclerGridLayout;
import com.wo2b.gallery.R;
import com.wo2b.gallery.business.localalbum.LocalImageFactory;
import com.wo2b.gallery.global.GIntent;
import com.wo2b.gallery.model.image.AlbumInfo;
import com.wo2b.gallery.model.image.PhotoInfo;
import com.wo2b.gallery.model.image.PhotoInfoSet;
import com.wo2b.gallery.model.localalbum.LocalImage;
import com.wo2b.gallery.ui.global.ActivityBridge;
import com.wo2b.gallery.ui.image.SwipeRefreshGridBaseActivity;
import com.wo2b.wrapper.view.dialog.DialogUtils;

/**
 * Local image gridview layout.
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * 
 */
public class LocalImageGridActivity extends SwipeRefreshGridBaseActivity
{

	private RecyclerViewListAdapter<PhotoInfo> mImageAdapter = null;
	private AlbumInfo mAlbumInfo;
	private List<PhotoInfo> mPhotoInfos = new ArrayList<PhotoInfo>();
	
	private static final int MSG_LOAD_PHOTOS = 1;
	
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
		setActionBarTitle(R.string.local_album);
		mRecyclerGridLayout = (SwipeRefreshRecyclerGridLayout) findViewById(R.id.pullToRefreshGridView);
		
		Intent intent = getIntent();
		mAlbumInfo = (AlbumInfo) intent.getSerializableExtra(GIntent.EXTRA_ALBUM);
		setActionBarTitle(getString(R.string.name_number, mAlbumInfo.getName(), mAlbumInfo.getPicnum()));
	}
	
	@Override
	protected void setDefaultValues()
	{
		mSaveOptions = new SaveImageOptions.Builder()
			.medule("LOCAL_ALBUM")
			.title(getString(R.string.local_album))
			.extraDir(null)
			.build();
		
		mOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.warn_image_loading)
			.showImageForEmptyUri(R.drawable.warn_image_empty)
			.showImageOnFail(R.drawable.warn_image_error)
			.cacheInMemory(true)
			//.cacheOnDisc(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.displayer(new RoundedBitmapDisplayer(10))
			.saveImageOptions(mSaveOptions)
			.build();
		
		progressDialog = DialogUtils.createLoadingDialog(mContext, getString(R.string.hint_loading));
		progressDialog.show();
		getSubHandler().sendEmptyMessage(MSG_LOAD_PHOTOS);
	}
	
	Dialog progressDialog;
	
	@Override
	protected void bindEvents()
	{
		mRecyclerGridLayout.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(RecyclerViewHolder holder, View view, int position)
			{
				startImagePagerActivity(position);
			}
		});
	}

	private void startImagePagerActivity(int position)
	{
		PhotoInfoSet photoInfoSet = new PhotoInfoSet();
		photoInfoSet.setAlbumid("local_album");
		photoInfoSet.setAlbumname(mSaveOptions.getTitle());
		photoInfoSet.setData(mPhotoInfos);
		photoInfoSet.setStartIndex(0);
		photoInfoSet.setTotal(mPhotoInfos.size());
		
		ActivityBridge.gotoImageViewerActivity(this, photoInfoSet, position, mSaveOptions.getExtraDir());
	}
	
	@Override
	protected boolean uiHandlerCallback(Message msg)
	{
		switch (msg.what)
		{
			case MSG_LOAD_PHOTOS:
			{
				if (progressDialog.isShowing())
				{
					progressDialog.dismiss();
				}
				mImageAdapter = new RecyclerViewListAdapter<PhotoInfo>(new WeakReference(this),
				        R.layout.image_grid_list_item, mPhotoInfos);
				mRecyclerGridLayout.setAdapter(mImageAdapter);
				
				mImageAdapter.notifyDataSetChanged();
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
				LocalImageFactory factory = LocalImageFactory.getInstance(getApplicationContext());
				List<LocalImage> albums = factory.queryImagesByAlbumId(mAlbumInfo.getAlbumid());
				
				PhotoInfo photoInfo = null;
				LocalImage album = null;
				
				int size = albums.size();
				for (int i = 0; i < size; i++)
				{
					album = albums.get(i);
					
					photoInfo = new PhotoInfo();
					photoInfo.setName(album.getTitle());
					photoInfo.setSmallUrl("file:///" + album.getData());
					photoInfo.setLargeUrl("file:///" + album.getData());
					
					mPhotoInfos.add(photoInfo);
				}
				
				getUiHandler().sendEmptyMessage(MSG_LOAD_PHOTOS);
				
				break;
			}
		}
		
		return super.subHandlerCallback(msg);
	}

	public static class RecyclerViewListAdapter<ITEM> extends RecyclerViewAdapter<ITEM>
	{
		
		private WeakReference<LocalImageGridActivity> mWeakReference;
		
		public RecyclerViewListAdapter(WeakReference<LocalImageGridActivity> weakReference, int layoutId,
		        List<ITEM> items)
		{
			super(weakReference.get(), layoutId, items);
			this.mWeakReference = weakReference;
		}
		
		@Override
		public void onBindViewHolder(RecyclerViewAdapter.RecyclerViewHolder holder, int position)
		{
			ImageView imageView = holder.findViewById(R.id.image);
			TextView desc = holder.findViewById(R.id.desc);
			LinearLayout desc_line = holder.findViewById(R.id.desc_line);
			
			LocalImageGridActivity activity = mWeakReference.get();
			PhotoInfo photoInfo = activity.mImageAdapter.getItem(position);
			activity.mImageLoader.displayImage(photoInfo.getSmallUrl(), imageView, activity.mOptions);
		}
		
	}

}
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.opencdk.view.swiperefresh.RecyclerViewAdapter;
import com.opencdk.view.swiperefresh.RecyclerViewAdapter.OnItemClickListener;
import com.opencdk.view.swiperefresh.RecyclerViewAdapter.RecyclerViewHolder;
import com.opencdk.view.swiperefresh.SwipeRefreshRecyclerGridLayout;
import com.wo2b.gallery.R;
import com.wo2b.gallery.business.localalbum.LocalImageFactory;
import com.wo2b.gallery.global.GIntent;
import com.wo2b.gallery.model.image.AlbumInfo;
import com.wo2b.gallery.model.localalbum.LocalImage;
import com.wo2b.gallery.ui.image.SwipeRefreshGridBaseActivity;
import com.wo2b.wrapper.view.dialog.DialogUtils;

/**
 * Local Album
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2014-11-12
 * @Modify 2015-12-1
 */
public class LocalAlbumActivity extends SwipeRefreshGridBaseActivity
{

	private RecyclerViewListAdapter<AlbumInfo> mImageAdapter = null;
	private List<AlbumInfo> mAlbumInfos = new ArrayList<AlbumInfo>();

	private static final int MSG_LOAD_PHOTOS = 1;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_album_grid_list);
		initView();
		setDefaultValues();
		bindEvents();
	}

	@Override
	protected void initView()
	{
		getSupportActionBar().setTitle(R.string.local_album);
		
		mRecyclerGridLayout = (SwipeRefreshRecyclerGridLayout) findViewById(R.id.pullToRefreshGridView);
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
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.common_edit, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.edit:
			{
				Intent intent = new Intent();
				intent.setClass(getContext(), LocalAlbumFocusListActivity.class);
				startActivity(intent);
				return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}

	Dialog progressDialog;

	@Override
	public void onResume()
	{
		super.onResume();

		progressDialog.show();
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
				AlbumInfo albumInfo = mImageAdapter.getItem(position);
				
				Intent intent = new Intent(getContext(), LocalImageGridActivity.class);
				intent.putExtra(GIntent.EXTRA_ALBUM, albumInfo);
				startActivity(intent);
			}
		});
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

				mImageAdapter = new RecyclerViewListAdapter<AlbumInfo>(new WeakReference(this),
				        R.layout.local_album_grid_list_item, mAlbumInfos);
				
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
				mAlbumInfos.clear();
				
				LocalImageFactory factory = LocalImageFactory.getInstance(getApplicationContext());
				List<LocalImage> albums = factory.getFocusList();

				AlbumInfo albumInfo = null;
				LocalImage localImage = null;

				int size = albums.size();
				for (int i = 0; i < size; i++)
				{
					localImage = albums.get(i);

					albumInfo = new AlbumInfo();
					albumInfo.setAlbumid(localImage.getBucketId());
					albumInfo.setPicnum(localImage.getImageCount());
					albumInfo.setName(localImage.getBeautifulName());
					albumInfo.setCoverurl("file:///" + localImage.getData());

					mAlbumInfos.add(albumInfo);
				}

				getUiHandler().sendEmptyMessage(MSG_LOAD_PHOTOS);

				break;
			}
		}
		
		return super.subHandlerCallback(msg);
	}
	
	public static class RecyclerViewListAdapter<ITEM> extends RecyclerViewAdapter<ITEM>
	{
		
		private WeakReference<LocalAlbumActivity> mWeakReference;
		
		public RecyclerViewListAdapter(WeakReference<LocalAlbumActivity> weakReference, int layoutId, List<ITEM> items)
		{
			super(weakReference.get(), layoutId, items);
			this.mWeakReference = weakReference;
		}
		
		@Override
		public void onBindViewHolder(RecyclerViewAdapter.RecyclerViewHolder holder, int position)
		{
			LocalAlbumActivity activity = mWeakReference.get();
			ImageView image = holder.findViewById(R.id.image);
			ImageView background = holder.findViewById(R.id.background);
			TextView name_number = holder.findViewById(R.id.name_number);
			
			AlbumInfo albumInfo = activity.mImageAdapter.getItem(position);
			name_number.setText(activity.getString(R.string.name_number, albumInfo.getName(), albumInfo.getPicnum()));
			background.setBackgroundResource(IMAGE_BACKGROUND[position % IMAGE_BACKGROUND.length]);
			activity.mImageLoader.displayImage(albumInfo.getCoverurl(), image, activity.mOptions);
		}
		
		private final int[] IMAGE_BACKGROUND = new int[] 
		{ 
			R.drawable.gallery_album_frame,
			R.drawable.gallery_album_frame1,
			R.drawable.gallery_album_frame2
		};
	}
	
}
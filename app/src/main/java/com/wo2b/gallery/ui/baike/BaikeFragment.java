package com.wo2b.gallery.ui.baike;

import java.util.ArrayList;
import java.util.List;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.SaveImageOptions;
import opensource.component.imageloader.core.display.RoundedBitmapDisplayer;
import opensource.component.otto.Subscribe;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.opencdk.bus.CDKEvent;
import com.opencdk.net.NetStatus;
import com.opencdk.util.ViewUtils;
import com.opencdk.view.swiperefresh.SwipeRefreshScrollView;
import com.opencdk.view.viewpager.AutoScrollPoster;
import com.opencdk.view.viewpager.AutoScrollPoster.OnItemViewClickListener;
import com.wo2b.gallery.R;
import com.wo2b.gallery.global.AppCacheFactory.ExtraDir;
import com.wo2b.gallery.global.GAdTempData;
import com.wo2b.gallery.global.GIntent;
import com.wo2b.gallery.global.provider.DataProvider;
import com.wo2b.gallery.model.image.AlbumInfo;
import com.wo2b.gallery.ui.image.ImageGridActivity;
import com.wo2b.gallery.ui.view.ImageRecommendView.RecommendItem;
import com.wo2b.wrapper.app.fragment.BaseFragment;
import com.wo2b.wrapper.app.service.DaemonService;
import com.wo2b.wrapper.component.common.Wo2bAppListActivity;
import com.wo2b.wrapper.view.XListView;
import com.wo2b.wrapper.view.XListView.OnItemClickListener;

/**
 * Baike Fragment
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 2.0.0
 * @since 2014-11-12
 * @Modify 2015-11-16
 */
public class BaikeFragment extends BaseFragment implements OnClickListener
{
	
	private static final String TAG = "BaikeFragment";
	
	private AutoScrollPoster mPosterView;
	private SwipeRefreshScrollView mSwipeRefreshView;
	private XListView mListView;
	private LinearLayout mWarningBar;
	private TextView mWarningHomepage;
	private TextView mWarningClose;

	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private SaveImageOptions mSaveOptions;

	private BaseAdapter mAdapter;
	private List<AlbumInfo> mAlbumInfos = new ArrayList<AlbumInfo>();
	private RecommendItem[] mRecommendItems;

	private static final int MSG_LOAD = 1;
	private static final int MSG_ADS_LOAD = 2;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mImageLoader = ImageLoader.getInstance();
		mSaveOptions = new SaveImageOptions.Builder()
			.medule("Baike_Album_List")
			.extraDir(ExtraDir.ALBUM)
			.build();
		mOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.warn_image_loading)
			.showImageForEmptyUri(R.drawable.warn_image_empty)
			.showImageOnFail(R.drawable.warn_image_error)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.displayer(new RoundedBitmapDisplayer(6))
			.saveImageOptions(mSaveOptions)
			.build();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View root = inflater.inflate(R.layout.tu_baike_main, container, false);
		initView(root);
		bindEvents();
		
		getSubHandler().sendEmptyMessage(MSG_LOAD);
		getSubHandler().sendEmptyMessage(MSG_ADS_LOAD);
		
		return root;
	}
	
	protected boolean busEventEnable()
	{
		return true;
	}
	
	/**
	 * 网络监听
	 * 
	 * @param msg
	 */
	@Subscribe
	public void networkMonitor(Message msg)
	{
		if (msg.what == CDKEvent.NETWORK_STATUS)
		{
			NetStatus ns = (NetStatus) msg.obj;
			switch (ns)
			{
				case CONNECTED:
				case MOBILE:
				case WIFI:
				{
					hideWarningBar();
					break;
				}
				case DISCONNECTED:
				{
					showWarningBar(getString(R.string.hint_network_check));
					break;
				}
			}
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
		mPosterView.resumeScroll();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		mPosterView.stopScroll();
	}

	@Override
	protected void initView(View view)
	{
		mSwipeRefreshView = findViewByIdExt(view, R.id.swipeRefreshView);
		mPosterView = findViewByIdExt(view, R.id.wo2b_viewpager);
		SaveImageOptions saveOptions = new SaveImageOptions.Builder()
			.medule("Baike_Recommend")
			.extraDir(ExtraDir.ADS)
			.build();

		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.poster_default)
			.showImageForEmptyUri(R.drawable.poster_default)
			.showImageOnFail(R.drawable.poster_default)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.saveImageOptions(saveOptions)
			.build();
		
		mPosterView.setDisplayImageOptions(displayImageOptions);
		mPosterView.setScaleType(ScaleType.FIT_XY);
		mPosterView.needLoadAnimation(false);
		mPosterView.setOnItemViewClickListener(new OnItemViewClickListener()
		{
			
			@Override
			public void onItemViewClick(View view, Object item)
			{
				Intent intent = new Intent();
				intent.setClass(getActivity(), Wo2bAppListActivity.class);
				startActivity(intent);
			}
		});

		mListView = (XListView) view.findViewById(R.id.listview);
		mWarningBar = (LinearLayout) view.findViewById(R.id.warning_bar);
		mWarningHomepage = (TextView) view.findViewById(R.id.warning_homepage);
		mWarningClose = (TextView) view.findViewById(R.id.warning_close);

		// 网络状态
		Message msg = new Message();
		msg.what = CDKEvent.NETWORK_STATUS;
		msg.obj = DaemonService.mNetStatus;
		networkMonitor(msg);
	}
	
	/**
	 * 在首页显示警告或通知类信息, 如: 网络异常, 应用新版本等.
	 * 
	 * @param warning
	 */
	public void showWarningBar(CharSequence warning)
	{
		mWarningBar.setVisibility(View.VISIBLE);
		mWarningHomepage.setText(warning);
	}

	/**
	 * 隐藏警告栏
	 */
	public void hideWarningBar()
	{
		mWarningBar.setVisibility(View.GONE);
	}
	
	@Override
	protected void bindEvents()
	{
		mListView.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(Adapter adapter, View view, int position, long id)
			{
				Intent intent = new Intent(getContext(), ImageGridActivity.class);
				intent.putExtra(GIntent.EXTRA_ALBUM, (AlbumInfo) adapter.getItem(position));
				startActivity(intent);
			}
			
		});
		
		mWarningClose.setOnClickListener(this);
		
		mSwipeRefreshView.setOnRefreshListener(new OnRefreshListener()
		{
			
			@Override
			public void onRefresh()
			{
				getUiHandler().postDelayed(new Runnable()
				{
					
					@Override
					public void run()
					{
						mSwipeRefreshView.setRefreshing(false);
					}
				}, 2 * 1000);
			}
		});
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.warning_close:
			{
				// 隐藏警告栏
				hideWarningBar();
				break;
			}
		}
	}
	
	@Override
	protected boolean uiHandlerCallback(Message msg)
	{
		switch (msg.what)
		{
			case MSG_LOAD:
			{
				mAdapter = new ImageListAdapter();
				mListView.setAdapter(mAdapter);
				break;
			}
			case MSG_ADS_LOAD:
			{
				List<String> imageList = new ArrayList<String>();
				for (RecommendItem item : mRecommendItems)
				{
					imageList.add(item.image);
				}
				
				mPosterView.addItems(imageList);
				mPosterView.startAutoScroll(10 * 1000);
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
			case MSG_LOAD:
			{
				mAlbumInfos = DataProvider.getInstance().getCategoryList(getActivity());
				getUiHandler().sendEmptyMessage(MSG_LOAD);
				break;
			}
			case MSG_ADS_LOAD:
			{
				int length = GAdTempData.DefaultAdList.length;
				mRecommendItems = new RecommendItem[length];
				for (int i = 0; i < length; i++)
				{
					mRecommendItems[i] = new RecommendItem();
					mRecommendItems[i].image = "drawable://" + GAdTempData.DefaultAdList[i];
					mRecommendItems[i].drawableId = GAdTempData.DefaultAdList[i];
				}

				getUiHandler().sendEmptyMessage(MSG_ADS_LOAD);
				break;
			}
		}

		return super.subHandlerCallback(msg);
	}

	public void onImageListClick(View view)
	{
	}
	
	public void onImageGridClick(View view)
	{
	}
	
	public class ImageListAdapter extends BaseAdapter
	{
		
		public ImageListAdapter()
		{
		}
		
		@Override
		public int getCount()
		{
			return mAlbumInfos.size();
		}
		
		@Override
		public AlbumInfo getItem(int position)
		{
			return mAlbumInfos.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = LayoutInflater.from(mContext).inflate(R.layout.tu_baike_list_item, parent, false);
			}
			
			ImageView image = ViewUtils.get(convertView, R.id.image);
			TextView name = ViewUtils.get(convertView, R.id.name);
			TextView desc = ViewUtils.get(convertView, R.id.desc);
			
			AlbumInfo album = getItem(position);
			
			name.setText(album.getName());
			desc.setText(album.getDesc());
			
			mImageLoader.displayImage(album.getCoverurl(), image, mOptions);
			
			return convertView;
		}
		
	}
	
//	@Override
//	public void doUserVisibleHint()
//	{
//		
//	}
	
}

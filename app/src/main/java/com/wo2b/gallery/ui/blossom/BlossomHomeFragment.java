package com.wo2b.gallery.ui.blossom;

import java.util.List;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.SaveImageOptions;
import opensource.component.imageloader.core.display.RoundedBitmapDisplayer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.opencdk.view.RoundedImageView;
import com.opencdk.view.swiperefresh.RecyclerViewAdapter.RecyclerViewHolder;
import com.wo2b.gallery.R;
import com.wo2b.gallery.global.AppCacheFactory.ExtraDir;
import com.wo2b.gallery.global.GIntent;
import com.wo2b.gallery.global.provider.DataProvider;
import com.wo2b.gallery.model.image.AlbumInfo;
import com.wo2b.gallery.ui.image.ImageGridActivity;
import com.wo2b.wrapper.app.fragment.BaseListFragmentGroup;
import com.wo2b.wrapper.app.support.GroupExt;
import com.wo2b.wrapper.app.support.SimpleParams;
import com.wo2b.wrapper.app.support.XModel;

/**
 * 百花齐放
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 3.0.0
 * @since 2014-11-12
 * @Modify 2015-11-14
 */
public class BlossomHomeFragment extends BaseListFragmentGroup<AlbumInfo>
{
	
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private SaveImageOptions mSaveOptions;

	@Override
	public void realOnBindChildHolder(RecyclerViewHolder holder, GroupExt<AlbumInfo> groupExt, int position)
	{
		TextView tvName = holder.findViewById(R.id.name);
		RoundedImageView image = (RoundedImageView) holder.findViewById(R.id.image);
		TextView tvDesc = (TextView) holder.findViewById(R.id.desc);
		TextView tvPicnum = (TextView) holder.findViewById(R.id.picnum);
		
		final AlbumInfo albumInfo = groupExt.getValue();
		
		tvName.setText(albumInfo.getName());
		tvDesc.setText(albumInfo.getDesc());
		tvPicnum.setText(albumInfo.getPicnum() + "");
		
		mImageLoader.displayImage(albumInfo.getCoverurl(), image, mOptions);
	}
	
	@Override
	public View realInflateChildView(ViewGroup parent, int viewType)
	{
		return LayoutInflater.from(getContext()).inflate(R.layout.blossom_album_list_item, parent, false);
	}
	
	@Override
	public View realOnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.blossom_main, container, false);
		initView(view);
		bindEvents();
		
//		TextView headView = new TextView(getContext());
//		headView.setText("I'm Header View!!!");
//		headView.setGravity(Gravity.CENTER);
//		headView.setHeight(200);
//		setHeaderView(headView);
		
//		
////		View ll = LayoutInflater.from(mContext).inflate(R.layout.splash_main, null);
////		setHeaderView(ll);
//		
		
//		TextView footerView = new TextView(getContext());
//		footerView.setText("I'm Footer View!!!");
//		footerView.setHeight(200);
//		footerView.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
//		setFooterView(footerView);
		
		return view;
	}

	@Override
	public void realOnCreateViewComplete(View view, Bundle savedInstanceState)
	{
		mImageLoader = ImageLoader.getInstance();
		mSaveOptions = new SaveImageOptions.Builder()
			.medule("Baihua_Album_List")
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
			.displayer(new RoundedBitmapDisplayer(10))
			.saveImageOptions(mSaveOptions)
			.build();
		
		realExecuteFirstTime(null);
		// realOnPullDownExecuteTask(null);
	}
	
	@Override
	protected XModel<GroupExt<AlbumInfo>> realOnPullDown(SimpleParams params)
	{
		SystemClock.sleep(2 * 1000);
		
		// return BaseListFragment.XModel.empty();
		// return BaseListFragment.XModel.netError();
		// return BaseListFragment.XModel.unknown();
		
		List<GroupExt<AlbumInfo>> groupExt = DataProvider.getInstance().getChapterContentList(getActivity().getApplicationContext());
		return XModel.list(groupExt);
	}
	
	@Override
	protected XModel<GroupExt<AlbumInfo>> realOnPullUp(SimpleParams params)
	{
		SystemClock.sleep(5 * 1000);
		
		List<GroupExt<AlbumInfo>> groupExt = DataProvider.getInstance().getChapterContentListSington(mContext);
		return XModel.list(groupExt);
	}
	
	@Override
	protected void realOnChildItemClick(RecyclerViewHolder holder, GroupExt<AlbumInfo> groupX, int position)
	{
		AlbumInfo albumInfo = groupX.getValue();
		Intent intent = new Intent(getContext(), ImageGridActivity.class);
		intent.putExtra(GIntent.EXTRA_ALBUM, albumInfo);
		startActivity(intent);
	}

}
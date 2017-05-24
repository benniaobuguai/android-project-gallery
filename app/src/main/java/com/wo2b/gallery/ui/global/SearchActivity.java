package com.wo2b.gallery.ui.global;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.SaveImageOptions;
import opensource.component.imageloader.core.display.RoundedBitmapDisplayer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.opencdk.view.swiperefresh.RecyclerViewAdapter.RecyclerViewHolder;
import com.wo2b.gallery.R;
import com.wo2b.gallery.business.base.DatabaseHelper;
import com.wo2b.gallery.business.image.AlbumBiz;
import com.wo2b.gallery.global.AppCacheFactory.ExtraDir;
import com.wo2b.gallery.global.GIntent;
import com.wo2b.gallery.model.image.AlbumInfo;
import com.wo2b.gallery.ui.image.ImageGridActivity;
import com.wo2b.wrapper.app.SearchFragmentActivity;
import com.wo2b.wrapper.app.support.SimpleParams;
import com.wo2b.wrapper.app.support.XModel;

/**
 * Search Activity.
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * 
 */
public class SearchActivity extends SearchFragmentActivity<AlbumInfo> implements OnClickListener
{
	
	private static final String TAG = "SearchActivity";

	private ImageLoader mImageLoader = null;
	private DisplayImageOptions mOptions = null;
	private SaveImageOptions mSaveOptions = null;

	private AlbumBiz albumBiz = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_main_global);

		// 设置不可以上下拉动
		// getRockyListView().setMode(Mode.DISABLED);
		
		mImageLoader = ImageLoader.getInstance();
		mSaveOptions = new SaveImageOptions.Builder()
			.medule("COMMON_SEARCH")
			.extraDir(ExtraDir.ALBUM)	// 存储在相册路径下
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
		
		albumBiz = new AlbumBiz(DatabaseHelper.getDatabaseHelper(this));
	}

	@Override
	protected void realOnItemClick(RecyclerViewHolder holder, View view, int position)
	{
		AlbumInfo albumInfo = realGetItem(position);
		Intent intent = new Intent(getContext(), ImageGridActivity.class);
		intent.putExtra(GIntent.EXTRA_ALBUM, albumInfo);
		startActivity(intent);
	}
	
	@Override
	public void onClick(View v)
	{
		
	}
	
	@Override
	public View realInflate(ViewGroup parent, int viewType)
	{
	    return  LayoutInflater.from(getContext()).inflate(R.layout.blossom_album_list_item, parent, false);
	}
	
	@Override
	public void realOnBindViewHolder(RecyclerViewHolder holder, int position)
	{
		ImageView image = holder.findViewById(R.id.image);
		TextView name = holder.findViewById(R.id.name);
		TextView desc = holder.findViewById(R.id.desc);
		TextView picnum = holder.findViewById(R.id.picnum);
		
		AlbumInfo albumInfo = realGetItem(position);
		name.setText(albumInfo.getName());
		desc.setText(albumInfo.getDesc());
		picnum.setText(albumInfo.getPicnum() + "");
		mImageLoader.displayImage(albumInfo.getCoverurl(), image, mOptions);
	}
	
	@Override
	protected XModel<AlbumInfo> realOnPullDown(SimpleParams params)
	{
		if (params == null)
		{
			return XModel.empty();
		}
		
		String keyword = params.getKeyword();
		if (TextUtils.isEmpty(keyword))
		{
			return XModel.empty();
		}
		
		return XModel.list(albumBiz.queryByAlbumName(keyword));
	}
	
	@Override
	protected XModel<AlbumInfo> realOnPullUp(SimpleParams params)
	{
		return null;
	}
	
}

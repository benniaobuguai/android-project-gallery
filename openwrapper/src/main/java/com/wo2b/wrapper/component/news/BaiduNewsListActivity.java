package com.wo2b.wrapper.component.news;

import java.util.List;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.display.RoundedBitmapDisplayer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.opencdk.view.swiperefresh.RecyclerViewAdapter.RecyclerViewHolder;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.BaseFragmentListActivity;
import com.wo2b.wrapper.app.support.SimpleParams;
import com.wo2b.wrapper.app.support.XModel;
import com.wo2b.wrapper.component.image.DisplayImageBuilder;

/**
 * 新闻列表
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2014-11-2
 */
public class BaiduNewsListActivity extends BaseFragmentListActivity<News>
{

	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;

	private int mPage = 1;

	private String mTitle = null;
	private String mKeyword = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wrapper_news_main);

		Intent intent = getIntent();
		if (intent != null)
		{
			mTitle = intent.getStringExtra("title");
			mKeyword = intent.getStringExtra("keyword");
		}

		if (!TextUtils.isEmpty(mTitle))
		{
			setActionBarTitle(mTitle);
		}
		
		mImageLoader = ImageLoader.getInstance();
		mOptions = DisplayImageBuilder.getDefault()
					.cacheOnDisc(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(new RoundedBitmapDisplayer(6))
					.build();

		doSearch(mKeyword, mPage);
	}

	/**
	 * 执行搜索
	 * 
	 * @param keyword 关键字
	 * @param page 第几页
	 */
	protected void doSearch(String keyword, int page)
	{
		SimpleParams params = new SimpleParams();
		params.setIntValue1(page);
		params.setKeyword(keyword);

		realExecuteFirstTime(params);
	}

	@Override
	protected XModel<News> realOnPullDown(SimpleParams params)
	{
		List<News> list = NewsManager.getNewsList(params.getKeyword(), params.getIntValue1());
		params.setIntValue1(++mPage);
		
		return XModel.list(list);
	}
	
	@Override
	protected XModel<News> realOnPullUp(SimpleParams params)
	{
		List<News> list = NewsManager.getNewsList(params.getKeyword(), params.getIntValue1());
		params.setIntValue1(++mPage);
		
		return XModel.list(list);
	}

	@Override
	public View realInflate(ViewGroup parent, int viewType)
	{
	    return getLayoutInflater().inflate(R.layout.wrapper_news_main_list_item, parent, false);
	}
	
	@Override
	public void realOnBindViewHolder(RecyclerViewHolder holder, int position)
	{
		TextView tvTitle = holder.findViewById(R.id.title);
		TextView tvSource = holder.findViewById(R.id.source);
		TextView tvDate = holder.findViewById(R.id.date);
		ImageView ivNewsPic = holder.findViewById(R.id.newsPic);
		
		News news = realGetItem(position);

		tvTitle.setText(news.getTitle());
		tvSource.setText(news.getSource());
		tvDate.setText(news.getDate());

		if (TextUtils.isEmpty(news.getPhotoUrl()))
		{
			ivNewsPic.setVisibility(View.GONE);
		}
		else
		{
			ivNewsPic.setVisibility(View.VISIBLE);
			mImageLoader.displayImage(news.getPhotoUrl(), ivNewsPic, mOptions);
		}

		
	}
	
	@Override
	protected void realOnItemClick(RecyclerViewHolder holder, View view, int position)
	{
		super.realOnItemClick(holder, view, position);
		
		News news = realGetItem(position);
		Intent intent = new Intent();
		intent.setClass(this, BaiduNewsDetailActivity.class);
		intent.putExtra("news_title", news.getTitle());
		intent.putExtra("news_url", news.getUrl());
		
		startActivity(intent);
	}
	
	/**
	 * 进入百度新闻搜索列表
	 * 
	 * @param context
	 * @param title
	 * @param keyword
	 */
	public static final void gotoBaiduNewsListActivity(Context context, String title, String keyword)
	{
		Intent intent = new Intent();
		intent.setClass(context, BaiduNewsListActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("keyword", keyword);
		
		context.startActivity(intent);
	}

}

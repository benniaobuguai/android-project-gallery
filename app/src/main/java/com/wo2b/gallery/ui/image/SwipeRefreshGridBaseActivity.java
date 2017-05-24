package com.wo2b.gallery.ui.image;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.SaveImageOptions;
import android.os.Bundle;

import com.opencdk.view.swiperefresh.SwipeRefreshRecyclerGridLayout;
import com.wo2b.wrapper.app.BaseFragmentActivity;

/**
 * 
 * <pre>
 * AbsListView change to SwipeRefreshRecyclerGridLayout
 * 
 * </pre>
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2014-11-12
 * @Modify 2015-11-23
 */
public class SwipeRefreshGridBaseActivity extends BaseFragmentActivity
{

	protected static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
	protected static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";

	protected SwipeRefreshRecyclerGridLayout mRecyclerGridLayout;

	protected boolean pauseOnScroll = false;
	protected boolean pauseOnFling = true;

	protected ImageLoader mImageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions mOptions;
	protected SaveImageOptions mSaveOptions;

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		pauseOnScroll = savedInstanceState.getBoolean(STATE_PAUSE_ON_SCROLL, false);
		pauseOnFling = savedInstanceState.getBoolean(STATE_PAUSE_ON_FLING, true);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		applyScrollListener();
	}

	private void applyScrollListener()
	{
//		pullToRefreshGridView.getRefreshableView().setOnScrollListener(
//				new PauseOnScrollListener(mImageLoader, pauseOnScroll, pauseOnFling));
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		outState.putBoolean(STATE_PAUSE_ON_SCROLL, pauseOnScroll);
		outState.putBoolean(STATE_PAUSE_ON_FLING, pauseOnFling);
	}

}

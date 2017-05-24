package com.opencdk.view.swiperefresh;

import android.view.View;

/**
 * 
 * @author 笨鸟不乖
 * 
 */
public interface OnPullToRefreshListener
{

	public void onPullDownToRefresh(View view);

	public void onPullUpToRefresh(View view);

}

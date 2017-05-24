package com.opencdk.view.swiperefresh;

import android.view.View;

/**
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-11-16
 * @Modify 2015-11-16
 * @param <T>
 */
public interface IPullToRefresh<T extends View> {

	/**
	 * 
	 * @return
	 */
	public T getRefreshableView();
	
//	/**
//	 * 
//	 * @return
//	 */
//	public boolean isRefreshing();
//	
//	/**
//	 * 
//	 */
//	public void onRefreshComplete();
//
//
//	/**
//	 * 
//	 * @param listener
//	 */
//	public void setOnPullEventListener(OnPullEventListener<T> listener);
//
//	/**
//	 * 
//	 * @param listener
//	 */
//	public void setOnRefreshListener(OnRefreshListener<T> listener);
//
//	/**
//	 * 
//	 */
//	public void setRefreshing();
//
//	/**
//	 * 
//	 * @param doScroll
//	 */
//	public void setRefreshing(boolean doScroll);


}
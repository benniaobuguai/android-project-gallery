package com.opencdk.view.swiperefresh.wrapper;

/**
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-12-16
 * @Modify 2016-1-3
 */
public abstract interface ILoaderLayout
{
	
	public abstract void reset();
	
	public abstract void startLoading();
	
	public abstract void stopLoading();
	
}
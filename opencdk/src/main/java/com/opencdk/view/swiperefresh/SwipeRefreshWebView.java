package com.opencdk.view.swiperefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.opencdk.R;

/**
 * SwipeRefresh WebView
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-11-15
 * @Modify 2015-11-15
 */
public class SwipeRefreshWebView extends SwipeRefreshViewBase<WebView>
{
	
	public SwipeRefreshWebView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public SwipeRefreshWebView(Context context)
	{
		super(context);
	}
	
	@Override
	protected WebView createRefreshableView(Context context, AttributeSet attrs)
	{
		WebView webView = new WebView(context, attrs);
		
		webView.setId(R.id.srWebview);
		return webView;
	}
	
}

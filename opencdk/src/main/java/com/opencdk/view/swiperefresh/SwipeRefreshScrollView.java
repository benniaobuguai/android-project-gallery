package com.opencdk.view.swiperefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.opencdk.R;

/**
 * SwipeRefresh ScrollView
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-11-15
 * @Modify 2015-11-15
 */
public class SwipeRefreshScrollView extends SwipeRefreshViewBase<ScrollView>
{
	
	public SwipeRefreshScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public SwipeRefreshScrollView(Context context)
	{
		super(context);
	}
	
	@Override
	protected ScrollView createRefreshableView(Context context, AttributeSet attrs)
	{
		ScrollView scrollView = new ScrollView(context, attrs);
		
		// Use Generated ID (from res/values/ids.xml)
		scrollView.setId(R.id.srScrollview);
		return scrollView;
	}
	
}

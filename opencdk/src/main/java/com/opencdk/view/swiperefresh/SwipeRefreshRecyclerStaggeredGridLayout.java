package com.opencdk.view.swiperefresh;

import com.opencdk.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-11-17
 * @Modify 2015-11-17
 */
public class SwipeRefreshRecyclerStaggeredGridLayout extends SwipeRefreshRecyclerView<StaggeredGridLayoutManager>
{
	
	private static final String TAG = "SwipeRefreshRecyclerStaggeredGridLayout";
	
	public static final int DEFAULT_SPAN_COUNT = 1;
	
	int mSpanCount = DEFAULT_SPAN_COUNT;
	
	public SwipeRefreshRecyclerStaggeredGridLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public SwipeRefreshRecyclerStaggeredGridLayout(Context context)
	{
		super(context);
	}
	
//	@Override
//	protected void customObtainStyledAttributes(Context context, TypedArray a)
//	{
//		super.customObtainStyledAttributes(context, a);
//	}
	
	@Override
	protected StaggeredGridLayoutManager createLayoutManager(Context context, AttributeSet attrs)
	{
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeRefresh);
		mSpanCount = a.getInt(R.styleable.SwipeRefresh_srSpanCount, DEFAULT_SPAN_COUNT);
		a.recycle();
		
		final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(mSpanCount, mOrientation);
		
		return layoutManager;
	}
	
}

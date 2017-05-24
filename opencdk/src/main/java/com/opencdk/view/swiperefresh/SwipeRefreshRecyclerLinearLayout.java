package com.opencdk.view.swiperefresh;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-11-17
 * @Modify 2015-11-17
 */
public class SwipeRefreshRecyclerLinearLayout extends SwipeRefreshRecyclerView<LinearLayoutManager>
{
	
	private RecyclerViewDivider mRecyclerViewDivider;
	
	public SwipeRefreshRecyclerLinearLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public SwipeRefreshRecyclerLinearLayout(Context context)
	{
		super(context);
	}
	
	@Override
	protected LinearLayoutManager createLayoutManager(Context context, AttributeSet attrs)
	{
		final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
		
		mRecyclerViewDivider = new RecyclerViewDivider(context, mOrientation, mDivider);
		setRecyclerViewDivider(mRecyclerViewDivider);
		
		return layoutManager;
	}
	
	public void setRecyclerViewDivider(RecyclerViewDivider recyclerViewDivider)
	{
		mRefreshableView.addItemDecoration(recyclerViewDivider);
	}
	
	public RecyclerViewDivider getRecyclerViewDivider()
	{
		return mRecyclerViewDivider;
	}
	
}

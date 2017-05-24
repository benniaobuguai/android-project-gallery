package com.opencdk.view.swiperefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

import com.opencdk.R;

/**
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-11-17
 * @Modify 2015-11-17
 */
public class SwipeRefreshRecyclerGridLayout extends SwipeRefreshRecyclerView<GridLayoutManager>
{
	
	private static final String TAG = "SwipeRefreshRecyclerGridLayout";
	
	public static final int DEFAULT_SPAN_COUNT = 1;
	
	int mSpanCount = DEFAULT_SPAN_COUNT;
	
	public SwipeRefreshRecyclerGridLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public SwipeRefreshRecyclerGridLayout(Context context)
	{
		super(context);
	}
	
	@Override
	protected GridLayoutManager createLayoutManager(Context context, AttributeSet attrs)
	{
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeRefresh);
		mSpanCount = a.getInt(R.styleable.SwipeRefresh_srSpanCount, DEFAULT_SPAN_COUNT);
		a.recycle();
		
		
		final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), mSpanCount, mOrientation, false);
		layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
		{
			
			@Override
			public int getSpanSize(final int position)
			{
				if (getRefreshableView().getAdapter() instanceof RecyclerViewAdapter)
				{
					RecyclerViewAdapter<?> adapter = (RecyclerViewAdapter<?>) getRefreshableView().getAdapter();
					int spanItemPosition = -1;
					
					if (adapter.isHeaderView(position))
					{
						return getLayoutManager().getSpanCount();
					}
					else if (adapter.isFooterView(position))
					{
						return getLayoutManager().getSpanCount();
					}
					else if (adapter.hasLoaderView())
					{
						spanItemPosition = adapter.getItemCount() - 1;
						if (adapter.hasFooterView())
						{
							spanItemPosition--;
						}
					}

					// Log.I("info", "position: " + position + ", loadViewPosition: " + spanItemPosition + ", mSpanCount: " + getLayoutManager().getSpanCount());
					if (position == spanItemPosition)
					{
						return getLayoutManager().getSpanCount();
					}
				}
				
				return 1;
			}
		});

		getRefreshableView().setClipToPadding(false);
		
		//DividerGridItemDecoration mRecyclerViewDivider = new DividerGridItemDecoration(context, mDivider);
		//setRecyclerViewDivider(mRecyclerViewDivider);
		
		return layoutManager;
	}
	
//	@Override
//	protected void customObtainStyledAttributes(Context context, TypedArray a)
//	{
//		super.customObtainStyledAttributes(context, a);
//	}
	
	public void setRecyclerViewDivider(DividerGridItemDecoration recyclerViewDivider)
	{
		mRefreshableView.addItemDecoration(recyclerViewDivider);
	}
	
}

package com.opencdk.view.swiperefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.opencdk.R;
import com.opencdk.view.swiperefresh.wrapper.RefreshState;

/**
 * Base SwipeRefreshView
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-11-15
 * @Modify 2015-11-15
 * @param <T>
 */
public abstract class SwipeRefreshViewBase<T extends View> extends SwipeRefreshLayout implements IPullToRefresh<T>
{
	
	/**
	 * Reference from android.R.color.java
	 * 
	 * <pre>
	 * {@link android.R.color#holo_blue_light}
	 * {@link android.R.color#holo_red_light}
	 * {@link android.R.color#holo_orange_light}
	 * {@link android.R.color#holo_green_light}
	 * 
	 * </pre>
	 */
	private static final int[] SCHEME_COLORS = 
	{
		Color.parseColor("#ff33b5e5"),
		Color.parseColor("#ffff4444"),
		Color.parseColor("#ffffbb33"),
		Color.parseColor("#ff99cc00")
	};
	
	/**
     * <p>This view doesn't show scrollbars.</p>
     */
    static final int SCROLLBARS_NONE = 0x00000000;

    /**
     * <p>This view shows horizontal scrollbars.</p>
     */
    static final int SCROLLBARS_HORIZONTAL = 0x00000100;

    /**
     * <p>This view shows vertical scrollbars.</p>
     */
    static final int SCROLLBARS_VERTICAL = 0x00000200;

    /**
     * <p>Mask for use with setFlags indicating bits used for indicating which
     * scrollbars are enabled.</p>
     */
    static final int SCROLLBARS_MASK = 0x00000300;
    
	private int mScrollBars = SCROLLBARS_VERTICAL;
    
	protected abstract T createRefreshableView(Context context, AttributeSet attrs);
	
	T mRefreshableView;
	
	private RefreshState mState;
	
	@Override
	public final T getRefreshableView()
	{
		return mRefreshableView;
	}
	
	public SwipeRefreshViewBase(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context, attrs);
	}
	
	public SwipeRefreshViewBase(Context context)
	{
		super(context);
		initView(context, null);
	}
	
	private void initView(Context context, AttributeSet attrs)
	{
		// 自定义属性, 其它属性可自行扩展
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeRefresh);
		int paddingLeft = (int) a.getDimension(R.styleable.SwipeRefresh_srPaddingLeft, 0);
		int paddingTop = (int) a.getDimension(R.styleable.SwipeRefresh_srPaddingTop, 0);
		int paddingRight = (int) a.getDimension(R.styleable.SwipeRefresh_srPaddingRight, 0);
		int paddingBottom = (int) a.getDimension(R.styleable.SwipeRefresh_srPaddingBottom, 0);
		
		boolean srEnable = a.getBoolean(R.styleable.SwipeRefresh_srEnable, true);
		mScrollBars = a.getInteger(R.styleable.SwipeRefresh_srScrollbar, SCROLLBARS_VERTICAL);
		
		// customObtainStyledAttributes(context, a);
		
		mRefreshableView = createRefreshableView(context, attrs);
		addRefreshableView(context, mRefreshableView);
		this.setColorSchemeColors(SCHEME_COLORS);
		
		mRefreshableView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
		
		setEnabled(srEnable);
		
		if (SCROLLBARS_MASK == mScrollBars)
		{
			setHorizontalScrollBarEnabled(true);
			setVerticalScrollBarEnabled(true);
		}
		else
		{
			setHorizontalScrollBarEnabled(SCROLLBARS_HORIZONTAL == (SCROLLBARS_MASK & mScrollBars));
			setVerticalScrollBarEnabled(SCROLLBARS_VERTICAL == (SCROLLBARS_MASK & mScrollBars));
		}
		
		mRefreshableView.setVerticalScrollBarEnabled(true);
		
		a.recycle();
	}
	
	/**
	 * 提供钩子方法, 子类可扩展自定义属性
	 * 
	 * @param a
	 */
	public final void customObtainStyledAttributes(Context context, TypedArray a)
	{
		
	}
	
	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params)
	{
		final T refreshableView = getRefreshableView();
		if (refreshableView != null && refreshableView instanceof ViewGroup)
		{
			((ViewGroup) refreshableView).addView(child, index, params);
		}
		else
		{
			// android.support.v4.widget.CircleImageView
			super.addView(child, index, params);
		}
//		else
//		{
//			throw new UnsupportedOperationException("Refreshable View is not a ViewGroup, addView error!!!");
//		}
	}
	
	/**
	 * 添加可刷新的View
	 * 
	 * @param context
	 * @param refreshableView
	 */
	private void addRefreshableView(Context context, T refreshableView)
	{
		ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
		        ViewGroup.LayoutParams.MATCH_PARENT);
		addViewInternal(refreshableView, layoutParams);
	}

	/**
	 * {@link SwipeRefreshViewBase#addView(View, int, LayoutParams)}添加View的操作, 已经交由mRefreshableView.
	 */
	protected final void addViewInternal(View child, ViewGroup.LayoutParams params)
	{
		super.addView(child, -1, params);
	}
	
	@Override
	public void setColorSchemeColors(int... colors)
	{
		super.setColorSchemeColors(colors);
	}
	
	@Override
	public void setColorSchemeResources(int... colorResIds)
	{
		super.setColorSchemeResources(colorResIds);
	}
	
	@Override
	public boolean canChildScrollUp()
	{
		return super.canChildScrollUp();
	}
	
	
	
	// ========================================================================================
	// --> 重写部分View的属性, 交由RefreshableView去实现.
	@Override
	public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled)
	{
		mRefreshableView.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
	}
	
	@Override
	public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled)
	{
		mRefreshableView.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
	}
	
	public void updateRefreshState(RefreshState state)
	{
		this.mState = state;
	}
	
	public RefreshState getState()
	{
		return this.mState;
	}
	
}

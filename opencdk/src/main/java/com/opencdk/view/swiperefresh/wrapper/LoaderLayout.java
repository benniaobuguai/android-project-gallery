package com.opencdk.view.swiperefresh.wrapper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-12-16
 * @Modify 2016-1-3
 */
public abstract class LoaderLayout extends RelativeLayout implements ILoaderLayout
{
	
	public static final int LOADER_STYLE_AUTO = 0;
	public static final int LOADER_STYLE_MANUAL = 1;
	public static final int STATUS_IDLE = 0;
	public static final int STATUS_LOADING = 1;
	int mLoaderStyle = 0;
	int mStatus = 0;
	
	public LoaderLayout(Context paramContext)
	{
		super(paramContext);
		init(paramContext);
	}
	
	public LoaderLayout(Context paramContext, AttributeSet paramAttributeSet)
	{
		super(paramContext, paramAttributeSet);
		init(paramContext);
	}
	
	private void init(Context paramContext)
	{
		inflateLoaderView(this);
	}
	
	public int getStatus()
	{
		return this.mStatus;
	}
	
	public abstract View inflateLoaderView(ViewGroup paramViewGroup);
	
	public final void setHeight(int paramInt)
	{
		getLayoutParams().height = paramInt;
		requestLayout();
	}
	
	public void setLoaderViewStyle(int paramInt)
	{
		this.mLoaderStyle = paramInt;
	}
	
	public void setOnLoaderViewClickListener(final View.OnClickListener paramOnClickListener)
	{
		setOnClickListener(new View.OnClickListener()
		{
			
			public void onClick(View paramAnonymousView)
			{
				if (paramOnClickListener != null)
					paramOnClickListener.onClick(LoaderLayout.this);
			}
		});
	}
	
	public void setOnMoreButtonClick(View.OnClickListener paramOnClickListener)
	{
	}
	
	public final void setWidth(int paramInt)
	{
		getLayoutParams().width = paramInt;
		requestLayout();
	}
	
}
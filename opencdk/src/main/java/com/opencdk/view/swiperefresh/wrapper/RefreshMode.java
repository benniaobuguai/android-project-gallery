package com.opencdk.view.swiperefresh.wrapper;

/**
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-11-22
 * @Modify 2015-11-22
 */
public enum RefreshMode
{
	DISABLED(0x0),
	
	PULL_FROM_START(0x1),
	
	PULL_FROM_END(0x2),
	
	BOTH(0x3),
	
	MANUAL_REFRESH_ONLY(0x4);
	
	static RefreshMode mapIntToValue(final int modeInt)
	{
		for (RefreshMode value : RefreshMode.values())
		{
			if (modeInt == value.getIntValue())
			{
				return value;
			}
		}
		
		// If not, return default
		return getDefault();
	}
	
	static RefreshMode getDefault()
	{
		return PULL_FROM_START;
	}
	
	private int mIntValue;
	
	// The modeInt values need to match those from attrs.xml
	RefreshMode(int modeInt)
	{
		mIntValue = modeInt;
	}
	
	/**
	 * @return true if the mode permits Pull-to-Refresh
	 */
	boolean permitsPullToRefresh()
	{
		return !(this == DISABLED || this == MANUAL_REFRESH_ONLY);
	}
	
	/**
	 * @return true if this mode wants the Loading Layout Header to be shown
	 */
	public boolean showHeaderLoadingLayout()
	{
		return this == PULL_FROM_START || this == BOTH;
	}
	
	/**
	 * @return true if this mode wants the Loading Layout Footer to be shown
	 */
	public boolean showFooterLoadingLayout()
	{
		return this == PULL_FROM_END || this == BOTH || this == MANUAL_REFRESH_ONLY;
	}
	
	int getIntValue()
	{
		return mIntValue;
	}
	
}
package com.opencdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 
 * @author 笨鸟不乖
 * 
 */
public class TextViewMarquee extends TextView
{
	
	public TextViewMarquee(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}
	
	public TextViewMarquee(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}
	
	public TextViewMarquee(Context context)
	{
		super(context);
	}
	
	@Override
	public boolean isFocused()
	{
		return true;
	}
	
}

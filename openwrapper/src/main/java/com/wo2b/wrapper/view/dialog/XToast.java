package com.wo2b.wrapper.view.dialog;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wo2b.wrapper.R;

/**
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-4-29
 * @Modify 2015-8-3
 */
public final class XToast
{
	
	public static final int LENGTH_SHORT = 2 * 1000;
	
	public static final int LENGTH_LONG = 5 * 1000;
	
	private PopupWindow mPopupWindow;
	
	private TextView mTextView;
	
	private int mDuration = LENGTH_SHORT;
	
	private Context mContext;
	
	private Handler mHandler;
	
	private Runnable mShow = new Runnable()
	{
		
		@Override
		public void run()
		{
			// mPopupWindow.showAsDropDown(anchor);
		}
	};
	
	private Runnable mHide = new Runnable()
	{
		
		@Override
		public void run()
		{
			try
			{
				mPopupWindow.dismiss();
			}
			catch (Exception e)
			{
				// not attached to window manager
			}
		}
	};
	
	/**
	 * 
	 * @param context
	 * @param resId
	 * @param duration
	 */
	private XToast(Context context, int resId, int duration)
	{
		this(context, context.getResources().getString(resId), duration);
	}
	
	/**
	 * 
	 * @param context
	 * @param text
	 * @param duration
	 */
	private XToast(Context context, CharSequence text, int duration)
	{
		View contentView = LayoutInflater.from(context).inflate(R.layout.global_toast, null);
		this.mPopupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		// this.mPopupWindow.setContentView(contentView);
		
		this.mTextView = (TextView) contentView.findViewById(R.id.text);
		setText(text);
		
		this.mDuration = duration;
		
		this.mContext = context;
		this.mHandler = new Handler();
	}
	
	/**
	 * 
	 * @param context
	 * @param resId
	 * @param duration
	 * @return
	 */
	public static XToast makeToast(Context context, int resId, int duration)
	{
		return makeToast(context, context.getResources().getString(resId), duration);
	}
	
	/**
	 * 
	 * @param context
	 * @param text
	 * @param duration
	 * @return
	 */
	public static XToast makeToast(Context context, CharSequence text, int duration)
	{
		return new XToast(context, text, duration);
	}

	/**
	 * 
	 * @param text
	 */
	public void setText(CharSequence text)
	{
		mTextView.setText(text);
	}
	
	/**
	 * 
	 * @param resId
	 */
	public void setText(int resId)
	{
		mTextView.setText(resId);
	}
	
	/**
	 * {@link PopupWindow#showAsDropDown(View)}
	 * 
	 * @param anchor
	 */
	public void show(View anchor)
	{
		if (isShowing())
		{
			mHandler.removeCallbacks(mHide);
		}
		else
		{
			try
			{
				mPopupWindow.showAsDropDown(anchor);
			}
			catch (Exception e)
			{
				// java.lang.IllegalArgumentException: Requested window null does not exist
			}
		}
		
		mHandler.postDelayed(mHide, mDuration);
	}
	
	/**
	 * {@link PopupWindow#isShowing()}
	 * 
	 * @return
	 */
	public boolean isShowing()
	{
		return this.mPopupWindow.isShowing();
	}

}

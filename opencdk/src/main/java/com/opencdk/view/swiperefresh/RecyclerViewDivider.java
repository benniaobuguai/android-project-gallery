package com.opencdk.view.swiperefresh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView Divider by {@link RecyclerView.ItemDecoration}
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-11-16
 * @Modify 2015-11-16
 */
public class RecyclerViewDivider extends RecyclerView.ItemDecoration
{
	
	private static final int VERTICAL = LinearLayoutManager.VERTICAL;
	
	private static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;
	
	private int mOrientation;
	
	private Drawable mDivider;
	private int mDividerHeight;
	
	public RecyclerViewDivider(Context context, int orientation, Drawable divider)
	{
		setOrientation(orientation);
		setDivider(divider);
	}

	@Deprecated
	public RecyclerViewDivider(Context context, int orientation, int left, int top, int right, int bottom)
	{
		
	}
	
	/**
	 * 
	 * @param divider The drawable to use.
	 */
	public void setDivider(Drawable divider)
	{
		if (divider != null)
		{
			mDividerHeight = divider.getIntrinsicHeight();
			mDivider = divider;
		}
		else
		{
			mDividerHeight = 0;
			mDivider = new ColorDrawable(Color.parseColor("#258369"));
		}
	}
	
	public void setOrientation(int orientation)
	{
		if (orientation != HORIZONTAL && orientation != VERTICAL)
		{
			throw new IllegalArgumentException("invalid orientation");
		}
		
		mOrientation = orientation;
	}
	
	@Override
	public void onDraw(Canvas c, RecyclerView parent)
	{
		if (mOrientation == VERTICAL)
		{
			drawVertical(c, parent);
		}
		else
		{
			drawHorizontal(c, parent);
		}
	}

	public void drawVertical(Canvas c, RecyclerView parent)
	{
		final int left = parent.getPaddingLeft();
		final int right = parent.getWidth() - parent.getPaddingRight();

		final int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++)
		{
			final View child = parent.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			final int top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
			final int bottom = top + mDivider.getIntrinsicHeight();
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(c);
			
			
//			RectF r = new RectF(left, top, right, bottom);
//			Paint paint = new Paint();
//			paint.setFlags(Paint.DITHER_FLAG);
//			paint.setColor(Color.parseColor("#00000000"));
//			
//			c.drawRect(r, paint);
			
		}
	}

	public void drawHorizontal(Canvas c, RecyclerView parent)
	{
		final int top = parent.getPaddingTop();
		final int bottom = parent.getHeight() - parent.getPaddingBottom();
		
		final int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++)
		{
			final View child = parent.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			final int left = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
			final int right = left + mDivider.getIntrinsicHeight();
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(c);
		}
	}
	
	@SuppressWarnings("rawtypes")
    @Override
	public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent)
	{
		if (parent.getAdapter() != null && itemPosition >= parent.getAdapter().getItemCount() - 2)
		{
			if (parent.getAdapter() instanceof RecyclerViewAdapter)
			{
				// 此处执行的次数很少, 优先判断位置再进行类型转换, 性能相对更好些.
				RecyclerViewAdapter adapter = (RecyclerViewAdapter) parent.getAdapter();
				if (adapter.hasLoaderView())
				{
					// 只有显示FooterView时, 才隐藏底部的线条, 其它保留最后一项的divider显示.
					// TODO: 同时存在HeaderView情况暂不提供支持
					return;
				}
			}
		}

		if (mOrientation == VERTICAL)
		{
			outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
		}
		else
		{
			outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
		}
	}
	
}

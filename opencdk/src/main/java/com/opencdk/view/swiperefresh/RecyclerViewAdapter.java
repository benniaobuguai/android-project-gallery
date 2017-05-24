package com.opencdk.view.swiperefresh;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.opencdk.view.swiperefresh.wrapper.LoaderLayout;

/**
 * 基于RecyclerView扩展的数据适配器
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2014-11-26
 * @param <Item>
 */
public abstract class RecyclerViewAdapter<Item> extends RecyclerView.Adapter<ViewHolder>
{
	
	public static final int ITEM_VIEW_TYPE_FIRST = 1;
	
	/** Item */
	public static final int ITEM_VIEW_TYPE_ITEM = 0;
	
	/** HeaderView */
	public static final int ITEM_VIEW_TYPE_HEADER = Integer.MIN_VALUE + 1;
	
	/** FooterView */
	public static final int ITEM_VIEW_TYPE_FOOTER = Integer.MIN_VALUE + 2;
	
	/** EmptyView */
	public static final int ITEM_VIEW_TYPE_EMPTY = Integer.MIN_VALUE + 3;
	
	/** LoadView */
	public static final int ITEM_VIEW_TYPE_LOADER = Integer.MIN_VALUE + 4;
	
	/** Group */
	public static final int ITEM_VIEW_TYPE_GROUP = Integer.MIN_VALUE + 5;
	
	
	private Context mContext;
	private int mLayoutId = -1;
	private List<Item> mItems;
	
	
	private LinearLayout mHeaderView;
	private LinearLayout mFooterView;
	private LoaderLayout mLoaderView;
	private View mEmptyView;
	private boolean mHasHeaderView = false;
	private boolean mHasFooterView = false;
	private boolean mHasLoaderView = false;
	private boolean mHasEmptyView = false;
	
	private OnItemClickListener mOnItemClickListener;
	private OnItemLongClickListener mOnLongClickListener;
	
	/**
	 * Handle item view
	 * 
	 * @param viewHolder
	 * @param position
	 */
	public abstract void onBindViewHolder(RecyclerViewHolder viewHolder, int position);
	
	/**
	 * Handle header view
	 * 
	 * @param viewHolder
	 * @param position
	 */
	public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int position)
	{
		viewHolder.setIsRecyclable(false);
	}
	
	/**
	 * Handle footer view
	 * 
	 * @param viewHolder
	 * @param position
	 */
	public void onBindFooterViewHolder(FooterViewHolder viewHolder, int position)
	{
		viewHolder.setIsRecyclable(false);
	}
	
	/**
	 * Handle empty view
	 * 
	 * @param viewHolder
	 * @param position
	 */
	public void onBindEmptyViewHolder(EmptyViewHolder viewHolder, int position)
	{
		int width = viewHolder.parent.getMeasuredWidth();
		int height = viewHolder.parent.getMeasuredHeight();
		viewHolder.itemView.setLayoutParams(new ViewGroup.LayoutParams(width, height));
		// viewHolder.itemView.requestLayout();
	}
	
	/**
	 * Inflate custom item view
	 * 
	 * @param parent
	 * @param viewType
	 */
	public View inflate(ViewGroup parent, int viewType)
	{
		if (mLayoutId == -1)
		{
			throw new IllegalArgumentException(
			        "Apply layoutId first, or override inflate(ViewGroup parent, int viewType) method!!!");
		}
		
		return LayoutInflater.from(getContext()).inflate(mLayoutId, parent, false);
	}
	
	/**
	 * RecyclerViewAdapter constructor with LoaderView
	 * 
	 * @param context
	 * @param items
	 */
	public RecyclerViewAdapter(Context context, List<Item> items)
	{
		this(context, -1, items);
	}
	
	/**
	 * RecyclerViewAdapter constructor
	 * 
	 * @param context
	 * @param items
	 * @param hasLoaderView
	 */
	public RecyclerViewAdapter(Context context, int layoutId, List<Item> items)
	{
		this.mContext = context;
		this.mLayoutId = layoutId;
		if (items == null)
		{
			items = new ArrayList<Item>();
		}
		
		if (items.size() > 0)
		{
			hideEmptyView();
		}
		
		this.mItems = items;
	}
	
	/**
	 * Notify any registered observers that the data set has changed.
	 * 
	 * @param items dataset
	 */
	public void notifyDataSetChanged(List<Item> items)
	{
		if (items == null)
		{
			items = new ArrayList<Item>();
		}
		
		if (items.size() > 0)
		{
			hideEmptyView();
		}
		
		this.mItems = items;
		
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount()
	{
		int itemCount = mItems.size();
		
		if (hasEmptyView() && itemCount > 0)
		{
			hideEmptyView();
		}
		
		if (hasHeaderView())
		{
			itemCount++;
		}
		if (hasFooterView())
		{
			itemCount++;
		}
		if (hasLoaderView())
		{
			itemCount++;
		}
		if (hasEmptyView())
		{
			itemCount++;
		}
		//Log.E("info", "getItemCount()--> " + itemCount);
		return itemCount;
	}
	
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position)
	{
		//Log.E("info", "onBindViewHolder(ViewHolder viewHolder, int position)--> " + position);
		if (hasEmptyView() && mItems != null && mItems.size() > 0)
		{
			hideEmptyView();
		}
		
		if ((hasEmptyView() && (hasHeaderView() && position == 1))
		        || (hasEmptyView() && (!hasHeaderView() && position == 0)))
		{
			onBindEmptyViewHolder((EmptyViewHolder) viewHolder, position);
			return;
		}

		if (position == 0 && hasHeaderView())
		{
			onBindHeaderViewHolder((HeaderViewHolder) viewHolder, position);
		}
		else if (position == getItemCount() - 1 && hasFooterView())
		{
			onBindFooterViewHolder((FooterViewHolder) viewHolder, position);
		}
		else
		{
//			if (hasHeaderView())
//			{
//				position--;
//				if (position < 0)
//				{
//					return;
//				}
//			}
			
			if (viewHolder instanceof RecyclerViewHolder)
			{
				onBindViewHolder((RecyclerViewHolder) viewHolder, position);
			}
		}
	}
	
	/**
	 * normal item, loader, header, footer etc.
	 * 
	 * <pre>
	 * Override int android.support.v7.widget.RecyclerView.Adapter.getItemViewType(int position), it is used to
	 * customize HeaderView and FooterView.
	 * 
	 * Custom your ItemViewType, please override {@link #getItemViewTypeExternal(int)}
	 * 
	 * </pre>
	 */
	@Override
	public final int getItemViewType(int position)
	{
		//Log.E("info", "getItem--> " + position);
		if ((hasEmptyView() && (hasHeaderView() && position == 1))
		        || (hasEmptyView() && (!hasHeaderView() && position == 0)))
		{
			return ITEM_VIEW_TYPE_EMPTY;
		}
		
		
		if (position == 0 && hasHeaderView())
		{
			return ITEM_VIEW_TYPE_HEADER;
		}
		else if (position == getItemCount() - 1 && hasFooterView())
		{
			return ITEM_VIEW_TYPE_FOOTER;
		}
		else if (hasLoaderView())
		{
			if (position == getItemCount() - 1 && !hasFooterView())
			{
				return ITEM_VIEW_TYPE_LOADER;
			}
			else if (position == getItemCount() - 2 && hasFooterView())
			{
				return ITEM_VIEW_TYPE_LOADER;
			}
		}
		
		return getItemViewTypeExternal(position);
	}
	
	/**
	 * Instead of {@link #getItemViewType(int)}
	 */
	public int getItemViewTypeExternal(int position)
	{
		return super.getItemViewType(position);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType)
	{
		//Log.E("info", "onCreateViewHolder(ViewGroup parent, final int viewType)--> " + viewType);
		
		if (viewType == ITEM_VIEW_TYPE_HEADER)
		{
			return new HeaderViewHolder(mHeaderView);
		}
		else if (viewType == ITEM_VIEW_TYPE_FOOTER)
		{
			return new FooterViewHolder(mFooterView);
		}
		else if (viewType == ITEM_VIEW_TYPE_EMPTY)
		{
			return new EmptyViewHolder(parent, mEmptyView);
		}
		else if (viewType == ITEM_VIEW_TYPE_LOADER)
		{
			return new LoaderViewHolder(mLoaderView);
		}
		else
		{
			final View view = inflate(parent, viewType);
			final RecyclerViewHolder holder = new RecyclerViewHolder(view);
			
			holder.bindEvent(view, new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					if (mOnItemClickListener != null)
					{
						int position = holder.getPosition();
						mOnItemClickListener.onItemClick(holder, v, position);
					}
				}
				
			});
			
			holder.bindEvent(view, new OnLongClickListener()
			{
				
				@Override
				public boolean onLongClick(View v)
				{
					if (mOnLongClickListener != null)
					{
						int position = holder.getPosition();
						mOnLongClickListener.onItemLongClick(holder, v, position);
					}
					
					return true;
				}
			});
			
			return holder;
		}
	}
	
	/**
	 * Returns current item
	 * 
	 * @param position
	 * @return
	 */
	public Item getItem(int position)
	{
		// Log.E("info", "getItem--> " + position);
		
		if (hasHeaderView())
		{
			position--;
		}
		
		if (position < 0 || position > mItems.size() - 1)
		{
			return null;
		}
		
		return mItems.get(position);
	}
	
	public Context getContext()
	{
		return mContext;
	}
	
	/**
	 * 
	 * @param position
	 * @return
	 */
	public boolean isHeaderView(int position)
	{
		return hasHeaderView() && position == 0;
	}
	
	public boolean isFooterView(int position)
	{
		return hasFooterView() && position == getItemCount() - 1;
	}
	
	
	// =====================================================================================
	// ###
	
	public boolean hasEmptyView()
	{
		return mHasEmptyView;
	}
	
	/**
	 * Set empty view
	 * 
	 * @param emptyView
	 */
	public void setEmptyView(View emptyView)
	{
		this.mHasEmptyView = true;
		this.mEmptyView = emptyView;
	}
	
	public void hideEmptyView()
	{
		this.mHasEmptyView = false;
	}
	
	public boolean hasLoaderView()
	{
		return mHasLoaderView;
	}
	
	public void showLoaderView()
	{
		if (mHasLoaderView)
		{
//			mLoaderView.pullToRefresh();
			mLoaderView.startLoading();
		}
	}
	
	public void hideLoaderView()
	{
		if (mHasLoaderView)
		{
			mLoaderView.reset();
		}
	}
	
	/**
	 * Set loader view
	 * 
	 * @param loaderView
	 */
	public void setLoaderView(LoaderLayout loaderView)
	{
		if (loaderView == null)
		{
			throw new NullPointerException("An empty load view is not supported !!!");
		}
		
		this.mLoaderView = loaderView;
		this.mHasLoaderView = true;
	}
	
	/**
	 * Add header view
	 * 
	 * @param view
	 */
	public void addHeaderView(View view)
	{
		if (mHeaderView == null)
		{
			mHeaderView = new LinearLayout(getContext());
			mHeaderView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
			        LinearLayout.LayoutParams.WRAP_CONTENT));
			mHeaderView.setOrientation(LinearLayout.VERTICAL);
		}
		
		this.mHasHeaderView = true;
		this.mHeaderView.addView(view);
	}

	public boolean hasHeaderView()
	{
		return mHasHeaderView;
	}

	/**
	 * Add footer view
	 * 
	 * @param view
	 */
	public void addFooterView(View view)
	{
		if (mFooterView == null)
		{
			mFooterView = new LinearLayout(getContext());
			mFooterView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
			        LinearLayout.LayoutParams.WRAP_CONTENT));
			mFooterView.setOrientation(LinearLayout.VERTICAL);
		}
		
		this.mHasFooterView = true;
		this.mFooterView.addView(view);
	}
	
	/**
	 * Hide footer view
	 */
	public void hideFooterView()
	{
		this.mHasFooterView = false;
		this.mFooterView = null;
	}
	
	public boolean hasFooterView()
	{
		return mHasFooterView;
	}

	public static interface OnItemClickListener
	{
		
		void onItemClick(RecyclerViewHolder holder, View view, int position);
		
	}
	
	/**
	 * Item click listener
	 * 
	 * @param listener
	 */
	public void setOnItemClickListener(OnItemClickListener listener)
	{
		this.mOnItemClickListener = listener;
	}
	
	public static interface OnItemLongClickListener
	{
		
		void onItemLongClick(RecyclerViewHolder holder, View view, int position);
		
	}
	
	/**
	 * Item long click listener
	 * 
	 * @param listener
	 */
	public void setOnLongClickListener(OnItemLongClickListener listener)
	{
		this.mOnLongClickListener = listener;
	}
	
	/**
	 * RecyclerView Holder
	 */
	public static class RecyclerViewHolder extends RecyclerView.ViewHolder
	{

		private View mItemView;

		public RecyclerViewHolder(View itemView)
		{
			super(itemView);

			this.mItemView = itemView;
		}

		/**
		 * Find view by target id
		 * 
		 * @param id
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public <T extends View> T findViewById(int id)
		{
			return (T) mItemView.findViewById(id);
		}

		/**
		 * Returns custom ItemView
		 * 
		 * @return
		 */
		public View getItemView()
		{
			return this.mItemView;
		}

		/**
		 * Bind events(touch, click, longclick etc).
		 * 
		 * @param listener
		 * @param view
		 */
		public void bindEvent(final View view, final OnClickListener listener)
		{
			if (view == null)
			{
				throw new NullPointerException("Can not bind event to null view.");
			}
			
			view.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					if (listener != null)
					{
						listener.onClick(v);
					}
				}
			});
		}
		
		/**
		 * Bind events(touch, click, longclick etc).
		 * 
		 * @param listener
		 * @param view
		 */
		public void bindEvent(final View view, final OnLongClickListener listener)
		{
			if (view == null)
			{
				throw new NullPointerException("Can not bind event to null view.");
			}
			
			view.setOnLongClickListener(new OnLongClickListener()
			{
				
				@Override
				public boolean onLongClick(View v)
				{
					if (listener != null)
					{
						return listener.onLongClick(v);
					}
					
					return false;
				}
			});
		}
	}
	
	public static class HeaderViewHolder extends RecyclerView.ViewHolder
	{
		
		public HeaderViewHolder(View view)
		{
			super(view);
		}
		
	}
	
	public static class FooterViewHolder extends RecyclerView.ViewHolder
	{
		
		public FooterViewHolder(View view)
		{
			super(view);
		}
		
	}
	
	public static class EmptyViewHolder extends RecyclerView.ViewHolder
	{
		
		public ViewGroup parent;
		
		public EmptyViewHolder(ViewGroup parent, View view)
		{
			super(view);
			this.parent = parent;
		}

	}
	
	public static class LoaderViewHolder extends RecyclerView.ViewHolder
	{
		
		public LoaderViewHolder(View view)
		{
			super(view);
		}
		
	}
	
}
package com.opencdk.view.swiperefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.opencdk.R;
import com.opencdk.util.log.Log;
import com.opencdk.view.swiperefresh.RecyclerViewAdapter.OnItemClickListener;
import com.opencdk.view.swiperefresh.RecyclerViewAdapter.OnItemLongClickListener;
import com.opencdk.view.swiperefresh.RecyclerViewAdapter.RecyclerViewHolder;
import com.opencdk.view.swiperefresh.wrapper.LoaderLayout;
import com.opencdk.view.swiperefresh.wrapper.LoaderView;
import com.opencdk.view.swiperefresh.wrapper.RefreshMode;
import com.opencdk.view.swiperefresh.wrapper.RefreshState;

/**
 * SwipeRefresh RecyclerView
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-11-16
 * @Modify 2015-11-16
 */
public abstract class SwipeRefreshRecyclerView<T extends RecyclerView.LayoutManager> extends SwipeRefreshViewBase<RecyclerView> implements OnPullToRefreshListener
{

	// =========================================================================
	private static final String TAG = "SwipeRefreshRecyclerView";
	
	private static final boolean DEBUG = false;
	
	public static final int HORIZONTAL = OrientationHelper.HORIZONTAL;
	
	public static final int VERTICAL = OrientationHelper.VERTICAL;
	
	public static final int LOADER_STYLE_AUTO = LoaderLayout.LOADER_STYLE_AUTO;
	
	public static final int LOADER_STYLE_MANUAL = LoaderLayout.LOADER_STYLE_MANUAL;
	
	public static final int LOADER_STYLE_NONE = -1;
	
	/** Load style, default value is {@link #VERTICAL} */
	int mOrientation;
	Boolean mReverseLayout = false;
	Drawable mDivider;
	/** Load style, default value is {@link #LOADER_STYLE_AUTO} */
	int mLoaderStyle;

	private RecyclerViewAdapter<?> mRecyclerViewAdapter;
	
	protected abstract T createLayoutManager(Context context, AttributeSet attrs);
	
	T mLayoutManager;
	
	private OnItemClickListener mOnItemClickListener;
	private OnItemLongClickListener mOnItemLongClickListener;
	
	public SwipeRefreshRecyclerView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context, attrs);
	}
	
	public SwipeRefreshRecyclerView(Context context)
	{
		super(context);
		initView(context, null);
	}
	
	public final T getLayoutManager()
	{
		return mLayoutManager;
	}
	
	@Override
	protected RecyclerView createRefreshableView(Context context, AttributeSet attrs)
	{
		RecyclerView recyclerView = new RecyclerView(context, attrs);
		
		recyclerView.setId(R.id.srRecyclerView);
		return recyclerView;
	}
	
	private void initView(Context context, AttributeSet attrs)
	{
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwipeRefresh);
		mOrientation = a.getInt(R.styleable.SwipeRefresh_srOrientation, VERTICAL);
		mReverseLayout = a.getBoolean(R.styleable.SwipeRefresh_srReverseLayout, false);
		mDivider = a.getDrawable(R.styleable.SwipeRefresh_srDivider);
		mLoaderStyle = a.getInt(R.styleable.SwipeRefresh_srLoaderStyle, LOADER_STYLE_NONE);
		a.recycle();
		
		mLayoutManager = createLayoutManager(context, attrs);
		addLayoutManager(context, mLayoutManager);
		
		setOrientation(mOrientation);
		setReverseLayout(mReverseLayout);
		mRefreshableView.setHasFixedSize(true);
		mRefreshableView.setOnScrollListener(new RecyclerView.OnScrollListener()
		{

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState)
			{
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy)
			{
				super.onScrolled(recyclerView, dx, dy);
				
				if (mLoaderStyle != LOADER_STYLE_AUTO)
				{
					return;
				}

				// Load automatic
				final int lastVisibleItem = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
				int totalItemCount = getLayoutManager().getItemCount();
				
				if (mRecyclerViewAdapter != null && mRecyclerViewAdapter.hasLoaderView())
				{
					if (mRecyclerViewAdapter.hasEmptyView())
					{
						// RecyclerView can't be pulled up when there's no data.
						return;
					}
					
					totalItemCount--;
					if (mRecyclerViewAdapter.hasFooterView())
					{
						totalItemCount--;
					}
				}

				//Log.I("info", "lastVisibleItem: " + lastVisibleItem + ", totalItemCount: " + totalItemCount);
				
				
				// lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载
				// dy>0 表示向下滑动
				// if (lastVisibleItem >= totalItemCount - 4 && dy > 0)
				if (lastVisibleItem >= totalItemCount - 1 && dy > 0)
				{
					//Log.I("info", "lastVisibleItem >= totalItemCount - 1 && dy > 0");
					onPullUpToRefresh(mRefreshableView);
				}
			}
		});
		
		this.setOnRefreshListener(new OnRefreshListener()
		{
			
			@Override
			public void onRefresh()
			{
				if (mRecyclerViewAdapter != null && mRecyclerViewAdapter.hasEmptyView())
				{
					// Ignoring current [swipe-refresh] when EmptyView is showing.
					return;
				}
				
				onPullDownToRefresh(SwipeRefreshRecyclerView.this);
			}
		});
	}
	
	public void setAdapter(RecyclerViewAdapter<?> adapter)
	{
		mRecyclerViewAdapter = adapter;
		mRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(RecyclerViewHolder holder, View view, int position)
			{
				if (mOnItemClickListener != null)
				{
					mOnItemClickListener.onItemClick(holder, view, position);
				}
			}
		});
		mRecyclerViewAdapter.setOnLongClickListener(new OnItemLongClickListener()
		{
			
			@Override
			public void onItemLongClick(RecyclerViewHolder holder, View view, int position)
			{
				if (mOnItemLongClickListener != null)
				{
					mOnItemLongClickListener.onItemLongClick(holder, view, position);
				}
			}
		});
		
		
		if (mHeaderView != null)
		{
			mRecyclerViewAdapter.addHeaderView(mHeaderView);
		}
		if (mFooterView != null)
		{
			mRecyclerViewAdapter.addFooterView(mFooterView);
		}
		if (mEmptyView != null)
		{
			mRecyclerViewAdapter.setEmptyView(mEmptyView);
		}
		
		if (mLoaderStyle != LOADER_STYLE_NONE)
		{
			if (mLoaderView == null)
			{
				mLoaderView = new LoaderView(getContext()/* , RefreshMode.BOTH */);
			}
			mLoaderView.setLoaderViewStyle(mLoaderStyle);
			
			// TEMP CODE:
			mLoaderView.setOnMoreButtonClick(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					onPullUpToRefresh(mRefreshableView);
				}
			});
			
			mRecyclerViewAdapter.setLoaderView(mLoaderView);
		}

		mRefreshableView.setAdapter(adapter);
		onRefreshComplete();
	}
	
	private View mHeaderView;
	private View mFooterView;
	private View mEmptyView;
	private LoaderLayout mLoaderView;
	
	public void addHeaderView(View view)
	{
		this.mHeaderView = view;
	}
	
	public void addFooterView(View view)
	{
		this.mFooterView = view;
	}
	
	public void setEmptyView(View view)
	{
		this.mEmptyView = view;
	}
	
	public void setLoaderView(LoaderLayout loaderView)
	{
		this.mLoaderView = loaderView;
	}

	public void setRefreshMode(RefreshMode mode)
	{
		
	}
	
	@Override
	public void updateRefreshState(RefreshState state)
	{
		super.updateRefreshState(state);
		switch (state)
		{
			case RESET:
			{
				//int position1 = ((LinearLayoutManager) this.getLayoutManager()).findLastVisibleItemPosition();
				//int position2 = ((LinearLayoutManager) this.getLayoutManager()).findLastCompletelyVisibleItemPosition();
				
				mRecyclerViewAdapter.hideLoaderView();
				break;
			}
			case MANUAL_REFRESHING:
			{
				
				break;
			}
			case OVERSCROLLING:
			case PULL_TO_REFRESH:
			case REFRESHING:
			case RELEASE_TO_REFRESH:
			{
				mRecyclerViewAdapter.showLoaderView();
				break;
			}
		}
	}
	
	/**
	 * 刷新成功
	 */
	public void onRefreshComplete()
	{
		if (this.isRefreshing())
		{
			this.setRefreshing(false);
		}
		
		updateRefreshState(RefreshState.RESET);
		
		// mRecyclerViewAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 
	 * @param listener
	 */
	public void setOnItemClickListener(final OnItemClickListener listener)
	{
		this.mOnItemClickListener = listener;
	}
	
	/**
	 * 
	 * @param listener
	 */
	public void setOnItemLongClickListener(final OnItemLongClickListener listener)
	{
		this.mOnItemLongClickListener = listener;
	}
	
	private OnPullToRefreshListener mOnPullToRefreshListener = null;

	public void setOnPullToRefreshListener(OnPullToRefreshListener listener)
	{
		this.mOnPullToRefreshListener = listener;
	}
	
//	public void setPullMode(Mode mode)
//	{
//
//	}
	
	@Override
	public void onPullDownToRefresh(View view)
	{
		if (mOnPullToRefreshListener != null)
		{
			mOnPullToRefreshListener.onPullDownToRefresh(view);
		}
	}

	@Override
	public void onPullUpToRefresh(View view)
	{
		// mLoadView.pullToRefresh();
		updateRefreshState(RefreshState.REFRESHING);
		
		if (mOnPullToRefreshListener != null)
		{
			mOnPullToRefreshListener.onPullUpToRefresh(view);
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);

//		post(new Runnable()
//		{
//			@Override
//			public void run()
//			{
//				requestLayout();
//			}
//		});
	}
	
	/**
	 * 添加LayoutManager
	 * 
	 * @param context
	 * @param layoutManager
	 */
	private void addLayoutManager(Context context, T layoutManager)
	{
		mRefreshableView.setLayoutManager(layoutManager);
	}
	
	/**
	 * Returns the current orientaion of the SwipeRefreshRecyclerView.
	 * 
	 * @return
	 */
	public int getOrientation()
	{
		if (this.getLayoutManager() instanceof LinearLayoutManager)
		{
			return ((LinearLayoutManager) this.getLayoutManager()).getOrientation();
		}
		else if (this.getLayoutManager() instanceof StaggeredGridLayoutManager)
		{
			return ((StaggeredGridLayoutManager) this.getLayoutManager()).getOrientation();
		}
		
		return VERTICAL;
	}
	
	/**
	 * Sets the orientation of the SwipeRefreshRecyclerView.
	 * 
	 * @param orientation
	 */
	public void setOrientation(int orientation)
	{
		this.mOrientation = orientation;
		if (this.getLayoutManager() instanceof LinearLayoutManager)
		{
			((LinearLayoutManager) this.getLayoutManager()).setOrientation(orientation);
		}
		else if (this.getLayoutManager() instanceof StaggeredGridLayoutManager)
		{
			((StaggeredGridLayoutManager) this.getLayoutManager()).setOrientation(orientation);
		}
	}
	
	/**
	 * Returns if views are laid out from the opposite direction of the SwipeRefreshRecyclerView.
	 * 
	 * @return
	 */
	public boolean getReverseLayout()
	{
		if (this.getLayoutManager() instanceof LinearLayoutManager)
		{
			return ((LinearLayoutManager) this.getLayoutManager()).getReverseLayout();
		}
		else if (this.getLayoutManager() instanceof StaggeredGridLayoutManager)
		{
			return ((StaggeredGridLayoutManager) this.getLayoutManager()).getReverseLayout();
		}
		
		return false;
	}
	
	/**
	 * Used to reverse item traversal and layout order.
	 * 
	 * @param reverseLayout
	 */
	public void setReverseLayout(boolean reverseLayout)
	{
		this.mReverseLayout = reverseLayout;
		if (this.getLayoutManager() instanceof LinearLayoutManager)
		{
			((LinearLayoutManager) this.getLayoutManager()).setReverseLayout(reverseLayout);
		}
		else if (this.getLayoutManager() instanceof StaggeredGridLayoutManager)
		{
			((StaggeredGridLayoutManager) this.getLayoutManager()).setReverseLayout(reverseLayout);
		}
	}
	
}

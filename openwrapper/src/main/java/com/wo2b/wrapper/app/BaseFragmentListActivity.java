package com.wo2b.wrapper.app;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;

import com.opencdk.util.log.Log;
import com.opencdk.view.swiperefresh.OnPullToRefreshListener;
import com.opencdk.view.swiperefresh.RecyclerViewAdapter;
import com.opencdk.view.swiperefresh.RecyclerViewAdapter.OnItemClickListener;
import com.opencdk.view.swiperefresh.RecyclerViewAdapter.RecyclerViewHolder;
import com.opencdk.view.swiperefresh.SwipeRefreshRecyclerLinearLayout;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.support.SimpleParams;
import com.wo2b.wrapper.app.support.XModel;
import com.wo2b.wrapper.view.EmptyView;
import com.wo2b.wrapper.view.EmptyView.OnEmptyViewClickListener;

/**
 * ListView-->SwipeList
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 4.0.0
 * @since 2014-11-2
 * @Modify 2015-12-1
 * @param <Model>
 */
public abstract class BaseFragmentListActivity<Model> extends BaseFragmentActivity implements OnPullToRefreshListener
{
	
	/** 请求数据的默认分页数 */
	public static final int PAGE_COUNT_DEFAULT = 20;
	protected int mOffset = 0;
	protected int mCount = PAGE_COUNT_DEFAULT;
	protected int mFirstQueryCount = PAGE_COUNT_DEFAULT;

	/** 适配器视图 */
	private RecyclerViewAdapter<Model> mRecyclerViewAdapter;
	
	/** 数据集 */
	private LinkedList<Model> mListData = new LinkedList<Model>();
	
	/** 是否正在加载数据 */
	private boolean mIsLoading = false;
	
	private SwipeRefreshRecyclerLinearLayout mRecyclerListView;
	
	private View mHeaderView;
	private View mFooterView;
	private EmptyView mEmptyView;
	
	private SimpleParams mParams;
	
	/**
	 * 解析布局
	 * 
	 * @param parent
	 * @param viewType
	 */
	public abstract View realInflate(ViewGroup parent, int viewType);
	
	/**
	 * 绘制列表视图
	 * 
	 * @param viewHolder
	 * @param position
	 */
	public abstract void realOnBindViewHolder(RecyclerViewAdapter.RecyclerViewHolder holder, int position);
	
	// View mLoadView; -- delete
	public void realOnBindFooterViewHolder(RecyclerViewAdapter.FooterViewHolder holder, int position)
	{
		
	}
	
	// View mEmptyView; -- delete
	public void realOnBindEmptyViewHolder(RecyclerViewAdapter.EmptyViewHolder holder, int position)
	{
		
	}
	
	/**
	 * 实现此方法, 执行下拉操作, 默认第一次执行时, 会调用些方法.
	 * 
	 * @param params
	 * @return
	 */
	protected abstract XModel<Model> realOnPullDown(SimpleParams params);
	
	/**
	 * 实现此方法, 执行上拉操作
	 * 
	 * @param params
	 * @return
	 */
	protected abstract XModel<Model> realOnPullUp(SimpleParams params);
	
	/**
	 * 子线程获取数据
	 * 
	 * @author 笨鸟不乖
	 * @email benniaobuguai@gmail.com
	 */
	public static abstract class GetDataTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
	{

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.I("info", "ListActivity-->onCreate()");
		
		// 设置图标, 需要获取各个项目的图标, 再进行设置.
		// setActionBarIcon(getApplicationActionBarIcon());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSupportContentChangedExt()
	{
		//super.onContentChanged();
		
		Log.I("info", "ListActivity-->onSupportContentChanged()");
		
		mRecyclerListView = findViewByIdExt(R.id.wo2b_listview);
		mEmptyView = new EmptyView(getContext());
		mEmptyView.setOnEmptyViewClickListener(new OnEmptyViewClickListener()
		{
			
			@Override
			public void onEmptyViewClick(EmptyView emptyView)
			{
				realExecuteFirstTime(null);
			}
		});
		
		mRecyclerViewAdapter = new RecyclerViewListAdapter<Model>(
		        new WeakReference<BaseFragmentListActivity<Model>>(this), mListData);
		mRecyclerListView.setEmptyView(mEmptyView);
		
		if (mHeaderView != null)
		{
			mRecyclerListView.addHeaderView(mHeaderView);
		}
		if (mFooterView != null)
		{
			mRecyclerListView.addFooterView(mFooterView);
		}
		
		mRecyclerListView.setAdapter(mRecyclerViewAdapter);

		mRecyclerListView.setOnPullToRefreshListener(this);
		mRecyclerListView.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(RecyclerViewHolder holder, View view, int position)
			{
				realOnItemClick(holder, view, position);
			}
			
		});
		
		// 加载完成
		// realOnCreateViewComplete(view, savedInstanceState);
	}

	public static class RecyclerViewListAdapter<ITEM> extends RecyclerViewAdapter<ITEM>
	{
		
		private WeakReference<BaseFragmentListActivity<ITEM>> mWeakReference;
		
		public RecyclerViewListAdapter(WeakReference<BaseFragmentListActivity<ITEM>> weakReference, List<ITEM> items)
		{
			super(weakReference.get(), items);
			this.mWeakReference = weakReference;
		}
		
		@Override
		public void onBindViewHolder(RecyclerViewAdapter.RecyclerViewHolder holder, int position)
		{
			mWeakReference.get().realOnBindViewHolder(holder, position);
		}
		
		@Override
		public void onBindFooterViewHolder(RecyclerViewAdapter.FooterViewHolder viewHolder, int position)
		{
			// super.onBindFooterViewHolder(viewHolder, position);
			mWeakReference.get().realOnBindFooterViewHolder(viewHolder, position);
		}
		
		@Override
		public void onBindEmptyViewHolder(RecyclerViewAdapter.EmptyViewHolder viewHolder, int position)
		{
			super.onBindEmptyViewHolder(viewHolder, position);
			mWeakReference.get().realOnBindEmptyViewHolder(viewHolder, position);
		}
		
		@Override
		public View inflate(ViewGroup parent, int viewType)
		{
			return mWeakReference.get().realInflate(parent, viewType);
		}
		
		@Override
		public int getItemViewTypeExternal(int position)
		{
			return mWeakReference.get().realGetItemViewType(position);
		}

	}
	
	@Override
	public void onPullDownToRefresh(View view)
	{
		realOnPullDownToRefresh(mRecyclerListView, mParams);
	}
	
	@Override
	public void onPullUpToRefresh(View view)
	{
		realOnPullUpToRefresh(mRecyclerListView, mParams);
	}
	
	/**
	 * 第一次执行获取数据
	 * 
	 * @param params
	 */
	public void realExecuteFirstTime(SimpleParams params)
	{
		if (mEmptyView != null)
		{
			mEmptyView.onLoad();
		}
		
		if (mListData.isEmpty() || mListData.size() < mFirstQueryCount)
		{
			// 第一次取到数据后, 只能点击屏幕获取数据; 取得的数据<分页数量, 不允许上下拉.
		}

		mParams = params;
		
		realOnPullDownExecuteTask(params);
	}
	

	/**
	 * 执行获取数据的任务
	 */
	private void executeOnPullDownGetDataTask(final SimpleParams params)
	{
		new GetDataTask<SimpleParams, Void, XModel<Model>>()
		{

			@Override
			protected XModel<Model> doInBackground(SimpleParams... params)
			{
				XModel<Model> xModel = null;
				if (params == null || params.length == 0)
				{
					xModel = realOnPullDown(null);
				}
				else
				{
					xModel = realOnPullDown(params[0]);
				}

				if (xModel != null && !xModel.isEmpty() && xModel.mStatus == XModel.OK)
				{
					// 请求数据的起始位置发生变化
					mOffset += mCount;
					// mOffset = 0;
				}

				return xModel;
			}

			@Override
			protected void onPostExecute(XModel<Model> result)
			{
				if (result == null)
				{
					mEmptyView.onDataEmpty();
				}
				else if (result.mStatus == XModel.NOT_INTERNET)
				{
					// 网络错误
					if (mListData == null || mListData.isEmpty())
					{
						mEmptyView.onNetworkError();
					}
					else
					{
						mEmptyView.onDataEmpty();
					}
				}
				else if (result.mStatus == XModel.OK && result.mList != null)
				{
					// 正常取到结果, 有数据或没数据
					if (result.mList.isEmpty())
					{
						mEmptyView.onDataEmpty();
					}
					else
					{
						mListData.clear();
						mListData.addAll(0, result.mList);
						mRecyclerViewAdapter.notifyDataSetChanged();
					}
				}
				else if (result.mStatus == XModel.NOT_DATA || result.mStatus == XModel.NOT_SDCARD
				        || result.mStatus == XModel.UNKNOWN)
				{
					mEmptyView.onDataEmpty();
				}
				else
				{
					// Call onRefreshComplete when the list has been refreshed.
					mRecyclerListView.onRefreshComplete();
				}

				mParams = params;

				mRecyclerListView.onRefreshComplete();
				
				// 加载完成
				realOnLoadComplete();
				
				// 状态置为可加载状态
				mIsLoading = false;
				
				super.onPostExecute(result);
			}
		}.execute(params);
	}
	
	/**
	 * 执行获取数据的任务
	 */
	private void executeOnPullUpGetDataTask(final SimpleParams params)
	{
		new GetDataTask<SimpleParams, Void, XModel<Model>>()
		{

			@Override
			protected XModel<Model> doInBackground(SimpleParams... params)
			{
				XModel<Model> xModel = null;
				if (params == null || params.length == 0)
				{
					xModel = realOnPullUp(null);
				}
				else
				{
					xModel = realOnPullUp(params[0]);
				}

				if (xModel != null && !xModel.isEmpty() && xModel.mStatus == XModel.OK)
				{
					// 请求数据的起始位置发生变化
					mOffset += mCount;
				}

				return xModel;
			}

			@Override
			protected void onPostExecute(XModel<Model> result)
			{
				if (result == null)
				{
					//showToast(R.string.hint_no_more_data);
				}
				else if (result.mStatus == XModel.NOT_INTERNET)
				{
					// 网络错误
					if (mListData == null || mListData.isEmpty())
					{
						// 表示第一次取数据
						//mEmptyView.onNetworkError();
					}
					else
					{
						//mEmptyView.onDataEmpty();
						mRecyclerViewAdapter.notifyDataSetChanged();
					}
				}
				else if (result.mStatus == XModel.OK && result.mList != null)
				{
					// 正常取到结果, 有数据或没数据
					mListData.addAll(result.mList);
					mRecyclerViewAdapter.notifyDataSetChanged();
				}
				else
				{
					//showToast(R.string.hint_no_more_data);
					mRecyclerViewAdapter.notifyDataSetChanged();
				}
				// Auto scroll to the last item
				// mRecyclerListView.getRefreshableView().scrollToPosition(mRecyclerViewListAdapter.getItemCount()-1);

				// Call onRefreshComplete when the list has been refreshed.
				mRecyclerListView.onRefreshComplete();
				// 状态置为可加载状态
				mIsLoading = false;
				
				// Call after get data
				realOnLoadComplete();
				
				super.onPostExecute(result);
			}
		}.execute(params);
	}
	
	/**
	 * 
	 * @param refreshView
	 */
	public void realOnPullDownToRefresh(SwipeRefreshRecyclerLinearLayout recyclerListView, SimpleParams params)
	{
		// Set loading message
		// mEmptyView.onLoad();

		String label = DateUtils.formatDateTime(this, System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

		// Update the LastUpdatedLabel
		// refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

		// Do work to refresh the list here.
		realOnPullDownExecuteTask(params);
	}

	/**
	 * 
	 * @param refreshView
	 */
	public void realOnPullUpToRefresh(SwipeRefreshRecyclerLinearLayout recyclerListView, SimpleParams params)
	{
		String label = DateUtils.formatDateTime(this, System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

		// Update the LastUpdatedLabel
		// refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

		// Do work to refresh the list here.
		realOnPullUpExecuteTask(params);
	}
	
	/**
	 * 下拉执行的工作任务, 有任务在执行时, 直接返回, 不允许同一时间多次调用.
	 */
	public void realOnPullDownExecuteTask(SimpleParams params)
	{
		if (mIsLoading)
		{
			return;
		}
		mIsLoading = true;

		executeOnPullDownGetDataTask(params);
	}

	/**
	 * 上拉执行的工作任务, 有任务在执行时, 直接返回, 不允许同一时间多次调用.
	 */
	public void realOnPullUpExecuteTask(SimpleParams params)
	{
		if (mIsLoading)
		{
			return;
		}
		mIsLoading = true;
		
		executeOnPullUpGetDataTask(params);
	}
	
	/**
	 * Return SwipeRefreshRecyclerLinearLayout
	 * 
	 * @return
	 */
	public SwipeRefreshRecyclerLinearLayout getRecyclerListView()
	{
		return mRecyclerListView;
	}

	/**
	 * Return RecyclerViewAdapter
	 * 
	 * @return
	 */
	public RecyclerViewAdapter<Model> getRecyclerViewAdapter()
	{
		return mRecyclerViewAdapter;
	}

	/**
	 * 
	 * @param position
	 * @return
	 */
	public int realGetItemViewType(int position)
	{
		return RecyclerViewAdapter.ITEM_VIEW_TYPE_ITEM;
	}
	
	/**
	 * 返回数据对象
	 * 
	 * @return
	 */
	protected LinkedList<Model> realGetListData()
	{
		return this.mListData;
	}

	/**
	 * 返回当前选项的Item值
	 * 
	 * @param position
	 * @return
	 */
	protected Model realGetItem(int position)
	{
		return mRecyclerViewAdapter.getItem(position);
	}

	/**
	 * 某些情况下, 子类需要添加一个Item至当前已经存在的列表中
	 * 
	 * @param item
	 */
	protected void realAddNewItem(Model item)
	{
		++mOffset;
		mListData.add(0, item);
		mRecyclerViewAdapter.notifyDataSetChanged();
		
		// Call onRefreshComplete when the list has been refreshed.
		// mRecyclerListView.onRefreshComplete();
	}
	
	/**
	 * 
	 * @param position
	 */
	protected void realNotifyItemRemoved(int position)
	{
		mListData.remove(position);
		mRecyclerViewAdapter.notifyItemRemoved(position);
	}
	
	/**
	 * 加载完成
	 */
	protected void realOnLoadComplete()
	{
		
	}
	
	/**
	 * 点击事件
	 * 
	 * @param holder
	 * @param view
	 * @param position
	 */
	protected void realOnItemClick(RecyclerViewHolder holder, View view, int position)
	{
		
	}
	
}

package com.wo2b.wrapper.app.fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.opencdk.view.swiperefresh.OnPullToRefreshListener;
import com.opencdk.view.swiperefresh.RecyclerViewAdapter;
import com.opencdk.view.swiperefresh.RecyclerViewAdapter.RecyclerViewHolder;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.support.GroupExt;

/**
 * 支持分组的基类Fragment
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-11-14
 * @Modify 2015-11-14
 * @param <Model>
 */
public abstract class BaseListFragmentGroup<Model> extends BaseListFragment<GroupExt<Model>> implements
        OnPullToRefreshListener
{
	
	/**
	 * 加载列表项布局
	 * 
	 * @param parent
	 * @param position
	 * @return
	 */
	public abstract View realInflateChildView(ViewGroup parent, int viewType);
	
	/**
	 * 绘制列表项视图
	 * 
	 * @param holder
	 * @param groupX
	 * @param position
	 */
	public abstract void realOnBindChildHolder(RecyclerViewHolder holder, GroupExt<Model> groupX, int position);
	
	/**
	 * 加载列表分组布局, 有默认的分组模板, 子类可重写此方法, 自行布局. 此方法的重写一般需要同时重写{@link #realOnBindGroupHolder(RecyclerViewHolder, GroupExt, int)}
	 * 
	 * 
	 * @param parent
	 * @param viewType
	 * @return
	 */
	public View realInflateGroupView(ViewGroup parent, int viewType)
	{
		return LayoutInflater.from(getContext()).inflate(R.layout.wrapper_base_group_list_item, parent, false);
	}
	
	/**
	 * 绘制列表分组视图, 提供默认的分组的视图显示规则(默认根据组名显示).
	 * 
	 * @param holder
	 * @param groupX
	 * @param position
	 */
	public void realOnBindGroupHolder(RecyclerViewHolder holder, GroupExt<Model> groupX, int position)
	{
		TextView tvGroup = holder.findViewById(R.id.tv_group_name);
		
		if (!TextUtils.isEmpty(groupX.getGroupName()))
		{
			tvGroup.setText(groupX.getGroupName());
		}
		else
		{
			// NOTICE: 建议在数据解释时, 考虑分组名为空的情况, 由解析提供默认分组名称.
			tvGroup.setText(R.string.hint_unknown_group);
		}
		
		
//		String initial = groupX.getInitial();
//		if (!TextUtils.isEmpty(initial))
//		{
//			group_tv.setText(groupX.getInitial().toUpperCase());
//		}
	}
	
	@Override
	public int realGetItemViewType(int position)
	{
		GroupExt<Model> groupX = (GroupExt<Model>) realGetItem(position);
		
		if (groupX != null && groupX.isGroup())
		{
			return RecyclerViewAdapter.ITEM_VIEW_TYPE_GROUP;
		}
		else
		{
			return RecyclerViewAdapter.ITEM_VIEW_TYPE_ITEM;
		}
	}

	@Override
	public View realInflate(ViewGroup parent, int viewType)
	{
		if (viewType == RecyclerViewAdapter.ITEM_VIEW_TYPE_GROUP)
		{
			return realInflateGroupView(parent, viewType);
		}
		else if (viewType == RecyclerViewAdapter.ITEM_VIEW_TYPE_ITEM)
		{
			return realInflateChildView(parent, viewType);
		}
		else
		{
			// Never happen ==!
			throw new IllegalArgumentException("不支持的参数类型, [viewType: " + viewType + "]");
		}
	}

	@Override
	public void realOnBindViewHolder(RecyclerViewHolder holder, int position)
	{
		GroupExt<Model> groupX = (GroupExt<Model>) realGetItem(position);

		if (groupX == null)
		{
			// header or footer
			return;
		}
		
		if (groupX.isGroup())
		{
			realOnBindGroupHolder(holder, groupX, position);
		}
		else
		{
			realOnBindChildHolder(holder, groupX, position);
		}
	}

	@Override
	protected void realOnItemClick(RecyclerViewHolder holder, View view, int position)
	{
		GroupExt<Model> groupX = (GroupExt<Model>) realGetItem(position);

		if (groupX.isGroup())
		{
			realOnGroupItemClick(holder, groupX, position);
		}
		else
		{
			realOnChildItemClick(holder, groupX, position);
		}
	}

	/**
	 * 列表项点击事件
	 * 
	 * @param holder
	 * @param groupX
	 * @param position
	 */
	protected void realOnGroupItemClick(RecyclerViewHolder holder, GroupExt<Model> groupX, int position)
	{
		
	}
	
	/**
	 * 分组项点击事件
	 * 
	 * @param holder
	 * @param groupX
	 * @param position
	 */
	protected void realOnChildItemClick(RecyclerViewHolder holder, GroupExt<Model> groupX, int position)
	{
		
	}

}

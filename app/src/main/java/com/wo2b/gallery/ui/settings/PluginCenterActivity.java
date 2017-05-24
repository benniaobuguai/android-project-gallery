package com.wo2b.gallery.ui.settings;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;

import com.opencdk.view.swiperefresh.RecyclerViewAdapter.RecyclerViewHolder;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.BaseFragmentListActivity;
import com.wo2b.wrapper.app.support.SimpleParams;
import com.wo2b.wrapper.app.support.XModel;
import com.wo2b.xxx.webapp.manager.plugin.PluginInfo;

/**
 * 插件库
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 */
public class PluginCenterActivity extends BaseFragmentListActivity<PluginInfo>
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wrapper_wo2b_plugin_list);
		
		realExecuteFirstTime(null);
		
		setActionBarTitle(getString(R.string.plugin_center));
	}
	
	@Override
	public View realInflate(ViewGroup parent, int viewType)
	{
		return null;
	}
	
	@Override
	public void realOnBindViewHolder(RecyclerViewHolder holder, int position)
	{
		
	}
	
	@Override
	protected XModel<PluginInfo> realOnPullDown(SimpleParams params)
	{
		SystemClock.sleep(10 * 1000);
		
		return null;
	}
	
	@Override
	protected XModel<PluginInfo> realOnPullUp(SimpleParams params)
	{
		return null;
	}
	
}

package com.wo2b.gallery.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.wo2b.gallery.R;
import com.wo2b.wrapper.app.BaseFragmentActivity;
import com.wo2b.wrapper.view.XPreference;
import com.wo2b.wrapper.view.XPreferenceExtra;
import com.wo2b.wrapper.view.XPreferenceExtra.OnXPreferenceSelectListener;

/**
 * Settings Activity.
 * 
 * @author 笨鸟不乖
 * 
 */
public class SettingsActivity extends BaseFragmentActivity implements OnClickListener
{
	
	private static final String TAG = "SettingsActivity";
	
	private XPreference xp_traffic_statistics;
	private XPreference xp_clear_storage;
	private XPreference xp_plugin_center;
	
	// 这部分是从超级能力里面提取出来的
	private XPreferenceExtra xp_wallpaper;
	private XPreferenceExtra xp_auto_play;
	private XPreferenceExtra xp_cache_local;
	private XPreferenceExtra xp_image_download;
	private XPreferenceExtra xp_bg_music;
	private XPreference xp_open_lock;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uc_setting_main);
		
		initView();
	}

	@Override
	protected void initView()
	{
		setActionBarTitle(R.string.setting);

		xp_traffic_statistics = (XPreference) findViewById(R.id.xp_traffic_statistics);
		xp_clear_storage = (XPreference) findViewById(R.id.xp_storage_manager);
		xp_plugin_center = (XPreference) findViewById(R.id.xp_plugin_center);

		xp_wallpaper = (XPreferenceExtra) findViewById(R.id.xp_wallpaper);
		xp_auto_play = (XPreferenceExtra) findViewById(R.id.xp_auto_play);
		xp_cache_local = (XPreferenceExtra) findViewById(R.id.xp_cache_local);
		xp_image_download = (XPreferenceExtra) findViewById(R.id.xp_image_download);
		xp_bg_music = (XPreferenceExtra) findViewById(R.id.xp_bg_music);
		xp_open_lock = (XPreference) findViewById(R.id.xp_open_lock);

		xp_wallpaper.setOnXPreferenceChangeListener(new OnXPreferenceSelectListenerImpl());
		xp_auto_play.setOnXPreferenceChangeListener(new OnXPreferenceSelectListenerImpl());
		xp_cache_local.setOnXPreferenceChangeListener(new OnXPreferenceSelectListenerImpl());
		xp_image_download.setOnXPreferenceChangeListener(new OnXPreferenceSelectListenerImpl());
		xp_bg_music.setOnXPreferenceChangeListener(new OnXPreferenceSelectListenerImpl());
		xp_open_lock.setOnClickListener(new OnLockClickListener());

		xp_traffic_statistics.setOnClickListener(this);
		xp_clear_storage.setOnClickListener(this);
		xp_plugin_center.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.xp_traffic_statistics:
			{
				onTrafficStatisticsClick(v);
				break;
			}
			case R.id.xp_storage_manager:
			{
				onStorageManagerClick(v);
				break;
			}
			case R.id.xp_plugin_center:
			{
				onPluginCenterClick(v);
				break;
			}
		}
	}
	
	/**
	 * 进入超级能力, 暂时全部已经直接提取至设置主界面, 有更多功能再进行独立出去.
	 * @param v
	 */
	private void onSuperPowerClick(View v)
	{
		Intent intent = new Intent();
		intent.setClass(this, SuperPowerActivity.class);
		startActivity(intent);
	}
	
	private void onTrafficStatisticsClick(View v)
	{
		Intent intent = new Intent();
		intent.setClass(this, TrafficStatisticsActivity.class);
		startActivity(intent);
	}
	
	private void onStorageManagerClick(View v)
	{
		Intent intent = new Intent();
		intent.setClass(this, StorageManagerActivity.class);
		startActivity(intent);
	}
	
	private void onPluginCenterClick(View v)
	{
		Intent intent = new Intent(getContext(), PluginCenterActivity.class);
		startActivity(intent);
	}
	
	
	public class OnXPreferenceSelectListenerImpl extends OnXPreferenceSelectListener
	{

		@Override
		public boolean onXPreferenceSelected(XPreference preference, boolean isChecked)
		{
			if (!isChecked)
			{
				showToast("UnSelected, should do lots of things.");
			}
			else
			{
				showToast("Selected, do nothing!!!");
			}

			return true;
		}

	}
	
	/**
	 * 锁屏回调
	 * 
	 */
	public class OnLockClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent();
			intent.setClass(getContext(), LockViewActivity.class);
			intent.setAction(LockViewActivity.ACTION_LOCK_ENCODE);
			startActivity(intent);
		}

	}
	
}

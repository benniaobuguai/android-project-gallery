package com.wo2b.wrapper.app.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

import com.opencdk.bus.CDKBus;
import com.opencdk.bus.CDKEvent;
import com.opencdk.net.NetStatus;
import com.opencdk.util.log.Log;

/**
 * 系统守护进程
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2015-4-2
 */
public class DaemonService extends Service
{
	
	private static final String TAG = "Wrapper.DaemonService";

	/**
	 * 网络状态
	 */
	public static NetStatus mNetStatus = NetStatus.CONNECTED;

	// 网络状态监听
	private static BroadcastReceiver mConnectionReceiver = new BroadcastReceiver()
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			Log.I(TAG, "Network state changed, action: " + action);

			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
			NetworkInfo mobileNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if (mobileNetInfo.isConnected() && wifiNetInfo.isConnected())
			{
				mNetStatus = NetStatus.CONNECTED;
			}
			else if (mobileNetInfo.isConnected())
			{
				mNetStatus = NetStatus.MOBILE;
			}
			else if (wifiNetInfo.isConnected())
			{
				Log.I(TAG, "Wifi Network is connected.");
				mNetStatus = NetStatus.WIFI;
			}
			else if (!mobileNetInfo.isConnected() && !wifiNetInfo.isConnected())
			{
				// 所有网络都没有连接至网络
				mNetStatus = NetStatus.DISCONNECTED;
			}
			else
			{
				// 所有网络都没有连接至网络
				mNetStatus = NetStatus.DISCONNECTED;
			}

			Log.D(TAG, "Network status changed: " + mNetStatus.name());

			CDKBus.post(CDKEvent.NETWORK_STATUS, mNetStatus);
		}
	};

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.I(TAG, "DaemonService-->onCreate");

		registerNetworkBroadcastReceiver();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		unregisterNetworkBroadcastReceiver();
	}

	/**
	 * 注册网络连接广播接收器
	 */
	private void registerNetworkBroadcastReceiver()
	{
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		// intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		registerReceiver(mConnectionReceiver, intentFilter);
	}

	/**
	 * 取消网络连接广播接收器
	 */
	private void unregisterNetworkBroadcastReceiver()
	{
		if (mConnectionReceiver != null)
		{
			unregisterReceiver(mConnectionReceiver);
		}
	}

	// NETWORK
	public boolean isNetworkAvailable()
	{
		Context context = getApplicationContext();
		ConnectivityManager connect = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connect == null)
		{
			return false;
		}
		else
		{
			// get all network info
			NetworkInfo[] info = connect.getAllNetworkInfo();
			if (info != null)
			{
				for (int i = 0; i < info.length; i++)
				{
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}
				}
			}
		}

		return false;
	}

}

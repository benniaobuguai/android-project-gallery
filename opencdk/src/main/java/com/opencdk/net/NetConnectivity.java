package com.opencdk.net;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

/**
 * 网络连接管理器
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 */
public final class NetConnectivity
{
	
	/**
	 * 判断是否有网络连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context)
	{
		if (context != null)
		{
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = manager.getActiveNetworkInfo();
			if (networkInfo != null)
			{
				return networkInfo.isAvailable();
			}
		}
		
		return false;
	}
	
	/**
	 * 判断WIFI网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context)
	{
		if (context != null)
		{
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (networkInfo != null)
			{
				return networkInfo.isAvailable();
			}
		}
		
		return false;
	}
	
	/**
	 * 判断MOBILE网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context)
	{
		if (context != null)
		{
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (networkInfo != null)
			{
				return networkInfo.isAvailable();
			}
		}
		
		return false;
	}
	
	/**
	 * 获取当前网络连接的类型信息
	 * 
	 * @param context
	 * @return
	 */
	public static int getConnectedType(Context context)
	{
		if (context != null)
		{
			ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = manager.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isAvailable())
			{
				return networkInfo.getType();
			}
		}
		
		return -1;
	}
	
	/**
	 * 
	 * @param context
	 * @param intent
	 * @param action
	 */
	public static void wifiStateChangedAction(Context context, Intent intent, String action)
	{
		if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action))
		{
			// 在此监听wifi有无
			int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
			switch (wifiState)
			{
				case WifiManager.WIFI_STATE_DISABLED:
				{
					Toast.makeText(context, "WIFI_STATE_DISABLED", Toast.LENGTH_LONG).show();
					break;
				}
				case WifiManager.WIFI_STATE_DISABLING:
				{
					Toast.makeText(context, "WIFI_STATE_DISABLING", Toast.LENGTH_LONG).show();
					break;
				}
				case WifiManager.WIFI_STATE_ENABLED:
				{
					Toast.makeText(context, "WIFI_STATE_ENABLED", Toast.LENGTH_LONG).show();
					break;
				}
				case WifiManager.WIFI_STATE_ENABLING:
				{
					Toast.makeText(context, "WIFI_STATE_ENABLING", Toast.LENGTH_LONG).show();
					break;
				}
				case WifiManager.WIFI_STATE_UNKNOWN:
				{
					Toast.makeText(context, "WIFI_STATE_UNKNOWN", Toast.LENGTH_LONG).show();
					break;
				}
			}
		}
	}
	
}

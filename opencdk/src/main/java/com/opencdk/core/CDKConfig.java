package com.opencdk.core;

import android.content.Context;
import android.os.Environment;

/**
 * <pre>
 * <ul><b>版本更新日志</b></ul>
 * <li>2013-12-18 Library基本稳定<b> [1.0.0]</b></li>
 * <li>2014-01-15 Library更改包名<b> [2.0.0]</b></li>
 * <li>2015-10-10 Library重构<b> [3.0.0]</b></li>
 * </pre>
 * 
 * @author 笨鸟不乖
 * 
 */
public class CDKConfig
{

	/**
	 * 类库名称
	 */
	public final static String LIBRARY_NAME = "CDK";
	
	/**
	 * Sdk版本
	 */
	public final static String LIBRARY_VERSION = "3.0.0";
	
	/**
	 * 通用日志标签
	 */
	public final static String TAG = "CDK";
	
	/**
	 * 调试模式: 用于控制打印日志等
	 */
	public static boolean DEBUG = true;
	
	/**
	 * 发布版本
	 */
	public final static boolean RELEASE = false;
	
	/**
	 * 保存路径
	 */
	public static final String APP_DIR = Environment.getExternalStorageDirectory() + "/opencdk";
	
	/**
	 * 临时数据
	 */
	private static final String APP_DIR_TEMP = APP_DIR + "/temp";
	
	/**
	 * 普通缓存
	 */
	private static final String APP_DIR_CACHE = APP_DIR + "/cache";
	
	private Context context;
	
	private CDKContext cdkContext;
	
	private boolean hasAdBanner;
	private boolean hasAdPointsWall;
	
	private CDKConfig(final Builder builder)
	{
		this.context = builder.context;
		this.cdkContext = builder.cdkContext;
		this.hasAdBanner = builder.hasAdBanner;
		this.hasAdPointsWall = builder.hasAdPointsWall;
	}
	
	/**
	 * Returns library version.
	 * 
	 * @return
	 */
	public static String getLibraryVersion()
	{
		return LIBRARY_VERSION;
	}
	
	/**
	 * Returns library name.
	 * 
	 * @return
	 */
	public static String getLibraryName()
	{
		return LIBRARY_NAME;
	}
	
	/**
	 * Always the application context.
	 * 
	 * @return
	 */
	public Context getContext()
	{
		return context;
	}

	/**
	 * Returns cdk context.
	 * 
	 * @return
	 */
	public CDKContext getCDKContext()
	{
		return cdkContext;
	}

	/**
	 * 是否需要显示Banner广告
	 * 
	 * @return
	 */
	public boolean hasAdBanner()
	{
		return hasAdBanner;
	}
	
	/**
	 * 是否需要显示积分墙广告
	 * 
	 * @return
	 */
	public boolean hasAdPointsWall()
	{
		return hasAdPointsWall;
	}
	
	/**
	 * 应用程序文件的根目录
	 * 
	 * @return
	 */
	public static String getAppDir()
	{
		return APP_DIR;
	}
	
	/**
	 * 临时文件目录, 临时策略-->用以"伪装"保存用户信息
	 * 
	 * @return
	 */
	public static String getAppTempDir()
	{
		return APP_DIR_TEMP;
	}
	
	/**
	 * 缓存目录
	 * 
	 * @return
	 */
	public static String getAppCacheDir()
	{
		return APP_DIR_CACHE;
	}

	public static class Builder
	{
		
		private Context context;
		private CDKContext cdkContext;
		private boolean hasAdBanner;
		private boolean hasAdPointsWall;
		
		public Builder(Context application)
		{
			this.context = application;
			this.cdkContext = CDKContext.DEFAULT_CDK_CONTEXT;
		}
		
		public Builder cdkContext(CDKContext cdkContext)
		{
			this.cdkContext = cdkContext;
			return this;
		}
		
		/**
		 * 是否需要显示Banner广告
		 * 
		 * @param hasAdBanner
		 * @return
		 */
		public Builder hasAdBanner(boolean hasAdBanner)
		{
			this.hasAdBanner = hasAdBanner;
			return this;
		}
		
		/**
		 * 是否需要显示积分墙广告
		 * 
		 * @param hasAdPointsWall
		 * @return
		 */
		public Builder hasAdPointsWall(boolean hasAdPointsWall)
		{
			this.hasAdPointsWall = hasAdPointsWall;
			return this;
		}
		
		public CDKConfig build()
		{
			return new CDKConfig(this);
		}
		
	}
	
	// ======================================================================================================================
	//
	/**
	 * 默认时长
	 */
	public static final long DEFAULT_TIME = 0;
	
	public static enum Device
	{
		
		/** 手机 */
		PHONE(0x00000001, "phone"),
		
		/** 平板 */
		PAD(0x00000002, "pad"),
		
		/** 机顶盒 */
		STB(0x00000003, "stb");
		
		/** 设备id */
		private int deviceId;
		
		/** 设备描述 */
		private String deviceDesc;
		
		/** 设备名称 */
		private String deviceName;
		
		private Device(int deviceId, String deviceDesc)
		{
			this.deviceId = deviceId;
			this.deviceDesc = deviceDesc;
			this.deviceName = android.os.Build.MODEL;
		}
		
		/**
		 * 返回设备id
		 * 
		 * @return
		 */
		public int getDeviceId()
		{
			return deviceId;
		}
		
		/**
		 * 设备描述
		 * 
		 * @return
		 */
		public String getDeviceDesc()
		{
			return deviceDesc;
		}
		
		/**
		 * 返回设备名称
		 * 
		 * @return
		 */
		public String getDeviceName()
		{
			return deviceName;
		}
		
		@Override
		public String toString()
		{
			return "[" + deviceDesc + ", " + deviceName + "]";
		}
		
	}
	
}

package com.opencdk.core;

import android.content.Context;

import com.opencdk.util.log.Log;

/**
 * CDK, 项目通用类库.
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 2.0.0
 * @date 2014-07-15
 */
public final class CDK
{
	
	public static final String TAG = CDKConfig.TAG;

	private static final byte[] mLock = new byte[0];

	/** CDK对象 */
	private static volatile CDK mCDK = null;

	/** CDKConfig */
	private CDKConfig mCDKConfig;

	/** CDK Context */
	private CDKContext mCDKContext;

	/** 静态调试标识, 方便全局使用 */
	public static boolean DEBUG = false;
	
	/**
	 * 二级调试开关, 仅提供开发人员使用, 会提示更多更全的日志信息.
	 */
	public static final boolean DEBUG_SECONDARY = true;
	
	/**
	 * 私有构造函数
	 * 
	 * @param context
	 */
	private CDK()
	{
		// 默认的ClientContext
		this.mCDKContext = CDKContext.DEFAULT_CDK_CONTEXT;
		// 默认已经发布, 并支持调试
		this.mCDKContext.setFlags(CDKContext.FLAG_RELEASE | CDKContext.FLAG_DEBUG);

		DEBUG = this.mCDKContext.isDebug();
	}
	
	/**
	 * 返回CDK实例
	 * 
	 * @return
	 */
	public static CDK getInstance()
	{
		if (mCDK == null)
		{
			synchronized (mLock)
			{
				if (mCDK == null)
				{
					mCDK = new CDK();
				}
			}
		}

		return mCDK;
	}
	
	/**
	 * 类库初始化
	 * 
	 * @param context
	 * @return
	 */
	public void init(CDKConfig cdkConfig)
	{
		Log.I(TAG, "Welcome to use " + CDKConfig.LIBRARY_NAME + "(v" + CDKConfig.LIBRARY_VERSION + ").");
		this.mCDKConfig = cdkConfig;
		this.mCDKContext = cdkConfig.getCDKContext();
		
		if (DEBUG)
		{
			Log.W(TAG, CDKConfig.LIBRARY_NAME + " is in debug mode.");
		}
	}
	
	/**
	 * Change CDK Context.
	 * 
	 * @param cdkContext
	 */
	public void changedCDKContext(CDKContext cdkContext)
	{
		this.mCDKContext = cdkContext;
		
		if (DEBUG)
		{
			printCDKContext(this.mCDKContext);
		}
	}
	
	/**
	 * 打印客户端信息
	 * 
	 * @param cdkContext
	 */
	private static void printCDKContext(CDKContext cdkContext)
	{
		Log.E(TAG, "------------------------ CDK Context ------------------------");
		
		Log.I(TAG, "APP PackageName: " + cdkContext.getPkgname());
		
		
//		switch (cdkContext.getDeviceType())
//		{
//			case CDKConfig.Device.PHONE:
//			{
//				Log.I(TAG, "DEVICE_TYPE: PHONE");
//				break;
//			}
//			case CDKConfig.Device.PAD:
//			{
//				Log.I(TAG, "DEVICE_TYPE: PAD");
//				break;
//			}
//			case CDKConfig.Device.STB:
//			{
//				Log.I(TAG, "DEVICE_TYPE: STB");
//				break;
//			}
//			default:
//			{
//				Log.E(TAG, "Unknown device");
//				break;
//			}
//		}
		Log.I(TAG, "DEVICE_NAME: " + cdkContext.getAppname());
		Log.I(TAG, "RELEASE_FLG: " + cdkContext.isRelease());
		Log.I(TAG, "DEBUG_FLG: " + cdkContext.isDebug());
		
		Log.E(TAG, "------------------------ CDK Context ------------------------");
	}

	/**
	 * Returns CDKConfig.
	 * 
	 * @return
	 */
	public CDKConfig getCDKConfig()
	{
		return mCDKConfig;
	}

	/**
	 * Returns CDKContext.
	 * 
	 * @return
	 */
	public CDKContext getCDKContext()
	{
		return mCDKContext;
	}

	/**
	 * Returns ApplicationContext.
	 * 
	 * @return
	 */
	public Context getContext()
	{
		if (mCDKConfig != null)
		{
			return mCDKConfig.getContext();
		}

		return null;
	}
	
	/**
	 * 版本信息
	 * 
	 * @return
	 */
	public String getSdkVersion()
	{
		return CDKConfig.getLibraryVersion();
	}
	
	/**
	 * 类库名称
	 * 
	 * @return
	 */
	public String getSdkName()
	{
		return CDKConfig.getLibraryName();
	}
	
	/**
	 * 主动/异常退出, Library释放资源
	 */
	public void onDestroy()
	{
		Log.I(TAG, "cdk--->onDestroy()");
		
		// CDK资源释放
		
		Log.I(TAG, "cdk quit.");
	}

}

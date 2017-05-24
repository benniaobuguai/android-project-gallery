package com.opencdk.core;

import android.app.Application;
import android.content.Context;

/**
 * CDK Application
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 2.0.0
 * @since 2015-4-1
 * @Modify 2015-7-15
 */
public abstract class CDKApplication extends Application
{

	/** 是否已经进入过应用程序 */
	public static boolean isEntry = false;
	
	private static Context mGApplication = null;

	@Override
	public void onCreate()
	{
		super.onCreate();

		// 自行实现, 系统崩溃的处理类
		// CrashHandler.getInstance().init(this);

		mGApplication = this;

		setUp();
	}

	public static class Config
	{

		public static final boolean DEVELOPER_MODE = false;
	}

	// -------------------- To do sth. --------------------
	/**
	 * Call when application set up.
	 */
	protected abstract void setUp();

	/**
	 * Not support now, it will be used in future.
	 */
	@Deprecated
	protected abstract void tearDown();

	// -------------------- Common --------------------

	public static Context getGApplication()
	{
		return mGApplication;
	}

	public static boolean isEntry()
	{
		return isEntry;
	}

	public static void setEntry(boolean isEntry)
	{
		CDKApplication.isEntry = isEntry;
	}

}

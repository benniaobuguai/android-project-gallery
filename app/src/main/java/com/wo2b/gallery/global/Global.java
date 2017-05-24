package com.wo2b.gallery.global;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.opencdk.util.DeviceInfoManager;
import com.opencdk.util.ManifestTools;
import com.opencdk.core.CDK;
import com.opencdk.core.CDKConfig;
import com.opencdk.core.CDKContext;
import com.opencdk.util.upgrade.VersionInfo;

/**
 * 全局信息及控制等
 * 
 * @author 笨鸟不乖
 * 
 */
public class Global
{

	/**
	 * 调试信息
	 */
	public static boolean Debug = true;

	/**
	 * 在此时间间隔内的连续操作只取第一次操作, 后续操作均会忽略.
	 */
	public static final int VIEW_REPEAT_CLICK_TIME_SPAN = 800;

	/**
	 * 应用版本, 默认为免费版
	 */
	public static Version mVersion = Version.Defualt;
	
	/**
	 * 系统缓存标识符
	 */
	public static final String SYSTEM_CACHE_PREFIX = "g_s_";

	/**
	 * 全局缓存标识符
	 */
	public static final String GLOBAL_CACHE_PREFIX = "g_c_";

	/**
	 * 生成系统缓存索引值
	 * 
	 * @param key
	 * @return
	 */
	public static String systemCacheKey(String key)
	{
		return SYSTEM_CACHE_PREFIX + key;
	}

	/**
	 * 生成全局缓存索引值
	 * 
	 * @param key
	 * @return
	 */
	public static String globalCacheKey(String key)
	{
		return GLOBAL_CACHE_PREFIX + key;
	}
	
	/**
	 * 系统初始化
	 * 
	 * @param context
	 */
	public static void init(Context context)
	{
		if (CDK.getInstance().getContext() != null)
		{
			return;
		}

		Context application = context.getApplicationContext();
		
		// 用于获取浏览器代理
		WebView webview = new WebView(context);
		webview.layout(0, 0, 0, 0);
		WebSettings webSettings = webview.getSettings();
		
		ApplicationInfo applicationInfo = context.getApplicationInfo();
		
		VersionInfo versionInfo = ManifestTools.getVersionInfo(application);
		String userAgent = webSettings.getUserAgentString();
		
		// Structure the CDKContext.
		CDKContext cdkContext = new CDKContext(application.getPackageName());
		//clientInfo.setAppicon(R.drawable.ic_launcher);
		cdkContext.setAppicon(applicationInfo.icon);
		cdkContext.setAppname(ManifestTools.getApplicationLable(application));
		cdkContext.setDeviceType(CDKConfig.Device.PHONE);
		cdkContext.setAlias(android.os.Build.MODEL);
		cdkContext.setAndroidSdkVersion(android.os.Build.VERSION.SDK_INT);
		cdkContext.setMac(DeviceInfoManager.getMacAddress(application));
		
		// Webkit user-agent
		cdkContext.setUserAgent(userAgent);
		
		if (versionInfo != null)
		{
			cdkContext.setAppVersionCode(versionInfo.getVersionCode());
			cdkContext.setAppVersionName(versionInfo.getVersionName());
		}
		
		// FIXME: Take attention...
		cdkContext.addFlags(CDKContext.FLAG_DEBUG | CDKContext.FLAG_RELEASE);
		// cdkContext.addFlags(CDKContext.FLAG_DEBUG);
		
		// TODO: 广告
		CDKConfig config = new CDKConfig.Builder(application)
			.cdkContext(cdkContext)
			.hasAdBanner(false)		// 显示积分Banner
			.hasAdPointsWall(true)	// 显示积分墙
			.build();
		
		CDK.getInstance().init(config);
	}

	/**
	 * 应用版本
	 * 
	 * <pre>
	 * 0-默认版; 1-其它版本
	 * 
	 * </pre>
	 */
	public static enum Version
	{

		/**
		 * 默认版
		 */
		Defualt(0, "default", "com.wo2b.gallery");

		/**
		 * 
		 */
		public int mType;

		/**
		 * 唯一标识
		 */
		public String mAppId;

		/**
		 * 包名
		 */
		public String mPkgName;

		Version(int type, String appId, String pkgName)
		{
			this.mType = type;
			this.mAppId = appId;
			this.mPkgName = pkgName;
		}

	}

}

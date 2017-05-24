package com.opencdk.core;


/**
 * 客户端上下文
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 2.0.0
 * @date 2014-07-15
 */
public class CDKContext
{
	
	/**
	 * PHONE客户端, 默认
	 */
	public final static CDKContext DEFAULT_CDK_CONTEXT = new CDKContext
	(
		"com.opencdk",
		"Open Common Develop Kit"
	);
	
	/**
	 * 应用包名唯一标识
	 */
	private String pkgname;

	/**
	 * 应用名称
	 */
	private String appname;

	/**
	 * 应用图标
	 */
	private int appicon;
	
	/**
	 * 客户端设备类型, {@link CDKConfig.Device}
	 */
	private CDKConfig.Device deviceType;
	
	/**
	 * 别名
	 */
	private String alias;
	
	/**
	 * 客户端请求标识, 用于标识支持[版本], 或者[调试], etc.
	 */
	private int flags;
	
	/**
	 * mac地址
	 */
	private String mac;
	
	/**
	 * 浏览器代理, 为了保护服务器数据, 所有请求都需要带代理信息.
	 */
	private String userAgent;
	
	/**
	 * 安卓系统版本
	 */
	private int androidSdkVersion;
	
	/**
	 * 版本编码, 对应于Manifest.xml的版本信息.
	 */
	private int appVersionCode;
	
	/**
	 * 版本名称, 对应于Manifest.xml的版本信息.
	 */
	private String appVersionName;
	
	public CDKContext(String pkgname)
	{
		this(pkgname, pkgname);
	}
	
	public CDKContext(String pkgname, String appname)
	{
		this.pkgname = pkgname;
		this.appname = appname;
	}
	
	public String getPkgname()
	{
		return pkgname;
	}
	
	public void setPkgname(String pkgname)
	{
		this.pkgname = pkgname;
	}

	/**
	 * 返回应用名称
	 * 
	 * @return
	 */
	public String getAppname()
	{
		return appname;
	}

	/**
	 * 设置应用名称
	 * 
	 * @param appname
	 */
	public void setAppname(String appname)
	{
		this.appname = appname;
	}
	
	/**
	 * 设置应用图标
	 * 
	 * @param appicon
	 */
	public void setAppicon(int appicon)
	{
		this.appicon = appicon;
	}

	/**
	 * 返回应用图标
	 * 
	 * @return
	 */
	public int getAppicon()
	{
		return appicon;
	}
	
	public CDKConfig.Device getDeviceType()
	{
		return deviceType;
	}
	
	public void setDeviceType(CDKConfig.Device deviceType)
	{
		this.deviceType = deviceType;
	}

	public String getAlias()
	{
		return alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}
	
	public int getFlags()
	{
		return flags;
	}
	
	public CDKContext setFlags(int flags)
	{
		this.flags = flags;
		return this;
	}
	
	public CDKContext addFlags(int flags)
	{
		this.flags |= flags;
		return this;
	}
	
	/**
	 * 客户端版本是否为正式版本
	 * 
	 * @return
	 */
	public boolean isRelease()
	{
		return (this.flags & FLAG_RELEASE) == FLAG_RELEASE;
	}
	
	/**
	 * 调试模式是否开启
	 * 
	 * @return
	 */
	public boolean isDebug()
	{
		return (this.flags & FLAG_DEBUG) == FLAG_DEBUG;
	}
	
	public String getMac()
	{
		return mac;
	}

	public void setMac(String mac)
	{
		this.mac = mac;
	}
	
	/**
	 * 浏览器代理, 为了保护服务器数据, 所有请求都需要带代理信息.
	 * 
	 * @return
	 */
	public String getUserAgent()
	{
		return userAgent;
	}

	/**
	 * 浏览器代理, 为了保护服务器数据, 所有请求都需要带代理信息.
	 * 
	 * @param userAgent
	 */
	public void setUserAgent(String userAgent)
	{
		this.userAgent = userAgent;
	}
	
	/**
	 * 返回客户端的Android SDK版本
	 * 
	 * @return
	 */
	public int getAndroidSdkVersion()
	{
		return androidSdkVersion;
	}
	
	/**
	 * 设置客户端的Android SDK版本
	 * 
	 * @param androidSdkVersion
	 */
	public void setAndroidSdkVersion(int androidSdkVersion)
	{
		this.androidSdkVersion = androidSdkVersion;
	}

	public int getAppVersionCode()
	{
		return appVersionCode;
	}

	public void setAppVersionCode(int appVersionCode)
	{
		this.appVersionCode = appVersionCode;
	}

	public String getAppVersionName()
	{
		return appVersionName;
	}

	public void setAppVersionName(String versionName)
	{
		this.appVersionName = versionName;
	}

	/**
	 * 返回唯一标识符
	 */
	@Override
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(this.pkgname);
		sb.append("-");
		sb.append(this.appname);
		sb.append("-");
		sb.append(this.deviceType);

		return sb.toString();
	}

	
	// ---------------------------------------------------------------------
	// ---------------------------------------------------------------------
	// ClientContext flags (see flags variable).
	// <-------> <--> <---------------------->
	// 0100 0000 0000 0000 0000 0000 0000 0000
	// 前7位, 系统级标识; 中间4位, 暂保留; 后20位, Library向上层业务提供.
	// 注: 第一位为符号位.
	// 举例: (0x40000000 | 0x8000000) 表示是正式版本并且支持调试.
	
	/**
	 * 版本发布标识
	 */
	public static final int FLAG_RELEASE = 0x40000000;
	
	/**
	 * 调试标识
	 */
	public static final int FLAG_DEBUG = 0x8000000;

}

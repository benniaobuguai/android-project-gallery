package com.wo2b.wrapper.preference;

import com.opencdk.config.CDKPreference;
import com.wo2b.wrapper.R;

/**
 * 偏好设置工具类
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 2.0.0
 * @since 2015-10-20
 * @Modify 2015-8-2
 */
public final class XPreferenceManager extends CDKPreference
{
	
	private static volatile XPreferenceManager mInstance = null;

	/**
	 * 私有构造函数
	 * 
	 * @param application
	 */
	private XPreferenceManager()
	{

	}

	/**
	 * 返回XPreferenceManager对象
	 * 
	 * @param context
	 * @return
	 */
	public static XPreferenceManager getInstance()
	{
		if (mInstance == null)
		{
			synchronized (XPreferenceManager.class)
			{
				if (mInstance == null)
				{
					mInstance = new XPreferenceManager();
				}
			}
		}

		return mInstance;
	}
	
	// -------------------------------- High Frequency --------------------------------
	/**
	 * 设置壁纸
	 * 
	 * @return
	 */
	public boolean isSetWallpaper()
	{
		return getBoolean(getKeyString(R.string.pk_wallpaper), true);
	}
	
	/**
	 * 是否自动播放
	 * 
	 * @return
	 */
	public boolean isAutoPlay()
	{
		return getBoolean(getKeyString(R.string.pk_auto_play), true);
	}
	
	/**
	 * 是否缓存本地
	 * 
	 * @return
	 */
	public boolean cacheLocalable()
	{
		return getBoolean(getKeyString(R.string.pk_cache_local), true);
	}
	
	/**
	 * 是否下载图片
	 * 
	 * @return
	 */
	public boolean imageDownloadable()
	{
		return getBoolean(getKeyString(R.string.pk_image_download), true);
	}
	
	/**
	 * 是否下载图片
	 * 
	 * @return
	 */
	public boolean hasMusicBackground()
	{
		return getBoolean(getKeyString(R.string.pk_bg_music), true);
	}
	
	/**
	 * 是否加载
	 * 
	 * @return
	 */
	public boolean isPasswordLock()
	{
		return getBoolean(getKeyString(R.string.pk_password_lock), true);
	}
	
}

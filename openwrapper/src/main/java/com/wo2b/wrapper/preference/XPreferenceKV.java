package com.wo2b.wrapper.preference;

/**
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-8-2
 * @Modify 2015-8-2
 */
public interface XPreferenceKV
{
	
	public interface Values
	{
		
		public static final String PREF_SLEEP_MODE_QUIT = "exit";
		public static final String PREF_SLEEP_MODE_STOP = "stop";
		public static final String PREF_SLEEP_MODE_DEFAULT = PREF_SLEEP_MODE_QUIT;
		
	}
	
	public interface Keys
	{
		
		/** 进入密码 */
		public static final String ENTRY_PASSWORD = "entry_password";
		
		/** 睡眠模式 */
		public static final String PREF_SLEEP_MODE = "sleep_mode";
		
		/** 使用次数 */
		public static final String PREF_USE_COUNT = "use_count";
		
	}
	
}
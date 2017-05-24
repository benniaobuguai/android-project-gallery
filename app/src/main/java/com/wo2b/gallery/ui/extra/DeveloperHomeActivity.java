package com.wo2b.gallery.ui.extra;

import android.os.Bundle;

import com.wo2b.wrapper.app.BrowserFragmentActivity;
import com.wo2b.gallery.R;

/**
 * 开发者主页
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-7-15
 * @Modify 2015-7-15
 */
public class DeveloperHomeActivity extends BrowserFragmentActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_developer_home);

		initView();
		getWebview().loadUrl("file:///android_asset/www/developer_home.html");
	}

	@Override
	protected void initView()
	{
		setActionBarTitle(R.string.support_developer);
	}

}

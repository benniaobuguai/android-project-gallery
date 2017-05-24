package com.wo2b.wrapper.component.common;

import android.os.Bundle;

import com.wo2b.xxx.webapp.openapi.OpenApiUrl;
import com.wo2b.wrapper.app.BrowserFragmentActivity;

/**
 * 开发者首页
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2014-10-2
 */
public class DeveloperHomeActivity extends BrowserFragmentActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// realRequestFeature(FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		getWebview().loadUrl(OpenApiUrl.getIndexUrl());
	}

}

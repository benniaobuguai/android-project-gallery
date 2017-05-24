package com.wo2b.gallery.ui.extra;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.wo2b.wrapper.app.BrowserFragmentActivity;
import com.wo2b.gallery.R;

/**
 * 无题
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * 
 */
public class NoTitleActivity extends BrowserFragmentActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tu_no_title);

		initView();
		getWebview().loadUrl("file:///android_asset/www/no_title.html");
	}

	@Override
	protected void initView()
	{
		setActionBarTitle(R.string.no_title);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.common_like, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.like:
			{
				// Intent intent = new Intent();
				// intent.setClass(this, DeveloperHomeActivity.class);
				// startActivity(intent);
				return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}

}

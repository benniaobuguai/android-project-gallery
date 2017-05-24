package com.wo2b.gallery.ui.temp.articles;

import android.content.Intent;
import android.os.Bundle;

import com.wo2b.wrapper.app.BrowserFragmentActivity;
import com.wo2b.gallery.R;

/**
 * 文章阅读
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * 
 */
public class ArticleReaderActivity extends BrowserFragmentActivity
{

	/**
	 * 文章标题
	 */
	public static final String ARTICLE_TITLE = "article_title";

	/**
	 * 文章地址
	 */
	public static final String ARTICLE_ADDRESS = "article_address";

	private String article_title = null;
	private String article_address = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_article_reader);

		Intent intent = getIntent();
		article_title = intent.getStringExtra(ARTICLE_TITLE);
		article_address = intent.getStringExtra(ARTICLE_ADDRESS);

		initView();

		getWebview().loadUrl(article_address);
	}

	@Override
	protected void initView()
	{
		setActionBarTitle(article_title);
	}

}

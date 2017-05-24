package com.wo2b.wrapper.app.support;

/**
 * Tab Item Info.
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 */
public class TabItem
{

	public int itemId;

	public int title;

	public int icon;

	public TabItem(int itemId, int title)
	{
		this(itemId, title, 0);
	}

	public TabItem(int itemId, int title, int icon)
	{
		this.itemId = itemId;
		this.title = title;
		this.icon = icon;
	}

}

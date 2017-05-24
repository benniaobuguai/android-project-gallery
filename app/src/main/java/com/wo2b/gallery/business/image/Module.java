package com.wo2b.gallery.business.image;

/**
 * 图片模块
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * 
 */
public enum Module
{
	
	A("A"), // 图界精品
	H("H"), // 爱图说
	L("L"); // 本地图片
	
	public String value;
	
	private Module(String module)
	{
		this.value = module;
	}
	
}

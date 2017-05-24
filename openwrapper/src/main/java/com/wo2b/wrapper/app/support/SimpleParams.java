package com.wo2b.wrapper.app.support;

import java.util.Date;

/**
 * 可扩展的参数模板
 * 
 * <pre>
 * 此类主要是提供作钩子参数传递, 为了使用便捷, 默认提供部分属性.
 * 子类需要进行额外扩展时, 直接继承此类使用. 
 * 不建议继承后, 还使用此类相关属性, 造成混乱.
 * 
 * </pre>
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2014-10-30
 * @Modify 2015-11-14
 */
public class SimpleParams
{
	
	// ================================================================================================
	// 默认约定, 可继续扩展.
	
	private String keyword;
	
	private int intValue1;
	private int intValue2;
	private int intValue3;
	
	private String strValue1;
	private String strValue2;
	private String strValue3;
	
	private Date date1;
	private Date date2;
	private Date date3;
	
	public SimpleParams()
	{
		
	}
	
	/**
	 * 
	 * @param keyword
	 */
	public SimpleParams(String keyword)
	{
		this.keyword = keyword;
	}
	
	public final String getKeyword()
	{
		return keyword;
	}
	
	public final void setKeyword(String keyword)
	{
		this.keyword = keyword;
	}
	
	public final int getIntValue1()
	{
		return intValue1;
	}
	
	public final void setIntValue1(int intValue1)
	{
		this.intValue1 = intValue1;
	}
	
	public final int getIntValue2()
	{
		return intValue2;
	}
	
	public final void setIntValue2(int intValue2)
	{
		this.intValue2 = intValue2;
	}
	
	public final int getIntValue3()
	{
		return intValue3;
	}
	
	public final void setIntValue3(int intValue3)
	{
		this.intValue3 = intValue3;
	}
	
	public final String getStrValue1()
	{
		return strValue1;
	}
	
	public final void setStrValue1(String strValue1)
	{
		this.strValue1 = strValue1;
	}
	
	public final String getStrValue2()
	{
		return strValue2;
	}
	
	public final void setStrValue2(String strValue2)
	{
		this.strValue2 = strValue2;
	}
	
	public final String getStrValue3()
	{
		return strValue3;
	}
	
	public final void setStrValue3(String strValue3)
	{
		this.strValue3 = strValue3;
	}
	
	public final Date getDate1()
	{
		return date1;
	}
	
	public final void setDate1(Date date1)
	{
		this.date1 = date1;
	}
	
	public final Date getDate2()
	{
		return date2;
	}
	
	public final void setDate2(Date date2)
	{
		this.date2 = date2;
	}
	
	public final Date getDate3()
	{
		return date3;
	}
	
	public final void setDate3(Date date3)
	{
		this.date3 = date3;
	}
	
}

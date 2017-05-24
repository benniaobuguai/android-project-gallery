package com.wo2b.wrapper.app.support;

import java.util.List;

/**
 * 扩展的数据结构
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-10-30
 * @Modify 2015-12-13
 * @param <DataType>
 */
public final class XModel<DataType>
{
	
	/** 正常状态 */
	public static final int OK = 1;
	
	/** 没有数据 */
	public static final int NOT_DATA = 2;
	
	/** 没有网络 */
	public static final int NOT_INTERNET = 3;
	
	/** 没有SD卡 */
	public static final int NOT_SDCARD = 4;
	
	/** 未知状态 */
	public static final int UNKNOWN = -1;
	
	/** 状态码 */
	public int mStatus;
	
	/** 描述 */
	public String mDesc;
	
	/** 数据 */
	public List<DataType> mList;
	
	/**
	 * 
	 * @param status
	 * @param desc
	 * @param list
	 */
	private XModel(int status, String desc, List<DataType> list)
	{
		this.mStatus = status;
		this.mDesc = desc;
		this.mList = list;
	}
	
	/**
	 * 空对象或空集合返回true, 否则返回false
	 * 
	 * @return
	 */
	public boolean isEmpty()
	{
		return mList == null || mList.isEmpty();
	}
	
	/**
	 * 空数据集
	 * 
	 * @return
	 */
	public static <T> XModel<T> empty()
	{
		return list(XModel.NOT_DATA, null);
	}
	
	/**
	 * 网络错误
	 * 
	 * @return
	 */
	public static <T> XModel<T> netError()
	{
		return list(XModel.NOT_INTERNET, null);
	}
	
	/**
	 * 不明错误
	 * 
	 * @return
	 */
	public static <T> XModel<T> unknown()
	{
		return list(XModel.UNKNOWN, null);
	}
	
	/**
	 * 正常返回数据
	 * 
	 * @param list
	 * @return
	 */
	public static <T> XModel<T> list(List<T> list)
	{
		return list(XModel.OK, list);
	}
	
	/**
	 * 返回状态及数据集, 无具体描述
	 * 
	 * @param status 状态 {@link GConstants}
	 * @param list 数据集
	 * @return
	 */
	public static <T> XModel<T> list(int status, List<T> list)
	{
		return list(status, null, list);
	}
	
	/**
	 * 返回完整的数据结构
	 * 
	 * @param status 状态
	 * @param desc 描述
	 * @param list 数据集
	 * @return
	 */
	public static <T> XModel<T> list(int status, String desc, List<T> list)
	{
		return new XModel<T>(status, desc, list);
	}
	
}
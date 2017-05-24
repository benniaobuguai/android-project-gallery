package com.wo2b.wrapper.app.support;

public class GroupExt<T>
{
	
	private long id;
	
	private String groupName;
	
	/** true表示此group项, false: 表示item项, 对应的value为item的实体bean */
	private boolean isGroup;
	
	/** 是否显示首字母 */
	private boolean initialItem = false;
	
	/** 首字母 */
	private String initial;
	
	private T value;
	
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public String getInitial()
	{
		return initial;
	}
	
	public void setInitial(String initial)
	{
		this.initial = initial;
	}
	
	public String getGroupName()
	{
		return groupName;
	}
	
	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}
	
	public boolean isGroup()
	{
		return isGroup;
	}
	
	public void setGroup(boolean isGroup)
	{
		this.isGroup = isGroup;
	}
	
	public T getValue()
	{
		return value;
	}
	
	public void setValue(T value)
	{
		this.value = value;
	}
	
}

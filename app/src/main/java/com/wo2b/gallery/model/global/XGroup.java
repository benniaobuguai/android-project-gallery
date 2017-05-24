package com.wo2b.gallery.model.global;

public class XGroup
{
	
	private long groupId;
	
	private String groupName;
	
	public XGroup()
	{
		
	}
	
	public XGroup(long groupId, String groupName)
	{
		this.groupId = groupId;
		this.groupName = groupName;
	}
	
	@Override
	public String toString()
	{
		return "XGroup [groupId=" + groupId + ", groupName=" + groupName + "]";
	}
	
	public long getGroupId()
	{
		return groupId;
	}
	
	public void setGroupId(long groupId)
	{
		this.groupId = groupId;
	}
	
	public String getGroupName()
	{
		return groupName;
	}
	
	public void setGroupName(String groupName)
	{
		this.groupName = groupName;
	}
	
}

package com.opencdk.util.upgrade;


/**
 * 
 * @author 笨鸟不乖
 * 
 */
public class ConcreteUpgradeHandler implements UpgradeHandler
{
	
	public void setOnStartCheckListener(OnStartCheckListener listener)
	{
		
	}
	
	public interface OnStartCheckListener
	{
		
	}
	
	@Override
	public void startCheck()
	{
		
	}
	
	@Override
	public void versionFound(VersionInfo clientVersion, VersionInfo serverVersion)
	{
		
	}
	
	@Override
	public void versionNotFound(VersionInfo clientVersion)
	{
		
	}
	
	@Override
	public void checkOnError()
	{
		
	}
	
	@Override
	public void upgrade(final VersionInfo clientVersion, final VersionInfo serverVersion)
	{
		
	}
	
}

package com.opencdk.util.upgrade;

import com.opencdk.core.exception.SdkParseException;

/**
 * 默认的版本信息解析器
 * 
 * @author 笨鸟不乖
 * 
 */
public class VersionInfoDefaultParser extends VersionInfoParser
{
	
	@Override
	protected VersionInfo parseVersionInfoSelf(String versionString) throws SdkParseException
	{
		return null;
	}
	
	@Override
	protected boolean isParseSelf()
	{
		// 使用默认的解析
		return false;
	}
	
}

package com.opencdk.core.exception;

/**
 * 权限异常类
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 */
public class SdkAuthorizedException extends RuntimeException
{

	private static final long serialVersionUID = -8009209578172932607L;

	public SdkAuthorizedException()
	{
		super();
	}

	public SdkAuthorizedException(String detailMessage, Throwable throwable)
	{
		super(detailMessage, throwable);
	}

	public SdkAuthorizedException(String detailMessage)
	{
		super(detailMessage);
	}

	public SdkAuthorizedException(Throwable throwable)
	{
		super(throwable);
	}

}

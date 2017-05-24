package com.opencdk.core.exception;

/**
 * 没有登录异常
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 */
public class SdkNoLoginException extends RuntimeException
{

	private static final long serialVersionUID = 3771706437662490190L;

	public SdkNoLoginException()
	{
		super();
	}

	public SdkNoLoginException(String detailMessage, Throwable throwable)
	{
		super(detailMessage, throwable);
	}

	public SdkNoLoginException(String detailMessage)
	{
		super(detailMessage);
	}

	public SdkNoLoginException(Throwable throwable)
	{
		super(throwable);
	}

}

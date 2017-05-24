package com.opencdk.core.exception;

/**
 * 系统环境出异常时, 需要中止退出程序.
 * 
 * @author 笨鸟不乖
 * 
 */
public class SkdEnvironmentAbortException extends RuntimeException
{

	private static final long serialVersionUID = 1L;

	public SkdEnvironmentAbortException()
	{
		super();
	}

	public SkdEnvironmentAbortException(String detailMessage, Throwable throwable)
	{
		super(detailMessage, throwable);
	}

	public SkdEnvironmentAbortException(String detailMessage)
	{
		super(detailMessage);
	}

	public SkdEnvironmentAbortException(Throwable throwable)
	{
		super(throwable);
	}

}

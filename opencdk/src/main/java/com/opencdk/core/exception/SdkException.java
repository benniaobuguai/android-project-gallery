package com.opencdk.core.exception;

/**
 * OpenCDK exception
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 */
public class SdkException extends Exception
{

	private static final long serialVersionUID = -9221686361186151961L;

	public SdkException()
	{
		super();
	}

	public SdkException(String detailMessage, Throwable throwable)
	{
		super(detailMessage, throwable);
	}

	public SdkException(String detailMessage)
	{
		super(detailMessage);
	}

	public SdkException(Throwable throwable)
	{
		super(throwable);
	}

}

package com.ilya.sergeev.potlach.client;

public class SecuredRestException extends RuntimeException
{
	private static final long serialVersionUID = 1L;
	
	public SecuredRestException()
	{
		super();
	}
	
	public SecuredRestException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause);
	}
	
	public SecuredRestException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public SecuredRestException(String message)
	{
		super(message);
	}
	
	public SecuredRestException(Throwable cause)
	{
		super(cause);
	}
	
}

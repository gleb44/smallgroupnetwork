package com.smallgroupnetwork.validation;

/**
 * Date: 5/7/12
 * Time: 7:13 PM
 *
 * @author Reviakin Aleksey it.blackdog@gmail.com
 */
public class ValidationResult
{
	private String field;
	private String messageCode;
	private Object[] parameters;
	private String message;

	public ValidationResult()
	{
	}

	public ValidationResult( String field, String messageCode, Object... parameters )
	{
		this.field = field;
		this.messageCode = messageCode;
		this.parameters = parameters;
	}

	public String getField()
	{
		return field;
	}

	public void setField( String field )
	{
		this.field = field;
	}

	public String getMessageCode()
	{
		return messageCode;
	}

	public void setMessageCode( String messageCode )
	{
		this.messageCode = messageCode;
	}

	public Object[] getParameters()
	{
		return parameters;
	}

	public void setParameters( Object[] parameters )
	{
		this.parameters = parameters;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage( String message )
	{
		this.message = message;
	}
}

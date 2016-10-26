package com.smallgroupnetwork.validation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 7/5/12
 * Time: 7:37 PM
 *
 * @author Reviakin Aleksey it.blackdog@gmail.com
 */
public class ValidationException extends RuntimeException
{
	private LinkedList<ValidationResult> items;

	public ValidationException()
	{
		super();
	}

	public ValidationException( Collection<ValidationResult> items )
	{
		setValidationResults( items );
	}

	public ValidationException( String message, Collection<ValidationResult> items )
	{
		super( message );
		setValidationResults( items );
	}

	public ValidationException( String message, Throwable cause, Collection<ValidationResult> items )
	{
		super( message, cause );
		setValidationResults( items );
	}

	public ValidationException( Throwable cause, Collection<ValidationResult> items )
	{
		super( cause );
		setValidationResults( items );
	}

	public ValidationException( ValidationResult... items )
	{
		setValidationResults( Arrays.asList( items ) );
	}

	public ValidationException( String message, ValidationResult... items )
	{
		super( message );
		setValidationResults( Arrays.asList( items ) );
	}

	public ValidationException( String message, Throwable cause, ValidationResult... items )
	{
		super( message, cause );
		setValidationResults( Arrays.asList( items ) );
	}

	public ValidationException( Throwable cause, ValidationResult... items )
	{
		super( cause );
		setValidationResults( Arrays.asList( items ) );
	}


	private void setValidationResults( Collection<ValidationResult> results )
	{
		this.items = new LinkedList<>( results );
	}

	public List<ValidationResult> getItems()
	{
		return items;
	}

	@Override
	@JsonIgnore
	public StackTraceElement[] getStackTrace()
	{
		return super.getStackTrace();
	}

	@Override
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder( super.toString() );
		for( ValidationResult item : items )
		{
			stringBuilder.append( "\r\n" ).append( "SearchResult: " ).append( item.getField() );
			if( item.getMessage() != null )
			{
				stringBuilder.append( " Message: " ).append( item.getMessage() );
			}
			else
			{
				stringBuilder.append( " Code: " ).append( item.getMessageCode() );
				if( item.getParameters() != null && item.getParameters().length != 0 )
				{
					stringBuilder.append( " Params: [" );
					for( Object param : item.getParameters() )
					{
						stringBuilder.append( ' ' ).append( param );
					}
					stringBuilder.append( " ]" );
				}
			}

		}
		return stringBuilder.toString();
	}
}

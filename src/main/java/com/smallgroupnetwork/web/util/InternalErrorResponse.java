package com.smallgroupnetwork.web.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 10/7/13
 * Time: 6:29 PM
 *
 * @author Reviakin Aleksey it.blackdog@gmail.com
 */
public class InternalErrorResponse extends AbstractFailedResponse
{
	private String name;
	private List<String> trace;

	public InternalErrorResponse()
	{
	}

	public InternalErrorResponse( String name, StackTraceElement[] stackTrace )
	{
		this.name = name;
		this.trace = new ArrayList<>( stackTrace.length );
		for( StackTraceElement element : stackTrace )
		{
			this.trace.add( element.toString() );
		}
	}

	public String getName()
	{
		return name;
	}

	public List<String> getTrace()
	{
		return trace;
	}

	@Override
	public String reason()
	{
		return "internal_error";
	}
}

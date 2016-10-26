package com.smallgroupnetwork.web.util;

import com.smallgroupnetwork.persistence.Paging;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.WebRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 10/3/13
 * Time: 1:24 PM
 *
 * @author Reviakin Aleksey it.blackdog@gmail.com
 */
@Aspect
public class PagingParameterAspect
{
	public static final String LIMIT_PARAMETER_NAME = "limit";
	public static final String OFFSET_PARAMETER_NAME = "offset";
	public static final String SORT_ORDER_PARAMETER_NAME = "order";
	public static final String DESCENDING_SORT_ORDER_VALUE = "desc";
	public static final String SORT_COLUMN_PARAMETER_NAME = "sort";

	private static final Pattern ONLY_DIGITS = Pattern.compile( "^\\d+$" );

	@Autowired
	private WebRequest webRequest;

	@Around( value = "execution(public * *(.., com.smallgroupnetwork.persistence.Paging,..)) && @annotation(org.springframework.web.bind.annotation.RequestMapping)" )
	public Object getPagingParameterFromRequest( ProceedingJoinPoint joinPoint ) throws Throwable
	{
		Object[] arguments = joinPoint.getArgs();
		Object[] newArguments = new Object[arguments.length];

		Paging pagingFromRequest = new Paging();
		boolean pagingParameterFound = false;
		Integer limit = parseNumberValue( webRequest.getParameter( LIMIT_PARAMETER_NAME ) );
		if( limit != null )
		{
			pagingFromRequest.setLimit( limit );
			pagingParameterFound = true;
		}
		Integer offset = parseNumberValue( webRequest.getParameter( OFFSET_PARAMETER_NAME ) );
		if( offset != null )
		{
			pagingFromRequest.setOffset( offset );
			pagingParameterFound = true;
		}
		String sortOrder = webRequest.getParameter( SORT_ORDER_PARAMETER_NAME );
		if( StringUtils.isNotBlank( sortOrder ) )
		{
			pagingFromRequest.setSortDesc( DESCENDING_SORT_ORDER_VALUE.equalsIgnoreCase( sortOrder ) );
			pagingParameterFound = true;
		}
		String sortColumn = webRequest.getParameter( SORT_COLUMN_PARAMETER_NAME );
		if( StringUtils.isNotBlank( sortColumn ) )
		{
			pagingFromRequest.setSortColumn( sortColumn );
			pagingParameterFound = true;
		}

		if( pagingParameterFound )
		{
			for( int i = 0; i < arguments.length; i++ )
			{
				Object argument = arguments[i];
				if( argument instanceof Paging )
				{
					argument = pagingFromRequest;
				}
				newArguments[i] = argument;
			}
		}
		else
		{
			newArguments = arguments;
		}
		return joinPoint.proceed( newArguments );
	}

	private Integer parseNumberValue( String parameter )
	{
		if( parameter == null )
		{
			return null;
		}
		Matcher matcher = ONLY_DIGITS.matcher( parameter );
		if( matcher.find() )
		{
			return Integer.parseInt( parameter );
		}
		return null;
	}
}

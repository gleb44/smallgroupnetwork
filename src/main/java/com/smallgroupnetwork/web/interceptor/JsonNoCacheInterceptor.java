package com.smallgroupnetwork.web.interceptor;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.WebContentGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: gleb
 * Date: 5/21/14
 * Time: 4:55 PM
 */
public class JsonNoCacheInterceptor extends WebContentGenerator implements HandlerInterceptor
{
	@Override
	public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception
	{
		try
		{
			if( request.getMethod().equalsIgnoreCase( RequestMethod.GET.name() ) )
			{
				if( handler instanceof HandlerMethod )
				{
					HandlerMethod method = (HandlerMethod) handler;
					RequestMapping methodAnnotation = method.getMethodAnnotation( RequestMapping.class );
					if( methodAnnotation != null && methodAnnotation.produces() != null && methodAnnotation.produces().length > 0 )
					{
						for( String contentType : methodAnnotation.produces() )
						{
							if( MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase( contentType ) )
							{
								preventCaching( response );
							}
						}
					}
				}
			}
		}
		catch( Exception e )
		{
			//suppress error
		}
		return true;
	}

	@Override
	public void postHandle( HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView ) throws Exception
	{
	}

	@Override
	public void afterCompletion( HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex ) throws Exception
	{
	}
}

package com.smallgroupnetwork.web.interceptor;

import com.smallgroupnetwork.security.AccountHolder;
import com.smallgroupnetwork.security.UserAuthentication;
import com.smallgroupnetwork.web.exception.UnauthorizedException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: gleb
 * Date: 5/21/14
 * Time: 4:55 PM
 */
public class UserAuthorizationInterceptor extends HandlerInterceptorAdapter
{
	@Override
	public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception
	{
		UserAuthentication userAuthentication = (UserAuthentication) request.getSession().getAttribute( AccountHolder.USER_KEY );
		if( userAuthentication != null )
		{
			AccountHolder.setUserAuthentication( userAuthentication );
			return true;
		}
		else
		{
			throw new UnauthorizedException();
		}
	}

	@Override
	public void afterCompletion( HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex ) throws Exception
	{
		AccountHolder.setUserAuthentication( null );
	}
}

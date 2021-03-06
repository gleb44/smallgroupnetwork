package com.smallgroupnetwork.web.interceptor;


import com.smallgroupnetwork.model.AdminAccess;
import com.smallgroupnetwork.security.AccountHolder;
import com.smallgroupnetwork.web.exception.UnauthorizedException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: gleb
 * Date: 5/21/14
 * Time: 4:55 PM
 */
public class AdminAuthorizationInterceptor extends HandlerInterceptorAdapter
{
	@Override
	public boolean preHandle( HttpServletRequest request, HttpServletResponse response, Object handler ) throws Exception
	{
		AdminAccess admin = AccountHolder.getAdminAccess();
		if( admin != null )
		{
			return true;
		}
		else
		{
			throw new UnauthorizedException();
		}
	}
}
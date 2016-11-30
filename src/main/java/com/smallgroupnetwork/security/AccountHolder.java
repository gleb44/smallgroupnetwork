package com.smallgroupnetwork.security;

import com.smallgroupnetwork.model.AdminAccess;
import com.smallgroupnetwork.model.User;

/**
 * User: gleb
 * Date: 5/20/14
 * Time: 4:05 PM
 */
public class AccountHolder
{
	public static final String USER_KEY = "com.smallgroupnetwork.user.auth";
	public static final String FILES_KEY = "com.smallgroupnetwork.admin.files";

	private static ThreadLocal<UserAuthentication> userHolder = new ThreadLocal<>();

	public static User getUser()
	{
		UserAuthentication userAuthentication = userHolder.get();
		return userAuthentication == null ? null : userAuthentication.getUser();
	}

	public static AdminAccess getAdminAccess()
	{
		User user = getUser();
		return user == null ? null : user.getAdminAccess();
	}

	public static UserAuthentication getUserAuthentication()
	{
		return userHolder.get();
	}

	public static void setUserAuthentication( UserAuthentication user )
	{
		userHolder.set( user );
	}
}

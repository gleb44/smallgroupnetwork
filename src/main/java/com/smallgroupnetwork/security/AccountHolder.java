package com.smallgroupnetwork.security;

import com.smallgroupnetwork.model.Admin;

/**
 * User: gleb
 * Date: 5/20/14
 * Time: 4:05 PM
 */
public class AccountHolder
{
	public static final String FILES_KEY = "com.smallgroupnetwork.admin.files";
	public static final String ADMIN_KEY = "com.smallgroupnetwork.admin.auth";

	private static ThreadLocal<Admin> adminHolder = new ThreadLocal<>();

	public static void setAdmin( Admin admin )
	{
		adminHolder.set( admin );
	}

	public static Admin getAdmin()
	{
		return adminHolder.get();
	}
}

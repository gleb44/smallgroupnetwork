package com.smallgroupnetwork.security;

import com.smallgroupnetwork.model.User;

/**
 * User: gleb
 * Date: 7/29/2014
 * Time: 5:17 PM
 */
public class UserAuthentication
{
	private String accessToken;
	private User user;

	public UserAuthentication()
	{
	}

	public UserAuthentication( String accessToken, User user )
	{
		this.accessToken = accessToken;
		this.user = user;
	}

	public String getAccessToken()
	{
		return accessToken;
	}

	public void setAccessToken( String accessToken )
	{
		this.accessToken = accessToken;
	}

	public User getUser()
	{
		return user;
	}

	public void setUser( User user )
	{
		this.user = user;
	}
}

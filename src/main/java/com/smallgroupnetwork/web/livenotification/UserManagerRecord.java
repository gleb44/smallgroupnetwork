package com.smallgroupnetwork.web.livenotification;

import com.smallgroupnetwork.security.UserAuthentication;

import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * User: gleb
 * Date: 3/3/16
 * Time: 1:59 PM
 */
public class UserManagerRecord implements Serializable
{
	private UserAuthentication userData;
	private CopyOnWriteArraySet<String> httpSessions = new CopyOnWriteArraySet<String>();

	public UserManagerRecord( UserAuthentication userData )
	{
		this.userData = userData;
	}

	public CopyOnWriteArraySet<String> getHttpSessions()
	{
		return httpSessions;
	}

	public void setHttpSessions( CopyOnWriteArraySet<String> httpSessions )
	{
		this.httpSessions = httpSessions;
	}

	public UserAuthentication getUserData()
	{

		return userData;
	}

	public void setUserData( UserAuthentication userData )
	{
		this.userData = userData;
	}
}

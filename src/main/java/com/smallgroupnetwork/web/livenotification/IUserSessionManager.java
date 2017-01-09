package com.smallgroupnetwork.web.livenotification;

import com.smallgroupnetwork.security.UserAuthentication;

/**
 * User: gyazykov
 * Date: 1/02/16.
 * Time: 3:14 PM
 */

public interface IUserSessionManager
{
	void onUserLogin( UserAuthentication user, String httpSessionId );

	void onUserLogout( Long userId, String httpSessionId );

	UserAuthentication getUserInfo( Long userId );
}

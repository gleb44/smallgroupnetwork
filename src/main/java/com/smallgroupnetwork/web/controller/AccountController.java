package com.smallgroupnetwork.web.controller;

import com.smallgroupnetwork.model.Account;
import com.smallgroupnetwork.model.User;
import com.smallgroupnetwork.security.AccountHolder;
import com.smallgroupnetwork.security.UserAuthentication;
import com.smallgroupnetwork.service.IAccountService;
import com.smallgroupnetwork.service.IUserService;
import com.smallgroupnetwork.web.exception.UnauthorizedException;
import com.smallgroupnetwork.web.livenotification.IUserSessionManager;
import com.smallgroupnetwork.web.util.DefaultMessages;
import com.smallgroupnetwork.web.util.OkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * User: gleb
 * Date: 12/5/13
 * Time: 12:25 PM
 */
@Controller
@RequestMapping( Routes.ACCOUNT )
public class AccountController
{
	@Autowired
	private IAccountService accountService;
	@Autowired
	private IUserService userService;
	@Autowired
	@Qualifier( "userSessionManager" )
	private IUserSessionManager userSessionManager;

	private String getSessionKey()
	{
		return AccountHolder.USER_KEY;
	}

	private User findUserByAccount( String login, String password, HttpSession session )
	{
		Account account = accountService.findAccount( login, password );
		return userService.read( account.getId() );
	}

	private UserAuthentication authenticate( User user, HttpSession session )
	{
		UserAuthentication authentication = new UserAuthentication();
		authentication.setUser( user );
		authentication.setAccessToken( user.getId().toString() );

		session.setAttribute( getSessionKey(), authentication.getUser().getId() );

		return authentication;
	}

	@RequestMapping( value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public User register( @RequestBody Account account, HttpSession session )
	{
		Account result = accountService.register( account );
		UserAuthentication authentication = authenticate( result.getUser(), session );

		//store in global storage instead of session
		userSessionManager.onUserLogin( authentication, session.getId() );

		return result.getUser();
	}

	@RequestMapping( value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public User login( @RequestParam( value = "login" ) String login, @RequestParam( value = "password" ) String password, HttpSession session )
	{
		User user = findUserByAccount( login, password, session );
		UserAuthentication authentication = authenticate( user, session );

		//store in global storage instead of session
		userSessionManager.onUserLogin( authentication, session.getId() );

		return user;
	}

	@RequestMapping( value = "/info", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public User info( HttpSession session )
	{
		Long sessionData = (Long) session.getAttribute( getSessionKey() );
		if( sessionData == null )
		{
			return null;
		}
		UserAuthentication subject = userSessionManager.getUserInfo( sessionData );
		if( subject == null )
		{
			return null;
		}
		else
		{
			return subject.getUser();
		}
	}

	@RequestMapping( value = "/logout", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public OkResponse logout( HttpSession session )
	{
		Long userId = (Long) session.getAttribute( getSessionKey() );
		session.removeAttribute( getSessionKey() );
		if( userId != null )
		{
			userSessionManager.onUserLogout( userId, session.getId() );
		}
		return DefaultMessages.OK_RESPONSE;
	}

	@RequestMapping( value = "/change-password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public User changePassword( @RequestParam( value = "login" ) String login,
		@RequestParam( value = "password" ) String password,
		@RequestParam( value = "newPassword" ) String newPassword,
		HttpSession session )
	{
		accountService.changePassword( login, password, newPassword );

		User user = findUserByAccount( login, password, session );
		authenticate( user, session );
		return user;
	}
}

package com.smallgroupnetwork.web.controller;

import com.smallgroupnetwork.model.Account;
import com.smallgroupnetwork.model.User;
import com.smallgroupnetwork.security.AccountHolder;
import com.smallgroupnetwork.security.UserAuthentication;
import com.smallgroupnetwork.service.IAccountService;
import com.smallgroupnetwork.service.IUserService;
import com.smallgroupnetwork.web.exception.UnauthorizedException;
import com.smallgroupnetwork.web.util.DefaultMessages;
import com.smallgroupnetwork.web.util.OkResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

	private String getSessionKey()
	{
		return AccountHolder.USER_KEY;
	}

	private User findUserByAccount( String login, String password, HttpSession session )
	{
		Account account = accountService.findAccount( login, password );
		return userService.read( account.getId() );
	}

	private void authenticate( User user, HttpSession session )
	{
		UserAuthentication authentication = new UserAuthentication();
		authentication.setUser( user );
		authentication.setAccessToken( user.getId().toString() );

		session.setAttribute( getSessionKey(), authentication );
	}

	@RequestMapping( value = "/register", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public User register( @RequestBody Account account, HttpSession session )
	{
		Account result = accountService.register( account );
		authenticate( result.getUser(), session );
		return result.getUser();
	}

	@RequestMapping( value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public User login( @RequestParam( value = "login" ) String login, @RequestParam( value = "password" ) String password, HttpSession session )
	{
		User user = findUserByAccount( login, password, session );
		authenticate( user, session );
		return user;
	}

	@RequestMapping( value = "/info", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public User info( HttpSession session )
	{
		UserAuthentication authentication = (UserAuthentication) session.getAttribute( getSessionKey() );
		if( authentication == null )
		{
			throw new UnauthorizedException();
		}
		return authentication.getUser();
	}

	@RequestMapping( value = "/logout", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public OkResponse logout( HttpSession session )
	{
		session.removeAttribute( getSessionKey() );
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

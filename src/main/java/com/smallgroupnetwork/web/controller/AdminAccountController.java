package com.smallgroupnetwork.web.controller;

import com.smallgroupnetwork.model.Admin;
import com.smallgroupnetwork.security.AccountHolder;
import com.smallgroupnetwork.service.IAdminService;
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
@RequestMapping( Routes.ADMIN_ACCOUNT )
public class AdminAccountController
{
	@Autowired
	private IAdminService adminService;

	private String getSessionKey()
	{
		return AccountHolder.ADMIN_KEY;
	}

	@RequestMapping( value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public Admin login( @RequestBody( required = true ) Admin user, HttpSession session )
	{
		Admin admin = adminService.findAccountAndLogin( user.getEmail(), user.getPassword() );
		session.setAttribute( getSessionKey(), admin );
		return admin;
	}

	@RequestMapping( value = "/info", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public Admin getLoggedInfo( HttpSession session )
	{
		Admin admin = (Admin) session.getAttribute( getSessionKey() );
		if( admin == null )
		{
			throw new UnauthorizedException();
		}
		return admin;
	}

	@RequestMapping( value = "/logout", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public OkResponse signOut( HttpSession session )
	{
		session.removeAttribute( getSessionKey() );
		return DefaultMessages.OK_RESPONSE;
	}

	@RequestMapping( value = "/change-password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public Admin changePassword( @RequestParam( value = "login", required = true ) String login,
		@RequestParam( value = "password", required = true ) String password,
		@RequestParam( value = "newPassword", required = true ) String newPassword,
		HttpSession session )
	{
		adminService.changePassword( login, password, newPassword );
		Admin admin = adminService.findAccountAndLogin( login, newPassword );
		session.setAttribute( getSessionKey(), admin );
		return admin;
	}

}

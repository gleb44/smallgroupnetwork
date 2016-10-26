package com.smallgroupnetwork.web.controller;

import com.smallgroupnetwork.model.Admin;
import com.smallgroupnetwork.security.AccountHolder;
import com.smallgroupnetwork.service.IAdminService;
import com.smallgroupnetwork.validation.ValidationException;
import com.smallgroupnetwork.validation.ValidationResult;
import com.smallgroupnetwork.web.exception.UnauthorizedException;
import com.smallgroupnetwork.web.util.DefaultMessages;
import com.smallgroupnetwork.web.util.OkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
	@Value( "${admin.password}" )
	private String adminPassword;

	@Autowired
	private IAdminService adminService;

	private String getSessionKey()
	{
		return AccountHolder.ADMIN_KEY;
	}

	@RequestMapping( value = "/sign-in", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public Admin signIn(@RequestBody(required = true) Admin admin, HttpSession session )
	{
		if( !adminPassword.equals( admin.getPassword() ) )
		{
			throw new ValidationException( "Password Incorrect", new ValidationResult( "password", "Password incorrect" ) );
		}
		admin.setId( 1L );
		session.setAttribute( getSessionKey(), admin );
		return admin;
	}

/*
	@RequestMapping( value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public Admin login( @RequestParam( value = "login", required = true ) String login,
	                    @RequestParam( value = "password", required = true ) String password,
	                    HttpSession session, HttpServletRequest request )
	{
		Admin admin = adminService.findAccountAndLogin( login, password );
		session.setAttribute( getSessionKey(), admin );
		return admin;
	}
*/

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

	@RequestMapping( value = "/sign-out", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public OkResponse signOut( HttpSession session )
	{
		session.removeAttribute( getSessionKey() );
		return DefaultMessages.OK_RESPONSE;
	}

/*
	@RequestMapping( value = "/change-password", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public Admin changePassword( @RequestParam( value = "login", required = true ) String login,
	                             @RequestParam( value = "password", required = true ) String password,
	                             @RequestParam( value = "newPassword", required = true ) String newPassword,
	                             HttpSession session, HttpServletRequest request )
	{
		adminService.changePassword( login, password, newPassword );
		Admin admin = adminService.findAccountAndLogin( login, newPassword );
		session.setAttribute( getSessionKey(), admin );
		return admin;
	}
*/
}

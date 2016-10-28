package com.smallgroupnetwork.web.controller;

import com.smallgroupnetwork.web.exception.ResourceNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * User: gleb
 * Date: 2/26/15
 * Time: 1:11 PM
 */
@Controller
public class LayoutController
{
	@RequestMapping( value = "/**", method = { RequestMethod.GET, RequestMethod.HEAD } )
	public String userLayout( HttpServletRequest request, HttpSession session )
	{
		String requestURI = request.getRequestURI();
		if( requestURI.startsWith( "/api" ) || requestURI.startsWith( "/public/sapi" ) )
		{
			throw new ResourceNotFoundException(); //we didn't found mapping for this method so fallback to 404
		}
		else
		{
			return "index";
		}
	}
}

package com.smallgroupnetwork.web.util;

/**
 * Date: 10/7/13
 * Time: 6:29 PM
 *
 * @author Reviakin Aleksey it.blackdog@gmail.com
 */
public class NotFoundResponse extends AbstractFailedResponse
{
	@Override
	public String reason()
	{
		return "resource_not_found";
	}
}

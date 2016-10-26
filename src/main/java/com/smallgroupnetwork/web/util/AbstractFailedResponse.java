package com.smallgroupnetwork.web.util;

import com.fasterxml.jackson.annotation.JsonGetter;

/**
 * Date: 10/7/13
 * Time: 9:31 PM
 *
 * @author Reviakin Aleksey it.blackdog@gmail.com
 */
public abstract class AbstractFailedResponse
{
	final boolean success = false;

	public boolean isSuccess()
	{
		return success;
	}

	@JsonGetter( "reason" )
	public abstract String reason();
}

package com.smallgroupnetwork.web.websocket.mesaging.message;

/**
 * User: gyazykov
 * Date: 1/02/16.
 * Time: 3:14 PM
 */

public class Pong extends Ping
{
	private Long oldTime;

	public Long getOldTime()
	{
		return oldTime;
	}

	public void setOldTime( Long oldTime )
	{
		this.oldTime = oldTime;
	}
}

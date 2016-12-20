package com.smallgroupnetwork.web.websocket.mesaging.message;

/**
 * User: gyazykov
 * Date: 1/02/16.
 * Time: 3:14 PM
 */

public class Ping extends BaseMessage
{
	private Long time;

	public Ping()
	{
	}

	public Ping( Long time )
	{
		this.time = time;
	}

	public Long getTime()
	{
		return time;
	}

	public void setTime( Long time )
	{
		this.time = time;
	}
}

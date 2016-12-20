package com.smallgroupnetwork.web.websocket.mesaging.event;

import com.smallgroupnetwork.web.websocket.mesaging.IMessageService;
import org.springframework.context.ApplicationEvent;

/**
 * User: gyazykov
 * Date: 1/02/16.
 * Time: 3:14 PM
 */

public abstract class BaseEvent extends ApplicationEvent
{
	private static final long serialVersionUID = 1L;

	private Long userId;
	private String sessionId;
	private String wsSessionId;

	public BaseEvent( IMessageService eventService, Long userId, String sessionId, String wsSessionId )
	{
		super( eventService );

		this.userId = userId;
		this.sessionId = sessionId;
		this.wsSessionId = wsSessionId;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId( Long userId )
	{
		this.userId = userId;
	}

	public String getSessionId()
	{
		return sessionId;
	}

	public void setSessionId( String sessionId )
	{
		this.sessionId = sessionId;
	}

	public String getWsSessionId()
	{
		return wsSessionId;
	}

	public void setWsSessionId( String wsSessionId )
	{
		this.wsSessionId = wsSessionId;
	}
}

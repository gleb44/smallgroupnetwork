package com.smallgroupnetwork.web.websocket.mesaging.event;

import com.smallgroupnetwork.web.websocket.mesaging.IMessageService;

/**
 * User: gyazykov
 * Date: 17/02/16.
 * Time: 3:14 PM
 */

public class ConnectionClosedEvent extends BaseEvent
{
	public ConnectionClosedEvent( IMessageService eventService, Long userId, String sessionId, String wsSessionId )
	{
		super( eventService, userId, sessionId, wsSessionId );
	}
}

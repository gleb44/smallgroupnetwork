package com.smallgroupnetwork.web.websocket.mesaging.event;

import com.smallgroupnetwork.web.websocket.mesaging.IMessageService;
import com.smallgroupnetwork.web.websocket.mesaging.message.BaseMessage;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

/**
 * User: gyazykov
 * Date: 17/02/16.
 * Time: 3:14 PM
 */

public class MessageEvent<T extends BaseMessage> extends BaseEvent implements ResolvableTypeProvider
{
	private T message;

	public MessageEvent( T message, IMessageService eventService, Long userId, String sessionId, String wsSessionId )
	{
		super( eventService, userId, sessionId, wsSessionId );
		this.message = message;
	}

	public T getMessage()
	{
		return message;
	}

	public void setMessage( T message )
	{
		this.message = message;
	}

	@Override
	public ResolvableType getResolvableType()
	{
		return ResolvableType.forClassWithGenerics( getClass(), ResolvableType.forInstance( getMessage() ) );
	}
}

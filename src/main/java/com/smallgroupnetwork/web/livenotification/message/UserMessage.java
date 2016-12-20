package com.smallgroupnetwork.web.livenotification.message;

import com.smallgroupnetwork.web.websocket.mesaging.message.BaseMessage;

/**
 * User: gyazykov
 * Date: 02/19/16.
 * Time: 11:30 AM
 */
public class UserMessage<T> extends BaseMessage
{
	private T source;
	private OperationType operationType;

	public UserMessage( T source, OperationType operationType )
	{
		this.source = source;
		this.operationType = operationType;
	}

	public T getSource()
	{
		return source;
	}

	public void setSource( T source )
	{
		this.source = source;
	}

	public OperationType getOperationType()
	{
		return operationType;
	}

	public void setOperationType( OperationType operationType )
	{
		this.operationType = operationType;
	}
}

package com.smallgroupnetwork.web.livenotification.message;

/**
 * User: gyazykov
 * Date: 02/19/16.
 * Time: 11:30 AM
 */
public class GroupMessage<T> extends UserMessage<T>
{
	public GroupMessage( T source, OperationType operationType )
	{
		super( source, operationType );
	}
}

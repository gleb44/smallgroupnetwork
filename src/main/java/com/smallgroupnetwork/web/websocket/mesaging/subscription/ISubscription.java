package com.smallgroupnetwork.web.websocket.mesaging.subscription;

import com.smallgroupnetwork.web.websocket.mesaging.IMessageService;

/**
 * User: gyazykov
 * Date: 1/02/16.
 * Time: 3:14 PM
 */
public interface ISubscription
{
	void setProcessor( IMessageService eventService );

	boolean isAutoSubscribe();

	SubscriptionType getType();

	boolean subscribe( String httpSessionId );

	boolean unsubscribe( String httpSessionId );

	boolean isSubscribed( String httpSessionId );
}

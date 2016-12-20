package com.smallgroupnetwork.web.websocket.mesaging;

import com.smallgroupnetwork.web.websocket.mesaging.subscription.SubscriptionType;

import java.io.Serializable;

/**
 * User: gyazykov
 * Date: 1/02/16.
 * Time: 3:14 PM
 */
public interface IMessageService
{
	void setMessageHandler( IMessageHandler messageHandler );

	void sendMessage( String httpSessionId, Serializable message );

	void sendMessageToAll( String fromWsSessionId, Serializable message );

	boolean subscribe( String httpSessionId, SubscriptionType type );

	boolean unsubscribe( String httpSessionId, SubscriptionType type );

	boolean isSubscribed( String httpSessionId, SubscriptionType type );
}

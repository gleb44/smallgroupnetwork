package com.smallgroupnetwork.web.websocket.mesaging.subscription;

import com.smallgroupnetwork.web.websocket.mesaging.IMessageService;
import com.smallgroupnetwork.web.websocket.mesaging.event.MessageEvent;
import com.smallgroupnetwork.web.websocket.mesaging.message.Ping;
import com.smallgroupnetwork.web.websocket.mesaging.message.Pong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.CopyOnWriteArraySet;

/**
 * User: gyazykov
 * Date: 1/02/16.
 * Time: 3:14 PM
 */
@Service
public class PingSubscription implements ISubscription
{
	private static final Logger logger = LoggerFactory.getLogger( PingSubscription.class );

	private final CopyOnWriteArraySet<String> httpSessionIds = new CopyOnWriteArraySet<>();

	private IMessageService processor;

	@Override
	public void setProcessor( IMessageService processor )
	{
		this.processor = processor;
	}

	@Override
	public boolean isAutoSubscribe()
	{
		return true;
	}

	@Override
	public SubscriptionType getType()
	{
		return SubscriptionType.Ping;
	}

	@Override
	public boolean subscribe( String httpSessionId )
	{
		return httpSessionIds.add( httpSessionId );
	}

	@Override
	public boolean unsubscribe( String httpSessionId )
	{
		return httpSessionIds.remove( httpSessionId );
	}

	@Override
	public boolean isSubscribed( String httpSessionId )
	{
		return httpSessionIds.contains( httpSessionId );
	}

	@Scheduled( fixedDelay = 60000 )
	private void process()
	{
		if( this.processor == null )
		{
			return;
		}

		final Ping message = new Ping( System.currentTimeMillis() );
		httpSessionIds.forEach( httpSessionId -> this.processor.sendMessage( httpSessionId, message ) );
	}

	@EventListener
	public void processPongEvent( MessageEvent<Pong> event )
	{
		// TODO Test

		if( logger.isDebugEnabled() )
		{
			logger.debug( "Process Pong. " + event.getMessage().toString() );
		}
	}
}

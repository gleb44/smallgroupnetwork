package com.smallgroupnetwork.web.websocket.mesaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.smallgroupnetwork.web.serialization.HibernateAwareObjectMapper;
import com.smallgroupnetwork.web.websocket.mesaging.event.BaseEvent;
import com.smallgroupnetwork.web.websocket.mesaging.event.ConnectionClosedEvent;
import com.smallgroupnetwork.web.websocket.mesaging.event.ConnectionEstablishedEvent;
import com.smallgroupnetwork.web.websocket.mesaging.event.MessageEvent;
import com.smallgroupnetwork.web.websocket.mesaging.message.BaseMessage;
import com.smallgroupnetwork.web.websocket.mesaging.subscription.ISubscription;
import com.smallgroupnetwork.web.websocket.mesaging.subscription.SubscriptionType;
import com.smallgroupnetwork.web.websocket.session.ISocketHandlerListener;
import com.smallgroupnetwork.web.websocket.session.SessionRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: gyazykov
 * Date: 1/02/16.
 * Time: 3:14 PM
 */
@Service
public class MessageService implements IMessageService, ISocketHandlerListener, ApplicationEventPublisherAware
{
	private static final Logger logger = LoggerFactory.getLogger( MessageService.class );

	private Map<SubscriptionType, ISubscription> subscriptionMap = new HashMap<>();
	private IMessageHandler messageHandler;
	private ApplicationEventPublisher eventPublisher;
	private TypeReference reference = new TypeReference<BaseMessage>()
	{
	};

	private boolean publish = false;

	// Set true to enable event publishing.
	public void setPublish( boolean publish )
	{
		this.publish = publish;
	}

	@Autowired
	private HibernateAwareObjectMapper jacksonObjectMapper;
	@Autowired
	private SessionRegistry sessionRegistry;
	@Autowired
	private ISubscription serverTimeSubscription;

	@Override
	public void setMessageHandler( IMessageHandler messageHandler )
	{
		this.messageHandler = messageHandler;
	}

	@Override
	public void setApplicationEventPublisher( ApplicationEventPublisher applicationEventPublisher )
	{
		this.eventPublisher = applicationEventPublisher;
	}

	private void publishEvent( BaseEvent event )
	{
		if( logger.isDebugEnabled() )
		{
			logger.debug( "Publish Message: " + event );
		}
		try
		{
			eventPublisher.publishEvent( event );
		}
		catch( Exception e )
		{
			logger.error( "Message publishing Error.", e );
		}
	}

	@PostConstruct
	public void init() throws Exception
	{
		serverTimeSubscription.setProcessor( this );
		subscriptionMap.put( serverTimeSubscription.getType(), serverTimeSubscription );
	}

	private TextMessage getTextMessage( Serializable data ) throws JsonProcessingException
	{
		return new TextMessage( jacksonObjectMapper.writeValueAsString( data ) );
	}

	@Override
	public void sendMessage( String httpSessionId, Serializable message )
	{
		Collection<WebSocketSession> socketSessions = sessionRegistry.findSocketSessions( httpSessionId );
		if( socketSessions != null )
		{
			try
			{
				final TextMessage textMessage = getTextMessage( message );

				if( logger.isDebugEnabled() )
				{
					logger.debug( "Send Message. HttpSessionId: " + httpSessionId + ". Message: " + textMessage.getPayload() );
				}

				socketSessions.forEach( socketSession -> {
					try
					{
						socketSession.sendMessage( textMessage );
					}
					catch( IOException e )
					{
						logger.warn( "Send error. HttpSessionId: " + httpSessionId, e );
					}
				} );
			}
			catch( JsonProcessingException e )
			{
				logger.warn( "Message transformation error. HttpSessionId: " + httpSessionId, e );
			}
		}
	}

	@Override
	public void sendMessageToAll( String fromWsSessionId, Serializable message )
	{
		// TODO not implemented
	}

	/*
	 * SocketHandler Events
	 *
	 */

	@Override
	public void handleMessage( Long userId, String wsSessionId, String httpSessionId, String data ) throws IOException
	{
		if( logger.isDebugEnabled() )
		{
			logger.debug( "Handle Message. HttpSessionId: " + httpSessionId + ". Message: " + data );
		}

		try
		{
			BaseMessage message = jacksonObjectMapper.readValue( data, reference );

			if( messageHandler != null )
			{
				messageHandler.handleMessage( message, userId, httpSessionId, wsSessionId );
			}
			if( publish )
			{
				publishEvent( new MessageEvent<>( message, this, userId, httpSessionId, wsSessionId ) );
			}
		}
		catch( Exception e )
		{
			logger.error( "Message handle Error.", e );
		}
	}

	@Async
	@Override
	public void connectionEstablished( Long userId, String wsSessionId, String httpSessionId )
	{
		// Auto Subscribe

		subscriptionMap.values().stream().filter( ISubscription::isAutoSubscribe ).forEach( subscription -> {
			subscription.subscribe( httpSessionId );
		} );

		if( publish )
		{
			publishEvent( new ConnectionEstablishedEvent( this, userId, httpSessionId, wsSessionId ) );
		}
	}

	@Async
	@Override
	public void connectionClosed( Long userId, String wsSessionId, String httpSessionId )
	{
		// Unsubscribe from all

		for( ISubscription subscription : subscriptionMap.values() )
		{
			subscription.unsubscribe( httpSessionId );
		}

		if( publish )
		{
			publishEvent( new ConnectionClosedEvent( this, userId, httpSessionId, wsSessionId ) );
		}
	}

	/*
	 * Subscription
	 *
	 */

	@Override
	public boolean subscribe( String httpSessionId, SubscriptionType type )
	{
		return subscriptionMap.get( type ).subscribe( httpSessionId );
	}

	@Override
	public boolean unsubscribe( String httpSessionId, SubscriptionType type )
	{
		return subscriptionMap.get( type ).unsubscribe( httpSessionId );
	}

	@Override
	public boolean isSubscribed( String httpSessionId, SubscriptionType type )
	{
		return subscriptionMap.get( type ).isSubscribed( httpSessionId );
	}
}

package com.smallgroupnetwork.web.websocket.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * User: gyazykov
 * Date: 2/10/16.
 * Time: 3:14 PM
 */

public class SocketHandler implements WebSocketHandler
{
	@Autowired
	private SessionRegistry sessionRegistry;
	@Autowired( required = false )
	private ISocketHandlerListener listener;

	@Override
	public void afterConnectionEstablished( WebSocketSession session ) throws Exception
	{
		String httpSessionId = (String) session.getAttributes().get( SessionRegistry.getWsSessionHttpSessionIdAttrName() );
		if( !sessionRegistry.addSocketSession( httpSessionId, session ) )
		{
			// Close WebSocketSession when HttpSession already removed.
			session.close();
		}

		if( listener != null )
		{
			Long userId = (Long) session.getAttributes().get( SessionRegistry.getWsSessionUserIdAttrName() );
			listener.connectionEstablished( userId, session.getId(), httpSessionId );
		}
	}

	@Override
	public void handleMessage( WebSocketSession session, WebSocketMessage<?> message ) throws Exception
	{
		String httpSessionId = (String) session.getAttributes().get( SessionRegistry.getWsSessionHttpSessionIdAttrName() );
		sessionRegistry.updateSessionLastAccess( httpSessionId );

		if( listener != null )
		{
			Long userId = (Long) session.getAttributes().get( SessionRegistry.getWsSessionUserIdAttrName() );
			listener.handleMessage( userId, session.getId(), httpSessionId, ((TextMessage) message).getPayload() );
		}
	}

	@Override
	public void handleTransportError( WebSocketSession session, Throwable exception ) throws Exception
	{
		session.sendMessage( new TextMessage( exception.getMessage() ) );
		session.close();
	}

	@Override
	public void afterConnectionClosed( WebSocketSession session, CloseStatus closeStatus ) throws Exception
	{
		String httpSessionId = (String) session.getAttributes().get( SessionRegistry.getWsSessionHttpSessionIdAttrName() );
		sessionRegistry.removeSocketSession( httpSessionId, session );

		if( listener != null )
		{
			Long userId = (Long) session.getAttributes().get( SessionRegistry.getWsSessionUserIdAttrName() );
			listener.connectionClosed( userId, session.getId(), httpSessionId );
		}
	}

	@Override
	public boolean supportsPartialMessages()
	{
		return false;
	}
}

package com.smallgroupnetwork.web.websocket.session;

import com.smallgroupnetwork.security.AccountHolder;
import com.smallgroupnetwork.security.UserAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * User: gyazykov
 * Date: 2/10/16.
 * Time: 3:14 PM
 */

public class SocketHandshakeInterceptor implements HandshakeInterceptor
{
	private static final Logger logger = LoggerFactory.getLogger( SocketHandshakeInterceptor.class );

	@Override
	public boolean beforeHandshake( ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes ) throws Exception
	{
		HttpSession session = getSession( request );
		if( session != null )
		{
			UserAuthentication authentication = AccountHolder.getUserAuthentication();
			if( authentication != null && authentication.getUser() != null )
			{
				attributes.put( SessionRegistry.getWsSessionHttpSessionIdAttrName(), session.getId() );
				attributes.put( SessionRegistry.getWsSessionUserIdAttrName(), authentication.getUser().getId() );

				if( logger.isDebugEnabled() )
				{
					logger.debug( "WS Handshake Accept. SessionId: " + session.getId() );
				}

				// Accept WebSocketSession creation
				return true;
			}

			if( logger.isDebugEnabled() )
			{
				logger.debug( "UserAuthentication in null. WS Handshake failure. SessionId: " + session.getId() );
			}

			// Abort WebSocketSession creation when HttpSession not authenticated.
			return false;
		}
		else
		{
			if( logger.isDebugEnabled() )
			{
				logger.debug( "Session in null. WS Handshake failure." );
			}

			// Abort WebSocketSession creation when HttpSession not exist.
			return false;
		}
	}

	private HttpSession getSession( ServerHttpRequest request )
	{
		if( request instanceof ServletServerHttpRequest )
		{
			ServletServerHttpRequest serverRequest = (ServletServerHttpRequest) request;
			return serverRequest.getServletRequest().getSession( false );
		}
		return null;
	}

	@Override
	public void afterHandshake( ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception )
	{
		// none
	}
}

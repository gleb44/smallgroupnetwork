package com.smallgroupnetwork.web.websocket.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * User: gyazykov
 * Date: 2/10/16.
 * Time: 3:14 PM
 */

@Service
public class SessionRegistry implements ISessionRegistry
{
	private static final Logger logger = LoggerFactory.getLogger( SessionRegistry.class );

	private static final String LAST_ACCESS_SESSION_ATTR_NAME = "lastAccessTime";
	public static final String WS_SESSION_HTTP_SESSION_ID_ATTR_NAME = "httpSessionId";
	public static final String WS_SESSION_USER_ID_ATTR_NAME = "authenticatedUserId";

	public static String getLastAccessSessionAttrName()
	{
		return LAST_ACCESS_SESSION_ATTR_NAME;
	}

	public static String getWsSessionHttpSessionIdAttrName()
	{
		return WS_SESSION_HTTP_SESSION_ID_ATTR_NAME;
	}

	public static String getWsSessionUserIdAttrName()
	{
		return WS_SESSION_USER_ID_ATTR_NAME;
	}


	private final ConcurrentMap<String, SessionRecord> sessionRecord = new ConcurrentHashMap<>();


	@Value( "${server.user.session.timeout}" )
	private Long sessionTimeout;


	@Scheduled( fixedDelay = 20000 )
	private void invalidateExpiredSessions()
	{
		if( sessionRecord.isEmpty() )
		{
			return;
		}

		sessionRecord.forEach( ( httpSessionId, record ) -> {
			try
			{
				Long lastAccessTime = (Long) record.getHttpSession().getAttribute( getLastAccessSessionAttrName() );
				if( lastAccessTime == null || lastAccessTime + this.sessionTimeout < System.currentTimeMillis() )
				{
					record.getHttpSession().invalidate();

					if( logger.isDebugEnabled() )
					{
						logger.debug( "Invalidate Expired Sessions id: " + httpSessionId + " time: " + lastAccessTime );
					}
				}
			}
			catch( IllegalStateException ignored )
			{
				if( logger.isDebugEnabled() )
				{
					logger.debug( "Cannot invalidate session. The Session has already been invalidated. SessionId: " + httpSessionId );
				}
			}
		} );
	}

	@Override
	public HttpSession getSession( String httpSessionId )
	{
		SessionRecord record = sessionRecord.get( httpSessionId );
		if( record != null )
		{
			return record.getHttpSession();
		}
		return null;
	}

	@Override
	public void addSession( HttpSession session )
	{
		sessionRecord.put( session.getId(), new SessionRecord( session ) );
	}

	@Override
	public void removeSession( HttpSession session )
	{
		closeAssociatedSocketSessions( sessionRecord.remove( session.getId() ) );
	}

	@Override
	public void updateSessionLastAccess( String httpSessionId )
	{
		SessionRecord record = sessionRecord.get( httpSessionId );
		if( record != null )
		{
			try
			{
				record.getHttpSession().setAttribute( SessionRegistry.getLastAccessSessionAttrName(), System.currentTimeMillis() );

				if( logger.isDebugEnabled() )
				{
					logger.debug( "Update Session LastAccess id: " + httpSessionId + " time: " + record.getHttpSession().getAttribute( getLastAccessSessionAttrName() ) );
				}
			}
			catch( IllegalStateException ignored )
			{
				if( logger.isDebugEnabled() )
				{
					logger.debug( "Cannot update LastAccessSession value. The Session has already been invalidated" );
				}
			}
		}
	}

	@Override
	public boolean addSocketSession( String httpSessionId, WebSocketSession session )
	{
		SessionRecord record = sessionRecord.get( httpSessionId );
		if( record != null )
		{
			record.getWebSocketSessions().put( session.getId(), session );
			return true;
		}
		return false;
	}

	@Override
	public void removeSocketSession( String httpSessionId, WebSocketSession session )
	{
		SessionRecord record = sessionRecord.get( httpSessionId );
		if( record != null )
		{
			record.getWebSocketSessions().remove( session.getId() );
		}
	}

	@Override
	public Collection<WebSocketSession> findSocketSessions( String httpSessionId )
	{
		SessionRecord record = sessionRecord.get( httpSessionId );
		if( record != null )
		{
			return Collections.unmodifiableCollection( record.getWebSocketSessions().values() );
		}
		return null;
	}

	@Override
	public void closeSocketSessions( String httpSessionId )
	{
		closeAssociatedSocketSessions( sessionRecord.get( httpSessionId ) );
	}

	private void closeAssociatedSocketSessions( SessionRecord record )
	{
		if( record != null )
		{
			record.getWebSocketSessions().values().forEach( webSocketSession -> {
				try
				{
					webSocketSession.close( CloseStatus.NORMAL );
				}
				catch( IOException ignored )
				{
					// none
				}
			} );
		}
	}

	public static class SessionRecord
	{
		private HttpSession httpSession;
		private ConcurrentMap<String, WebSocketSession> webSocketSessions = new ConcurrentHashMap<>();

		public SessionRecord( HttpSession httpSession )
		{
			this.httpSession = httpSession;
		}

		public HttpSession getHttpSession()
		{
			return httpSession;
		}

		public ConcurrentMap<String, WebSocketSession> getWebSocketSessions()
		{
			return webSocketSessions;
		}
	}
}


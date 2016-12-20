package com.smallgroupnetwork.web.websocket.session;

import org.springframework.web.socket.WebSocketSession;

import javax.servlet.http.HttpSession;
import java.util.Collection;

/**
 * User: gyazykov
 * Date: 2/10/16.
 * Time: 3:14 PM
 */

public interface ISessionRegistry
{
	HttpSession getSession( String httpSessionId );

	void addSession( HttpSession session );

	void removeSession( HttpSession session );

	void updateSessionLastAccess( String httpSessionId );

	boolean addSocketSession( String httpSessionId, WebSocketSession session );

	void removeSocketSession( String httpSessionId, WebSocketSession session );

	Collection<WebSocketSession> findSocketSessions( String httpSessionId );

	void closeSocketSessions( String httpSessionId );
}

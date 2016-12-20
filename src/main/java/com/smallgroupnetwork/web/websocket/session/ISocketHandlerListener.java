package com.smallgroupnetwork.web.websocket.session;

import java.io.IOException;

/**
 * User: gyazykov
 * Date: 1/02/16.
 * Time: 3:14 PM
 */
public interface ISocketHandlerListener
{
	void handleMessage( Long userId, String wsSessionId, String httpSessionId, String data ) throws IOException;

	void connectionEstablished( Long userId, String wsSessionId, String httpSessionId );

	void connectionClosed( Long userId, String wsSessionId, String httpSessionId );
}

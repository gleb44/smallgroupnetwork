package com.smallgroupnetwork.web.websocket.mesaging;

import com.smallgroupnetwork.web.websocket.mesaging.message.BaseMessage;

/**
 * User: gyazykov
 * Date: 1/02/16.
 * Time: 3:14 PM
 */
public interface IMessageHandler
{
	void handleMessage( BaseMessage message, Long userId, String httpSessionId, String wsSessionId );
}

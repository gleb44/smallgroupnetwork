package com.smallgroupnetwork.web.livenotification;

import com.smallgroupnetwork.web.livenotification.message.OperationType;

import java.io.Serializable;

/**
 * User: gyazykov
 * Date: 1/02/16.
 * Time: 3:14 PM
 */

public interface ILiveNotificationSender
{
	void sendToUser( Long userId, OperationType type, Serializable message );

	void sendToAll( OperationType type, Serializable payload );
}

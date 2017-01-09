package com.smallgroupnetwork.service.notification;

import com.smallgroupnetwork.web.livenotification.ILiveNotificationSender;
import com.smallgroupnetwork.web.livenotification.ILiveNotificationSenderAdapter;
import com.smallgroupnetwork.web.livenotification.message.OperationType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * User: gyazykov
 * Date: 1/02/16.
 * Time: 3:14 PM
 */

@Service( "liveNotificationSenderAdapter" )
public class LiveNotificationSenderAdapter implements ILiveNotificationSenderAdapter
{
	private ILiveNotificationSender proxy;

	@Override
	public void setProxy( ILiveNotificationSender proxy )
	{
		this.proxy = proxy;
	}

	private ILiveNotificationSender getProxy()
	{
		Assert.notNull( proxy, "LiveNotificationSender must not be null" );
		return proxy;
	}

	@Override
	public void sendToUser( Long userId, OperationType type, Serializable message )
	{
		getProxy().sendToUser( userId, type, message );
	}

	@Override
	public void sendToAll( OperationType type, Serializable message )
	{
		getProxy().sendToAll( type, message );
	}
}

package com.smallgroupnetwork.service.notification;

import com.smallgroupnetwork.model.AdminAccess;
import com.smallgroupnetwork.model.GroupAdminActivity;
import com.smallgroupnetwork.model.NotificationAdminActivity;
import com.smallgroupnetwork.model.Participant;
import com.smallgroupnetwork.service.IParticipantService;
import com.smallgroupnetwork.service.activity.IGroupAdminActivityService;
import com.smallgroupnetwork.web.livenotification.ILiveNotificationSender;
import com.smallgroupnetwork.web.livenotification.ILiveNotificationSenderAdapter;
import com.smallgroupnetwork.web.livenotification.message.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * User: gyazykov
 * Date: 1/02/16.
 * Time: 3:14 PM
 */

@Service( "liveNotificationSenderAdapter" )
public class LiveNotificationSenderAdapter implements ILiveNotificationSenderAdapter
{
	@Autowired
	private IParticipantService participantService;
	@Autowired
	private IGroupAdminActivityService groupAdminActivityService;

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
	public void sendToGroup( Long groupId, OperationType type, Serializable message )
	{
		getProxy().sendToGroup( groupId, type, message );
	}

	@Async
	@Override
	public void onAdminAccessChanged( Long userId, AdminAccess access )
	{
		getProxy().onAdminAccessChanged( userId, access );
	}

	@Async
	@Override
	public void onParticipantAdded( Participant participant )
	{
		participant = participantService.read( participant.getId() );
		if( participant != null )
		{
			getProxy().onParticipantAdded( participant );
		}
	}

	@Async
	@Override
	public void onParticipantRemoved( Participant participant )
	{
		getProxy().onParticipantRemoved( participant );
	}

	@Async
	@Override
	public void onInterestedAdded( Participant participant, Set<Long> hostId )
	{
		getProxy().onInterestedAdded( participant, hostId );
	}

	@Async
	@Override
	public void onCandidateRemoved( Participant participant )
	{
		getProxy().onCandidateRemoved( participant );
	}

	@Async
	@Override
	public void onGroupAdminActivityAdded( GroupAdminActivity activity )
	{
		GroupAdminActivity activityFetched = groupAdminActivityService.read( activity.getId() );
		if( activityFetched != null )
		{
			getProxy().onGroupAdminActivityAdded( activityFetched );
		}
	}

	@Async
	@Override
	public void onAdminNotificationAdded( List<NotificationAdminActivity> activities )
	{
		getProxy().onAdminNotificationAdded( activities );
	}

	@Async
	@Override
	public void onAdminNotificationUpdated( List<NotificationAdminActivity> activities )
	{
		getProxy().onAdminNotificationUpdated( activities );
	}
}

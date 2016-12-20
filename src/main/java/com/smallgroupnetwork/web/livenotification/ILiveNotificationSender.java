package com.smallgroupnetwork.web.livenotification;

import com.smallgroupnetwork.model.AdminAccess;
import com.smallgroupnetwork.model.GroupAdminActivity;
import com.smallgroupnetwork.model.NotificationAdminActivity;
import com.smallgroupnetwork.model.Participant;
import com.smallgroupnetwork.web.livenotification.message.OperationType;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * User: gyazykov
 * Date: 1/02/16.
 * Time: 3:14 PM
 */

public interface ILiveNotificationSender
{
	void sendToUser( Long userId, OperationType type, Serializable message );

	void sendToGroup( Long groupId, OperationType type, Serializable message );

	void onAdminAccessChanged( Long userId, AdminAccess access );

	void onParticipantAdded( Participant participant );

	void onParticipantRemoved( Participant participant );

	void onInterestedAdded( Participant participant, Set<Long> hostId );

	void onCandidateRemoved( Participant participant );

	void onGroupAdminActivityAdded( GroupAdminActivity activity );

	void onAdminNotificationAdded( List<NotificationAdminActivity> activities );

	void onAdminNotificationUpdated( List<NotificationAdminActivity> activities );
}

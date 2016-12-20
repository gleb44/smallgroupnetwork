package com.smallgroupnetwork.web.livenotification.message;

/**
 * User: gyazykov
 * Date: 02/19/16.
 * Time: 11:30 AM
 */
public enum OperationType
{
	AdminAccessChanged,
	ParticipantAdded,
	ParticipantUpdated,
	ParticipantRemoved,
	PostAdded,
	PostUpdated,
	PostRemoved,
	PostCommentAdded,
	PostCommentUpdated,
	PostCommentRemoved,
	ChatMessageAdded,
	OnlineStatusUpdated,
	// AdminActivity
	GroupAdminActivityAdded,
	AdminNotificationAdded,
	AdminNotificationUpdated
}

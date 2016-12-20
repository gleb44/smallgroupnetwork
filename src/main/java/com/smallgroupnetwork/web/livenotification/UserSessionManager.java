package com.smallgroupnetwork.web.livenotification;

import com.smallgroupnetwork.model.AdminAccess;
import com.smallgroupnetwork.model.NotificationAdminActivity;
import com.smallgroupnetwork.model.Group;
import com.smallgroupnetwork.model.GroupAdminActivity;
import com.smallgroupnetwork.model.Participant;
import com.smallgroupnetwork.model.User;
import com.smallgroupnetwork.model.dto.ParticipantNotifications;
import com.smallgroupnetwork.security.UserAuthentication;
import com.smallgroupnetwork.service.IParticipantAcknowledgeService;
import com.smallgroupnetwork.service.IUserAcknowledgeService;
import com.smallgroupnetwork.service.activity.INotificationAdminActivityService;
import com.smallgroupnetwork.web.livenotification.message.FeedbackMessage;
import com.smallgroupnetwork.web.livenotification.message.GroupMessage;
import com.smallgroupnetwork.web.livenotification.message.OperationType;
import com.smallgroupnetwork.web.livenotification.message.UserMessage;
import com.smallgroupnetwork.web.websocket.mesaging.IMessageHandler;
import com.smallgroupnetwork.web.websocket.mesaging.IMessageService;
import com.smallgroupnetwork.web.websocket.mesaging.message.BaseMessage;
import com.smallgroupnetwork.web.websocket.session.SessionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * User: gyazykov
 * Date: 1/02/16.
 * Time: 3:14 PM
 */
@Service( "userSessionManager" )
public class UserSessionManager implements IUserSessionManager, ILiveNotificationSender, IMessageHandler
{
	private final ConcurrentHashMap<Long, UserManagerRecord> userToHttpSessions = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<Long, CopyOnWriteArraySet<String>> groupToHttpSessions = new ConcurrentHashMap<>();

	@Autowired
	private IMessageService messageService;
	@Autowired
	private SessionRegistry sessionRegistry;
	@Autowired
	@Qualifier( "liveNotificationSenderAdapter" )
	private ILiveNotificationSenderAdapter liveNotificationSenderAdapter;
	@Autowired
	private IParticipantAcknowledgeService participantAcknowledgeService;
	@Autowired
	private IUserAcknowledgeService userAcknowledgeService;
	@Autowired
	private INotificationAdminActivityService notificationAdminActivityService;

	@PostConstruct
	public void init()
	{
		Assert.notNull( liveNotificationSenderAdapter, "LiveNotificationSenderAdapter must not be null" );
		Assert.notNull( messageService, "MessageService must not be null" );

		liveNotificationSenderAdapter.setProxy( this );
		messageService.setMessageHandler( this );
	}

	/*
	 * Live Notification Sender
	 *
	 */

	@Override
	public void sendToUser( Long userId, OperationType type, Serializable payload )
	{
		UserManagerRecord record = userToHttpSessions.get( userId );
		if( record != null )
		{
			UserMessage<Serializable> message = new UserMessage<>( payload, type );
			record.getHttpSessions().forEach( httpSessionId -> messageService.sendMessage( httpSessionId, message ) );
		}
	}

	@Override
	public void sendToGroup( Long groupId, OperationType type, Serializable payload )
	{
		GroupMessage<Serializable> message = new GroupMessage<>( payload, type );
		CopyOnWriteArraySet<String> httpSessionIds = groupToHttpSessions.get( groupId );
		if( httpSessionIds != null )
		{
			httpSessionIds.forEach( httpSessionId -> messageService.sendMessage( httpSessionId, message ) );
		}
	}

	@Override
	public void onAdminAccessChanged( Long userId, AdminAccess access )
	{
		UserManagerRecord record = userToHttpSessions.get( userId );
		if( record != null )
		{
			final UserMessage<AdminAccess> message = new UserMessage<>( access, OperationType.AdminAccessChanged );

			record.getHttpSessions().forEach( httpSessionId -> {
				UserAuthentication authentication = record.getUserData();
				if( authentication != null )
				{
					User user = authentication.getUser();
					synchronized( user )
					{
						user.setAdminAccess( access );
					}

					// Send notification to user session
					messageService.sendMessage( httpSessionId, message );
				}
			} );
		}
	}

	@Override
	public void onParticipantAdded( Participant participant )
	{
		// Send notification to group users
		sendToGroup( participant.getGroup().getId(), OperationType.ParticipantAdded, participant );

		UserManagerRecord record = userToHttpSessions.get( participant.getUser().getId() );
		if( record != null )
		{
			UserMessage<Participant> message = new UserMessage<>( participant, OperationType.ParticipantAdded );

			record.getHttpSessions().forEach( httpSessionId -> {
				UserAuthentication authentication = record.getUserData();
				if( authentication != null )
				{
					User user = authentication.getUser();
					synchronized( user )
					{
						// Add participant to user session
						Set<Participant> newParticipants = user.getParticipants().stream().filter( iterParticipant -> !iterParticipant.getId().equals( participant.getId() ) )
							.collect( Collectors.toSet() );

						newParticipants.add( participant );
						user.setParticipants( newParticipants );
					}

					addParticipant( participant, httpSessionId );

					// Send notification to user session
					messageService.sendMessage( httpSessionId, message );
				}
			} );
		}
	}

	@Override
	public void onParticipantRemoved( Participant participant )
	{
		UserManagerRecord record = userToHttpSessions.get( participant.getUser().getId() );
		if( record != null )
		{
			UserMessage<Participant> message = new UserMessage<>( participant, OperationType.ParticipantRemoved );

			record.getHttpSessions().forEach( httpSessionId -> {
				// Remove group to Participant association
				removeParticipant( participant, httpSessionId );

				UserAuthentication authentication = record.getUserData();
				if( authentication != null )
				{
					// Remove participant from user session
					User user = authentication.getUser();
					synchronized( user )
					{
						Set<Participant> newParticipants = user.getParticipants().stream().filter( iterParticipant -> !iterParticipant.getId().equals( participant.getId() ) )
							.collect( Collectors.toSet() );

						user.setParticipants( newParticipants );
					}

					// Send notification to user session
					messageService.sendMessage( httpSessionId, message );
				}
			} );
		}

		// Send notification to group users
		sendToGroup( participant.getGroup().getId(), OperationType.ParticipantRemoved, participant );
	}

	@Override
	public void onCandidateRemoved( Participant participant )
	{
		sendToGroup( participant.getGroup().getId(), OperationType.ParticipantRemoved, participant );
	}

	@Override
	public void onGroupAdminActivityAdded( GroupAdminActivity activity )
	{
		sendToUser( activity.getUser().getId(), OperationType.GroupAdminActivityAdded, activity );
	}

	@Override
	public void onAdminNotificationAdded( List<NotificationAdminActivity> activities )
	{
		activities.forEach( activity -> sendToUser( activity.getUser().getId(), OperationType.AdminNotificationAdded, activity ) );
	}

	@Override
	public void onAdminNotificationUpdated( List<NotificationAdminActivity> activities )
	{
		activities.forEach( activity -> sendToUser( activity.getUser().getId(), OperationType.AdminNotificationUpdated, activity ) );
	}

	@Override
	public void onInterestedAdded( Participant participant, Set<Long> hostId )
	{
		GroupMessage<Serializable> message = new GroupMessage<>( participant, OperationType.ParticipantAdded );
		hostId.forEach( id -> {
			UserManagerRecord record = userToHttpSessions.get( id );
			if( record != null )
			{
				record.getHttpSessions().forEach( httpSessionId -> messageService.sendMessage( httpSessionId, message ) );
			}
		} );
	}

	@Override
	public void handleMessage( BaseMessage message, Long userId, String httpSessionId, String wsSessionId )
	{
		if( !(message instanceof FeedbackMessage) )
		{
			return;
		}
		FeedbackMessage feedbackMessage = (FeedbackMessage) message;

		Long[] readIds = feedbackMessage.getIds();
		Optional<Long> maxReadId = Arrays.stream( readIds ).max( Comparator.<Long>naturalOrder() );

		switch( feedbackMessage.getFeedbackType() )
		{
			case GroupAdminActivityRead:
			{
				userAcknowledgeService.updateLastRead( userId, maxReadId.get() );
				break;
			}
			case AdminNotificationRead:
			{
				notificationAdminActivityService.markAsRead( readIds );
				break;
			}
			case PostRead:
			case ChatMessageRead:
			{
				participantAcknowledgeService.updateLastRead( userId, feedbackMessage.getGroupId(), feedbackMessage.getFeedbackType(), maxReadId.get() );
				break;
			}
			default:
			{
				throw new IllegalArgumentException( "Invalid feedback type: " + feedbackMessage.getFeedbackType() );
			}
		}
	}

	/*
	 * User Session Manager
	 *
	 */

	private void addSessionToGroups( final User user, final String httpSessionId )
	{
		if( !CollectionUtils.isEmpty( user.getParticipants() ) )
		{
			user.getParticipants().forEach( participant -> addParticipant( participant, httpSessionId ) );
		}
	}

	protected void removeSessionFromGroups( String httpSessionId, User user )
	{
		// Remove group to user session associations
		if( !CollectionUtils.isEmpty( user.getParticipants() ) )
		{
			user.getParticipants().forEach( participant -> removeParticipant( participant, httpSessionId ) );
		}
	}

	private void updateOnlineStatus( Participant participant, Long groupId )
	{
		Participant result = new Participant();
		result.setId( participant.getId() );
		result.setNotifications( new ParticipantNotifications( participant.getUser() != null && userToHttpSessions.containsKey( participant.getUser().getId() ) ) );
		sendToGroup( groupId, OperationType.OnlineStatusUpdated, result );
	}

	private void addParticipant( Participant participant, String httpSessionId )
	{
		Long groupId = participant.getGroup().getId();
		// Create group item when item not exist
		groupToHttpSessions.compute( groupId, ( key, val ) -> {
			CopyOnWriteArraySet<String> groupSessions = val == null ? new CopyOnWriteArraySet<>() : val;
			groupSessions.add( httpSessionId );
			return groupSessions;
		} );

		updateOnlineStatus( participant, groupId );
	}

	private void removeParticipant( Participant participant, String httpSessionId )
	{
		Long groupId = participant.getGroup().getId();
		// Remove group item without session associations
		groupToHttpSessions.computeIfPresent( groupId, ( key, val ) -> {
			val.remove( httpSessionId );
			return val.isEmpty() ? null : val;
		} );

		updateOnlineStatus( participant, groupId );
	}

	@Override
	public void onUserLogin( UserAuthentication userData, String httpSessionId )
	{
		User user = userData.getUser();
		// Add user to session association
		userToHttpSessions.compute( user.getId(), ( key, val ) -> {
			UserManagerRecord record = val == null ? new UserManagerRecord( userData ) : val;
			record.getHttpSessions().add( httpSessionId );
			return record;
		} );

		// Add group to user session associations
		addSessionToGroups( user, httpSessionId );
	}

	@Override
	public void onUserLogout( Long userId, String httpSessionId )
	{
		// Close all webSocket Sessions when user logout
		sessionRegistry.closeSocketSessions( httpSessionId );

		// Remove user to session association
		UserManagerRecord record = userToHttpSessions.get( userId );
		if( record != null )
		{
			// Remove user to httpSessions association item without session
			userToHttpSessions.computeIfPresent( userId, ( key, val ) -> {
				val.getHttpSessions().remove( httpSessionId );
				return val.getHttpSessions().isEmpty() ? null : val;
			} );

			User user = record.getUserData().getUser();
			removeSessionFromGroups( httpSessionId, user );
		}
	}

	@Override
	public UserAuthentication getUserInfo( Long userId )
	{
		UserManagerRecord record = userToHttpSessions.get( userId );
		return record == null ? null : record.getUserData();
	}

	@Override
	public void initOnlineStatus( Group group )
	{
		group.getParticipants().forEach( participant -> {
			participant.setNotifications( new ParticipantNotifications( participant.getUser() != null && userToHttpSessions.containsKey( participant.getUser().getId() ) ) );
		} );
	}
}

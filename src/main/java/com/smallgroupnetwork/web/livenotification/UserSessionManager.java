package com.smallgroupnetwork.web.livenotification;

import com.smallgroupnetwork.model.User;
import com.smallgroupnetwork.security.UserAuthentication;
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

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: gyazykov
 * Date: 1/02/16.
 * Time: 3:14 PM
 */
@Service( "userSessionManager" )
public class UserSessionManager implements IUserSessionManager, ILiveNotificationSender, IMessageHandler
{
	private final ConcurrentHashMap<Long, UserManagerRecord> userToHttpSessions = new ConcurrentHashMap<>();

	@Autowired
	private IMessageService messageService;
	@Autowired
	private SessionRegistry sessionRegistry;
	@Autowired
	@Qualifier( "liveNotificationSenderAdapter" )
	private ILiveNotificationSenderAdapter liveNotificationSenderAdapter;

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
	public void sendToAll( OperationType type, Serializable payload )
	{
		GroupMessage<Serializable> message = new GroupMessage<>( payload, type );
		userToHttpSessions.forEach( ( aLong, userManagerRecord ) -> {
			userManagerRecord.getHttpSessions().forEach( httpSessionId -> messageService.sendMessage( httpSessionId, message ) );
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

		switch( feedbackMessage.getFeedbackType() )
		{
			case ChatMessageRead:
			{
				// TODO
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
		}
	}

	@Override
	public UserAuthentication getUserInfo( Long userId )
	{
		UserManagerRecord record = userToHttpSessions.get( userId );
		return record == null ? null : record.getUserData();
	}
}

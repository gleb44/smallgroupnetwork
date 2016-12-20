package com.smallgroupnetwork.web.listener;

import com.smallgroupnetwork.security.AccountHolder;
import com.smallgroupnetwork.web.livenotification.IUserSessionManager;
import com.smallgroupnetwork.web.util.HttpSessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.File;
import java.util.List;

/**
 * User: gleb
 * Date: 6/9/14
 * Time: 12:13 PM
 */
@Component
public class SessionListener implements HttpSessionListener, ApplicationContextAware
{
	private final Logger logger = LoggerFactory.getLogger( this.getClass() );

	@Autowired
	@Qualifier( "userSessionManager" )
	private IUserSessionManager userSessionManager;

	@Override
	public void sessionCreated( HttpSessionEvent se )
	{
		logger.debug( "Session created: " + se.getSession().getId() );
	}

	@Override
	public void sessionDestroyed( HttpSessionEvent se )
	{
		final HttpSession session = se.getSession();

		try
		{
			logger.debug( "Session destroyed: " + session.getId() );
			List<File> tempFiles = HttpSessionUtil.getListAttribute( session, File.class, AccountHolder.FILES_KEY );

			if( !CollectionUtils.isEmpty( tempFiles ) )
			{
				for( File tempFile : tempFiles )
				{
					if( tempFile.exists() )
					{
						if( tempFile.delete() )
						{
							logger.debug( "Deleted file: " + tempFile + " for session: " + session.getId() );
						}
						else
						{
							logger.debug( "Can't delete file: " + tempFile + " for session: " + session.getId() );
						}
					}
					else
					{
						logger.debug( "Skip file deletion: " + tempFile + " for session: " + session.getId() );
					}
				}
			}
		}
		catch( Exception ex )
		{
			logger.error( "Clean up temp files failed", ex );
		}

		Long userId = (Long) session.getAttribute( AccountHolder.USER_KEY );
		if( userId != null )
		{
			userSessionManager.onUserLogout( userId, se.getSession().getId() );
		}
	}

		/*
	 * Register Listener
	 *
	 */

	@Override
	public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException
	{
		if( applicationContext instanceof WebApplicationContext )
		{
			((WebApplicationContext) applicationContext).getServletContext().addListener( this );

			if( logger.isDebugEnabled() )
			{
				logger.debug( "Listener registered" );
			}
		}
		else
		{
			throw new RuntimeException( "Must be inside a web application context" );
		}
	}
}

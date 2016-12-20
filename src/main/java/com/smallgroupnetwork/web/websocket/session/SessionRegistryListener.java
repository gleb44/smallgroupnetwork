package com.smallgroupnetwork.web.websocket.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * User: gyazykov
 * Date: 2/10/16.
 * Time: 3:14 PM
 */

@Component
public class SessionRegistryListener implements HttpSessionListener, ApplicationContextAware
{
	private static final Logger logger = LoggerFactory.getLogger( SessionRegistryListener.class );

	@Autowired
	private SessionRegistry sessionRegistry;

	/*
	 * Session Listener
	 *
	 */

	@Override
	public void sessionCreated( HttpSessionEvent se )
	{
		if( logger.isDebugEnabled() )
		{
			logger.debug( "Session Created: " + se.getSession().getId() );
		}

		sessionRegistry.addSession( se.getSession() );
	}

	@Override
	public void sessionDestroyed( HttpSessionEvent se )
	{
		if( logger.isDebugEnabled() )
		{
			logger.debug( "Session Destroyed: " + se.getSession().getId() );
		}

		sessionRegistry.removeSession( se.getSession() );
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

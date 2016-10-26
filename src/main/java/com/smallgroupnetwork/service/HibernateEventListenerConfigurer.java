package com.smallgroupnetwork.service;

import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class HibernateEventListenerConfigurer
{
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private AttachmentEventListener listener;

	@PostConstruct
	public void registerListeners()
	{
		EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory).getServiceRegistry().getService( EventListenerRegistry.class );

		registry.getEventListenerGroup( EventType.PRE_INSERT ).appendListener( listener );
		registry.getEventListenerGroup( EventType.POST_COMMIT_INSERT ).appendListener( listener );
		registry.getEventListenerGroup( EventType.POST_COMMIT_UPDATE ).appendListener( listener );
		registry.getEventListenerGroup( EventType.POST_COMMIT_DELETE ).appendListener( listener );
	}
}
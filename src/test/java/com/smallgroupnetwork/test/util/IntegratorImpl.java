package com.smallgroupnetwork.test.util;

import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

/**
 * User: gyazykov
 * Date: 11/29/2016
 * Time: 1:28 PM
 */
public class IntegratorImpl implements Integrator
{
	/**
	 * Initialize HibernateInfoHolder.
	 * Show org.hibernate.integrator.internal.IntegratorServiceImpl
	 */
	@Override
	public void integrate( Metadata metadata, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry )
	{
		HibernateInfoHolder.setMetadata( metadata );
		HibernateInfoHolder.setSessionFactory( sessionFactory );
		HibernateInfoHolder.setServiceRegistry( serviceRegistry );
	}

	@Override
	public void disintegrate( SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry )
	{
	}
}

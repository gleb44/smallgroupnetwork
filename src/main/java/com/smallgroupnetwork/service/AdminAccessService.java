package com.smallgroupnetwork.service;

import com.smallgroupnetwork.model.AdminAccess;
import com.smallgroupnetwork.model.User;
import com.smallgroupnetwork.persistence.AbstractPersistenceService;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: gleb
 * Date: 3/26/15
 * Time: 2:42 PM
 */
@Service
public class AdminAccessService extends AbstractPersistenceService<AdminAccess, Long> implements IAdminAccessService
{
	@Override
	protected void applyFetching4Edit( Criteria criteria )
	{
		super.applyFetching4Edit( criteria );
		criteria.setFetchMode( "parents", FetchMode.JOIN );
	}

	@Override
	@Transactional
	public void saveOrUpdate( AdminAccess object )
	{
		super.saveOrUpdate( object );
		User user = (User) getSession().get( User.class, object.getAdmin().getId() );
		user.setAdminAccess( object );
	}
}

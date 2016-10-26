package com.smallgroupnetwork.service;

import com.smallgroupnetwork.model.Admin;
import com.smallgroupnetwork.persistence.AbstractPersistenceService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * User: gleb
 * Date: 5/14/14
 * Time: 3:30 PM
 */
@Service
public class AdminService extends AbstractPersistenceService<Admin, Long> implements IAdminService
{
	@Resource( name = "passwordEncoder" )
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public Admin merge( Admin admin )
	{
		try
		{
			if( admin.getId() != null && admin.getPassword() == null )
			{
				Admin oldAdmin = load( admin.getId() );
				admin.setPassword( oldAdmin.getPassword() );
			}
			else
			{
				admin.setPassword( passwordEncoder.encode( admin.getPassword() ) );
			}
			return super.merge( admin );
		}
		catch( ConstraintViolationException ex )
		{
			addValidationResult( "login", "admin.login.duplicate", admin.getEmail() );
			validate();
			return null;
		}
	}

	@Override
	@Transactional( readOnly = true )
	public Admin findAccountAndLogin( String login, String password )
	{
		if( login == null || password == null )
		{
			addValidationResult( "password", "user.password.empty" );
			validate();
		}
		Admin admin = findByLogin( login );
		if( admin == null )
		{
			addValidationResult( "login", "user.login.invalid", login );
			validate();
			return null;
		}
		if( !passwordEncoder.matches( password, admin.getPassword() ) )
		{
			addValidationResult( "login", "user.password.invalid", login );
			validate();
		}
		return admin;
	}

	@Override
	@Transactional( readOnly = true )
	public Admin findByLogin( final String login )
	{
		Criteria criteria = getSession().createCriteria( getEntityClass() ).add( Restrictions.eq( "email", login.toLowerCase() ) );
		return (Admin) criteria.uniqueResult();
	}

	@Override
	@Transactional
	public void changePassword( String login, String password, String newPassword )
	{
		Admin admin = findAccountAndLogin( login, password );
		admin.setPassword( passwordEncoder.encode( newPassword ) );
	}

}

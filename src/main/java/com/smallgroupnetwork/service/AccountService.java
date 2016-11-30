package com.smallgroupnetwork.service;

import com.smallgroupnetwork.model.Account;
import com.smallgroupnetwork.persistence.AbstractPersistenceService;
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
public class AccountService extends AbstractPersistenceService<Account, Long> implements IAccountService
{
	@Resource( name = "passwordEncoder" )
	private PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public void saveOrUpdate( Account account )
	{
		try
		{
			if( account.getId() != null && account.getPassword() == null )
			{
				Account oldAccount = load( account.getId() );
				account.setPassword( oldAccount.getPassword() );
			}
			else
			{
				account.setPassword( passwordEncoder.encode( account.getPassword() ) );
			}
			super.saveOrUpdate( account );
		}
		catch( ConstraintViolationException ex )
		{
			addValidationResult( "login", "account.login.duplicate", account.getLogin() );
			validate();
		}
	}

	@Override
	@Transactional( readOnly = true )
	public Account findAccount( String login, String password )
	{
		if( login == null || password == null )
		{
			addValidationResult( "password", "user.password.empty" );
			validate();
		}
		Account account = findByLogin( login );
		if( account == null )
		{
			addValidationResult( "login", "user.login.invalid", login );
			validate();
			return null;
		}
		if( !passwordEncoder.matches( password, account.getPassword() ) )
		{
			addValidationResult( "login", "user.password.invalid", login );
			validate();
		}
		return account;
	}

	@Override
	@Transactional( readOnly = true )
	public Account findByLogin( final String login )
	{
		return (Account) createCriteria().add( Restrictions.eq( "login", login.toLowerCase() ) ).uniqueResult();
	}

	@Override
	@Transactional
	public void changePassword( String login, String password, String newPassword )
	{
		Account account = findAccount( login, password );
		account.setPassword( passwordEncoder.encode( newPassword ) );
	}

}

package com.smallgroupnetwork.service;

import com.smallgroupnetwork.model.Account;
import com.smallgroupnetwork.persistence.IPersistenceService;

/**
 * User: gleb
 * Date: 5/14/14
 * Time: 3:29 PM
 */
public interface IAccountService extends IPersistenceService<Account, Long>
{
	Account findAccount( String login, String password );

	Account findByLogin( final String login );

	void changePassword( String login, String password, String newPassword );
}

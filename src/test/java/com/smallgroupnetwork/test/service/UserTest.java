package com.smallgroupnetwork.test.service;

/**
 * Date: 10/1/13
 * Time: 8:18 PM
 */

import com.smallgroupnetwork.model.Account;
import com.smallgroupnetwork.model.AdminRole;
import com.smallgroupnetwork.model.Profile;
import com.smallgroupnetwork.model.User;
import com.smallgroupnetwork.service.IAccountService;
import com.smallgroupnetwork.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Date;

@ContextConfiguration( { "/test-config.xml" } )
public class UserTest extends AbstractTestNGSpringContextTests
{
	@Autowired
	IUserService userService;
	@Autowired
	IAccountService accountService;

	private static final String login = "test19";

	private User createUser()
	{
		User user = new User();
		user.setFirstName( login );
		user.setLastName( login );
		user.setEmail( login + "@test.com" );

		Profile profile = new Profile();
		profile.setBirthDate( new Date() );
		profile.setCountry( "Test Country" );
		profile.setState( "Test State" );
		profile.setCity( "Test City" );
		profile.setAddress( "Test Address" );
		profile.setPostalCode( "Test PostalCode" );

		profile.setUser( user );
		user.setProfile( profile );

		return userService.merge( user );
	}

	@Test
	public void testFindByFullName()
	{
		User user = createUser();
		Assert.assertNotNull( user );

		user = userService.get( user.getId() );
		Assert.assertNotNull( user );
		Assert.assertNotNull( user.getProfile() );

		user = userService.findByFullname( user.getFirstName() + ' ' + user.getLastName() );
		Assert.assertNotNull( user );

		userService.addAdminAccess( user.getId(), AdminRole.Admin );
		user = userService.get( user.getId() );
		Assert.assertNotNull( user );
		Assert.assertNotNull( user.getAdminAccess() );

		Account account = new Account();
		account.setUser( user );
		account.setLogin( login );
		account.setPassword( "demo1234" );
		accountService.saveOrUpdate( account );
	}
}

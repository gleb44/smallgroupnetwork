package com.smallgroupnetwork.model;

import com.smallgroupnetwork.persistence.BaseEntity;
import org.hibernate.validator.constraints.Email;

import javax.persistence.MappedSuperclass;

/**
 * User: gleb
 * Date: 5/21/14
 * Time: 5:55 PM
 */
@MappedSuperclass
public abstract class Person implements BaseEntity<Long>
{
	private String email;
	private String firstName;
	private String lastName;

	@Email
	public String getEmail()
	{
		return email;
	}

	public void setEmail( String email )
	{
		if( email == null )
		{
			this.email = null;
		}
		else
		{
			email = email.toLowerCase().trim();
			this.email = email.isEmpty() ? null : email;
		}
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName( String firstName )
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName( String lastName )
	{
		this.lastName = lastName;
	}
}

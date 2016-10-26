package com.smallgroupnetwork.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * User: gleb
 * Date: 5/21/14
 * Time: 6:02 PM
 */
@Entity
@Table( uniqueConstraints = @UniqueConstraint( name = "uk_admin_email", columnNames = "email" ) )
public class Admin extends Person
{
	private Long id;
	private String password;

	@Id
	@GeneratedValue
	public Long getId()
	{
		return id;
	}

	public void setId( Long id )
	{
		this.id = id;
	}

	@JsonIgnore
	public String getPassword()
	{
		return password;
	}

	@JsonProperty
	public void setPassword( String password )
	{
		this.password = password;
	}
}

package com.smallgroupnetwork.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smallgroupnetwork.persistence.StaticEntity;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * User: gyazykov
 * Date: 11/29/16
 * Time: 4:08 PM
 */
@Entity
@Table( uniqueConstraints = @UniqueConstraint( name = "uk_account_login", columnNames = "login" ) )
@DynamicUpdate
public class Account extends StaticEntity<Long>
{
	private User user;
	private String login;
	private String password;

	@MapsId
	@OneToOne( optional = false, fetch = FetchType.LAZY )
	@JoinColumn( name = "id", foreignKey = @javax.persistence.ForeignKey( name = "fk_account_user" ) )
	@BatchSize( size = 20 )
	public User getUser()
	{
		return user;
	}

	public void setUser( User user )
	{
		this.user = user;
	}

	@JsonIgnore
	public String getLogin()
	{
		return login;
	}

	@JsonProperty
	public void setLogin( String login )
	{
		this.login = login;
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

package com.smallgroupnetwork.model;

import com.smallgroupnetwork.persistence.StaticEntity;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
@DynamicUpdate
public class Profile extends StaticEntity<Long>
{
	private User user;
	private Date birthDate;
	private String country;
	private String state;
	private String postalCode;
	private String city;
	private String address;

	@MapsId
	@OneToOne( optional = false, fetch = FetchType.LAZY )
	@JoinColumn( name = "id", foreignKey = @ForeignKey( name = "fk_profile_user" ) )
	@BatchSize( size = 20 )
	public User getUser()
	{
		return user;
	}

	public void setUser( User user )
	{
		this.user = user;
	}

	public Date getBirthDate()
	{
		return birthDate;
	}

	public void setBirthDate( Date birthDate )
	{
		this.birthDate = birthDate;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry( String country )
	{
		this.country = country;
	}

	public String getState()
	{
		return state;
	}

	public void setState( String state )
	{
		this.state = state;
	}

	public String getPostalCode()
	{
		return postalCode;
	}

	public void setPostalCode( String postalCode )
	{
		this.postalCode = postalCode;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity( String city )
	{
		this.city = city;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress( String address )
	{
		this.address = address;
	}
}
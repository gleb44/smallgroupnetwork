package com.smallgroupnetwork.model;

import com.smallgroupnetwork.persistence.SyntheticEntity;
import org.hibernate.validator.constraints.Email;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * User: gleb
 * Date: 5/21/14
 * Time: 6:02 PM
 */
@Entity
@Table( name = "user_account", uniqueConstraints = @UniqueConstraint( name = "uk_admin_email", columnNames = "email" ) )
public class User extends SyntheticEntity
{
	private String email;
	private String firstName;
	private String lastName;
	private Attachment avatar;

	private Profile profile;
	private AdminAccess adminAccess;

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

	@ManyToOne( fetch = FetchType.LAZY, optional = true )
	@JoinColumn( name = "id_avatar", foreignKey = @javax.persistence.ForeignKey( name = "fk_user_attachment" ) )
	public Attachment getAvatar()
	{
		return avatar;
	}

	public void setAvatar( Attachment avatar )
	{
		this.avatar = avatar;
	}

	@OneToOne( optional = true, fetch = FetchType.LAZY )
	@JoinColumn( name = "id_profile", foreignKey = @javax.persistence.ForeignKey( name = "fk_user_profile" ) )
	public Profile getProfile()
	{
		return profile;
	}

	public void setProfile( Profile profile )
	{
		this.profile = profile;
	}

	@OneToOne( optional = true, fetch = FetchType.LAZY )
	@JoinColumn( name = "id_admin_access", foreignKey = @javax.persistence.ForeignKey( name = "fk_user_admin_access" ) )
	public AdminAccess getAdminAccess()
	{
		return adminAccess;
	}

	public void setAdminAccess( AdminAccess adminAccess )
	{
		this.adminAccess = adminAccess;
	}

	public void update( User user )
	{
		this.setEmail( user.getEmail() );
		this.setFirstName( user.getFirstName() );
		this.setLastName( user.getLastName() );

		if( this.profile == null )
		{
			return;
		}
		Profile userProfile = user.getProfile();
		this.profile.setBirthDate( userProfile.getBirthDate() );
		this.profile.setCountry( userProfile.getCountry() );
		this.profile.setState( userProfile.getState() );
		this.profile.setPostalCode( userProfile.getPostalCode() );
		this.profile.setCity( userProfile.getCity() );
		this.profile.setAddress( userProfile.getAddress() );
	}
}

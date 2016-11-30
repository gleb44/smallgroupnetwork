package com.smallgroupnetwork.model;

import com.smallgroupnetwork.persistence.StaticEntity;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

/**
 * User: gleb
 * Date: 3/16/15
 * Time: 10:12 PM
 */
@Entity
public class AdminAccess extends StaticEntity<Long>
{
	private User admin;
	private AdminRole adminRole;

	@MapsId
	@OneToOne( optional = false, fetch = FetchType.LAZY )
	@JoinColumn( name = "id", foreignKey = @javax.persistence.ForeignKey( name = "fk_admin_access_user" ) )
	@BatchSize( size = 20 )
	public User getAdmin()
	{
		return admin;
	}

	public void setAdmin( User profile )
	{
		this.admin = profile;
	}

	@Enumerated( EnumType.STRING )
	@Column( length = 50 )
	public AdminRole getAdminRole()
	{
		return adminRole;
	}

	public void setAdminRole( AdminRole adminRole )
	{
		this.adminRole = adminRole;
	}
}

package com.smallgroupnetwork.service;

import com.smallgroupnetwork.model.AdminAccess;
import com.smallgroupnetwork.model.AdminRole;
import com.smallgroupnetwork.model.Attachment;
import com.smallgroupnetwork.model.Profile;
import com.smallgroupnetwork.model.User;
import com.smallgroupnetwork.persistence.AbstractPersistenceService;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.LockOptions;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * User: gleb
 * Date: 5/14/14
 * Time: 3:30 PM
 */
@Service
public class UserService extends AbstractPersistenceService<User, Long> implements IUserService
{
	@Autowired
	private IAttachmentService attachmentService;
	@Autowired
	private IAdminAccessService adminAccessService;

	@Override
	protected void applyFetching( Criteria criteria )
	{
		criteria.setFetchMode( "profile", FetchMode.JOIN );
		criteria.setFetchMode( "adminAccess", FetchMode.JOIN );
	}

	@Override
	@Transactional( readOnly = true )
	public User findByFullname( String fullname )
	{
		Criteria criteria = createCriteria();
		criteria.add( Restrictions.sqlRestriction( "CONCAT({alias}.first_name, ' ', {alias}.last_name) ILIKE ?", '%' + fullname.trim() + '%', StandardBasicTypes.STRING ) );
		List list = criteria.list();
		return list.isEmpty() ? null : (User) list.get( 0 );
	}

	@Override
	@Transactional
	public User merge( User user )
	{
		Profile profile = user.getProfile();

		if( user.getId() != null )
		{
			User oldUser = (User) getSession().get( User.class, user.getId(), LockOptions.UPGRADE );
			if( oldUser != null )
			{
				// Update
				oldUser.update( user );
				if( oldUser.getProfile() == null )
				{
					profile.setUser( oldUser );
					oldUser.setProfile( profile );
					getSession().save( profile );
				}
				return oldUser;
			}
		}

		// Create
		user.setProfile( null );
		getSession().save( user );

		user.setProfile( profile );
		getSession().save( profile );

		return user;
	}

	@Override
	@Transactional
	public Attachment updateAvatar( Long userId, Attachment attachment )
	{
		User user = (User) getSession().get( User.class, userId, LockOptions.UPGRADE );
		Attachment avatar = user.getAvatar();
		if( avatar != null )
		{
			getSession().delete( attachment );
		}
		attachmentService.saveNewAttachment( attachment );
		user.setAvatar( attachment );

		return attachment;
	}

	@Override
	@Transactional
	public User addAdminAccess( Long userId, AdminRole adminRole )
	{
		Assert.isTrue( adminRole != null, "AdminRole is required" );

		User user = (User) getSession().get( User.class, userId, LockOptions.UPGRADE );
		if( user.getAdminAccess() == null )
		{
			// New AdminAccess
			AdminAccess access = new AdminAccess();
			access.setAdmin( user );
			access.setAdminRole( adminRole );

			adminAccessService.saveOrUpdate( access );

			user.setAdminAccess( access );
		}
		else
		{
			user.getAdminAccess().setAdminRole( adminRole );
		}
		return get( user.getId() );
	}

	@Override
	@Transactional
	public void updateAdminRole( Long userId, AdminRole adminRole )
	{
		User user = (User) getSession().get( User.class, userId, LockOptions.UPGRADE );
		AdminAccess access = user.getAdminAccess();
		if( access == null )
		{
			addValidationResult( "", "user.admin.access.not.exist" );
			validate();
		}
		user.getAdminAccess().setAdminRole( adminRole );
	}

	@Override
	@Transactional
	public void removeWithAdminRole( Long userId )
	{
		User user = (User) getSession().get( User.class, userId, LockOptions.UPGRADE );
		AdminAccess access = user.getAdminAccess();
		if( access == null )
		{
			addValidationResult( "", "user.admin.access.not.exist" );
			validate();
		}
		user.setAdminAccess( null );
		adminAccessService.delete( access.getId() );
	}
}

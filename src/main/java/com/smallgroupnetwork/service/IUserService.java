package com.smallgroupnetwork.service;

import com.smallgroupnetwork.model.AdminRole;
import com.smallgroupnetwork.model.Attachment;
import com.smallgroupnetwork.model.User;
import com.smallgroupnetwork.persistence.IPersistenceService;

/**
 * User: gleb
 * Date: 5/14/14
 * Time: 3:29 PM
 */
public interface IUserService extends IPersistenceService<User, Long>
{
	Attachment updateAvatar( Long userId, Attachment attachment );

	User findByFullname( String fullname );

	User addAdminAccess( Long userId, AdminRole adminRole );

	void updateAdminRole( Long userId, AdminRole adminRole );

	void removeWithAdminRole( Long userId );
}

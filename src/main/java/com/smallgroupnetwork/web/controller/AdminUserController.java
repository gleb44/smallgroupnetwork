package com.smallgroupnetwork.web.controller;

import com.smallgroupnetwork.model.User;
import com.smallgroupnetwork.persistence.IPersistenceService;
import com.smallgroupnetwork.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * User: gyazykov
 * Date: 5/16/14.
 * Time: 5:14 PM
 */
@Controller
@RequestMapping( Routes.ADMIN_USER )
public class AdminUserController extends AbstractReadOnlyController<User, Long>
{
	@Autowired
	private IUserService userService;

	@Override
	public IPersistenceService persistentService()
	{
		return userService;
	}
}

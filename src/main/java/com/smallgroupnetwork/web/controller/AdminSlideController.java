package com.smallgroupnetwork.web.controller;

import com.smallgroupnetwork.model.Slide;
import com.smallgroupnetwork.persistence.IPersistenceService;
import com.smallgroupnetwork.service.ISlideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.smallgroupnetwork.web.controller.Routes.ADMIN_CAROUSEL_SLIDE;

/**
 * User: gyazykov
 * Date: 5/16/14.
 * Time: 5:14 PM
 */
@Controller
@RequestMapping( ADMIN_CAROUSEL_SLIDE )
public class AdminSlideController extends AbstractRestFullController<Slide, Long>
{
	@Autowired
	private ISlideService slideService;

	public IPersistenceService<Slide, Long> persistentService()
	{
		return slideService;
	}

	@Override
	protected Slide store( Slide object )
	{
		return persistentService().merge( object );
	}
}

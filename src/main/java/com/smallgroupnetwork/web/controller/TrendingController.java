package com.smallgroupnetwork.web.controller;

import com.smallgroupnetwork.model.SearchRequest;
import com.smallgroupnetwork.persistence.Paging;
import com.smallgroupnetwork.service.ISearchRequestService;
import com.smallgroupnetwork.web.util.GridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * User: gyazykov
 * Date: 5/16/14.
 * Time: 5:14 PM
 */
@Controller
@RequestMapping( Routes.TRENDING )
public class TrendingController
{
	@Autowired
	private ISearchRequestService searchRequestService;

	@RequestMapping( value = { "/", "" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public GridResult<SearchRequest> readAll( Paging paging )
	{
		if( paging == null )
		{
			paging = new Paging();
		}
		paging.setSortColumn( "amount" );
		paging.setSortDesc( true );

		return new GridResult<>( searchRequestService.readAll( paging ), paging.getTotal() );
	}
}

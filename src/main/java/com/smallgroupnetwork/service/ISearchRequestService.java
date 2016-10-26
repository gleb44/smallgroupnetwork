package com.smallgroupnetwork.service;

import com.smallgroupnetwork.model.SearchRequest;
import com.smallgroupnetwork.persistence.IPersistenceService;

/**
 * User: gleb
 * Date: 5/23/14
 * Time: 4:36 PM
 */
public interface ISearchRequestService extends IPersistenceService<SearchRequest, String>
{
	void increment( String search );
}

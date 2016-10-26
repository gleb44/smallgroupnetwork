package com.smallgroupnetwork.web.controller;

import com.smallgroupnetwork.persistence.BaseEntity;
import com.smallgroupnetwork.persistence.IPersistenceService;
import com.smallgroupnetwork.persistence.Paging;
import com.smallgroupnetwork.web.exception.ResourceNotFoundException;
import com.smallgroupnetwork.web.util.GridResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;

/**
 * @author Reviakin Aleksey it.blackdog@gmail.com
 *         Date: 12/25/13
 *         Time: 7:13 PM
 */
public abstract class AbstractReadOnlyController<Z extends BaseEntity<I>, I extends Serializable>
{
	public abstract IPersistenceService<Z, I> persistentService();

	@RequestMapping( value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public Z read( @PathVariable I id )
	{
		Z entity = persistentService().read( id );
		if( entity == null )
		{
			throw new ResourceNotFoundException();
		}
		return entity;
	}

	@RequestMapping( value = { "/", "" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public GridResult<Z> readAll( final Paging paging )
	{
		return new GridResult<>( persistentService().readAll( paging ), paging.getTotal() );
	}
}

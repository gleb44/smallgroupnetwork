package com.smallgroupnetwork.web.controller;

import com.smallgroupnetwork.persistence.BaseEntity;
import com.smallgroupnetwork.web.util.DefaultMessages;
import com.smallgroupnetwork.web.util.OkResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;

/**
 * Date: 10/2/13
 * Time: 11:51 AM
 *
 * @author Reviakin Aleksey it.blackdog@gmail.com
 */
public abstract class AbstractRestFullController<Z extends BaseEntity<I>, I extends Serializable> extends AbstractReadOnlyController<Z, I>
{
	protected Z store( Z object )
	{
		persistentService().saveOrUpdate( object );
		return object;
	}

	@RequestMapping( value = { "/", "" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public Z create( @RequestBody Z object )
	{
		object.setId( null );
		Z storedEntity = store( object );
		return persistentService().read( storedEntity.getId() );
	}

	@RequestMapping( value = { "/", "" }, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public Z update( @RequestBody Z object )
	{
		Z storedEntity = store( object );
		return persistentService().read( storedEntity.getId() );
	}

	@RequestMapping( value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public OkResponse delete( @PathVariable I id )
	{
		persistentService().delete( id );
		return DefaultMessages.OK_RESPONSE;
	}
}

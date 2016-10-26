package com.smallgroupnetwork.persistence;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Abstract entity with automatically generated identifier.
 * Date: 13.02.12
 * Time: 14:30
 */
public class DynamicEntity<I extends Serializable> implements BaseEntity<I>
{
	protected I id;

	public I getId()
	{
		return id;
	}

	public void setId( I id )
	{
		this.id = id;
	}

	public static <T extends Serializable, M extends BaseEntity<T>> Set<T> convertToIdSet( Collection<M> target )
	{
		HashSet<T> result = new HashSet<>( target.size() );
		for( BaseEntity<T> entity : target )
		{
			T entityId = entity.getId();
			if( entityId != null )
			{
				result.add( entityId );
			}
		}
		return result;
	}


	public static <T extends Serializable, M extends BaseEntity<T>> Map<T, M> convertToIdMap( Collection<M> target )
	{
		Map<T, M> result = new HashMap<>( target.size() );
		for( M entity : target )
		{
			T entityId = entity.getId();
			if( entityId != null )
			{
				result.put( entityId, entity );
			}
		}
		return result;
	}
}

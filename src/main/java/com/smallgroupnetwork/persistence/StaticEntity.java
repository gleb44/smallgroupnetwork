package com.smallgroupnetwork.persistence;

import org.hibernate.annotations.Immutable;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Abstract read-only entity.
 * Date: 13.02.12
 * Time: 14:27
 *
 */
@Immutable
@MappedSuperclass
public abstract class StaticEntity<I extends Serializable> implements BaseEntity<I>
{
	protected I id;

	@Id
	public I getId()
	{
		return id;
	}

	public void setId( I id )
	{
		this.id = id;
	}
}

package com.smallgroupnetwork.persistence;

import java.io.Serializable;

/**
 * Base abstract entity
 * Date: 13.02.12
 * Time: 14:24
 */

//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY)
public interface BaseEntity<I extends Serializable> extends Serializable
{
	public I getId();

	void setId( I id );
}

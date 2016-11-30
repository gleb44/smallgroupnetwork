package com.smallgroupnetwork.model;

/**
 * User: gleb
 * Date: 3/25/15
 * Time: 6:11 PM
 */
public enum AdminRole
{
	Admin, Staff;

	public static AdminRole findByName( final String name )
	{
		for( AdminRole item : values() )
		{
			if( item.name().equals( name ) )
			{
				return item;
			}
		}
		return null;
	}
}

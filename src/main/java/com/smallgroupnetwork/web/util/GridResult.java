package com.smallgroupnetwork.web.util;

import java.io.Serializable;
import java.util.List;

/**
 * Date: 10/2/13
 * Time: 12:50 PM
 *
 * @author Reviakin Aleksey it.blackdog@gmail.com
 */
public class GridResult<T extends Serializable>
{
	private List<T> result;
	private Long count;

	public GridResult()
	{
	}

	public GridResult( List<T> result )
	{
		this.result = result;
		this.count = (long) result.size();
	}

	public GridResult( List<T> result, Long count )
	{
		this.result = result;
		this.count = count;
	}

	public List<T> getResult()
	{
		return result;
	}

	public void setResult( List<T> result )
	{
		this.result = result;
	}

	public Long getCount()
	{
		return count;
	}

	public void setCount( Long count )
	{
		this.count = count;
	}
}

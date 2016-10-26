package com.smallgroupnetwork.model;

import com.smallgroupnetwork.persistence.StaticEntity;

import javax.persistence.Entity;
import java.util.Date;

/**
 * User: gleb
 * Date: 7/22/16
 * Time: 2:11 PM
 */
@Entity
public class SearchRequest extends StaticEntity<String>
{
	private Date lastSearched = new Date();
	private int amount = 1;

	public Date getLastSearched()
	{
		return lastSearched;
	}

	public void setLastSearched( Date lastSearched )
	{
		this.lastSearched = lastSearched;
	}

	public int getAmount()
	{
		return amount;
	}

	public void setAmount( int amount )
	{
		this.amount = amount;
	}
}

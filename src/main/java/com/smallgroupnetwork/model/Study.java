package com.smallgroupnetwork.model;

import com.smallgroupnetwork.persistence.SyntheticEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

/**
 * User: gleb
 * Date: 5/21/14
 * Time: 5:55 PM
 */
@Entity
@DynamicUpdate
public class Study extends SyntheticEntity
{
	private Date created = new Date();
	private String title;
	private String description;
	private String speaker;
	private String link;
	private int startMin;
	private int startSec;
	private int viewsCount;

	@Column(nullable = false, updatable = false)
	public Date getCreated()
	{
		return created;
	}

	public void setCreated( Date created )
	{
		this.created = created;
	}

	@Column(nullable = false)
	public String getTitle()
	{
		return title;
	}

	public void setTitle( String title )
	{
		this.title = title;
	}

	@Column(length = 5120)
	public String getDescription()
	{
		return description;
	}

	public void setDescription( String description )
	{
		this.description = description;
	}

	public String getSpeaker()
	{
		return speaker;
	}

	public void setSpeaker( String speaker )
	{
		this.speaker = speaker;
	}

	@Column(nullable = false)
	public String getLink()
	{
		return link;
	}

	public void setLink( String link )
	{
		this.link = link;
	}

	public int getStartMin()
	{
		return startMin;
	}

	public void setStartMin( int startMin )
	{
		this.startMin = startMin;
	}

	public int getStartSec()
	{
		return startSec;
	}

	public void setStartSec( int startSec )
	{
		this.startSec = startSec;
	}

	public int getViewsCount()
	{
		return viewsCount;
	}

	public void setViewsCount( int viewsCount )
	{
		this.viewsCount = viewsCount;
	}
}

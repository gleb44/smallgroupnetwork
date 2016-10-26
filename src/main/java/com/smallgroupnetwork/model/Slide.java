package com.smallgroupnetwork.model;

import com.smallgroupnetwork.persistence.SyntheticEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * User: gleb
 * Date: 5/23/14
 * Time: 12:10 PM
 */
@Entity
public class Slide extends SyntheticEntity
{
	private Integer indexNumber;
	private String link;
	private Attachment attachment;

	@Column( nullable = false )
	public Integer getIndexNumber()
	{
		return indexNumber;
	}

	public void setIndexNumber( Integer indexNumber )
	{
		this.indexNumber = indexNumber;
	}

	public String getLink()
	{
		return link;
	}

	public void setLink( String link )
	{
		this.link = link;
	}

	@OneToOne( fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL, orphanRemoval = true )
	@JoinColumn( name = "id_attachment", foreignKey = @javax.persistence.ForeignKey( name = "fk_carousel_slide_attachment" ) )
	public Attachment getAttachment()
	{
		return attachment;
	}

	public void setAttachment( Attachment attachment )
	{
		this.attachment = attachment;
	}
}

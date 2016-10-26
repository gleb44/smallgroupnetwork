package com.smallgroupnetwork.model;

import com.smallgroupnetwork.persistence.SyntheticEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.File;
import java.io.InputStream;
import java.util.Date;

@Entity
@Table( name = "attachment" )
@JsonIgnoreProperties( ignoreUnknown = true )
@JsonTypeInfo( use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class", defaultImpl = Attachment.class )
public class Attachment extends SyntheticEntity implements Cloneable
{
	private Date uploaded = new Date();
	private String fileName;
	private String contentType;
	private String description;

	private InputStream inputStream;
	private String path;
	private Long duration;
	private long size;

	public Attachment()
	{
	}

	public Date getUploaded()
	{
		return uploaded;
	}

	public void setUploaded( Date uploaded )
	{
		this.uploaded = uploaded;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName( String fileName )
	{
		this.fileName = fileName;
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType( String mimeType )
	{
		this.contentType = mimeType;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription( String description )
	{
		this.description = description;
	}

	@Transient
	@JsonIgnore
	public InputStream getInputStream()
	{
		return inputStream;
	}

	public void setInputStream( InputStream inputStream )
	{
		this.inputStream = inputStream;
	}

	@Transient
	@JsonIgnore
	public File getFile()
	{
		if( path == null )
		{
			return null;
		}
		return new File( path );
	}

	public void setFile( File file )
	{
		this.path = file.getPath();
		this.size = file.length();
	}

	public Long getDuration()
	{
		return duration;
	}

	public void setDuration( Long duration )
	{
		this.duration = duration;
	}

	@Transient
	public String getPath()
	{
		return path;
	}

	public void setPath( String path )
	{
		this.path = path;
	}

	@Transient
	@JsonIgnore
	public long getSize()
	{
		return size;
	}

	public void setSize( long size )
	{
		this.size = size;
	}

	public void applyAttachment( Attachment attachment )
	{
		uploaded = new Date();
		fileName = attachment.getFileName();
		contentType = attachment.getContentType();

		inputStream = attachment.getInputStream();
		path = attachment.getPath();
	}
}

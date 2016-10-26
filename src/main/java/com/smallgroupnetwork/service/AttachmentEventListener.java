package com.smallgroupnetwork.service;

import com.smallgroupnetwork.model.Attachment;
import com.smallgroupnetwork.validation.ValidationException;
import com.smallgroupnetwork.validation.ValidationResult;
import org.hibernate.event.spi.PostCommitDeleteEventListener;
import org.hibernate.event.spi.PostCommitInsertEventListener;
import org.hibernate.event.spi.PostCommitUpdateEventListener;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;

/**
 * User: gleb
 * Date: 6/4/14
 * Time: 5:27 PM
 */
@Component
public class AttachmentEventListener implements PostCommitInsertEventListener, PostCommitUpdateEventListener, PostCommitDeleteEventListener, PreInsertEventListener
{
	private Logger logger = LoggerFactory.getLogger( this.getClass() );
	@Autowired
	private FileService fileService;

	@Override
	public void onPostInsertCommitFailed( PostInsertEvent event )
	{
		//	TODO: Implement this
	}

	@Override
	public void onPostUpdateCommitFailed( PostUpdateEvent event )
	{
		//		TODO: Implement this
	}

	@Override
	public void onPostDeleteCommitFailed( PostDeleteEvent event )
	{
		//		TODO: Implement this
	}

	@Override
	public boolean onPreInsert( PreInsertEvent event )
	{
		Object entity = event.getEntity();
		if( entity instanceof Attachment )
		{
			Attachment attachment = (Attachment) entity;
			if( attachment.getInputStream() == null && attachment.getFile() == null )
			{
				throw new ValidationException( Arrays.asList( new ValidationResult( "attachment", "attachment.concurrent.modification" ) ) );
			}
		}
		return false;
	}

	@Override
	public void onPostInsert( PostInsertEvent event )
	{
		Object entity = event.getEntity();
		Serializable id = event.getId();
		storeAttachmentFile( id, entity, "insert" );
	}

	@Override
	public void onPostUpdate( PostUpdateEvent event )
	{
		Object entity = event.getEntity();
		Serializable id = event.getId();
		storeAttachmentFile( id, entity, "update" );
	}

	private void storeAttachmentFile( Serializable id, Object entity, String logAction )
	{
		if( entity instanceof Attachment )
		{
			logger.debug( "On " + logAction + " attachment, id: " + id );
			try
			{

				Attachment attachment = (Attachment) entity;
				InputStream inputStream = attachment.getInputStream();
				if( inputStream != null )
				{
					logger.debug( "Storing attachment, id: " + id );
					fileService.storeAttachment( (Long) id, inputStream );
				}
				else
				{
					File file = attachment.getFile();
					if( file != null )
					{
						logger.debug( "Moving to attachment, id: " + id + " file: " + file );
						fileService.moveToAttachment( (Long) id, file );
					}
				}
			}
			catch( IOException e )
			{
				logger.error( "Error on " + logAction + " attachment, id: " + id, e );
			}
		}
	}

	@Override
	public void onPostDelete( PostDeleteEvent event )
	{
		Object entity = event.getEntity();
		Serializable id = event.getId();
		if( entity instanceof Attachment )
		{
			try
			{
				if( fileService.removeAttachment( (Long) id ) )
				{
					logger.debug( "On Delete attachment, id: " + id );
				}
				else
				{
					logger.error( "Can't Delete attachment, id: " + id );
				}
			}
			catch( Exception e )
			{
				logger.error( "Error while deleting attachment, id: " + id );
			}
		}
	}

	@Override
	public boolean requiresPostCommitHanding( EntityPersister persister )
	{
		return true;
	}
}

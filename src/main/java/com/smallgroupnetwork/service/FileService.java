package com.smallgroupnetwork.service;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * User: gleb
 * Date: 5/23/14
 * Time: 1:03 PM
 */

@Service
public class FileService
{
	@Value( "${root.file.path}" )
	private String rootPath;

	protected String attachmentPath = "/attachment";

	public File getAttachment( Long id )
	{
		File attachmentFolder = new File( rootPath + attachmentPath );
		if( !attachmentFolder.exists() )
		{
			attachmentFolder.mkdirs();
		}
		if( !attachmentFolder.isDirectory() )
		{
			throw new IllegalStateException( "Unexpected state (" + attachmentFolder + ") expected to be directory" );
		}
		return new File( attachmentFolder, id.toString() );
	}

	public void storeAttachment( Long id, InputStream inputStream ) throws IOException
	{
		File attachmentFile = getAttachment( id );
		FileUtils.copyInputStreamToFile( inputStream, attachmentFile );
	}

	public File createTempFile( InputStream inputStream ) throws IOException
	{
		Path tempFile = Files.createTempFile( null, null );
		Files.copy( inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING );
		return tempFile.toFile();
	}

	public void moveToAttachment( Long id, File file ) throws IOException
	{
		Files.move( file.toPath(), getAttachment( id ).toPath(), StandardCopyOption.REPLACE_EXISTING );
	}

	public boolean removeAttachment( Long id )
	{
		File attachmentFile = getAttachment( id );
		return attachmentFile.delete();
	}
}

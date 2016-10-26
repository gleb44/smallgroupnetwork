package com.smallgroupnetwork.web.util;

import com.smallgroupnetwork.model.Attachment;
import com.smallgroupnetwork.web.util.streaming.StreamingViewRenderer;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * User: gleb
 * Date: 5/23/14
 * Time: 6:07 PM
 */
public class AttachmentUtil
{
	public static Attachment createAttachment( MultipartFile file ) throws IOException
	{
		String filename = file.getOriginalFilename();
		Attachment attachment = new Attachment();
		attachment.setInputStream( file.getInputStream() );
		attachment.setUploaded( new Date() );
		attachment.setFileName( filename );
		attachment.setContentType( file.getContentType() );
		attachment.setSize( file.getSize() );
		return attachment;
	}

	public static View createViewForAttachment( File targetFile, Attachment attachment, HttpServletResponse response, WebRequest webRequest )
	{
		if( targetFile == null || !targetFile.exists() )
		{
			response.setStatus( HttpServletResponse.SC_NOT_FOUND );
			return null;
		}

		long lastModified = targetFile.lastModified();
		if( webRequest.checkNotModified( lastModified ) )
		{
			response.setStatus( HttpServletResponse.SC_NOT_MODIFIED );
			return null;
		}

		return new FileDataView( targetFile, attachment.getFileName(), attachment.getContentType() );
	}

	public static View createStreamingViewForAttachment( File targetFile, Attachment attachment )
	{
		return new StreamingViewRenderer( targetFile, attachment.getFileName(), attachment.getContentType() );
	}
}

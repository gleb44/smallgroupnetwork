package com.smallgroupnetwork.web.controller;

import com.smallgroupnetwork.model.Attachment;
import com.smallgroupnetwork.security.AccountHolder;
import com.smallgroupnetwork.service.FileService;
import com.smallgroupnetwork.service.IAttachmentService;
import com.smallgroupnetwork.web.util.AttachmentUtil;
import com.smallgroupnetwork.web.util.HttpSessionUtil;
import com.smallgroupnetwork.web.util.streaming.StreamingViewRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

import static com.smallgroupnetwork.web.controller.Routes.ADMIN_ATTACH;
import static com.smallgroupnetwork.web.controller.Routes.ATTACH;

/**
 * User: glebov
 * Date: 5/16/14.
 * Time: 5:14 PM
 */
@Controller
public class AttachmentController
{
	@Autowired
	private FileService fileService;
	@Autowired
	private IAttachmentService attachmentService;


	@RequestMapping( value = { ADMIN_ATTACH + "/", ADMIN_ATTACH + "" }, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public Attachment create( @RequestParam MultipartFile file, HttpSession session ) throws IOException
	{
		Attachment attachment = AttachmentUtil.createAttachment( file );
		File tempFile = fileService.createTempFile( file.getInputStream() );
		attachment.setFile( tempFile );
		HttpSessionUtil.getListAttribute( session, File.class, AccountHolder.FILES_KEY ).add( tempFile );
		return attachment;
	}

	@RequestMapping( value = { ADMIN_ATTACH + "/", ADMIN_ATTACH + "" }, method = RequestMethod.GET )
	public View getTempAttachment( Attachment attachment, HttpServletResponse response, WebRequest webRequest )
	{
		if( attachment == null || attachment.getFile() == null )
		{
			response.setStatus( HttpServletResponse.SC_NOT_FOUND );
			return null;
		}
		return AttachmentUtil.createViewForAttachment( attachment.getFile(), attachment, response, webRequest );
	}

	@RequestMapping( value = { ADMIN_ATTACH + "/{id}", ATTACH + "/{id}" }, method = RequestMethod.GET )
	public View getAttachment( @PathVariable Long id, HttpServletResponse response, WebRequest webRequest )
	{
		Attachment attachment = attachmentService.read( id );
		return getAttachmentView( id, response, webRequest, attachment );
	}

	private View getAttachmentView( Long id, HttpServletResponse response, WebRequest webRequest, Attachment attachment )
	{
		File file = fileService.getAttachment( id );
		if( attachment == null || file == null )
		{
			response.setStatus( HttpServletResponse.SC_NOT_FOUND );
			return null;
		}
		if( attachment.getContentType() != null && attachment.getContentType().startsWith( "video" ) )
		{
			return AttachmentUtil.createStreamingViewForAttachment( file, attachment );
		}
		else
		{
			return AttachmentUtil.createViewForAttachment( file, attachment, response, webRequest );
		}
	}

	@RequestMapping( value = { ATTACH + "/stream" }, method = RequestMethod.GET )
	public View getPublicStream( @RequestParam String path, HttpServletResponse response, HttpServletRequest request )
	{
		String realPath = request.getServletContext().getRealPath( "/img" + path );
		File file = new File( realPath );
		if( !file.exists() )
		{
			response.setStatus( HttpServletResponse.SC_NOT_FOUND );
			return null;
		}
		String contentType = request.getServletContext().getMimeType( realPath );
		return new StreamingViewRenderer( file, file.getName(), contentType );
	}
}

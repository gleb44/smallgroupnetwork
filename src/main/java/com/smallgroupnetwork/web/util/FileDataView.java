package com.smallgroupnetwork.web.util;

import org.apache.commons.io.FileUtils;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Map;

class FileDataView extends AbstractView
{
	private File file;
	private String outputFileName;

	FileDataView( File file, String outputFileName, String contentType )
	{
		this.file = file;
		this.outputFileName = outputFileName;
		setContentType( contentType );
	}

	@Override
	protected void renderMergedOutputModel( Map<String, Object> model, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		response.setContentType( getContentType() );

		if( outputFileName != null )
		{
			response.addHeader( "Content-Disposition", "inline" + ";filename=\"" + outputFileName + '\"' );
		}
		response.setContentLength( (int) file.length() );

		ServletOutputStream outputStream = response.getOutputStream();
		FileUtils.copyFile( file, outputStream );
		outputStream.flush();
		outputStream.close();
	}

}

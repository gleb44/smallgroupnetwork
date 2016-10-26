package com.smallgroupnetwork.web.listener;

import com.smallgroupnetwork.security.AccountHolder;
import com.smallgroupnetwork.web.util.HttpSessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.io.File;
import java.util.List;

/**
 * User: gleb
 * Date: 6/9/14
 * Time: 12:13 PM
 */
@WebListener( "sessionListener" )
public class SessionListener implements HttpSessionListener
{
	private final Logger logger = LoggerFactory.getLogger( this.getClass() );

	@Override
	public void sessionCreated( HttpSessionEvent se )
	{
		logger.debug( "Session created: " + se.getSession().getId() );
	}

	@Override
	public void sessionDestroyed( HttpSessionEvent se )
	{
		final HttpSession session = se.getSession();

		try
		{
			logger.debug( "Session destroyed: " + session.getId() );
			List<File> tempFiles = HttpSessionUtil.getListAttribute( session, File.class, AccountHolder.FILES_KEY );

			if( !CollectionUtils.isEmpty( tempFiles ) )
			{
				for( File tempFile : tempFiles )
				{
					if( tempFile.exists() )
					{
						if( tempFile.delete() )
						{
							logger.debug( "Deleted file: " + tempFile + " for session: " + session.getId() );
						}
						else
						{
							logger.debug( "Can't delete file: " + tempFile + " for session: " + session.getId() );
						}
					}
					else
					{
						logger.debug( "Skip file deletion: " + tempFile + " for session: " + session.getId() );
					}
				}
			}
		}
		catch( Exception ex )
		{
			logger.error( "Clean up temp files failed", ex );
		}
	}
}

package com.smallgroupnetwork.utils;

import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Priority;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.rolling.RollingFileAppender;
import org.apache.log4j.rolling.RollingPolicy;
import org.apache.log4j.rolling.TimeBasedRollingPolicy;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.OptionHandler;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RollingCleanAppender implements Appender, OptionHandler
{
	private RollingFileAppender rollingFileAppender = new RollingFileAppender();
	private int maxBackupIndex = -1;
	private String targetPath;
	private String fileNameFilter = null;
	private long nextCheck = -1;
	private static final Pattern LOG_FILE_DATE_PATTERN = Pattern.compile( "\\%d(\\{.+\\})?" );

	@Override
	public void activateOptions()
	{
		rollingFileAppender.activateOptions();
		TimeBasedRollingPolicy rollingPolicy = (TimeBasedRollingPolicy) rollingFileAppender.getRollingPolicy();
		if( rollingPolicy != null && fileNameFilter == null )
		{
			String separator = "/";
			String[] patternSplit = rollingPolicy.getFileNamePattern().split( separator );
			targetPath = StringUtils.join( patternSplit, separator, 0, patternSplit.length - 1 );
			fileNameFilter = patternSplit[patternSplit.length - 1];
			Matcher matcher = LOG_FILE_DATE_PATTERN.matcher( fileNameFilter );
			if( matcher.find() )
			{
				fileNameFilter = Pattern.quote( fileNameFilter.substring( 0, matcher.start() ) ) + ".+" + Pattern.quote( fileNameFilter.substring( matcher.end() ) );
			}
		}
	}

	@Override
	public void doAppend( LoggingEvent event )
	{
		rollingFileAppender.doAppend( event );
		if( maxBackupIndex >= 0 && fileNameFilter != null )
		{
			synchronized( this )
			{
				long time = System.currentTimeMillis();
				if( time > nextCheck )
				{
					try
					{
						nextCheck = time + (24 * 60 * 60 * 1000);
						File logDir = new File( targetPath );
						File[] files = logDir.listFiles( (FileFilter) new RegexFileFilter( fileNameFilter ) );
						if( files != null )
						{
							for( File file : files )
							{
								LogLog.debug( "Inspecting file: " + file.getName() );
								BasicFileAttributes fileAttributes = Files.readAttributes( file.toPath(), BasicFileAttributes.class );
								FileTime fileTime = fileAttributes.lastModifiedTime();
								long fileAge = TimeUnit.DAYS.convert( System.currentTimeMillis() - fileTime.toMillis(),
								                                      TimeUnit.MILLISECONDS );
								LogLog.debug( "File: +" + file.getName() + " was modified: " + fileAge + " days ago" );
								if( fileAge > maxBackupIndex )
								{
									boolean result = file.delete();
									LogLog.debug( "File: +" + file.getName() + " matches deletion rule, delete result: " + result );
								}
							}
						}


					}
					catch( Exception ex )
					{
						LogLog.error( "Failed to process files", ex );
					}
				}
			}
		}
	}

	public int getMaxBackupIndex()
	{
		return maxBackupIndex;
	}

	public void setMaxBackupIndex( int maxBackupIndex )
	{
		this.maxBackupIndex = maxBackupIndex;
	}

	public void setAppend( boolean flag )
	{
		rollingFileAppender.setAppend( flag );
	}

	public boolean getAppend()
	{
		return rollingFileAppender.getAppend();
	}

	public RollingPolicy getRollingPolicy()
	{
		return rollingFileAppender.getRollingPolicy();
	}

	public void setRollingPolicy( RollingPolicy policy )
	{
		rollingFileAppender.setRollingPolicy( policy );
	}

	public Priority getThreshold()
	{
		return rollingFileAppender.getThreshold();
	}

	public void setThreshold( Priority threshold )
	{
		rollingFileAppender.setThreshold( threshold );
	}

	@Override
	public void addFilter( Filter newFilter )
	{
		rollingFileAppender.addFilter( newFilter );
	}

	@Override
	public Filter getFilter()
	{
		return rollingFileAppender.getFilter();
	}

	@Override
	public void clearFilters()
	{
		rollingFileAppender.clearFilters();
	}

	@Override
	public void close()
	{
		rollingFileAppender.close();
	}

	@Override
	public String getName()
	{
		return rollingFileAppender.getName();
	}

	@Override
	public void setErrorHandler( ErrorHandler errorHandler )
	{
		rollingFileAppender.setErrorHandler( errorHandler );
	}

	@Override
	public ErrorHandler getErrorHandler()
	{
		return rollingFileAppender.getErrorHandler();
	}

	@Override
	public void setLayout( Layout layout )
	{
		rollingFileAppender.setLayout( layout );
	}

	@Override
	public Layout getLayout()
	{
		return rollingFileAppender.getLayout();
	}

	@Override
	public void setName( String name )
	{
		rollingFileAppender.setName( name );
	}

	@Override
	public boolean requiresLayout()
	{
		return rollingFileAppender.requiresLayout();
	}
}
package com.smallgroupnetwork.service.conversion.processors;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * User: gyazykov
 * Date: 10/8/15
 * Time: 6:36 PM
 */
final class StudyConversion
{
	private SimpleDateFormat dateFormat = new SimpleDateFormat( "M/d/yy", Locale.US );

	StudyConversion()
	{
		dateFormat.setTimeZone( TimeZone.getTimeZone( "UTC" ) );
	}

	SimpleDateFormat getDateFormat()
	{
		return (SimpleDateFormat) dateFormat.clone();
	}
}

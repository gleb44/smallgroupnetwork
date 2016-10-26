package com.smallgroupnetwork.service.conversion.processors;

/**
 * User: gyazykov
 * Date: 10/8/15
 * Time: 6:36 PM
 * <p/>
 * Day of week in ISO-8601 1-7 Mon-Sun
 */
public class DaysOfWeekConversion
{
	private static final String[] daysOfWeek = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
	private static final String[] daysOfWeekAbbreviation = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };

	private DaysOfWeekConversion()
	{
	}

	public static String dayNameByIndex( int index )
	{
		return daysOfWeek[index - 1];
	}

	public static String dayAbbreviationByIndex( int index )
	{
		return daysOfWeekAbbreviation[index - 1];
	}

	public static Integer dayIndexByName( String name )
	{
		for( int i = 0; i < daysOfWeek.length; i++ )
		{
			if( daysOfWeek[i].equalsIgnoreCase( name ) )
			{
				return (i + 1);
			}
		}
		return null;
	}

	public static Integer dayIndexByAbbreviation( String abbreviation )
	{
		for( int i = 0; i < daysOfWeekAbbreviation.length; i++ )
		{
			if( daysOfWeekAbbreviation[i].equalsIgnoreCase( abbreviation ) )
			{
				return (i + 1);
			}
		}
		return null;
	}

	public static Integer dayIndexByNameOrAbbreviation( String day )
	{
		Integer index = dayIndexByName( day );
		return (index != null) ? index : dayIndexByAbbreviation( day );
	}
}

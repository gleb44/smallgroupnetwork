package com.smallgroupnetwork.service.conversion.converters;

/**
 * User: gyazykov
 * Date: 21/4/15
 * Time: 5:32 PM
 */
public interface IExportWriter
{
	void writeRow( String[] col );

	void close();
}

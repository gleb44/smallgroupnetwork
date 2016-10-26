package com.smallgroupnetwork.service.conversion.converters;

import java.io.IOException;
import java.util.Map;

/**
 * User: gyazykov
 * Date: 21/4/15
 * Time: 5:32 PM
 */
public interface IImportReader
{
	Map<String, Integer> getHeader() throws IOException;

	String[] readNext() throws IOException;

	void close();
}

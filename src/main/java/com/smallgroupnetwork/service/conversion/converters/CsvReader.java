package com.smallgroupnetwork.service.conversion.converters;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * User: gyazykov
 * Date: 21/4/15
 * Time: 5:32 PM
 */
public class CsvReader implements IImportReader
{
	private CSVReader reader;

	public CsvReader( InputStream inputStream )
	{
		this.reader = new CSVReader( new InputStreamReader( inputStream ), CSVReader.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER );
	}

	@Override
	public Map<String, Integer> getHeader() throws IOException
	{
		String[] row = readNext();
		if( row == null )
		{
			return null;
		}

		Map<String, Integer> colNamesMap = new HashMap<>();
		for( int i = 0; i < row.length; i++ )
		{
			colNamesMap.put( row[i].trim(), i );
		}
		return colNamesMap;
	}

	@Override
	public String[] readNext() throws IOException
	{
		return reader.readNext();
	}

	@Override
	public void close()
	{
		try
		{
			this.reader.close();
		}
		catch( IOException ignored )
		{
		}
	}

}

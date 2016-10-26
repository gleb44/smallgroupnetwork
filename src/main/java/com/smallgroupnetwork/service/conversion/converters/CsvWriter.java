package com.smallgroupnetwork.service.conversion.converters;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * User: gyazykov
 * Date: 21/4/15
 * Time: 5:32 PM
 */
public class CsvWriter implements IExportWriter
{
	private CSVWriter writer;

	public CsvWriter( OutputStream outputStream )
	{
		this.writer = new CSVWriter( new OutputStreamWriter( outputStream ), CSVReader.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER );
	}

	@Override
	public void writeRow( String[] col )
	{
		this.writer.writeNext( col );
	}

	@Override
	public void close()
	{
		try
		{
			this.writer.close();
		}
		catch( IOException ignored )
		{
		}
	}

}

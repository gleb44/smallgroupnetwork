package com.smallgroupnetwork.service.conversion.converters;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: gyazykov
 * Date: 21/4/15
 * Time: 5:32 PM
 */
public class XlsxReader extends ExcelReader implements IImportReader
{
	public XlsxReader( InputStream inputStream ) throws IOException
	{
		super( inputStream, new XSSFWorkbook( inputStream ) );

		// TODO SXSSF is an API-compatible streaming extension of XSSF to be used when very large spreadsheets have to be produced, and heap space is limited.
	}
}

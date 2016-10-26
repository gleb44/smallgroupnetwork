package com.smallgroupnetwork.service.conversion.converters;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: gyazykov
 * Date: 21/4/15
 * Time: 5:32 PM
 */
public class XlsReader extends ExcelReader implements IImportReader
{
	public XlsReader( InputStream inputStream ) throws IOException
	{
		super( inputStream, new HSSFWorkbook( inputStream ) );
	}
}

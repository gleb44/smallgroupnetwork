package com.smallgroupnetwork.service.conversion.converters;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.OutputStream;

/**
 * User: gyazykov
 * Date: 21/4/15
 * Time: 5:32 PM
 */
public class XlsWriter extends ExcelWriter implements IExportWriter
{
	public XlsWriter( OutputStream outputStream )
	{
		super( outputStream, new HSSFWorkbook() );
	}
}

package com.smallgroupnetwork.service.conversion.converters;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;

/**
 * User: gyazykov
 * Date: 21/4/15
 * Time: 5:32 PM
 */
public class XlsxWriter extends ExcelWriter implements IExportWriter
{
	public XlsxWriter( OutputStream outputStream )
	{
		super( outputStream, new XSSFWorkbook() );

		// TODO SXSSF is an API-compatible streaming extension of XSSF to be used when very large spreadsheets have to be produced, and heap space is limited.
	}
}

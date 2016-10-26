package com.smallgroupnetwork.service.conversion.converters;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;

/**
 * User: gyazykov
 * Date: 21/4/15
 * Time: 5:32 PM
 */
public abstract class ExcelWriter implements IExportWriter
{
	protected OutputStream outputStream;
	protected Workbook workbook;
	protected Sheet sheet;
	protected int rowNum;

	public ExcelWriter( OutputStream outputStream, Workbook workbook )
	{
		this.outputStream = outputStream;

		this.workbook = workbook;
		this.sheet = this.workbook.createSheet();
		this.rowNum = sheet.getLastRowNum();
	}

	@Override
	public void writeRow( String[] col )
	{
		Row row = sheet.createRow( rowNum++ );
		int cellNum = 0;
		for( String colData : col )
		{
			Cell cell = row.createCell( cellNum++ );
			cell.setCellValue( colData );
		}
	}

	@Override
	public void close()
	{
		try
		{
			this.workbook.write( this.outputStream );
			this.outputStream.flush();
			this.outputStream.close();
			this.workbook.close();
		}
		catch( IOException ignored )
		{
		}
	}
}

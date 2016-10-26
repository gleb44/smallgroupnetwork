package com.smallgroupnetwork.service.conversion.converters;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * User: gyazykov
 * Date: 21/4/15
 * Time: 5:32 PM
 */
public abstract class ExcelReader implements IImportReader
{
	private final static Logger logger = LoggerFactory.getLogger( ExcelReader.class );

	protected InputStream inputStream;
	protected Workbook workbook;
	protected Sheet sheet;
	protected Iterator<Row> rowIterator;

	public ExcelReader( InputStream inputStream, Workbook workbook ) throws IOException
	{
		this.inputStream = inputStream;

		this.workbook = workbook;
		this.sheet = this.workbook.getSheetAt( 0 );
		this.rowIterator = this.sheet.iterator();
	}

	protected String[] nextRowAsArr()
	{
		if( !rowIterator.hasNext() )
		{
			return null;
		}

		List<String> colData = new ArrayList<>();

		Row row = rowIterator.next();
		Iterator<Cell> cellIterator = row.cellIterator();
		Cell cell;

		boolean allCellBlank = true;

		while( cellIterator.hasNext() )
		{
			cell = cellIterator.next();

			switch( cell.getCellType() )
			{
				case Cell.CELL_TYPE_STRING:
					colData.add( cell.getStringCellValue().trim() );
					allCellBlank = false;
					break;
				case Cell.CELL_TYPE_NUMERIC:
					colData.add( String.valueOf( cell.getNumericCellValue() ) );
					allCellBlank = false;
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					colData.add( String.valueOf( cell.getBooleanCellValue() ) );
					allCellBlank = false;
					break;
				case Cell.CELL_TYPE_BLANK:
					colData.add( null );
					break;
				default:
					logger.warn( "Unhandled col Type: " + cell.getCellType() );
			}
		}

		return (allCellBlank) ? null : colData.toArray( new String[colData.size()] );
	}

	@Override
	public Map<String, Integer> getHeader()
	{
		String[] row = readNext();
		if( row == null )
		{
			return null;
		}

		Map<String, Integer> colNamesMap = new HashMap<>();
		for( int i = 0; i < row.length; i++ )
		{
			colNamesMap.put( row[i], i );
		}
		return colNamesMap;
	}

	@Override
	public String[] readNext()
	{
		return nextRowAsArr();
	}

	@Override
	public void close()
	{
		try
		{
			this.inputStream.close();
			this.workbook.close();
		}
		catch( IOException ignored )
		{
		}
	}
}

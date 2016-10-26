package com.smallgroupnetwork.service.conversion;

import com.smallgroupnetwork.persistence.BaseEntity;
import com.smallgroupnetwork.persistence.ICriteriaModifier;
import com.smallgroupnetwork.persistence.Paging;
import com.smallgroupnetwork.persistence.IPartialResultCallback;
import com.smallgroupnetwork.service.conversion.converters.CsvReader;
import com.smallgroupnetwork.service.conversion.converters.CsvWriter;
import com.smallgroupnetwork.service.conversion.converters.IExportWriter;
import com.smallgroupnetwork.service.conversion.converters.IImportReader;
import com.smallgroupnetwork.service.conversion.converters.XlsReader;
import com.smallgroupnetwork.service.conversion.converters.XlsWriter;
import com.smallgroupnetwork.service.conversion.converters.XlsxReader;
import com.smallgroupnetwork.service.conversion.converters.XlsxWriter;
import com.smallgroupnetwork.service.conversion.processors.IDataProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: gyazykov
 * Date: 21/4/15
 * Time: 5:32 PM
 */
@Service
public class ConversionService implements IConversionService
{
	private static final int LIMIT = 50;

	private final Logger logger = LoggerFactory.getLogger( this.getClass() );

	private IImportReader createImportReader( FormatType fromType, InputStream inputStream ) throws IOException
	{
		switch( fromType )
		{
			case CSV:
				return new CsvReader( inputStream );
			case XLS:
				return new XlsReader( inputStream );
			case XLSX:
				return new XlsxReader( inputStream );
			default:
				throw new IllegalArgumentException( "Unknown FormatType: " + fromType );
		}
	}

	private IExportWriter createExportWriter( FormatType fromType, OutputStream outputStream )
	{
		switch( fromType )
		{
			case CSV:
				return new CsvWriter( outputStream );
			case XLS:
				return new XlsWriter( outputStream );
			case XLSX:
				return new XlsxWriter( outputStream );
			default:
				throw new IllegalArgumentException( "Unknown FormatType: " + fromType );
		}
	}

	@Override
	public <T extends BaseEntity<I>, I extends Serializable> void importData( IDataProcessor<T, I> dataProcessor, InputStream stream, FormatType fromType ) throws IOException
	{
		IImportReader reader = createImportReader( fromType, stream );

		final Map<String, Integer> colNamesMap = reader.getHeader();
		if( colNamesMap == null )
		{
			return;
		}

		String[] row;
		int count = 0;

		while( (row = reader.readNext()) != null )
		{
			try
			{
				dataProcessor.processImport( row, colNamesMap );
				++count;
			}
			catch( Exception e )
			{
				logger.warn( "Invalid Participant row", e );
			}
		}

		logger.debug( "Number of Processed Items: " + count );

		reader.close();
	}

	@Override
	public <T extends BaseEntity<I>, I extends Serializable> void exportData( final IDataProcessor<T, I> dataProcessor, final Set<String> exportCols,
		final ICriteriaModifier filter, ICriteriaModifier criteriaConditions, OutputStream stream, FormatType toType )
	{
		final IExportWriter writer = createExportWriter( toType, stream );

		writer.writeRow( dataProcessor.getHeader( exportCols ) );

		Paging paging = new Paging( 0, 0, "id", false );

		dataProcessor.findByConditions( criteriaConditions, paging, new IPartialResultCallback<T, I>()
		{
			@Override
			public int getPartSize()
			{
				return LIMIT;
			}

			@Override
			public void invoke( List<T> items )
			{
				Collection<Object[]> dataItems = dataProcessor.preparePartialResultData( items, filter );
				for( Object[] dataItem : dataItems )
				{
					String[] row = dataProcessor.itemToRow( dataItem, exportCols );
					if( row != null )
					{
						writer.writeRow( row );
					}
				}
			}
		} );

		logger.debug( "Number of Processed Items: " + paging.getTotal() );

		writer.close();
	}

}

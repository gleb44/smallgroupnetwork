package com.smallgroupnetwork.web.util;

import com.smallgroupnetwork.persistence.ICriteriaModifier;
import com.smallgroupnetwork.service.conversion.FormatType;
import com.smallgroupnetwork.service.conversion.IConversionService;
import com.smallgroupnetwork.service.conversion.processors.IDataProcessor;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Set;

/**
 * User: gyazykov
 * Date: 21/4/15
 * Time: 5:32 PM
 */
public class DocumentDataView extends AbstractView
{
	private IConversionService conversionService;
	private IDataProcessor dataProcessor;

	private String outputFileName;
	private FormatType outputFileType;
	private ICriteriaModifier conditions;
	private Set<String> exportCols;
	private ICriteriaModifier filter;

	public DocumentDataView( IConversionService conversionService, IDataProcessor dataProcessor, String outputFileName,
		FormatType outputFileType, ICriteriaModifier conditions, Set<String> exportCols, ICriteriaModifier filter )
	{
		this.conversionService = conversionService;
		this.dataProcessor = dataProcessor;

		this.outputFileName = outputFileName;
		this.outputFileType = outputFileType;
		this.conditions = conditions;
		this.exportCols = exportCols;
		this.filter = filter;

		if( FormatType.CSV.equals( this.outputFileType ) )
		{
			setContentType( "text/csv" );
		}
		else if( FormatType.XLS.equals( this.outputFileType ) || FormatType.XLSX.equals( this.outputFileType ) )
		{
			setContentType( "application/vnd.ms-excel" );
		}
		else
		{
			throw new IllegalArgumentException( "Unhandled document type: " + this.outputFileType );
		}
	}

	public DocumentDataView( IConversionService conversionService, IDataProcessor dataProcessor, String outputFileName,
		FormatType outputFileType, ICriteriaModifier conditions, Set<String> exportCols )
	{
		this( conversionService, dataProcessor, outputFileName, outputFileType, conditions, exportCols, null );
	}

	@Override
	protected void renderMergedOutputModel( Map<String, Object> model, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		response.setContentType( getContentType() );

		if( outputFileName != null )
		{
			response.setHeader( "Content-Disposition", "attachment; filename=\"" + outputFileName + "." + outputFileType + '\"' );
		}

		conversionService.exportData( dataProcessor, exportCols, filter, conditions, response.getOutputStream(), outputFileType );
	}
}

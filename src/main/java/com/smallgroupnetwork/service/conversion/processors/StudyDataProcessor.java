package com.smallgroupnetwork.service.conversion.processors;

import com.smallgroupnetwork.model.Study;
import com.smallgroupnetwork.persistence.ICriteriaModifier;
import com.smallgroupnetwork.persistence.IPersistenceService;
import com.smallgroupnetwork.service.IStudyService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * User: gyazykov
 * Date: 21/4/15
 * Time: 5:32 PM
 */
@Service
public class StudyDataProcessor extends AbstractDataProcessor<Study, Long>
{
	private static final String exportCreated = "studyCreated";
	private static final String exportTitle = "studyTitle";
	private static final String exportDescription = "studyDescription";
	private static final String exportSpeaker = "Speaker";
	private static final String exportLink = "studyLink";
	private static final String exportStartMin = "studyStartMin";
	private static final String exportStartSec = "studyStartSec";

	private static final Map<String, String> headerData = new LinkedHashMap<>();

	static
	{
		headerData.put( exportCreated, "Created" );
		headerData.put( exportTitle, "Title" );
		headerData.put( exportDescription, "Description" );
		headerData.put( exportSpeaker, "Speaker" );
		headerData.put( exportLink, "Video Link" );
		headerData.put( exportStartMin, "Start time (Min)" );
		headerData.put( exportStartSec, "Start time (sec)" );
	}

	private static final String importTitle = "Title";
	private static final String importDescription = "Description";
	private static final String importSpeaker = "Speaker";
	private static final String importLink = "Video Link";
	private static final String importStartMin = "Start time (Min)";
	private static final String importStartSec = "Start time (sec)";

	@Autowired
	private IStudyService studyService;

	@Override
	public IPersistenceService<Study, Long> persistentService()
	{
		return studyService;
	}

	@Override
	public String[] getHeader( Set<String> exportCols )
	{
		if( CollectionUtils.isEmpty( exportCols ) )
		{
			// All
			return headerData.entrySet().stream().map( Map.Entry::getValue ).toArray( String[]::new );
		}
		else
		{
			// Selected only
			return headerData.entrySet().stream().filter( entry -> exportCols.contains( entry.getKey() ) ).map( Map.Entry::getValue ).toArray( String[]::new );
		}
	}

	@Override
	public String[] itemToRow( Object[] dataItem, Set<String> exportCols )
	{
		StudyConversion conversion = new StudyConversion();

		Study study = (Study) dataItem[0];

		boolean all = CollectionUtils.isEmpty( exportCols );

		try
		{
			List<String> row = new ArrayList<>();
			if( all || exportCols.contains( exportCreated ) )
			{
				row.add( study.getCreated() == null ? null : conversion.getDateFormat().format( study.getCreated() ) );
			}
			if( all || exportCols.contains( exportTitle ) )
			{
				row.add( study.getTitle() );
			}
			if( all || exportCols.contains( exportDescription ) )
			{
				row.add( study.getDescription() );
			}
			if( all || exportCols.contains( exportSpeaker ) )
			{
				row.add( study.getSpeaker() );
			}
			if( all || exportCols.contains( exportLink ) )
			{
				row.add( study.getLink() );
			}
			if( all || exportCols.contains( exportStartMin ) )
			{
				row.add( String.valueOf( study.getStartMin() ) );
			}
			if( all || exportCols.contains( exportStartSec ) )
			{
				row.add( String.valueOf( study.getStartSec() ) );
			}
			return row.toArray( new String[row.size()] );
		}
		catch( Exception e )
		{
			logger.error( "Study export Error. Study id: " + study.getId(), e );
			return null;
		}
	}

	@Override
	public Study rowToItem( String[] row, Map<String, Integer> colNamesMap )
	{
		Study study = new Study();

		study.setCreated( new Date() );
		study.setTitle( getField( colNamesMap, row, importTitle ) );
		study.setDescription( getField( colNamesMap, row, importDescription ) );
		study.setSpeaker( getField( colNamesMap, row, importSpeaker ) );
		study.setLink( getField( colNamesMap, row, importLink ) );

		String startMinField = getField( colNamesMap, row, importStartMin );
		if( NumberUtils.isNumber( startMinField ) )
		{
			study.setStartMin( (int) Double.parseDouble( startMinField ) );
		}

		String startSecField = getField( colNamesMap, row, importStartSec );
		if( NumberUtils.isNumber( startSecField ) )
		{
			study.setStartSec( (int) Double.parseDouble( startSecField ) );
		}

		return study;
	}

	@Override
	public Collection<Object[]> preparePartialResultData( List<Study> items, ICriteriaModifier filter )
	{
		return items.stream().map( item -> new Object[] { item } ).collect( Collectors.toList() );
	}

	@Override
	public void processImport( String[] row, Map<String, Integer> colNamesMap )
	{
		Study item = rowToItem( row, colNamesMap );
		if( item != null )
		{
			persistentService().merge( item );
		}
	}

}

package com.smallgroupnetwork.web.controller;

import com.smallgroupnetwork.model.Study;
import com.smallgroupnetwork.model.dto.StudySearch;
import com.smallgroupnetwork.persistence.Paging;
import com.smallgroupnetwork.service.IStudyService;
import com.smallgroupnetwork.service.conversion.FormatType;
import com.smallgroupnetwork.service.conversion.IConversionService;
import com.smallgroupnetwork.service.conversion.processors.IDataProcessor;
import com.smallgroupnetwork.web.exception.ResourceNotFoundException;
import com.smallgroupnetwork.web.util.DefaultMessages;
import com.smallgroupnetwork.web.util.DocumentDataView;
import com.smallgroupnetwork.web.util.GridResult;
import com.smallgroupnetwork.web.util.OkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.View;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * User: gleb
 * Date: 12/5/13
 * Time: 12:25 PM
 */
@Controller
public class StudyController
{
	@Autowired
	private IStudyService studyService;
	@Autowired
	private IConversionService conversionService;
	@Autowired
	private IDataProcessor studyDataProcessor;

	@RequestMapping( value = { Routes.ADMIN_STUDY + "/", Routes.ADMIN_STUDY + "" }, method = RequestMethod.POST,
		produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public Study create( @RequestBody Study study )
	{
		study.setId( null );
		studyService.saveOrUpdate( study );
		return study;
	}

	@RequestMapping( value = { Routes.ADMIN_STUDY + "/", Routes.ADMIN_STUDY + "" }, method = RequestMethod.PUT,
		produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public Study update( @RequestBody Study study )
	{
		Study result = studyService.merge( study );
		return result;
	}

	@RequestMapping( value = { Routes.STUDY + "/views/{studyId}" }, method = RequestMethod.PUT,
		produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public OkResponse updateViewsCount( @PathVariable Long studyId )
	{
		studyService.incrementViewsCount( studyId );
		return DefaultMessages.OK_RESPONSE;
	}

	@RequestMapping( value = Routes.ADMIN_STUDY + "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public OkResponse delete( @PathVariable Long id )
	{
		studyService.delete( id );
		return DefaultMessages.OK_RESPONSE;
	}

	@RequestMapping( value = Routes.STUDY + "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public Study read( @PathVariable Long id )
	{
		Study entity = studyService.read( id );
		if( entity == null )
		{
			throw new ResourceNotFoundException();
		}
		return entity;
	}

	@RequestMapping( value = { Routes.STUDY + "/", Routes.STUDY + "" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public GridResult<Study> readAll( Paging paging )
	{
		if( paging == null )
		{
			paging = new Paging();
		}
		paging.setSortColumn( "created" );
		paging.setSortDesc( true );
		return new GridResult<>( studyService.readAll( paging ), paging.getTotal() );
	}

	@RequestMapping( value = { Routes.STUDY + "/find" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public GridResult<Study> findStudy( StudySearch search, Paging paging )
	{
		Assert.notNull( paging );
		List<Study> studies = studyService.findByFullText( search, paging );
		if( studies.isEmpty() )
		{
			//use pattern occurence search if nothing found by fulltext
			studies = studyService.findByPattern( search, paging );
		}
		return new GridResult<>( studies );
	}

	@RequestMapping( value = Routes.ADMIN_STUDY + "/import", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public OkResponse importStudy( @RequestParam MultipartFile file ) throws IOException
	{
		FormatType type = FormatType.valueOf( file.getOriginalFilename().split( "\\." )[1].toUpperCase() );
		conversionService.importData( studyDataProcessor, file.getInputStream(), type );
		return DefaultMessages.OK_RESPONSE;
	}

	@RequestMapping( value = Routes.ADMIN_STUDY + "/export", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
	public View exportGroups( StudySearch search, @RequestParam( required = false ) Set<String> exportCols ) throws IOException
	{
		return new DocumentDataView( conversionService, studyDataProcessor, "study", FormatType.XLS, search, exportCols );
	}
}

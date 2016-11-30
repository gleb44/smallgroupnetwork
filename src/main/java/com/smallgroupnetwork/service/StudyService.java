package com.smallgroupnetwork.service;

import com.smallgroupnetwork.model.Study;
import com.smallgroupnetwork.model.dto.StudySearch;
import com.smallgroupnetwork.model.dto.StudySort;
import com.smallgroupnetwork.persistence.AbstractPersistenceService;
import com.smallgroupnetwork.persistence.Paging;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

/**
 * User: gleb
 * Date: 5/14/14
 * Time: 3:30 PM
 */
@Service
public class StudyService extends AbstractPersistenceService<Study, Long> implements IStudyService
{
	@Override
	@Transactional
	public void incrementViewsCount( Long studyId )
	{
		getSession().getNamedQuery( "Study.incrementViewsCount" ).setParameter( "studyId", studyId ).executeUpdate();
	}

	@Override
	@Transactional( readOnly = true )
	public List<Study> findByFullText( StudySearch search, Paging paging )
	{
		String pattern = search.getPattern();
		if( StringUtils.isEmpty( pattern ) )
		{
			return Collections.emptyList();
		}

		String[] words = pattern.split( "\\W|_" );
		if( words.length == 0 )
		{
			return Collections.emptyList();
		}
		StringBuilder sb = new StringBuilder();
		for( String word : words )
		{
			if( word.length() == 0 )
			{
				continue;
			}
			if( sb.length() > 0 )
			{
				sb.append( " | " );
			}
			sb.append( word );
		}

		SQLQuery query = (SQLQuery) getSession().getNamedQuery( "Study.fullTextSearch" );
		StringBuilder queryString = new StringBuilder( query.getQueryString() );
		if( search.getSpeaker() != null )
		{
			queryString.append( "\n            AND speaker = :speaker" );
		}
		String sortMode = paging.getSortColumn();
		if( StudySort.MostRecent.name().equals( sortMode ) )
		{
			queryString.append( "\n            ORDER BY created DESC" );
		}
		else if( StudySort.MostPopular.name().equals( sortMode ) )
		{
			queryString.append( "\n            ORDER BY views_count DESC" );
		}
		else
		{
			queryString.append( "\n            ORDER BY ts_rank(p_search.document, to_tsquery(:searchPattern)) DESC" );
		}

		query = getSession().createSQLQuery( queryString.toString() );
		query.addEntity( Study.class );
		query.setParameter( "searchPattern", sb.toString() );
		if( search.getSpeaker() != null )
		{
			query.setParameter( "speaker", search.getSpeaker() );
		}
		if( paging.getOffset() != null )
		{
			query.setFirstResult( paging.getOffset() );
		}
		if( paging.getLimit() != null )
		{
			query.setMaxResults( paging.getLimit() );
		}
		return (List<Study>) query.list();
	}

	@Transactional( readOnly = true )
	public List<Study> findByPattern( StudySearch search, Paging paging )
	{
		String sortMode = paging.getSortColumn();
		if( StudySort.MostPopular.name().equals( sortMode ) )
		{
			paging.setSortColumn( "viewsCount" );
		}
		else
		{
			paging.setSortColumn( "created" );  //sort by most recent for all other cases
		}

		paging.setSortDesc( true );
		return findBy( search, paging );
	}
}

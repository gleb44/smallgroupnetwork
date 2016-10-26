package com.smallgroupnetwork.service;

import com.smallgroupnetwork.model.Study;
import com.smallgroupnetwork.model.dto.StudySearch;
import com.smallgroupnetwork.persistence.IPersistenceService;
import com.smallgroupnetwork.persistence.Paging;

import java.util.List;

/**
 * User: gleb
 * Date: 5/14/14
 * Time: 3:29 PM
 */
public interface IStudyService extends IPersistenceService<Study, Long>
{
	List<Study> findByFullText( StudySearch search, Paging paging );

	List<Study> findByPattern( StudySearch search, Paging paging );

	void incrementViewsCount( Long studyId );
}


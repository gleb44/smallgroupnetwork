package com.smallgroupnetwork.service;

import com.smallgroupnetwork.model.Attachment;
import com.smallgroupnetwork.persistence.IPersistenceService;

/**
 * User: gleb
 * Date: 6/9/14
 * Time: 4:38 PM
 */
public interface IAttachmentService extends IPersistenceService<Attachment, Long>
{
	void saveNewAttachment( Attachment attachment );
}

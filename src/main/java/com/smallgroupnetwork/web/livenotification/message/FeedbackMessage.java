package com.smallgroupnetwork.web.livenotification.message;

import com.smallgroupnetwork.web.websocket.mesaging.message.BaseMessage;

/**
 * User: gyazykov
 * Date: 02/19/16.
 * Time: 11:30 AM
 */
public class FeedbackMessage extends BaseMessage
{
	private FeedbackType feedbackType;
	private Long groupId;
	private Long[] ids;

	public FeedbackType getFeedbackType()
	{
		return feedbackType;
	}

	public void setFeedbackType( FeedbackType feedbackType )
	{
		this.feedbackType = feedbackType;
	}

	public Long getGroupId()
	{
		return groupId;
	}

	public void setGroupId( Long groupId )
	{
		this.groupId = groupId;
	}

	public Long[] getIds()
	{
		return ids;
	}

	public void setIds( Long[] ids )
	{
		this.ids = ids;
	}
}

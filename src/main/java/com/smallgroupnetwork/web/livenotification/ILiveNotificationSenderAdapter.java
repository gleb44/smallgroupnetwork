package com.smallgroupnetwork.web.livenotification;

/**
 * User: gyazykov
 * Date: 1/02/16.
 * Time: 3:14 PM
 */

public interface ILiveNotificationSenderAdapter extends ILiveNotificationSender
{
	void setProxy( ILiveNotificationSender proxy );
}

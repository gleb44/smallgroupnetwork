package com.smallgroupnetwork.persistence;

import java.io.Serializable;
import java.util.List;

/**
 * User: gyazykov
 * Date: 21/4/15
 * Time: 5:32 PM
 */
public interface IPartialResultCallback<T extends BaseEntity<I>, I extends Serializable>
{
	int getPartSize();

	void invoke( List<T> items );
}

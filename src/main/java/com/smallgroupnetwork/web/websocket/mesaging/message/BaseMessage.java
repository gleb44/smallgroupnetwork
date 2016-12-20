package com.smallgroupnetwork.web.websocket.mesaging.message;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

/**
 * User: gyazykov
 * Date: 1/02/16.
 * Time: 3:14 PM
 */

@JsonTypeInfo(
	use = JsonTypeInfo.Id.CLASS,
	include = JsonTypeInfo.As.PROPERTY,
	property = "@class",
	visible = true
)
public abstract class BaseMessage implements Serializable
{
}

package com.smallgroupnetwork.web.util;


import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class HttpSessionUtil
{
	public static <T> List<T> getListAttribute( HttpSession httpSession, Class<T> type, String key )
	{
		List<T> attributeValue = (List<T>) httpSession.getAttribute( key );
		if( attributeValue == null )
		{
			attributeValue = new ArrayList<>();
			httpSession.setAttribute( key, attributeValue );
		}
		return attributeValue;
	}
}

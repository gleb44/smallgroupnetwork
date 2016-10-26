package com.smallgroupnetwork.web.serialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

/**
 * Date: 10/2/13
 * Time: 11:34 AM
 *
 * @author Reviakin Aleksey it.blackdog@gmail.com
 */
public class HibernateAwareObjectMapper extends ObjectMapper
{
	public HibernateAwareObjectMapper()
	{
		Hibernate4Module h4m = new Hibernate4Module();
		h4m.disable( Hibernate4Module.Feature.USE_TRANSIENT_ANNOTATION );
		registerModule( h4m );

		SimpleModule antiCyclicModule = new CyclicAvoidModule();
		registerModule( antiCyclicModule );

		configure( SerializationFeature.FAIL_ON_EMPTY_BEANS, false );
		configure( SerializationFeature.INDENT_OUTPUT, true );
		configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
	}
}

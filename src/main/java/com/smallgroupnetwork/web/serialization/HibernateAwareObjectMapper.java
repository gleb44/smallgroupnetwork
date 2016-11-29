package com.smallgroupnetwork.web.serialization;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

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
		Hibernate5Module h5m = new Hibernate5Module();
		h5m.disable( Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION );
		registerModule( h5m );

		// TODO Check
		SimpleModule antiCyclicModule = new CyclicAvoidModule();
		registerModule( antiCyclicModule );

		configure( SerializationFeature.FAIL_ON_EMPTY_BEANS, false );
		configure( SerializationFeature.INDENT_OUTPUT, true );
		configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
	}
}

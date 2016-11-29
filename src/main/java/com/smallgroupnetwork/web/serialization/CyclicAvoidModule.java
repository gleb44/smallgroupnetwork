package com.smallgroupnetwork.web.serialization;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

/**
 * @author Reviakin Aleksey it.blackdog@gmail.com
 *         Date: 12/4/13
 *         Time: 7:55 PM
 */
public class CyclicAvoidModule extends SimpleModule
{

	public CyclicAvoidModule()
	{
		super( "CyclicAvoidModule", new Version( 1, 0, 0, null, "com.smallgroupnetwork.web", "cyclic" ) );
	}

	@Override
	public void setupModule( SetupContext context )
	{
		super.setupModule( context );
		context.addBeanSerializerModifier( new BeanSerializerModifier()
		{
			@Override
			public JsonSerializer<?> modifySerializer( SerializationConfig config, BeanDescription beanDesc, JsonSerializer<?> serializer )
			{
				return new CustomBeanSerializer( serializer );
			}
		} );
	}
}

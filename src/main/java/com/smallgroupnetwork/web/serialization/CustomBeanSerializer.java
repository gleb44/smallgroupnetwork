package com.smallgroupnetwork.web.serialization;

import com.smallgroupnetwork.persistence.BaseEntity;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Date: 10/2/13
 * Time: 11:41 AM
 *
 * @author Reviakin Aleksey it.blackdog@gmail.com
 */
public class CustomBeanSerializer extends JsonSerializer
{
	private JsonSerializer parentSerializer;

	private static ThreadLocal<Set<Object>> serializedObjects = new ThreadLocal<Set<Object>>()
	{
		@Override
		protected Set<Object> initialValue()
		{
			return new HashSet<>();
		}
	};

	public CustomBeanSerializer( JsonSerializer parentSerializer )
	{
		this.parentSerializer = parentSerializer;
	}

	@Override
	public void serialize( Object value, JsonGenerator jgen, SerializerProvider provider ) throws IOException
	{
		serializeInner( value, jgen, provider, null );
	}

	@Override
	public void serializeWithType( Object value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer ) throws IOException
	{
		serializeInner( value, jgen, provider, typeSer );
	}


	private void serializeInner( Object value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer ) throws IOException
	{
		Set<Object> objects = serializedObjects.get();
		if( !objects.contains( value ) )
		{
			try
			{
				objects.add( value );
				if( isLazyEntity( value ) )
				{
					serializeLazy( value, jgen, provider );
				}
				else
				{
					if( typeSer != null )
					{
						parentSerializer.serializeWithType( value, jgen, provider, typeSer );
					}
					else
					{
						parentSerializer.serialize( value, jgen, provider );
					}
				}
			}
			finally
			{
				objects.remove( value );
			}
		}
		else
		{
			writeObjectReferenceOrNull( value, jgen, provider );
		}
	}


	private boolean isLazyEntity( Object value )
	{
		if( value instanceof HibernateProxy )
		{
			HibernateProxy proxy = (HibernateProxy) value;
			return proxy.getHibernateLazyInitializer().isUninitialized();
		}
		return false;
	}

	private void serializeLazy( Object value, JsonGenerator jgen, SerializerProvider provider ) throws IOException
	{
		LazyInitializer initializer = ((HibernateProxy) value).getHibernateLazyInitializer();
		jgen.writeStartObject();
		provider.defaultSerializeField( "id", initializer.getIdentifier(), jgen );
		jgen.writeEndObject();
	}

	private void writeObjectReferenceOrNull( Object value, JsonGenerator jgen, SerializerProvider provider ) throws IOException
	{
		if( value instanceof BaseEntity )
		{
			jgen.writeStartObject();
			provider.defaultSerializeField( "id", ((BaseEntity) value).getId(), jgen );
			jgen.writeEndObject();
		}
		else
		{
			jgen.writeNull();
		}
	}
}

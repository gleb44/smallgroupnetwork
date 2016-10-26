package com.smallgroupnetwork.persistence.generator;

import org.hibernate.cfg.ObjectNameNormalizer;
import org.hibernate.dialect.Dialect;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.mapping.Table;

import java.util.Properties;

/**
 * User: gyazykov
 * Date: 21/4/15
 * Time: 5:32 PM
 */
public class TableSequenceStyleGenerator extends SequenceStyleGenerator
{
	@Override
	protected String determineSequenceName( Properties params, Dialect dialect )
	{
		String sequencePerEntitySuffix = ConfigurationHelper.getString( CONFIG_SEQUENCE_PER_ENTITY_SUFFIX, params, DEF_SEQUENCE_SUFFIX );
		// JPA_ENTITY_NAME value honors <class ... entity-name="..."> (HBM) and @Entity#name (JPA) overrides.
		String sequenceName = ConfigurationHelper.getBoolean( CONFIG_PREFER_SEQUENCE_PER_ENTITY, params, false )
		                      ? params.getProperty( TABLE ) + sequencePerEntitySuffix
		                      : DEF_SEQUENCE_NAME;
		ObjectNameNormalizer normalizer = ( ObjectNameNormalizer ) params.get( IDENTIFIER_NORMALIZER );
		sequenceName = ConfigurationHelper.getString( SEQUENCE_PARAM, params, sequenceName );
		if ( sequenceName.indexOf( '.' ) < 0 ) {
			sequenceName = normalizer.normalizeIdentifierQuoting( sequenceName );
			String schemaName = params.getProperty( SCHEMA );
			String catalogName = params.getProperty( CATALOG );
			sequenceName = Table.qualify(
				dialect.quote( catalogName ),
				dialect.quote( schemaName ),
				dialect.quote( sequenceName )
			                            );
		}
		else {
			// if already qualified there is not much we can do in a portable manner so we pass it
			// through and assume the user has set up the name correctly.
		}
		return sequenceName;
	}
}

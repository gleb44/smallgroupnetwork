package com.smallgroupnetwork.test.util;

import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.EnumSet;

/**
 * User: gyazykov
 * Date: 11/29/2016
 * Time: 1:20 PM
 */
public class DdlGenerator
{
	private static final String CREATE_PATH = "./db/db-schema-create.sql";
	private static final String DROP_PATH = "./db/db-schema-drop.sql";

	public static void main( String[] args ) throws IOException
	{
		clear();
		generate();
	}

	private static void clear() throws IOException
	{
		Files.deleteIfExists( Paths.get( CREATE_PATH ) );
		Files.deleteIfExists( Paths.get( DROP_PATH ) );
	}

	private static SchemaExport createSchemaExport()
	{
		return new SchemaExport()
			.setDelimiter( ";" )
			.setFormat( true )
			.setManageNamespaces( true )
			.setHaltOnError( true );
	}

	private static void generate()
	{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext( "/test-ddl-generator-config.xml" );

		SchemaExport schemaExport = createSchemaExport().setOutputFile( CREATE_PATH );
		schemaExport.createOnly( EnumSet.of( TargetType.SCRIPT ), HibernateInfoHolder.getMetadata() );

		SchemaExport schemaDrop = createSchemaExport().setOutputFile( DROP_PATH );
		schemaDrop.drop( EnumSet.of( TargetType.SCRIPT ), HibernateInfoHolder.getMetadata() );
	}
}

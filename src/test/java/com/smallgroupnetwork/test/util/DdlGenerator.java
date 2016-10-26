package com.smallgroupnetwork.test.util;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

/**
 * User: gleb
 * Date: 10/1/13
 * Time: 2:28 PM
 */
public class DdlGenerator
{
	public static void main( String[] args )
	{
		generate();
	}

	private static void generate()
	{
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext( "/test-ddl-generator-config.xml" );

		LocalSessionFactoryBean sessionFactoryBean = (LocalSessionFactoryBean) context.getBean( "&sessionFactory" );
		Configuration cfg = sessionFactoryBean.getConfiguration();

		SchemaExport schemaExport = new SchemaExport( cfg ).setOutputFile( "./db/db-schema-create.sql" ).setDelimiter( ";" );
		schemaExport.execute( true, false, false, true );

		SchemaExport schemaDrop = new SchemaExport( cfg ).setOutputFile( "./db/db-schema-drop.sql" ).setDelimiter( ";" );
		schemaDrop.drop( true, false );
	}
}

package com.smallgroupnetwork.persistence;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * User: gleb
 * Date: 10/9/13
 * Time: 7:11 PM
 */
@MappedSuperclass
public class SyntheticEntity extends DynamicEntity<Long>
{
	@Id
	@GenericGenerator(
		name = "sequence",
		strategy = "com.smallgroupnetwork.persistence.generator.TableSequenceStyleGenerator",
//		strategy = "enhanced-sequence",                                             // org.hibernate.id.enhanced.SequenceStyleGenerator
		parameters = {
			@Parameter( name = "prefer_sequence_per_entity", value = "true" ),
			@Parameter( name = "sequence_per_entity_suffix", value = "_seq" ),
//			@Parameter( name = "optimizer", value = "none" ),                       // "none", "hilo", "legacy-hilo", "pooled", "pooled-lo". Default 'none'
//			@Parameter( name = "increment_size", value = "1" )                      // Default 1
		} )
	@GeneratedValue( generator = "sequence", strategy = GenerationType.SEQUENCE )
//	@GeneratedValue() MySQl
	public Long getId()
	{
		return id;
	}
}

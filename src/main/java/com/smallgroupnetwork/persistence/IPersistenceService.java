package com.smallgroupnetwork.persistence;

import org.hibernate.Criteria;

import java.io.Serializable;
import java.util.List;

/**
 * User: gleb
 * Date: 2/16/12
 * Time: 4:37 PM
 */
public interface IPersistenceService<T extends BaseEntity<I>, I extends Serializable>
{
	Class<T> getEntityClass();

	Class<I> getIdClass();

	void saveOrUpdate( T object );

	T merge( T object );

	void delete( T entity );

	void delete( I id );

	T read( I id );

	T load( I id );

	T get( I id );

	List<T> readAll( Paging paging );

	List<T> readAll();

	long countAll();

	List<T> findByPattern( String pattern, Paging paging );

	long findCountByPattern( String pattern );

	long findCountBy( ICriteriaModifier modifier );

	List<T> findBy( ICriteriaModifier modifier, Paging paging );

	long findCountBy( Criteria criteria, ICriteriaModifier modifier );

	List findBy( Criteria criteria, ICriteriaModifier modifier, Paging paging );

	void findBy( ICriteriaModifier conditions, Paging paging, IPartialResultCallback<T, I> callback );
}

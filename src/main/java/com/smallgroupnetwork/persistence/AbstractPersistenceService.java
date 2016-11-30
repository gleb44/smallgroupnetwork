package com.smallgroupnetwork.persistence;

import com.smallgroupnetwork.validation.ValidationException;
import com.smallgroupnetwork.validation.ValidationResult;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.query.Query;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.Embedded;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * User: gleb
 * Date: 2/14/12
 * Time: 5:52 PM
 */
public class AbstractPersistenceService<T extends BaseEntity<I>, I extends Serializable> implements IPersistenceService<T, I>
{
	private static final ThreadLocal<List<ValidationResult>> validationResultsHolder = new ThreadLocal<List<ValidationResult>>();
	private Class<T> clazz;
	private Class<I> idClass;
	protected SessionFactory sessionFactory;

	@PostConstruct
	public void init()
	{
		this.clazz = getParameterClass( 0 );
//		this.idClass = getParameterClass(1);
	}

	protected Class getParameterClass( int i )
	{
		Type entityType = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[i];
		return entityType instanceof ParameterizedType ? (Class) ((ParameterizedType) entityType).getRawType() : (Class) entityType;
	}

	@Autowired
	public void setSessionFactory( SessionFactory sessionFactory )
	{
		this.sessionFactory = sessionFactory;
	}

	public Session getSession()
	{
		return sessionFactory.getCurrentSession();
	}

	@Override
	public Class<T> getEntityClass()
	{
		return clazz;
	}

	@Override
	public Class<I> getIdClass()
	{
		return idClass;
	}

	protected void addValidationResult( String field, String messageCode, Object... params )
	{
		ValidationResult validationResult = new ValidationResult();
		validationResult.setField( field );
		validationResult.setMessageCode( messageCode );
		validationResult.setParameters( params );

		List<ValidationResult> validationResults = validationResultsHolder.get();
		if( validationResults == null )
		{
			validationResults = new LinkedList<ValidationResult>();
			validationResultsHolder.set( validationResults );
		}
		validationResults.add( validationResult );
	}

	protected boolean hasErrors()
	{
		List<ValidationResult> validationResults = validationResultsHolder.get();
		return validationResults != null && !validationResults.isEmpty();
	}

	protected void validate() throws ValidationException
	{
		if( hasErrors() )
		{
			List<ValidationResult> validationResults = validationResultsHolder.get();
			validationResultsHolder.remove();
			throw new ValidationException( validationResults );
		}
	}

	protected Criteria createCriteria()
	{
		return getSession().createCriteria( clazz );
	}

	@Transactional
	public void saveOrUpdate( T object )
	{
		validate();
		getSession().saveOrUpdate( object );
	}

	@Transactional
	public T merge( T object )
	{
		validate();
		return (T) getSession().merge( object );
	}

	@Transactional
	public void delete( T entity )
	{
		getSession().delete( entity );
	}

	@Transactional
	public void delete( I id )
	{
		delete( load( id ) );
	}

	@Transactional( readOnly = true )
	public T read( I id )
	{
		Criteria criteria = createCriteria().add( Restrictions.idEq( id ) );
		applyFetching4Edit( criteria );
		return (T) criteria.uniqueResult();
	}

	@Transactional( readOnly = true )
	public T load( I id )
	{
		return (T) getSession().load( clazz, id );
	}

	@Transactional( readOnly = true )
	public T get( I id )
	{
		return (T) getSession().get( clazz, id );
	}

	@Transactional( readOnly = true )
	public List<T> readAll( final Paging paging )
	{
		if( paging != null )
		{
			paging.setTotal( countAll() );
		}
		Criteria criteria = createCriteria();
		applyFetching( criteria );
		applyPaging( criteria, paging );
		criteria.setResultTransformer( Criteria.DISTINCT_ROOT_ENTITY );
		return criteria.list();
	}

	@Transactional( readOnly = true )
	public List<T> readAll()
	{
		return readAll( null );
	}

	@Transactional( readOnly = true )
	public long countAll()
	{
		Criteria criteria = createCriteria();
		criteria.setProjection( Projections.rowCount() );
		return (Long) criteria.uniqueResult();
	}

	@Transactional( readOnly = true )
	public List<T> findByPattern( final String pattern, final Paging paging )
	{
		if( paging != null )
		{
			paging.setTotal( findCountByPattern( pattern ) );
		}
		Criteria criteria = createCriteria();
		applyFilter( criteria, pattern );
		applyFetching( criteria );
		applyPaging( criteria, paging );
		criteria.setResultTransformer( Criteria.DISTINCT_ROOT_ENTITY );
		return ((List<T>) criteria.list());
	}

	@Transactional( readOnly = true )
	public long findCountByPattern( final String pattern )
	{
		Criteria criteria = createCriteria();
		applyFilter( criteria, pattern );
		criteria.setProjection( Projections.rowCount() );
		return ((Long) criteria.uniqueResult());
	}

	@Transactional( readOnly = true )
	public long findCountBy( Criteria criteria, ICriteriaModifier modifier )
	{
		modifier.modify( criteria );
		criteria.setProjection( Projections.rowCount() );
		return ((Long) criteria.uniqueResult());
	}

	@Transactional( readOnly = true )
	public List findBy( Criteria criteria, ICriteriaModifier modifier, Paging paging )
	{
		modifier.modify( criteria );
		applyFetching( criteria );
		applyPaging( criteria, paging );
		criteria.setResultTransformer( Criteria.DISTINCT_ROOT_ENTITY );
		return criteria.list();
	}

	@Override
	@Transactional( readOnly = true )
	public long findCountBy( ICriteriaModifier modifier )
	{
		return findCountBy( createCriteria(), modifier );
	}

	@Override
	@Transactional( readOnly = true )
	public List<T> findBy( ICriteriaModifier modifier, Paging paging )
	{
		if( paging != null )
		{
			paging.setTotal( findCountBy( modifier ) );
		}
		return (List<T>) findBy( createCriteria(), modifier, paging );
	}

	@Override
	@Transactional( readOnly = true )
	public void findBy( ICriteriaModifier conditions, Paging paging, IPartialResultCallback<T, I> callback )
	{
		Criteria criteria = createCriteria();
		conditions.modify( criteria );
		applyFetching( criteria );
		applyPaging( criteria, paging );
		criteria.setResultTransformer( Criteria.DISTINCT_ROOT_ENTITY );
		criteria.setCacheMode( CacheMode.IGNORE );
		ScrollableResults results = criteria.scroll( ScrollMode.SCROLL_INSENSITIVE );

		List<T> part = new ArrayList<>( callback.getPartSize() );
		Set<I> ids = new HashSet<>();
		while( results.next() )
		{
			T item = (T) results.get( 0 );
			if( !ids.contains( item.getId() ) )
			{
				ids.add( item.getId() );
				part.add( item );
			}

			if( part.size() >= callback.getPartSize() || results.isLast() )
			{
				callback.invoke( part );
				getSession().clear();
				part.clear();
			}
		}
		results.close();
	}

	protected void applyFetching( Criteria criteria )
	{
	}

	protected void applyFetching4Edit( Criteria criteria )
	{
		applyFetching( criteria );
	}

	protected void applyFilter( Criteria criteria, String pattern )
	{
	}

	protected final void applyPaging( Criteria criteria, Paging paging )
	{
		if( paging != null )
		{
			if( paging.getOffset() != null )
			{
				criteria.setFirstResult( paging.getOffset() );
			}
			if( paging.getLimit() != null )
			{
				criteria.setMaxResults( paging.getLimit() );
			}

			String sortColumn = paging.getSortColumn();
			if( sortColumn != null )
			{
				int idx = sortColumn.lastIndexOf( '.' );
				if( idx != -1 )
				{
					String associationPath = sortColumn.substring( 0, idx );
					sortColumn = sortColumn.substring( idx + 1 );

					Iterator<CriteriaImpl.Subcriteria> iterator = ((CriteriaImpl) criteria).iterateSubcriteria();
					boolean pathFound = false;
					while( iterator.hasNext() )
					{
						CriteriaImpl.Subcriteria subCriteria = iterator.next();
						if( associationPath.equals( subCriteria.getPath() ) )
						{
							sortColumn = subCriteria.getAlias() + '.' + sortColumn;
							pathFound = true;
							break;
						}
					}
					if( !pathFound )
					{
						String entityName = ((CriteriaImpl) criteria).getEntityOrClassName();
						BeanInfo info;
						try
						{
							info = Introspector.getBeanInfo( Class.forName( entityName ) );
							for( PropertyDescriptor pd : info.getPropertyDescriptors() )
							{
								if( pd.getReadMethod().getAnnotation( Embedded.class ) != null )
								{
									if( associationPath.equals( pd.getName() ) )
									{   // Concat it back and do not touch as far as its the proper embeddable naming
										sortColumn = associationPath + '.' + sortColumn;
										pathFound = true;
										break;
									}
								}
							}
						}
						catch( Exception ignored )
						{
						}

						if( !pathFound )
						{
/*
                        String alias = "a_11" + new Random( ).nextInt( 100 );
						criteria = criteria.createAlias( associationPath, alias, JoinType.LEFT_OUTER_JOIN );
						sortColumn = alias + '.' + sortColumn;
*/
							criteria = criteria.createCriteria( associationPath, JoinType.LEFT_OUTER_JOIN );
						}
					}
				}
				criteria.addOrder( createOrder( sortColumn, paging.isSortDesc() ) );
			}
		}
	}

	protected final Order createOrder( String sortColumn, boolean sortDirection )
	{
		Order order = null;
		if( sortColumn != null )
		{
			if( sortDirection )
			{
				order = Order.desc( sortColumn );
			}
			else
			{
				order = Order.asc( sortColumn );
			}
		}
		return order;
	}

	protected List<T> findByQueryAndPattern( String queryName, final String pattern, final Paging paging )
	{
		if( queryName == null )
		{
			queryName = getEntityClass().getSimpleName() + ".findByPattern";
		}
		Query query = applyPaging( queryName, paging );
		query.setParameter( "pattern", "%" + pattern.toLowerCase() + "%" );
		return query.list();
	}

	protected long findCountByQueryAndPattern( String queryName, final String pattern )
	{
		if( queryName == null )
		{
			queryName = getEntityClass().getSimpleName() + ".findCountByPattern";
		}
		Query query = getSession().getNamedQuery( queryName ).setParameter( "pattern", "%" + pattern.toLowerCase() + "%" );
		return (Long) query.uniqueResult();
	}

	protected List<T> findByQueryAndParameter( final String queryName, final String paramName, final Object paramValue, final Paging paging )
	{
		Query query = applyPaging( queryName, paging );
		query.setParameter( paramName, paramValue );
		return query.list();
	}

	protected long findCountByQueryAndParameter( final String queryName, final String paramName, final Object paramValue )
	{
		Query query = getSession().getNamedQuery( queryName ).setParameter( paramName, paramValue );
		return (Long) query.uniqueResult();
	}

	protected List<T> findByQueryAndParams( final String queryName, final String[] paramNames, final Object[] paramValues, final Paging paging )
	{
		checkParams( paramNames, paramValues );
		Query query = applyPaging( queryName, paging );
		populateParams( paramNames, paramValues, query );
		return query.list();
	}

	protected long findCountByQueryAndParams( final String queryName, final String[] paramNames, final Object[] paramValues )
	{
		checkParams( paramNames, paramValues );
		Query query = getSession().getNamedQuery( queryName );
		populateParams( paramNames, paramValues, query );
		return (Long) query.uniqueResult();
	}

	private void checkParams( String[] paramNames, Object[] paramValues )
	{
		if( paramNames == null || paramValues == null || paramNames.length != paramValues.length )
		{
			throw new IllegalArgumentException( "Length of paramNames array must match length of values array" );
		}
	}

	private void populateParams( String[] paramNames, Object[] paramValues, Query query )
	{
		for( int i = 0; i < paramNames.length; i++ )
		{
			query.setParameter( paramNames[i], paramValues[i] );
		}
	}

	protected Query applyPaging( String queryName, Paging paging )
	{
		Session session = getSession();
		StringBuilder queryString = new StringBuilder( session.getNamedQuery( queryName ).getQueryString() );
		return applyPaging( session, queryString, paging );
	}

	protected Query applyPaging( Session session, StringBuilder queryString, Paging paging )
	{
		Query query;
		if( paging != null )
		{
			if( paging.getSortColumn() != null )
			{
				queryString.append( "\n ORDER BY " ).append( paging.getSortColumn() );
				if( paging.isSortDesc() )
				{
					queryString.append( " " ).append( "DESC" );
				}
			}
			query = session.createQuery( queryString.toString() );
			if( paging.getOffset() != null )
			{
				query.setFirstResult( paging.getOffset() );
			}
			if( paging.getLimit() != null )
			{
				query.setMaxResults( paging.getLimit() );
			}
		}
		else
		{
			query = session.createQuery( queryString.toString() );
		}
		return query;
	}
}

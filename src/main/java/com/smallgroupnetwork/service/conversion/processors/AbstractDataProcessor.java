package com.smallgroupnetwork.service.conversion.processors;

import com.smallgroupnetwork.model.Person;
import com.smallgroupnetwork.persistence.BaseEntity;
import com.smallgroupnetwork.persistence.ICriteriaModifier;
import com.smallgroupnetwork.persistence.IPartialResultCallback;
import com.smallgroupnetwork.persistence.IPersistenceService;
import com.smallgroupnetwork.persistence.Paging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;

/**
 * User: gyazykov
 * Date: 21/4/15
 * Time: 5:32 PM
 */
public abstract class AbstractDataProcessor<T extends BaseEntity<I>, I extends Serializable> implements IDataProcessor<T, I>
{

	protected final Logger logger = LoggerFactory.getLogger( this.getClass() );

	public abstract IPersistenceService<T, I> persistentService();

	public abstract T rowToItem( String[] row, Map<String, Integer> colNamesMap );

	@Override
	public void findByConditions( ICriteriaModifier criteriaConditions, Paging paging, IPartialResultCallback<T, I> callback )
	{
		persistentService().findBy( criteriaConditions, paging, callback );
	}

	@Override
	public void processImport( String[] row, Map<String, Integer> colNamesMap )
	{
		T item = rowToItem( row, colNamesMap );
		if( item != null )
		{
			persistentService().saveOrUpdate( item );
		}
	}

	protected String getFullName( Person user )
	{
		return user.getFirstName() + " " + user.getLastName();
	}

	protected String getField( Map<String, Integer> colNamesMap, String[] row, String colName )
	{
		Integer position = colNamesMap.get( colName );
		if( position == null )
		{
			return null;
		}
		String value = row[position];
		if( value == null )
		{
			return null;
		}
		return value.trim();
	}
}

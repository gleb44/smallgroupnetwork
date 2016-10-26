package com.smallgroupnetwork.service.conversion;

import com.smallgroupnetwork.persistence.BaseEntity;
import com.smallgroupnetwork.persistence.ICriteriaModifier;
import com.smallgroupnetwork.service.conversion.processors.IDataProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Set;

/**
 * User: gyazykov
 * Date: 21/4/15
 * Time: 5:32 PM
 */
public interface IConversionService
{
	<T extends BaseEntity<I>, I extends Serializable> void importData( IDataProcessor<T, I> dataProvider, InputStream stream, FormatType fromType ) throws IOException;

	<T extends BaseEntity<I>, I extends Serializable> void exportData( final IDataProcessor<T, I> dataProvider, Set<String> exportCols, ICriteriaModifier filter,
		ICriteriaModifier criteriaConditions, OutputStream stream, FormatType toType );
}

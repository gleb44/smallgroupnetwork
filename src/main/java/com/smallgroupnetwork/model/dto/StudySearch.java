package com.smallgroupnetwork.model.dto;

import com.smallgroupnetwork.persistence.ICriteriaModifier;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * User: gyazykov
 * Date: 6/4/16
 * Time: 11:36 AM
 */
public class StudySearch implements ICriteriaModifier
{
	private String pattern;
	private String speaker;

	@Override
	public void modify( final Criteria criteria )
	{
		if( pattern != null )
		{
			pattern = pattern.trim();
			if( pattern.length() > 0 )
			{
				criteria.add( Restrictions.or(
					Restrictions.ilike( "title", pattern, MatchMode.ANYWHERE ),
					Restrictions.ilike( "description", pattern, MatchMode.ANYWHERE ),
					Restrictions.ilike( "speaker", pattern, MatchMode.ANYWHERE ) ) );
			}
		}
		if( speaker != null )
		{
			criteria.add( Restrictions.eq( "speaker", speaker ) );
		}
	}

	public void setPattern( String pattern )
	{
		this.pattern = pattern;
	}

	public void setSpeaker( String speaker )
	{
		this.speaker = speaker;
	}

	public String getPattern()
	{
		return pattern;
	}

	public String getSpeaker()
	{
		return speaker;
	}
}

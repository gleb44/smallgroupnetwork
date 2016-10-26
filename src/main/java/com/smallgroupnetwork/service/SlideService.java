package com.smallgroupnetwork.service;

import com.smallgroupnetwork.model.Slide;
import com.smallgroupnetwork.persistence.AbstractPersistenceService;
import com.smallgroupnetwork.persistence.Paging;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

/**
 * User: gleb
 * Date: 5/14/14
 * Time: 3:30 PM
 */
@Service
public class SlideService extends AbstractPersistenceService<Slide, Long> implements ISlideService
{
	@Autowired
	private IAttachmentService attachmentService;

	@Override
	@Transactional( readOnly = true )
	public List<Slide> readAll( Paging paging )
	{
		if( paging == null )
		{
			paging = new Paging();
		}
		paging.setSortColumn( "indexNumber" );

		return super.readAll( paging );
	}

	@Override
	@Transactional
	public Slide merge( Slide slide )
	{
		if( slide.getAttachment() == null )
		{
			addValidationResult( "", "slide.empty" );
			validate();
		}
		Criteria criteria = createCriteria().addOrder( Order.asc( "indexNumber" ) ).setLockMode( LockMode.PESSIMISTIC_WRITE );
		List<Slide> slides = (List<Slide>) criteria.list();
		reorder( slide, slides );
		if( slide.getAttachment().getId() == null )
		{
			attachmentService.saveNewAttachment( slide.getAttachment() );

			if( slide.getId() != null )
			{
				Slide oldSlide = get( slide.getId() );
				if( oldSlide != null && oldSlide.getAttachment() != null )
				{
					attachmentService.delete( oldSlide.getAttachment().getId() );
				}
			}
		}
		else
		{
			slide.setAttachment( attachmentService.get( slide.getAttachment().getId() ) );
		}
		return super.merge( slide );
	}

	private void reorder( Slide slide, List<Slide> oldSlides )
	{
		if( slide.getId() != null )
		{
			boolean exist = false;
			for( Iterator<Slide> iterator = oldSlides.iterator(); iterator.hasNext(); )
			{
				Slide oldSlide = iterator.next();
				if( oldSlide.getId().equals( slide.getId() ) )
				{
					exist = true;
					if( oldSlide.getIndexNumber().equals( slide.getIndexNumber() ) )
					{
						return;  //no need to rearrange
					}
					iterator.remove();  //remove existing slide
					break;
				}
			}
			if( !exist )
			{
				addValidationResult( "", "slide.doesnt.exist" );
				validate();
			}
		}
		if( slide.getIndexNumber() == null || slide.getIndexNumber() < 0 || slide.getIndexNumber() > oldSlides.size() )
		{
			oldSlides.add( slide );
		}
		else
		{
			oldSlides.add( slide.getIndexNumber(), slide );
		}
		for( int i = 0; i < oldSlides.size(); i++ )
		{
			Slide oldSlide = oldSlides.get( i );
			oldSlide.setIndexNumber( i );
		}
	}
}

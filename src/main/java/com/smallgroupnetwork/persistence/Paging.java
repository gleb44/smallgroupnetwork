package com.smallgroupnetwork.persistence;

/**
 * User: gleb
 * Date: 2/16/12
 * Time: 10:51 AM
 */
public class Paging
{
	private Long total;
	private Integer offset;
	private Integer limit;
	private String sortColumn;
	private boolean sortDesc = false;

	public Paging()
	{
	}

	public Paging( Paging paging )
	{
		this( paging.getOffset(), paging.getLimit(), paging.getSortColumn(), paging.isSortDesc() );
	}

	public Paging( Integer limit )
	{
		this.limit = limit;
	}

	public Paging( Integer offset, Integer limit )
	{
		this.offset = offset;
		this.limit = limit;
	}

	public Paging( String sortColumn, boolean sortDirection )
	{
		this.sortColumn = sortColumn;
		this.sortDesc = sortDirection;
	}

	public Paging( Integer offset, Integer limit, String sortColumn, boolean sortDirection )
	{
		this.offset = offset;
		this.limit = limit;
		this.sortColumn = sortColumn;
		this.sortDesc = sortDirection;
	}

	public Integer getOffset()
	{
		return offset;
	}

	public void setOffset( Integer offset )
	{
		this.offset = offset;
	}

	public Integer getLimit()
	{
		return limit;
	}

	public void setLimit( Integer limit )
	{
		this.limit = limit;
	}

	public String getSortColumn()
	{
		return sortColumn;
	}

	public void setSortColumn( String sortColumn )
	{
		this.sortColumn = sortColumn;
	}

	public boolean isSortDesc()
	{
		return sortDesc;
	}

	public void setSortDesc( boolean sortDesc )
	{
		this.sortDesc = sortDesc;
	}

	public Long getTotal()
	{
		return total;
	}

	public void setTotal( Long total )
	{
		this.total = total;
	}
}

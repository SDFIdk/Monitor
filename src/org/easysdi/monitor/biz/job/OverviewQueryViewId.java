package org.easysdi.monitor.biz.job;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class OverviewQueryViewId implements Serializable {

	private static final long serialVersionUID = -5060137236621292400L;
	private long overviewId;
	private long queryId;

	public OverviewQueryViewId() {}
	public OverviewQueryViewId(Long overviewId, Long queryId) {
		this.overviewId = overviewId;
		this.queryId = queryId;
	}
	@Override
	public boolean equals(final Object other) {
		if (this == other)
			return true;
		if (!(other instanceof OverviewQueryViewId))
			return false;
		OverviewQueryViewId castOther = (OverviewQueryViewId) other;
		return new EqualsBuilder().append(overviewId, castOther.overviewId)
		.append(queryId, castOther.queryId).isEquals();
	}
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(overviewId).append(queryId)
		.toHashCode();
	}
	public long getOverviewId()
	{
		return this.overviewId;
	}
	public long getQueryId()
	{
		return this.queryId;
	}
	public void setOverviewId(long overviewId)
	{
		this.overviewId=overviewId;
	}
	public void setQueryId(long queryId)
	{
		this.queryId=queryId;
	}
}

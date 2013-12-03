package org.easysdi.monitor.biz.job;

import org.easysdi.monitor.dat.dao.OverviewQueryDaoHelper;

public class OverviewQuery {
	
	private long overviewQueryID;
	private Overview parentOverview;
	private Query query;
	private long overviewId;
	private long queryId;

	public OverviewQuery() {
		// TODO Auto-generated constructor stub
	}

	
	public long getOverviewQueryID() {
		return overviewQueryID;
	}
	public void setOverviewQueryID(long overviewQueryID) {
		this.overviewQueryID = overviewQueryID;
	}
	
	
	public Overview getParentOverview() {
		return parentOverview;
	}
	public void setParentOverview(Overview parentOverview) {
		this.parentOverview = parentOverview;
	}

	public void setQuery(Query query) {
		this.query = query;
	}
	public Query getQuery() {
		return query;
	}	

	public void setOverviewId(long jobId) {
		this.overviewId = jobId;
	}

	public void setOverviewIdString(String jobId) {
		this.overviewId = Long.parseLong(jobId);
	}

	public long getOverviewId() {
		return overviewId;
	}

	public void setQueryId(long queryId) {
		this.queryId = queryId;
	}

	public void setQueryIdString(String queryId) {
		this.queryId =  Long.parseLong(queryId);
	}

	public long getQueryId() {
		return queryId;
	}
	
	public static OverviewQuery getFromIdStrings(String overviewId, String queryId){
	       return OverviewQueryDaoHelper.getOverviewQueryDao().getOverviewQueryByIdString(overviewId, queryId);

	}
	
    public boolean delete() {
        return OverviewQueryDaoHelper.getOverviewQueryDao().delete(this);
    }
    
    public boolean persist() {

        return OverviewQueryDaoHelper.getOverviewQueryDao().persist(this);

    }
}

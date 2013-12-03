package org.easysdi.monitor.biz.job;

import org.easysdi.monitor.dat.dao.OverviewQueryViewDaoHelper;

public class OverviewQueryView {


	private boolean queryIsPublic;
	private Overview parentOverview;
	private Query query;
	private OverviewLastQueryResult lastQueryResult;
	private OverviewQueryViewId id;
	private String overviewName;
	private String queryName;
	
	public OverviewQueryView(){
	}

	public void setQueryIsPublic(boolean queryIsPublic) {
		this.queryIsPublic = queryIsPublic;
	}
	public boolean isQueryIsPublic() {
		return queryIsPublic;
	}
	public void setParentOverview(Overview parentOverview) {
		this.parentOverview = parentOverview;
	}
	public Overview getParentOverview() {
		return parentOverview;
	}
	public void setQuery(Query query) {
		this.query = query;
	}
	public Query getQuery() {
		return query;
	}
	public void setLastQueryResult(OverviewLastQueryResult lastQueryResult) {
		this.lastQueryResult = lastQueryResult;
	}
	public OverviewLastQueryResult getLastQueryResult() {
		return lastQueryResult;
	}

	public static OverviewQueryView getFromIdStrings(String overviewId, String queryId){
		return OverviewQueryViewDaoHelper.getOverviewQueryViewDao().getOverviewQueriesFromIdStrings(overviewId, queryId);
	}

	public void setId(OverviewQueryViewId id) {
		this.id = id;
	}

	public OverviewQueryViewId getId() {
		return id;
	}

	public void setOverviewName(String overviewName) {
		this.overviewName = overviewName;
	}

	public String getOverviewName() {
		return overviewName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public String getQueryName() {
		return queryName;
	}
}

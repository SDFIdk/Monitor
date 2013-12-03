package org.easysdi.monitor.dat.dao;

import java.util.List;

import org.easysdi.monitor.biz.job.OverviewQueryView;

public interface IOverviewQueryViewDao {
	OverviewQueryView getOverviewQueriesByNames(String overviewName, long queryId);
	OverviewQueryView getOverviewQueriesByIds(long overviewId, long queryId);
	OverviewQueryView getOverviewQueriesFromIdStrings(String overviewId, String queryId);

	List<OverviewQueryView> GetOverviewQueriesByOverviewId(long overviewId);
	List<OverviewQueryView> GetOverviewQueriesByOverviewName(String overviewName);
	List<OverviewQueryView> getOverviewQueriesFromOverviewIdString(String overviewId);
}

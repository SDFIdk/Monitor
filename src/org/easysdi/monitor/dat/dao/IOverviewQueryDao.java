package org.easysdi.monitor.dat.dao;

import org.easysdi.monitor.biz.job.OverviewQuery;;

public interface IOverviewQueryDao {
	
	boolean delete(OverviewQuery aPage);
	boolean persist(OverviewQuery aPage);
	OverviewQuery getOverviewQueryByIdString(String overviewId, String queryId);

}

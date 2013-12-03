package org.easysdi.monitor.biz.job;

import java.util.List;

import org.easysdi.monitor.dat.dao.OverviewQueryViewDaoHelper;

public class OverviewQueryViewCollection extends AbstractValueObject {

	public OverviewQueryViewCollection() {
	}
	
	public List<OverviewQueryView> findOverviews(String overviewId) {
		return OverviewQueryViewDaoHelper.getOverviewQueryViewDao().getOverviewQueriesFromOverviewIdString(overviewId);
	}

}

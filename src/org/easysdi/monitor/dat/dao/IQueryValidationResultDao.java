package org.easysdi.monitor.dat.dao;

import org.easysdi.monitor.biz.job.QueryValidationResult;

public interface IQueryValidationResultDao {
	boolean persist(QueryValidationResult queryValidationResult);

}

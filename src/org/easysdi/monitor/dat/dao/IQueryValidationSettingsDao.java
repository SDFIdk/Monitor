package org.easysdi.monitor.dat.dao;

import org.easysdi.monitor.biz.job.QueryValidationSettings;

public interface IQueryValidationSettingsDao {
	boolean persist(QueryValidationSettings queryValidationSettings);
}

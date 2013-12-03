package org.easysdi.monitor.dat.dao.hibernate;

import org.easysdi.monitor.biz.job.QueryValidationSettings;
import org.easysdi.monitor.dat.dao.IQueryValidationSettingsDao;
import org.easysdi.monitor.dat.dao.QueryValidationSettingsDaoHelper;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;

public class QueryValidationSettingsDao extends AbstractDao implements IQueryValidationSettingsDao  {

    public QueryValidationSettingsDao(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
        QueryValidationSettingsDaoHelper.setDao(this);
    }

    
	public boolean persist(QueryValidationSettings queryValidationSettings) {
		if (null == queryValidationSettings) {
			throw new IllegalArgumentException("queryValidationSettings can't be null");
		}

		try {
			this.getHibernateTemplate().saveOrUpdate(queryValidationSettings);
			return true;

		} catch (DataAccessException e) {
			return false;
		}
	}

}

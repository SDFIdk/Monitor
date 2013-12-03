package org.easysdi.monitor.dat.dao.hibernate;

import java.util.List;

import org.easysdi.monitor.biz.job.OverviewQuery;
import org.easysdi.monitor.dat.dao.IOverviewQueryDao;
import org.easysdi.monitor.dat.dao.OverviewQueryDaoHelper;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class OverviewQueryDao extends AbstractDao implements IOverviewQueryDao {

	private TransactionTemplate txTemplate;


	public OverviewQueryDao(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
		OverviewQueryDaoHelper.setOverviewQueryDao(this);
	}

	/**
	 * Defines the transaction manager for the persistence operations. 
	 * 
	 * @param   txManager  the Spring transaction manager for Hibernate
	 */
	public void setTxManager(PlatformTransactionManager txManager) {
		this.txTemplate = new TransactionTemplate(txManager);
	}

	/**
	 * Gets the transaction template for the persistence operations. 
	 * 
	 * @return   txTemplate  the Spring transaction template for Hibernate
	 */
	public TransactionTemplate getTxTemplate() {
		return this.txTemplate;
	}

	public boolean delete(OverviewQuery aPage) {

		try {
			this.getHibernateTemplate().delete(aPage);

			return true;

		} catch (DataAccessException e) {
			return false;
		}
	}

	public boolean persist(OverviewQuery aPage) {
		if (null == aPage) {
			throw new IllegalArgumentException("OverviewQuery can't be null");
		}

		try {
			this.getHibernateTemplate().saveOrUpdate(aPage);
			return true;

		} catch (DataAccessException e) {
			return false;
		}	}


	@SuppressWarnings("unchecked")
	public OverviewQuery getOverviewQueriesByNames(String overviewName,
			long queryId) {
		final DetachedCriteria search 
		= DetachedCriteria.forClass(OverviewQuery.class);
		search.createCriteria("parentOverview").add(Restrictions.eq("name", overviewName));
		search.add(Restrictions.eq("queryId", queryId));
		final List<OverviewQuery> result 
		= this.getHibernateTemplate().findByCriteria(search);
		if (null == result || 1 > result.size()) {
			return null;
		}
		return result.get(0);

	}
	@SuppressWarnings("unchecked")
	public OverviewQuery getOverviewQueriesByIds(long overviewId, long queryId){
		final DetachedCriteria search 
		= DetachedCriteria.forClass(OverviewQuery.class);
		search.add(Restrictions.eq("overviewId", overviewId));
		search.add(Restrictions.eq("queryId", queryId));
		final List<OverviewQuery> result 
		= this.getHibernateTemplate().findByCriteria(search);
		if (null == result || 1 > result.size()) {
			return null;
		}
		return result.get(0);
	}

	public OverviewQuery getOverviewQueryByIdString(String overviewId,
			String queryId) {
		long oId = 0;
		long qId = 0;

		if (null == overviewId || overviewId.equals("")) {
			throw new IllegalArgumentException(
			"Overview id can't be null or empty");
		}

		if (null == queryId || queryId.equals("")) {
			throw new IllegalArgumentException(
			"Query id can't be null or empty");
		}
		try{
			qId = Long.parseLong(queryId);
		}
		catch(NumberFormatException e)
		{
			throw new IllegalArgumentException(
			"QueryId must be a number");
		}

		try{
			oId = Long.parseLong(overviewId);
			return getOverviewQueriesByIds(oId, qId);
		}
		catch(NumberFormatException e)
		{
			return getOverviewQueriesByNames(overviewId, qId);
		}

	}

}

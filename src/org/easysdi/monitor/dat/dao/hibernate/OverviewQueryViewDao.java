package org.easysdi.monitor.dat.dao.hibernate;

import java.util.List;

import org.easysdi.monitor.biz.job.OverviewQueryView;
import org.easysdi.monitor.dat.dao.IOverviewQueryViewDao;
import org.easysdi.monitor.dat.dao.OverviewQueryViewDaoHelper;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public class OverviewQueryViewDao extends AbstractDao implements
IOverviewQueryViewDao {

	private TransactionTemplate txTemplate;

	public OverviewQueryViewDao(SessionFactory sessionFactory) {
		this.setSessionFactory(sessionFactory);
		OverviewQueryViewDaoHelper.setOverviewQueryViewDao(this);
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

	@SuppressWarnings("unchecked")
	public OverviewQueryView getOverviewQueriesByNames(String overviewName,
			 long queryId) {
		final DetachedCriteria search 
		= DetachedCriteria.forClass(OverviewQueryView.class);
		search.add(Restrictions.eq("overviewName", overviewName));
		search.add(Restrictions.eq("id.queryId", queryId));
		final List<OverviewQueryView> result 
		= this.getHibernateTemplate().findByCriteria(search);
		if (null == result || 1 > result.size()) {
			return null;
		}
		return result.get(0);
		
	}
	@SuppressWarnings("unchecked")
	public OverviewQueryView getOverviewQueriesByIds(long overviewId, long queryId){
		final DetachedCriteria search 
		= DetachedCriteria.forClass(OverviewQueryView.class);
		search.add(Restrictions.eq("id.overviewId", overviewId));
		search.add(Restrictions.eq("id.queryId", queryId));
		final List<OverviewQueryView> result 
		= this.getHibernateTemplate().findByCriteria(search);
		if (null == result || 1 > result.size()) {
			return null;
		}
		return result.get(0);
	}
	public OverviewQueryView getOverviewQueriesFromIdStrings(String overviewId,
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
	
	@SuppressWarnings("unchecked")
	public List<OverviewQueryView> GetOverviewQueriesByOverviewId(long overviewId){
		final DetachedCriteria search 
		= DetachedCriteria.forClass(OverviewQueryView.class);
		search.add(Restrictions.eq("id.overviewId", overviewId));
		final List<OverviewQueryView> result 
		= this.getHibernateTemplate().findByCriteria(search);
		if (null == result || 1 > result.size()) {
			return null;
		}
		return result;
	
	}

	@SuppressWarnings("unchecked")
	public List<OverviewQueryView> GetOverviewQueriesByOverviewName(String overviewName){
		final DetachedCriteria search 
		= DetachedCriteria.forClass(OverviewQueryView.class);
		search.add(Restrictions.eq("overviewName", overviewName));
		final List<OverviewQueryView> result 
		= this.getHibernateTemplate().findByCriteria(search);
		if (null == result || 1 > result.size()) {
			return null;
		}
		return result;
	
	}

	
	
	public List<OverviewQueryView> getOverviewQueriesFromOverviewIdString(
			String overviewId) {
		//long oId = 0;

		if (null == overviewId || overviewId.equals("")) {
			throw new IllegalArgumentException(
			"Overview id can't be null or empty");
		}
		return GetOverviewQueriesByOverviewName(overviewId);
		/*try{
			oId = Long.parseLong(overviewId);
			return GetOverviewQueriesByOverviewId(oId);
		}
		catch(NumberFormatException e)
		{
			;
		}*/
	}

}

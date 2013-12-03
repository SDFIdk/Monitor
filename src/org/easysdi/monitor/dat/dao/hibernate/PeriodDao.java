/**
 * 
 */
package org.easysdi.monitor.dat.dao.hibernate;

import java.util.LinkedList;
import java.util.List;

import org.easysdi.monitor.biz.job.Period;
import org.easysdi.monitor.dat.dao.PeriodDaoHelper;
import org.easysdi.monitor.dat.dao.IPeriodDao;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author berg3428
 *
 */
public class PeriodDao extends HibernateDaoSupport implements IPeriodDao {

	
	  /**
     * Creates a new period data access object.
     * 
     * @param   sessionFactory  the Hibernate session factory object
     */
    public PeriodDao(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
        PeriodDaoHelper.setPeriodDao(this);
    }

	public boolean delete(Period period) {
       if (null == period) {
            throw new IllegalArgumentException("Sla period can't be null");
        }

        try {
            this.getHibernateTemplate().delete(period);
            return true;
        } catch (DataAccessException e) {
            return false;
        }
	}

	public boolean persistPeriod(Period period) {
       if (null == period) {
            throw new IllegalArgumentException("Period can't be null");
        }
        
        /*if (!period.isValid()) {
            throw new IllegalStateException("Can't persist an invalid period");
        }*/

        try {
            this.getHibernateTemplate().saveOrUpdate(period);
            return true;

        } catch (DataAccessException e) {
            return false;
        }
	}

	public List<Period> getAllPeriod() {
		final HibernateTemplate hibernateTemplate = this.getHibernateTemplate();        
        return this.typePeriodResultList(hibernateTemplate.loadAll(Period.class));
	}
	
	private List<Period> typePeriodResultList(List<?> resultlist)
	{
		final List<Period> periodlist = new LinkedList<Period>();
		for(Object obj : resultlist)
		{
			if(obj instanceof Period)
			{
				periodlist.add((Period)obj);
			}
		}
		return periodlist;
	}

	public List<Period> findSlaPeriods(long SlaID) {
		 // Criteria search
        final DetachedCriteria search = DetachedCriteria.forClass(Period.class);

        if (SlaID > 0) {
            search.add(Restrictions.eq("slaId", SlaID));
        }
        final HibernateTemplate hibernateTemplate = this.getHibernateTemplate();
        final List<Period> result = this.typePeriodResultList(hibernateTemplate.findByCriteria(search));

        if (null == result) {
            return new LinkedList<Period>();
        }
        return result;
	}

	public Period getPeriodById(long searchedPeriodId) {
		 if (1 > searchedPeriodId) {
			  throw new IllegalArgumentException("Invalid period identifier");
		  }
	      return this.getHibernateTemplate().get(Period.class, searchedPeriodId);
		
	}

	public Period getPeriodFromIdString(String idString) {		
		 try {
	            final long periodId = Long.parseLong(idString);
	            return this.getPeriodById(periodId);
	        } catch (NumberFormatException e) {
				  throw new IllegalArgumentException("Invalid period identifier only ID search allowed");
	        }
	}
}

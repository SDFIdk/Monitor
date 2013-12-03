/**
 * 
 */
package org.easysdi.monitor.dat.dao.hibernate;

import java.util.LinkedList;
import java.util.List;

import org.easysdi.monitor.biz.job.Holiday;
import org.easysdi.monitor.dat.dao.HolidayDaoHelper;
import org.easysdi.monitor.dat.dao.IHolidayDao;
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
public class HolidayDao extends HibernateDaoSupport implements IHolidayDao {

	  /**
     * Creates a new holiday data access object.
     * 
     * @param   sessionFactory  the Hibernate session factory object
     */
    public HolidayDao(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
        HolidayDaoHelper.setHolidayDao(this);
    }
	
	  /**
     * {@inheritDoc}
     */
    public boolean persistHoliday(Holiday holiday) {

        if (null == holiday) {
            throw new IllegalArgumentException("Holiday can't be null");
        }
        
        // TODO
        /*if (!holday.isValid()) {
            throw new IllegalStateException("Can't persist an invalid holiday");
        }*/

        try {
            this.getHibernateTemplate().saveOrUpdate(holiday);
            return true;

        } catch (DataAccessException e) {
            return false;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean delete(Holiday holiday) {

        if (null == holiday) {
            throw new IllegalArgumentException("Holiday can't be null");
        }

        try {
            this.getHibernateTemplate().delete(holiday);
            return true;
        } catch (DataAccessException e) {
            return false;
        }
    }
    
    public List<Holiday> getAllHoliday(){
    	final HibernateTemplate hibernateTemplate = this.getHibernateTemplate();        
    	return this.typeHolidayResultList(hibernateTemplate.loadAll(Holiday.class));
	}

	/**
	 * Converts an Hibernate result into a strongly-typed Holiday results list.
	 * 
	 * @param   resultList  the holiday results list returned by Hibernate
	 * @return              the strongly-typed result list
	 */
	private List<Holiday> typeHolidayResultList(List<?> resultList) {
	    final List<Holiday> holidayList = new LinkedList<Holiday>();
	    for (Object holidayObject : resultList) {
	        if (holidayObject instanceof Holiday) {
	        	holidayList.add((Holiday) holidayObject);
	        }
	    }
	    return holidayList;
	}

	/**
	 *  Finds Holiday identified by name or id
	 */
	public Holiday getHolidayFromIdString(String idString) {
		 try {
	            final long holidayId = Long.parseLong(idString);
	            return this.getHolidayById(holidayId);
	        } catch (NumberFormatException e) {
	
	            return this.getHolidayByName(idString);
	        }
	}

	public Holiday getHolidayById(long searchedHolidayId) {
	  if (1 > searchedHolidayId) {
		  throw new IllegalArgumentException("Invalid holiday identifier");
	  }
	  return this.getHibernateTemplate().get(Holiday.class, searchedHolidayId);
	}

	@SuppressWarnings("unchecked")
	public Holiday getHolidayByName(String searchedHolidayName) {		
		if (null == searchedHolidayName || searchedHolidayName.equals("")) {
	    throw new IllegalArgumentException("Holiday name can't be null or empty");
	}
	
	final DetachedCriteria search = DetachedCriteria.forClass(Holiday.class);
	search.add(Restrictions.eq("name", searchedHolidayName));
	final List<Holiday> result = this.getHibernateTemplate().findByCriteria(search);
	
	if (null == result || 1 > result.size()) {
		throw new IllegalArgumentException("Found zero or more then one Holiday with the name");
	    }
	    return result.get(0);
	}
    
}

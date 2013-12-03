package org.easysdi.monitor.dat.dao.hibernate;

import java.util.List;

import org.easysdi.monitor.biz.alert.AbstractAction;
import org.easysdi.monitor.biz.alert.ActionType;
import org.easysdi.monitor.dat.dao.ActionDaoHelper;
import org.easysdi.monitor.dat.dao.IActionDao;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Provides action persistance operations through Hibernate.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class ActionDao extends HibernateDaoSupport implements IActionDao {

    /**
     * Creates a new action data access object.
     * 
     * @param   sessionFactory  the Hibernate session factory object
     */
    public ActionDao(SessionFactory sessionFactory) {
        this.setSessionFactory(sessionFactory);
        ActionDaoHelper.setActionDao(this);
    }



    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ActionType getType(String typeName) {
        final DetachedCriteria search 
            = DetachedCriteria.forClass(ActionType.class);
        search.add(Restrictions.eq("name", typeName));
        final List<ActionType> result 
            = this.getHibernateTemplate().findByCriteria(search);

        if (null == result || 1 > result.size()) {
            return null;
        }

        return result.get(0);
    }



    /**
     * {@inheritDoc}
     */
    public boolean persistAction(AbstractAction action) {

        try {
            this.getHibernateTemplate().saveOrUpdate(action);
            return true;

        } catch (DataAccessException e) {
            return false;
        }
    }



    /**
     * {@inheritDoc}
     */
    public boolean deleteAction(AbstractAction action) {

        try {
            this.getHibernateTemplate().delete(action);
            return true;

        } catch (DataAccessException e) {
            return false;
        }
    }

}

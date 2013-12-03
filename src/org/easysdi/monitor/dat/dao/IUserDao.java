package org.easysdi.monitor.dat.dao;

import org.easysdi.monitor.biz.security.User;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Provides user persistance operations.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public interface IUserDao {

    /**
     * Gets a user from its name.
     * 
     * @param   name    the user name
     * @return          the user if it's been found, or<br>
     *                  <code>null</code> otherwise
     */
    User getUser(String name);



    /**
     * Gets the Spring transaction template for the persistance system.
     * 
     * @return  the transaction template
     */
    TransactionTemplate getTxTemplate();
}

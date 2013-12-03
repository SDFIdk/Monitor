/**
 * 
 */
package org.easysdi.monitor.dat.dao;

import java.util.List;


import org.easysdi.monitor.biz.job.Sla;
/**
 * @author berg3428
 *
 */
public interface ISlaDao {
	
	/**
	 * 
	 * @return List<Sla>
	 */
	List<Sla> getAllSla();
	 
	/**
     * Erases a sla.
     * <p>
     * This will also delete all the related objects (period)
     * 
     * @param   sla
     * @return      <code>true</code> if the sla was successfully deleted
     */
	boolean delete(Sla sla);
	
	boolean persistSla(Sla sla);

	Sla getSlaByName(String searchedSlaName);

	Sla getSlaById(long searchedSlaId);

	Sla getSlaFromIdString(String idString);
	
}

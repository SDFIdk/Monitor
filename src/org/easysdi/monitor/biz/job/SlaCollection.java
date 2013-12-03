/**
 * 
 */
package org.easysdi.monitor.biz.job;

import java.util.List;

import org.easysdi.monitor.biz.job.Sla;
import org.easysdi.monitor.dat.dao.SlaDaoHelper;

/**
 * @author BERG3428
 *
 */
public class SlaCollection {
	
	/**
     * Gets list of all sla.
     * <p>
     * @return a list containing the sla
     */
    public List<Sla> getSla() {
       return SlaDaoHelper.getSlaDao().getAllSla();
    }
}

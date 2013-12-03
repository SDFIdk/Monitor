/**
 * 
 */
package org.easysdi.monitor.biz.job;
import java.util.List;

import org.easysdi.monitor.biz.job.Period;
import org.easysdi.monitor.dat.dao.PeriodDaoHelper;

/**
 * @author berg3428
 *
 */
public class PeriodCollection {

	/**
	 * Default constructor
	 */
	public PeriodCollection()
	{
		
	}
	
	public List<Period> findSlaPeriods(long SlaID)
	{
		return PeriodDaoHelper.getPeriodDao().findSlaPeriods(SlaID);
	}
	
	/**
     * Gets list of all periods.
     * <p>
     * @return a list containing the periods
     */
    
	public List<Period> getPeriod()
	{
		return PeriodDaoHelper.getPeriodDao().getAllPeriod();
	}

}

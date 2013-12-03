/**
 * 
 */
package org.easysdi.monitor.dat.dao;

import java.util.List;

import org.easysdi.monitor.biz.job.Period;

/**
 * @author berg3428
 *
 */
public interface IPeriodDao {
	
	boolean delete(Period period);
	
	boolean persistPeriod(Period period);
	
	/**
	 * 
	 * @return List<Period>
	 */
	List<Period> getAllPeriod();
	
	List<Period> findSlaPeriods(long SlaID);

	Period getPeriodById(long searchedPeriodId);

	Period getPeriodFromIdString(String idString);

}

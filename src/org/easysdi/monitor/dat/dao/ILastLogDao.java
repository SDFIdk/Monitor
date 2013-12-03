/**
 * 
 */
package org.easysdi.monitor.dat.dao;

import org.easysdi.monitor.biz.job.OverviewLastQueryResult;
/**
 * @author berg3428
 *
 */
public interface ILastLogDao {

	boolean delete(OverviewLastQueryResult lastresult);
	
	boolean create(OverviewLastQueryResult lastresult);
	
	OverviewLastQueryResult exist(long queryID);
	
}

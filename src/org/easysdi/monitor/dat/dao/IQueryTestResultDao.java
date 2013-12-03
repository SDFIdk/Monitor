/**
 * 
 */
package org.easysdi.monitor.dat.dao;

import org.easysdi.monitor.biz.job.QueryTestResult;

/**
 * @author berg3428
 *
 */
public interface IQueryTestResultDao {

		
	boolean delete(QueryTestResult result);
	
	boolean persistResult(QueryTestResult result);
	
	QueryTestResult getFromIdString(String idString);
	
}

/**
 * 
 */
package org.easysdi.monitor.dat.dao;

import java.util.List;

import org.easysdi.monitor.biz.job.Overview;
/**
 * @author BERG3428
 *
 */
public interface IOverviewDao {
	

	List<Overview> getAllOverviews();
	
	boolean delete(Overview aPage);
	
	boolean persistOverview(Overview overviewp);

	Overview getOverviewByName(String searchedOverviewName);

	Overview getOverviewById(long searchedOverviewId);

	Overview getOverviewFromIdString(String idString);
	
}

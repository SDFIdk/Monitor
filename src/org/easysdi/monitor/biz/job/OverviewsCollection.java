package org.easysdi.monitor.biz.job;

import java.util.List;
import org.easysdi.monitor.biz.job.Overview;
import org.easysdi.monitor.dat.dao.OverviewDaoHelper;

public class OverviewsCollection {
    /**
     * Looks for jobs meeting some criteria.
     * <p>
     * Each parameter can be <code>null</code> if its value is indifferent. 
     * 
     * @param   automatic           whether the jobs searched are automatic
     * @param   realTimeAllowed     whether the jobs searched can be executed
     *                              on demand 
     * @param   published           whether the jobs searched are accessible
     *                              to anybody
     * @param   alertsEnabled       whether the job triggers alerts
     * @return                      a list containing the jobs meeting the 
     *                              criteria
     */
    public List<Overview> findOverviews(Boolean published) {
       return OverviewDaoHelper.getOverviewDao().getAllOverviews();
    }

}

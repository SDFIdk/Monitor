package org.easysdi.monitor.biz.job;

import java.util.Map;

import org.deegree.framework.util.StringTools;
import org.easysdi.monitor.dat.dao.OverviewDaoHelper;

public class Overview {
	private long overviewID;
	private String name;
	private Map<Long,OverviewQuery> overviewQueries;
	private boolean isPublic;
	
	/**
     * No-argument constructor.
    */
	public Overview()
	{}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the usePassword
	 */
	public boolean isIsPublic() {
		return isPublic;
	}

	/**
	 * @param usePassword the usePassword to set
	 */
	public void setIsPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public long getOverviewID() {
		return overviewID;
	}

	public void setOverviewID(long overviewID) {
		this.overviewID = overviewID;
	}
    
	public Map<Long, OverviewQuery> getOverviewQueries() {
		return this.overviewQueries;
	}

	public void setOverviewQueries(Map<Long, OverviewQuery> overviewQueries) {
		this.overviewQueries = overviewQueries;
	}
	
	/**
	* Create the Overview in DB 
	* 
	* @return <code>true</code> if this Overview has successfully been saved
	*/
	public boolean createNewOverview()
	{
		return OverviewDaoHelper.getOverviewDao().persistOverview(this);
	}
	
    
    public static Overview createDefault(String name,boolean isPublic) {
        final Overview newOverview = new Overview();     
        newOverview.setName(name);
        newOverview.setIsPublic(isPublic);
        return newOverview;
    }
	
    /**
     * Erases this overview.
     * 
     * @return <code>true</code> if this overview has been successfully deleted
     */
    public boolean delete() {
        return OverviewDaoHelper.getOverviewDao().delete(this);
    }


    /**
     * Gets a overview from an identifying string.
     * 
     * @param   idString    a string containing either the overview's identifier or 
     *                      name
     * @return              the overview, if it's been found or<br>
     *                      <code>null</code> otherwise
     */
    public static Overview getFromIdString(String idString) {

        if (StringTools.isNullOrEmpty(idString)) {
            throw new IllegalArgumentException(
                   "Overview identifier string can't be null or empty.");
        }

        return OverviewDaoHelper.getOverviewDao().getOverviewFromIdString(idString);
    }



    /**
     * Gets a overview from its identifier.
     * 
     * @param   searchedOverviewId   the long identifying the overview
     * @return                  the overview if it's been found or<br>
     *                          <code>null</code> otherwise
     */
    public static Overview getFromId(long searchedOverviewId) {

        return OverviewDaoHelper.getOverviewDao().getOverviewById(searchedOverviewId);

    }



    /**
     * Gets a overview from its name.
     * 
     * @param   searchedJobName the name identifying the overview
     * @return                  the overview if it's been found or<br>
     *                          <code>null</code> otherwise
     */
    public Overview getOverview(String searchedOverviewName) {

        return OverviewDaoHelper.getOverviewDao().getOverviewByName(searchedOverviewName);

    }
  

    /**
     * Saves this overview.
     * 
     * @return <code>true</code> if this overview has successfully been saved
     */
    public boolean persist() {

        return OverviewDaoHelper.getOverviewDao().persistOverview(this);
    }
}

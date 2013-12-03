/**
 * 
 */
package org.easysdi.monitor.gui.webapp;

import java.util.Map;

import org.deegree.framework.util.BooleanUtil;
import org.easysdi.monitor.biz.job.Overview;

/**
 * @author BERG3428
 *
 */
public class OverviewInfo {

	 private String  name;
	 private Boolean isPublic;

	/**
    * 
	*/
	public OverviewInfo() {
	}
	/**
	 * @return the name
	 */
	private String getName() {
		return name;
	}

	/**
	 * @param name the url to set
	 */
	private void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the isPublic
	 */
	private Boolean getIsPublic() {
		return isPublic;
	}

	/**
	 * @param isPublic the usePassword to set
	 */
	private void setIsPublic(String isPublic) {
		this.isPublic = BooleanUtil.parseBooleanStringWithNull(isPublic);
	}

	/**
	 * @param isPublic the isPublic to set
	 */
	private void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}



	
	
	
	/**
     * Creates a overviewpage info object from a servlet request's parameters.
     * 
     * @param   requestParams       a map containing the request parameters
     * @param   enforceMandatory    <code>true</code> to fail if a mandatory
     *                              property isn't set
     *                              <p>
     *                              Basically, you should set this parameter 
     *                              to <code>true</code> if you intend to create
     *                              a new job and to <code>false</code> to 
     *                              modify an existing one.
     * @return                      <ul>
     *                              <li>the job info if it has been successfully
     *                              created</li>
     *                              <li><code>null</code> otherwise</li>
     *                              </ul>
     * @throws  MandatoryParameterException a null value was assigned to a 
     *                                      mandatory parameter 
     */
    public static OverviewInfo createFromParametersMap(
            Map<String, String> requestParams, Boolean enforceMandatory) 
        throws MandatoryParameterException {
        
        final OverviewInfo newOverviewPageInfo = new OverviewInfo();
        
        newOverviewPageInfo.setName(requestParams.get("name"));
        newOverviewPageInfo.setIsPublic(requestParams.get("isPublic"));
        if(newOverviewPageInfo.isPublic == null)
        {
        	newOverviewPageInfo.setIsPublic(false);
        }
        
        
        return newOverviewPageInfo;
    }
    
    /**
     * Creates a new overview from this object's information.
     * 
     * @return  <ul>
     *          <li>the overviewpage if it has been created successfully</li>
     *          <li><code>null</code> otherwise</li>
     *          </ul>
     * @throws  MandatoryParameterException a null value was assigned to a 
     *                                      mandatory parameter 
     */
    public Overview createOverview() throws MandatoryParameterException {
    	final Overview newOverview = Overview.createDefault(this.getName(),this.getIsPublic());
    	if(newOverview.createNewOverview())
    	{
    		return newOverview;
    	}
    	return null;
    }
    
}

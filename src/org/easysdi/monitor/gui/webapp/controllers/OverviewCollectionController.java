/**
 * 
 */
package org.easysdi.monitor.gui.webapp.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easysdi.monitor.biz.job.OverviewsCollection;
import org.easysdi.monitor.biz.job.Overview;
import org.easysdi.monitor.gui.webapp.MandatoryParameterException;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.OverviewInfo;
import org.easysdi.monitor.gui.webapp.security.SecurityConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author chri1509
 *
 */
@Controller
@RequestMapping({ "/overviews" })
public class OverviewCollectionController extends AbstractMonitorController {

	/**
	 * 
	 */
	public OverviewCollectionController() {
	}
    /**
     * Creates a new overviewpage.
     * 
     * @param   request                     the request that asked for the 's
     *                                      creation
     * @param   response                    the response to the request
     * @return                              an object containing the new job's
     *                                      information and which view must be
     *                                      used to display it
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>one of the overviewpage's parameters has
     *                                      an invalid value</li>
     *                                      <li>a lower-level error prevented 
     *                                      saving the new job</li>
     *                                      </ul>   
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request,
                               HttpServletResponse response)
        throws MonitorInterfaceException {
        //this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
        final ModelAndView result = new ModelAndView("overview");
        final Map<String, String> requestParams = this.getRequestParametersMap(request);
        Overview newOverview;

        try {
            final OverviewInfo overviewInfo = OverviewInfo.createFromParametersMap(requestParams, true);
            newOverview = overviewInfo.createOverview();
        } catch (MandatoryParameterException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "overviewpage.argument.illegal", e);
        } catch (IllegalArgumentException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "overviewpage.argument.illegal", e);
            
        } catch (IllegalStateException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "overviewpage.persist.error", e);
        }
        result.addObject("message", "overviewpage.create.success");
        result.addObject(newOverview);

        return result;
    }
    @RequestMapping(method = RequestMethod.GET) 
    public ModelAndView show(HttpServletRequest request,
                             HttpServletResponse response) 
        throws MonitorInterfaceException {
        final ModelAndView result = new ModelAndView("overviewsCollection");
 
        final boolean useAdminCollection 
            = this.isAdminRequest(request); 
        
        if (useAdminCollection) {
            this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
        }
        
        final Boolean onlyPublic = (useAdminCollection) ? null : true;

        result.addObject("overviewList",
                         new OverviewsCollection().findOverviews(onlyPublic));

        result.addObject("message", "overviewsCollection.details.success");

        return result;
    }
    
}

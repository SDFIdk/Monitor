/**
 * 
 */
package org.easysdi.monitor.gui.webapp.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easysdi.monitor.biz.job.PeriodCollection;
import org.easysdi.monitor.biz.job.Sla;
import org.easysdi.monitor.biz.job.Period;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.PeriodInfo;
//import org.easysdi.monitor.gui.webapp.security.SecurityConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author berg3428
 *
 */
@Controller
@RequestMapping ({"/sla/{slaIdString}/period", 
"/adminSla/{slaIdString}/period" })
public class PeriodCollectionController extends AbstractMonitorController {

	/**
	 * 
	 */
	public PeriodCollectionController() {
	}
	
	/**
     * Creates a new period for the sla.
     * 
     * @param   request     the request that asked for the period creation 
     * @param   response    the response to the request
     * @param   slaIdString a string identifying the parent sla of the new 
     *                      period. It can contain its identifier or its name
     * @return              an object containing the creation result and which
     *                      view must be used to display it
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the sla doesn't exist</li>
     *                                      <li>an error occurred during the 
     *                                      creation of the sla</li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String slaIdString)
        throws MonitorInterfaceException {

    	// TODO
        // this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
        
    	final ModelAndView result = new ModelAndView("period");
        final Map<String, String> requestParams = this.getRequestParametersMap(request);

        try {
            final Sla sla = this.getSla(slaIdString);
            final PeriodInfo periodInfo = PeriodInfo.createFromParametersMap(requestParams, sla, true);
            final Period newPeriod = periodInfo.createPeriod();
            
            if (newPeriod != null) {
                result.addObject("message", "period.create.success");
                result.addObject(newPeriod);
                return result;
            }

        } catch (IllegalArgumentException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "period.argument.illegal");
        } catch (IllegalStateException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "period.persist.error");
        }
        throw new MonitorInterfaceException(
                "An error prevented the creation of the period", 
                "period.create.error");
    }
    
    /**
     * Shows the period collection for a sla.
     * 
     * @param   request         the request that asked for the details of the 
     *                          collection
     * @param   response        the response to the request
     * @param   slaIdString     a string identifying the parent sla of the new 
     *                          period. It can contain its identifier or its name
     * @return                  an object containing the details of the 
     *                          period collection and which view must be used to
     *                          display them
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the sla doesn't exist</li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request,
                             HttpServletResponse response,
                             @PathVariable String slaIdString)
        throws MonitorInterfaceException {
    	
        final ModelAndView result = new ModelAndView("periodCollection");
        final Sla sla = this.getSla(slaIdString);
        result.addObject("message", "periodCollection.details.success");
        result.addObject("periodList", new PeriodCollection().findSlaPeriods(sla.getSlaId()));
        return result;
    }
}

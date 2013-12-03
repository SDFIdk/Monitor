/**
 * 
 */
package org.easysdi.monitor.gui.webapp.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.deegree.framework.util.BooleanUtil;
import org.easysdi.monitor.biz.job.SlaCollection;
import org.easysdi.monitor.biz.job.Sla;
import org.easysdi.monitor.gui.webapp.MandatoryParameterException;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
//import org.easysdi.monitor.gui.webapp.security.SecurityConstants;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author berg3428
 *
 */
@Controller
@RequestMapping({ "/sla" })
public class SlaCollectionController extends AbstractMonitorController{

	/**
     * Creates a new sla.
     * 
     * @param   request                     the request that asked for the 's
     *                                      creation
     * @param   response                    the response to the request
     * @return                              an object containing the new job's
     *                                      information and which view must be
     *                                      used to display it
     * @throws  MonitorInterfaceException   
     *                                       
     */
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView create(HttpServletRequest request,
                               HttpServletResponse response)
        throws MonitorInterfaceException {
    	
    	// NO NEED
        //this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
        
    	final ModelAndView result = new ModelAndView("sla");
        final Map<String, String> requestParams = this.getRequestParametersMap(request);
        Sla newSla;
        try {
        	
        	final String slaName = requestParams.get("name");
        	if(slaName == null || !StringUtils.isNotBlank(slaName))
        	{
        		 throw new MandatoryParameterException("name");
        	}
        	final boolean excludeWorst = BooleanUtil.parseBooleanStringWithNull(requestParams.get("isExcludeWorst"));
            final boolean measureTimeToFirst = BooleanUtil.parseBooleanStringWithNull(requestParams.get("isMeasureTimeToFirst"));
            newSla = new Sla(slaName,excludeWorst,measureTimeToFirst);
            
            if(!newSla.persist())
            {
            	throw new IllegalStateException("Sla couldn't be created");
            }
        } catch (MandatoryParameterException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "sla.argument.illegal", e);
        } catch (IllegalArgumentException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "sla.argument.illegal", e);
            
        } catch (IllegalStateException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "sla.persist.error", e);
        }
    	result.addObject("message", "sla.create.success");
        result.addObject(newSla);
        return result;
    }
    
    @RequestMapping(method = RequestMethod.GET) 
    public ModelAndView show(HttpServletRequest request,
                             HttpServletResponse response) 
        throws MonitorInterfaceException {
        final ModelAndView result = new ModelAndView("slaCollection");
 
        //final boolean useAdminCollection = this.isAdminRequest(request); 
        
      /*if (useAdminCollection) {
            this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
        }
        */
        //final Boolean onlyPublic = (useAdminCollection) ? null : true;
        // TODO Create sla collenction
        result.addObject("slaList", new SlaCollection().getSla());
        
        result.addObject("message", "slaCollection.details.success");

        return result;
    }

}
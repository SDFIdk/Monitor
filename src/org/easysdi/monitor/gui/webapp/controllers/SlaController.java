/**
 * 
 */
package org.easysdi.monitor.gui.webapp.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.commons.lang.StringUtils;
import org.deegree.framework.util.BooleanUtil;
import org.easysdi.monitor.biz.job.Sla;
//import org.easysdi.monitor.gui.webapp.MandatoryParameterException;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author BERG3428
 *
 */
@Controller
@RequestMapping({ "/sla/{identifyString}", "adminSla/{identifyString}" })
public class SlaController extends AbstractMonitorController {

	public SlaController()
	{
		
	}
	 
	/**
     * Deletes an sla.
     * 
     * @param   request                     the request that asked for the sla's
     *                                      deletion
     * @param   response                    the response to the request
     * @param   identifyString              the string that identifies the sla.
     *                                      It can contain its identifier or its
     *                                      name 
     * @return                              an object containing a success 
     *                                      message and which view must be used
     *                                      to display it
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the sla doesn't exist</li>
     *                                      <li>the sla couldn't be deleted</li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ModelAndView delete(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String identifyString)
        throws MonitorInterfaceException {

        final ModelAndView result = new ModelAndView("message");
        final Sla sla = this.getSla(identifyString);

        if (sla.delete()) {
            result.addObject("message", "sla.delete.success");
            return result;
        }
        
        throw new MonitorInterfaceException("Unable to delete the sla",
                                            "sla.delete.error");
    }
	
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView get(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String identifyString)
        throws MonitorInterfaceException {

        final ModelAndView result = new ModelAndView("sla");
        final Sla sla = this.getSla(identifyString);
        result.addObject("sla",sla);
		result.addObject("message", "sla.details.success");
		return result;
    } 
	
    @RequestMapping(method = RequestMethod.PUT)
    public ModelAndView put(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String identifyString)
        throws MonitorInterfaceException {
        final ModelAndView result = new ModelAndView("sla");
        final Map<String, String> requestParams = this.getRequestParametersMap(request);
		Sla sla;
		try
		{
			sla = this.getSla(identifyString);
			String slaName = requestParams.get("name"); 
			if(slaName != null)
        	{
				sla.setName(slaName);
        	}
			if(requestParams.get("isExcludeWorst") != null)
			{
				sla.setExcludeWorst(BooleanUtil.parseBooleanStringWithNull(requestParams.get("isExcludeWorst")));
			}
			if(requestParams.get("isMeasureTimeToFirst") != null)
			{
				sla.setMeasureTimeToFirst(BooleanUtil.parseBooleanStringWithNull(requestParams.get("isMeasureTimeToFirst")));
			}
						    
            if (sla.persist()) {
                result.addObject("message", "sla.modify.success");
                result.addObject(sla);
                return result;
            }
	
	    }	 
		catch (IllegalArgumentException e) {
	        throw new MonitorInterfaceException(e.getMessage(),
	                                            "sla.argument.illegal", e);
	    }
	        
	    throw new MonitorInterfaceException("Unable to modify the sla",
	                                        "sla.modify.error");
    }
}

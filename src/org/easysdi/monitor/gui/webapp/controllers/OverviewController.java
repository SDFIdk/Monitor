/**
 * 
 */
package org.easysdi.monitor.gui.webapp.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.deegree.framework.util.BooleanUtil;
import org.easysdi.monitor.biz.job.Overview;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Process the requests concerning a overviewpage.
 * 
 * @author BERG3428
 *
 */
@Controller
@RequestMapping({ "/overviews/{identifyString}", "adminOverviews/{identifyString}" })
public class OverviewController extends AbstractMonitorController {

	public OverviewController()
	{}
	
	   /**
     * Deletes an overview.
     * 
     * @param   request                     the request that asked for the overview's
     *                                      deletion
     * @param   response                    the response to the request
     * @param   identifyString              the string that identifies the overview.
     *                                      It can contain its identifier or its
     *                                      name 
     * @return                              an object containing a success 
     *                                      message and which view must be used
     *                                      to display it
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the overview doesn't exist</li>
     *                                      <li>the overview couldn't be deleted</li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ModelAndView delete(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String identifyString)
        throws MonitorInterfaceException {

        final ModelAndView result = new ModelAndView("message");
        final Overview overview = this.getOverview(identifyString);

        if (overview.delete()) {
            result.addObject("message", "overview.delete.success");
            
            return result;
            
        }
        
        throw new MonitorInterfaceException("Unable to delete the overview",
                                            "overview.delete.error");
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView get(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String identifyString)
        throws MonitorInterfaceException {

        final ModelAndView result = new ModelAndView("message");
        final Overview overview = this.getOverview(identifyString);
        result.addObject("overview",overview);

		result.addObject("message", "overviews.details.success");
		   
		return result;
    }  
	
    @RequestMapping(method = RequestMethod.PUT)
    public ModelAndView put(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String identifyString)
        throws MonitorInterfaceException {
        final ModelAndView result = new ModelAndView("overview");
        final Map<String, String> requestParams 
            = this.getRequestParametersMap(request);
		Overview overview;
		try
		{
			overview = this.getOverview(identifyString);
			overview.setIsPublic(BooleanUtil.parseBooleanStringWithNull(requestParams.get("isPublic")));
			
            if (overview.persist()) {
                result.addObject("message", "overview.modify.success");
                result.addObject(overview);
    
                return result;
    
            }
	
	    } catch (IllegalArgumentException e) {
	        throw new MonitorInterfaceException(e.getMessage(),
	                                            "overview.argument.illegal", e);
	    }
	        
	    throw new MonitorInterfaceException("Unable to modify the overview",
	                                        "overview.modify.error");
    }
}

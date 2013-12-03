package org.easysdi.monitor.gui.webapp.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easysdi.monitor.biz.job.OverviewQueryViewCollection;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({ "/overviews/{identifyString}/queries"})//, "adminOverviews/{identifyString}/queries" })
public class OverviewQueryCollectionController extends AbstractMonitorController {

	public OverviewQueryCollectionController() {
	}

	@RequestMapping(method = RequestMethod.GET) 
	    public ModelAndView show(HttpServletRequest request,
	                             HttpServletResponse response,
	                             @PathVariable String identifyString) 
	        throws MonitorInterfaceException {
	        final ModelAndView result = new ModelAndView("overviewQueryCollection");
	 
//	        final boolean useAdminCollection 
//	            = this.isAdminRequest(request); 
	        
//	        if (useAdminCollection) {
//	            this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
//	        }
	        
//	        final Boolean onlyPublic = (useAdminCollection) ? null : true;

	        result.addObject("overviewQueryCollection",
	                         new OverviewQueryViewCollection().findOverviews(identifyString));

	        result.addObject("message", "overviewQueryCollection.details.success");

	        return result;
	    }

}

package org.easysdi.monitor.gui.webapp.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.deegree.framework.util.BooleanUtil;
import org.easysdi.monitor.biz.job.Overview;
import org.easysdi.monitor.biz.job.OverviewQuery;
import org.easysdi.monitor.biz.job.OverviewQueryView;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({ "/overviews/{identifyString}/queries/{queryId}", "adminOverviews/{identifyString}/queries/{queryId}" })
public class OverviewQueryController extends AbstractMonitorController {

	@RequestMapping(method = RequestMethod.PUT)
	    public ModelAndView put(HttpServletRequest request,
	                               HttpServletResponse response,
	                               @PathVariable String identifyString,
	                               @PathVariable String queryId){
        final ModelAndView result = new ModelAndView("overviewQueryView");
        final Map<String, String> requestParams 
            = this.getRequestParametersMap(request);
    	
        final boolean isPublic =BooleanUtil.parseBooleanStringWithNull(requestParams.get("isPublic"));
        OverviewQuery overviewQuery = OverviewQuery.getFromIdStrings(identifyString, queryId);

        if(isPublic)
        {
         	//If it does not exist already create entry in OverviewQuery
            if (null == overviewQuery) {
            	overviewQuery = new OverviewQuery();
            	Overview overview = Overview.getFromIdString(identifyString);
 
            	overviewQuery.setOverviewId(overview.getOverviewID());
            	overviewQuery.setQueryIdString(queryId);
            	overviewQuery.persist();
            }        	
        }
        else
        {
        	//If it exists delete entry in OverviewQuery
            if (null != overviewQuery) {
            	overviewQuery.delete();
            }        	
        }
        OverviewQueryView overviewQueryView = OverviewQueryView.getFromIdStrings(identifyString, queryId);
        result.addObject("message", "overviewQuery.modify.success");
        result.addObject(overviewQueryView);

        return result;
	}
	
	   @RequestMapping(method = RequestMethod.GET)
	    public ModelAndView get(HttpServletRequest request,
	                               HttpServletResponse response,
	                               @PathVariable String identifyString,
	                               @PathVariable String queryId)
	        throws MonitorInterfaceException {

	        final ModelAndView result = new ModelAndView("overviewQueryView");
	        OverviewQueryView overviewQueryView = OverviewQueryView.getFromIdStrings(identifyString, queryId);
	        result.addObject(overviewQueryView);

			result.addObject("message", "overviewQuery.details.success");
			   
			return result;
	    }  
}

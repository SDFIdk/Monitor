package org.easysdi.monitor.gui.webapp.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easysdi.monitor.biz.job.Query;
import org.easysdi.monitor.biz.job.QueryValidationSettings;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.security.SecurityConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({ "/jobs/{jobIdString}/queries/{queryIdString}/validationSettings",
"/adminJobs/{jobIdString}/queries/{queryIdString}/validationSettings" })
public class QueryValidationSettingsController extends AbstractMonitorController {

	public QueryValidationSettingsController(){

	}

	@RequestMapping(method = RequestMethod.PUT)
	public ModelAndView modify(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable String jobIdString,
			@PathVariable String queryIdString)
	throws MonitorInterfaceException {
		this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
		final ModelAndView result = new ModelAndView("queryValidationSettings");
		final Map<String, String> requestParams
		= this.getRequestParametersMap(request);

		try {
			final Query parentQuery			
			= this.getQuery(jobIdString, queryIdString);
			QueryValidationSettings queryValidationSettings
				=parentQuery.getQueryValidationSettings();
			if(queryValidationSettings == null)
			{
				queryValidationSettings= new QueryValidationSettings();
			}
			queryValidationSettings.setQueryId(parentQuery.getQueryId());
			
		       final Boolean useSizeValidation = Boolean.valueOf(requestParams.get("useSizeValidation"));
		       final String normSizeString = requestParams.get("normSize");
		       final String normSizeToleranceString = requestParams.get("normSizeTolerance");
		       final Boolean useTimeValidation = Boolean.valueOf(requestParams.get("useTimeValidation"));
		       final String normTimeString = requestParams.get("normTime");
		       final Boolean useXpathValidation = Boolean.valueOf(requestParams.get("useXpathValidation"));
		       final String xpathExpression = requestParams.get("xpathExpression");
		       final String expectedXpathOutput = requestParams.get("expectedXpathOutput");
		       final Boolean useTextValidation = Boolean.valueOf(requestParams.get("useTextValidation"));
		       final String expectedTextMatch = requestParams.get("expectedTextMatch");
		       
		        long normSize = 0;
		        Float normSizeTolerance = null;
		        Float normTime = null;
		       
		       try
		       {
		    	   normSize = Long.parseLong(normSizeString);
		       }
		       catch(NumberFormatException e)
		       {
		       }
		       try
		       {
		    	   normSizeTolerance = Float.parseFloat(normSizeToleranceString);
		       }
		       catch(NumberFormatException e)
		       {
		       }
		       try
		       {
		    	   normTime = Float.parseFloat(normTimeString);
		       }
		       catch(NumberFormatException e)
		       {
		       }
		   	   
		       queryValidationSettings.setUseSizeValidation(useSizeValidation);
		       queryValidationSettings.setNormSize(normSize);
		       queryValidationSettings.setNormSizeTolerance(normSizeTolerance);
		       queryValidationSettings.setUseTimeValidation(useTimeValidation);
		       queryValidationSettings.setNormTime(normTime);
		       queryValidationSettings.setUseXpathValidation(useXpathValidation);
		       queryValidationSettings.setXpathExpression(xpathExpression);
		       queryValidationSettings.setExpectedXpathOutput(expectedXpathOutput);
		       queryValidationSettings.setUseTextValidation(useTextValidation);
		       queryValidationSettings.setExpectedTextMatch(expectedTextMatch);
			if (queryValidationSettings.persist()) {
				result.addObject("message", "queryValidationSettings.modify.success");
				result.addObject(queryValidationSettings);
				return result;
			
			}

		} catch (IllegalArgumentException e) {
			throw new MonitorInterfaceException(e.getMessage(),
			"queryValidationSettings.argument.illegal");
		}

		throw new MonitorInterfaceException("Unable to modify the query",
		"queryValidationSettings.modify.error");
	}

}

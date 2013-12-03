/**
 * 
 */
package org.easysdi.monitor.gui.webapp.controllers;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.easysdi.monitor.biz.job.Period;
import org.easysdi.monitor.gui.webapp.PeriodInfo;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author berg3428
 *
 */
@Controller
@RequestMapping ({"/sla/{slaIdString}/period/{periodIdString}",
"/admin/{slaIdString}/period/{periodIdString}"})
public class PeriodController extends AbstractMonitorController {

	/**
	 * Default constructor
	 */
	public PeriodController() {
		
	}
	
	@RequestMapping(method = RequestMethod.DELETE)
	 public ModelAndView delete(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String slaIdString,
                               @PathVariable String periodIdString)
        throws MonitorInterfaceException {
		
		final ModelAndView result = new ModelAndView("message");
		final Period period = this.getPeriod(periodIdString);
		
		if(period.delete())
		{
		     result.addObject("message", "period.delete.success");
	         return result;
		}
			
		 throw new MonitorInterfaceException("Unable to delete the period",
         "period.delete.error");
	}
	
	
	@RequestMapping(method = RequestMethod.PUT)
	 public ModelAndView modify(HttpServletRequest request,
                              HttpServletResponse response,
                              @PathVariable String slaIdString,
                              @PathVariable String periodIdString)
       throws MonitorInterfaceException {
		
		final ModelAndView result = new ModelAndView("period");
        final Map<String, String> requestParams = this.getRequestParametersMap(request);
		Period period;
		Period modifiedPeriod;
		try
		{
			// Get current period
			period = this.getPeriod(periodIdString);
			modifiedPeriod = PeriodInfo.createModifyParametersMap(requestParams,period);
			
			if(modifiedPeriod.isValid() && PeriodInfo.modifyPeriod(modifiedPeriod))
			{
				result.addObject("message", "period.modify.success");
	            result.addObject(modifiedPeriod);
	            return result;
			}
		}catch (IllegalArgumentException e) {
	        throw new MonitorInterfaceException(e.getMessage(),
                    "period.argument.illegal", e);
		}
		 throw new MonitorInterfaceException("Unable to modify the period",
        "period.modify.error");
	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	 public ModelAndView get(HttpServletRequest request,
                              HttpServletResponse response,
                              @PathVariable String slaIdString,
                              @PathVariable String periodIdString)
       throws MonitorInterfaceException {
		final ModelAndView result = new ModelAndView("period");
        final Period period = this.getPeriod(periodIdString);
        result.addObject("period",period);
		result.addObject("message", "period.details.success");
		return result;
	}
}
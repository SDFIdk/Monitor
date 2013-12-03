/**
 * 
 */
package org.easysdi.monitor.gui.webapp.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.easysdi.monitor.biz.job.Holiday;
import org.easysdi.monitor.gui.webapp.MandatoryParameterException;
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
@RequestMapping ({ "/holidays/{identifyString}", "adminHolidays/{identifyString}" })
public class HolidayController extends AbstractMonitorController {

	/**
	 * Dummy construtor
	 */
	public HolidayController() {
	}
	
	/**
     * Deletes an holiday.
     * 
     * @param   request                     the request that asked for the holiday's
     *                                      deletion
     * @param   response                    the response to the request
     * @param   identifyString              the string that identifies the holiday.
     *                                      It can contain its identifier or its
     *                                      name 
     * @return                              an object containing a success 
     *                                      message and which view must be used
     *                                      to display it
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the holiday doesn't exist</li>
     *                                      <li>the holiday couldn't be deleted</li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public ModelAndView delete(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String identifyString)
        throws MonitorInterfaceException {

        final ModelAndView result = new ModelAndView("message");
        final Holiday holiday  = this.getHoliday(identifyString);

        if (holiday.delete()) {
            result.addObject("message", "holiday.delete.success");
            return result;
        }
        
        throw new MonitorInterfaceException("Unable to delete the sla",
                                            "holiday.delete.error");
    }
	
    
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView show(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String identifyString)
        throws MonitorInterfaceException {

        final ModelAndView result = new ModelAndView("holiday");
        final Holiday holiday = this.getHoliday(identifyString);
        result.addObject("holiday",holiday);
		result.addObject("message", "holiday.details.success");
		return result;
    } 
	
    @RequestMapping(method = RequestMethod.PUT)
    public ModelAndView modify(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable String identifyString)
        throws MonitorInterfaceException {
        final ModelAndView result = new ModelAndView("holiday");
        // ONLY works with contenttype JSON
        final Map<String, String> requestParams = this.getRequestParametersMap(request);
		Holiday holiday;
		try
		{
			holiday = this.getHoliday(identifyString);
			String holidayName = requestParams.get("name"); 
			if(holidayName != null)
        	{
				holiday.setName(holidayName);
        	}

			final String holidayStrDate =  requestParams.get("date");
        	if(holidayStrDate != null)
        	{
        		if(!StringUtils.isNotBlank(holidayStrDate))
        		{
        			throw new MandatoryParameterException("date");
        		}
        		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                final Calendar holidayDate = Calendar.getInstance();
                try {
                	holidayDate.setTime(dateFormat.parse(holidayStrDate));
                	holiday.setDate(holidayDate);
                } catch (ParseException e) {
                    throw new MonitorInterfaceException(
                            "Holiday date format is invalid", "holiday.date.illegal");
                }
        	}
						    
            if (holiday.persist()) {
                result.addObject("message", "holiday.modify.success");
                result.addObject(holiday);
                return result;
            }
	
	    }catch(MandatoryParameterException e)
	    {
	    	throw new MonitorInterfaceException(e.getMessage(),
                    "holiday.argument.illegal", e);
	    }
		catch (IllegalArgumentException e) {
	        throw new MonitorInterfaceException(e.getMessage(),
	                                            "holiday.argument.illegal", e);
	    }
	    throw new MonitorInterfaceException("Unable to modify the holiday",
	                                        "holiday.modify.error");
    }
}

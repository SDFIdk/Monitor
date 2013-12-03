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
import org.easysdi.monitor.biz.job.HolidayCollection;
import org.easysdi.monitor.gui.webapp.MandatoryParameterException;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author BERG3428
 *
 */
@Controller 
@RequestMapping ({"/holidays"})
public class HolidayCollectionController extends AbstractMonitorController {

	/**
	 * 
	 */
	public HolidayCollectionController() {
	}
	
	@RequestMapping (method = RequestMethod.POST)
	public ModelAndView create(HttpServletRequest request,HttpServletResponse response)
	throws MonitorInterfaceException
	{
		final ModelAndView result = new ModelAndView("holiday");
        final Map<String, String> requestParams = this.getRequestParametersMap(request);
        Holiday newHoliday;

        // NO NEED
        //this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
        
    	try {	
        	final String holidayName = requestParams.get("name");
        	final String holidayStrDate =  requestParams.get("date");
        	if(holidayStrDate == null || !StringUtils.isNotBlank(holidayStrDate))
        	{
        		 throw new MandatoryParameterException("date");
        	}
        	
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            final Calendar holidayDate = Calendar.getInstance();

            try {
            	holidayDate.setTime(dateFormat.parse(holidayStrDate));
            } catch (ParseException e) {
                throw new MonitorInterfaceException(
                        "Holiday date format is invalid", "holiday.date.illegal");
            }
        	
            newHoliday = new Holiday(holidayName,holidayDate);
            
            if(!newHoliday.persist())
            {
            	throw new IllegalStateException("Holiday couldn't be created");
            }
        } catch (MandatoryParameterException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "holiday.argument.illegal", e);
        } catch (IllegalArgumentException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "holiday.argument.illegal", e);
            
        } catch (IllegalStateException e) {
            throw new MonitorInterfaceException(e.getMessage(),
                                                "holiday.persist.error", e);
        }
    	result.addObject("message", "holiday.create.success");
        result.addObject(newHoliday);
        return result;
	}
	
	@RequestMapping(method = RequestMethod.GET) 
    public ModelAndView show(HttpServletRequest request,
                             HttpServletResponse response) 
        throws MonitorInterfaceException {
        final ModelAndView result = new ModelAndView("holidayCollection");
 
        //final boolean useAdminCollection = this.isAdminRequest(request); 
      /*if (useAdminCollection) {
            this.enforceRole(SecurityConstants.ADMIN_ROLE, request, response);
        }
        */
        result.addObject("holidayList", new HolidayCollection().getHoliday());
        result.addObject("message", "holidayCollection.details.success");
        return result;
    }
}

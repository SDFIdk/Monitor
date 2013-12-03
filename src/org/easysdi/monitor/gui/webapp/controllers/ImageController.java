/**
 * 
 */
package org.easysdi.monitor.gui.webapp.controllers;



import java.io.OutputStream;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easysdi.monitor.biz.alert.Alert;
import org.easysdi.monitor.biz.job.OverviewLastQueryResult;
import org.easysdi.monitor.biz.job.QueryTestResult;

import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;






/**
 * @author berg3428
 *
 */
@Controller
@RequestMapping({ "/image/{classTypeString}/{idString}"})
public class ImageController{

	 /**
     * Creates a new controller.
     */
    public ImageController() {
        
    }
    
    /**
     * Gets image for an alert.
     * 
     * @param   request                     the request that asked for 
     *                                      displaying the alerts
     * @param   response                    the response to the request
     * 
     * @param classTypeString				the string that identifies where to get the
     * 										image from
     * 
     * @param   idString                    the idString that identifies the
     *                                      record to get the image from. It can only contain its
     *                                      identifier
     * @return                       		an response stream containing data for a given
     * 										content_type
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the xxx doesn't exist</li>
     *                                      </ul>
     */
    @RequestMapping(method = RequestMethod.GET)
    public void show(HttpServletRequest request,
                             HttpServletResponse response,
                             @PathVariable String classTypeString,
                             @PathVariable String idString)
        throws MonitorInterfaceException {
    	byte[] data = null;
    	String contentType = "";
    	OutputStream out;
    	if(classTypeString.toLowerCase().equals("alert"))
    	{
    		Alert alert = Alert.getFromIdString(idString);
    		data = alert.getImageError();
    		contentType = alert.getContentType();
    		
    		if(contentType != null && contentType != "" && data != null)
    		{
    			response.reset();
    			response.setContentType(contentType);
    			try
    	    	{
    	    		out = response.getOutputStream();    	    	
    	    		out.write(data);
    	    	    out.close();
    	    	}catch(Exception e)
    	    	{
    	    		throw new MonitorInterfaceException(e.getMessage(),
                    "image.stream.error",e);
    	    	}
    		}else
    		{
    			// TODO
    		}
    	}else if(classTypeString.toLowerCase().equals("lastoverview"))
    	{
    		OverviewLastQueryResult result = OverviewLastQueryResult.getFromIdString(idString);
    		data = result.getData();
    		contentType = result.getContentType();
    		if(contentType != null && contentType != "" && data != null)
    		{
    			response.reset();
    			response.setContentType(contentType);
    			try
    			{
    				out = response.getOutputStream();
    	    	    out.write(data);
    	    	    out.close();
    			}catch(Exception e)
    			{
    				throw new MonitorInterfaceException(e.getMessage(),
    	                    "image.stream.error",e);
    			}
    		}
    	}else if(classTypeString.toLowerCase().equals("preview"))
    	{
    		QueryTestResult result = QueryTestResult.getFromIdString(idString);
    		data = result.getData();
    		contentType = result.getContentType();
    		if(contentType != null && contentType != "" && data  != null)
    		{
    			response.reset();
    			response.setContentType(contentType);
    			try
    			{
    				out = response.getOutputStream();
    	    	    out.write(data);
    	    	    out.close();
    			}catch(Exception e)
    			{
    				throw new MonitorInterfaceException(e.getMessage(),
    	                    "image.stream.error",e);
    			}
    		}
    	}
    }
}

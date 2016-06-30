package org.easysdi.monitor.gui.webapp.controllers;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.job.*;
import org.easysdi.monitor.biz.logging.LastLog;
import org.easysdi.monitor.gui.webapp.AppContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Servlet implementation class ServiceApiGroupStatController
 */
@Controller
@RequestMapping({ "/serviceapi/groups/{jobId}/status" })
public class ServiceApiGroupStatusController extends AbstractMonitorController {
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServiceApiGroupStatusController() {
        super();
    }

	/**
	 * @throws Exception 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @RequestMapping(method = RequestMethod.GET)
	protected void doGet(HttpServletRequest request, HttpServletResponse response, @PathVariable String jobId) throws Exception {
    	
        final Map<String, String> requestParams = this.getRequestParametersMap(request);
                
        String dataOut;
        
        // Check if JSONP should be used
        String jsonp = "";
        if (requestParams.get("jsonpCallback") != null) {
        	jsonp=requestParams.get("jsonpCallback");
        }
                        
        final ObjectMapper mapper = AppContext.getContext().getBean("jsonMapper", ObjectMapper.class);
        
        try 
        {
        	Job job = Job.getFromIdString(jobId);
    		
    		final ObjectNode jsonEntry = mapper.createObjectNode();
    		final ObjectNode jsonStatusEntry = mapper.createObjectNode();

    		jsonEntry.put("GroupId", jobId);
    		jsonEntry.put("GroupName", job.getConfig().getJobName());
    		
    		String statusText = "";
    		Collection<org.easysdi.monitor.biz.job.Query> queries = job.getQueriesList();
    		Iterator<Query> iter = queries.iterator();
		    
    		while(iter.hasNext()) {
		    	  org.easysdi.monitor.biz.job.Query query = (Query) iter.next();
		    	  //LogManager queryLogManager = new LogManager(query);
		    	  //RawLogEntry log = queryLogManager.getLogFetcher().fetchLastLogBeforeDate(Calendar.getInstance());
		    	  LastLog log = LastLog.getLastLogQuery(query.getQueryId());
		    	  if(checkStatus(log.getStatus(),statusText))
		    	  {
		    		  statusText = log.getStatus();
		    	  }
		    }	
		    
			jsonEntry.put("Status", statusText);
    		jsonStatusEntry.put("success", true);
    		jsonStatusEntry.put("message", "Status data for group " + job.getConfig().getJobName() + " retrieved successfully");
    		jsonStatusEntry.put("data", jsonEntry);
	    	dataOut = jsonStatusEntry.toString();
        } 
        catch (Exception ex) 
        {
        	dataOut = CreateErrorMessage(ex.getMessage(), mapper);
        }

        if (!jsonp.equals("")) {
    		dataOut = jsonp + "(" + dataOut + ");";
    		response.setContentType("application/javascript");
    	} else {
        	response.setContentType("application/json");
    	}

		javax.servlet.ServletOutputStream out = response.getOutputStream();
		out.print(dataOut);
		out.flush();
	}

    private String CreateErrorMessage(String message, ObjectMapper mapper) {
    	final ObjectNode jsonEntry = mapper.createObjectNode();
    	ObjectNode noDataNode = mapper.createObjectNode();
    	jsonEntry.put("success", false);
    	jsonEntry.put("message", message);
    	jsonEntry.put("data", noDataNode);
    	return jsonEntry.toString();
    }
    
    private boolean checkStatus(String status,String current)
    {
    	boolean value = false;
    	if(status.equalsIgnoreCase("NOT_TESTED"))
    	{
    		if(current.isEmpty() || (!current.equalsIgnoreCase("UNAVAILABLE") && !current.equalsIgnoreCase("OUT_OF_ORDER") && !current.equalsIgnoreCase("AVAILABLE")))
    		{
    			value = true;
    		}	
    	}else if(status.equalsIgnoreCase("AVAILABLE"))
    	{
    		if(current.isEmpty() || (!current.equalsIgnoreCase("UNAVAILABLE") && !current.equalsIgnoreCase("OUT_OF_ORDER")))
    		{
    			value = true;
    		}
    	}else if(status.equalsIgnoreCase("OUT_OF_ORDER"))
    	{
    		if(current.isEmpty() || !current.equalsIgnoreCase("UNAVAILABLE"))
    		{
    			value = true;
    		}
    	}else if(status.equalsIgnoreCase("UNAVAILABLE"))
    	{
    		value = true;
    	}
    	return value;
    }
	
}

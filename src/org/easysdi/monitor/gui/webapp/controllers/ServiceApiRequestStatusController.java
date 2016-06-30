package org.easysdi.monitor.gui.webapp.controllers;

//import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.Query;
import org.easysdi.monitor.biz.logging.LastLog;
//import org.easysdi.monitor.biz.logging.LogManager;
//import org.easysdi.monitor.biz.logging.RawLogEntry;
import org.easysdi.monitor.gui.webapp.AppContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * Servlet implementation class ServiceApiGroupStatController
 */
@Controller
@RequestMapping({ "/serviceapi/groups/{jobId}/requests/{queryId}/status" })
public class ServiceApiRequestStatusController extends AbstractMonitorController {
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServiceApiRequestStatusController() {
        super();
    }

	/**
	 * @throws Exception 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @RequestMapping(method = RequestMethod.GET)
	protected void doGet(HttpServletRequest request, HttpServletResponse response, @PathVariable String jobId, @PathVariable String queryId) throws Exception {
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
    		Query query = Query.getFromIdStrings(jobId, queryId);
    	    //final LogManager queryLogManager = new LogManager(query);
    	    
    	    LastLog log = LastLog.getLastLogQuery(query.getQueryId());
    		//RawLogEntry log = queryLogManager.getLogFetcher().fetchLastLogBeforeDate(Calendar.getInstance());
    		
    	    final ObjectNode jsonEntry = mapper.createObjectNode();
    		final ObjectNode jsonStatusEntry = mapper.createObjectNode();

    		jsonEntry.put("GroupId", jobId);
    		jsonEntry.put("GroupName", job.getConfig().getJobName());
    		jsonEntry.put("RequestId", queryId);
    		jsonEntry.put("RequestName", query.getConfig().getQueryName());
    		if(log != null){
    			jsonEntry.put("Status", log.getStatus());
    		}else
    		{
    			jsonEntry.put("Status", "NOT_TESTED");
    		}
    		
    		//jsonEntry.put("Status", log.getStatus().getValue());
    		
    		/*
    		1	AVAILABLE
			2	OUT_OF_ORDER
			3	UNAVAILABLE
			4	NOT_TESTED
    		*/

    		jsonStatusEntry.put("success", true);
    		jsonStatusEntry.put("message", "Status data for request " + query.getConfig().getQueryName() + " retrieved successfully");
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
	
}

package org.easysdi.monitor.gui.webapp.controllers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.JobsCollection;
import org.easysdi.monitor.biz.job.Query;
import org.easysdi.monitor.biz.logging.LogManager;
import org.easysdi.monitor.biz.logging.RawLogEntry;
import org.easysdi.monitor.gui.webapp.AppContext;
import org.easysdi.monitor.gui.webapp.JobInfo;
import org.easysdi.monitor.gui.webapp.MandatoryParameterException;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Servlet implementation class JobStatus
 */
@Controller
@RequestMapping({ "/serviceapi/groupstatus" })
public class ServiceApiGroupRunningController extends AbstractMonitorController {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServiceApiGroupRunningController() {
        super();
    }
    
    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.GET)
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	final Map<String, String> requestParams = this.getRequestParametersMap(request);
    	 
    	// Check if JSONP should be used
         String jsonp = "";
         String jobIDStr = "";
         if (requestParams.get("jsonpCallback") != null) {
         	jsonp=requestParams.get("jsonpCallback");
         }
         if (requestParams.get("jobid") != null) {
          	jobIDStr =requestParams.get("jobid");
          }
                 
    	 String dataOut = "";
    	 final ObjectMapper mapper = AppContext.getContext().getBean("jsonMapper", ObjectMapper.class);
    	 try
    	 {
    		final ArrayNode jsonLogsCollection = mapper.createArrayNode();
	        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	if(jobIDStr != null && jobIDStr != "")
	    	{
	    	   Job job = Job.getFromIdString(jobIDStr);
	    	   boolean jobIsNotRunningOk = false;
			   if(job.getConfig().isAutomatic())
			   {
				   Collection<org.easysdi.monitor.biz.job.Query> queries = job.getQueriesList();
				   if(queries.size() == 0)
				   {
					  
				   }else
				   {
				   
					   ObjectNode jsonEntry = mapper.createObjectNode();
					   int seconds =job.getConfig().getTestInterval();
					   jsonEntry.put("GroupId", job.getJobId());
			    	   jsonEntry.put("GroupName", job.getConfig().getJobName());
			 		   jsonEntry.put("TestInterval_sec",seconds);
					   
			    	   Iterator iter = queries.iterator();
			    	   final ArrayNode jsonLogsCollectionQueryList = mapper.createArrayNode();
			    	   while(iter.hasNext()) {
			    		   	  ObjectNode jsonEntryQuery = mapper.createObjectNode();
			    		   	  org.easysdi.monitor.biz.job.Query query = (Query) iter.next();
					    	  LogManager queryLogManager = new LogManager(query);
					    	  RawLogEntry log = queryLogManager.getLogFetcher().fetchLastLogBeforeDate(Calendar.getInstance());
					    	  if(checkstatus(log.getRequestTime(),seconds))
					    	  {
					    		  jsonEntryQuery.put("Request", query.getConfig().getQueryName());
					    		  jsonEntryQuery.put("LastTest",dateFormat.format(log.getRequestTime().getTime()));
					    		  
					    	  }else
					    	  {
					    		  jsonEntryQuery.put("Request", query.getConfig().getQueryName());
					    		  jsonEntryQuery.put("LastTest",dateFormat.format(log.getRequestTime().getTime()));
					    		  jobIsNotRunningOk = true;
					    	  }
					    	  jsonLogsCollectionQueryList.add(jsonEntryQuery);
					    }
			    	   jsonEntry.put("Requests",jsonLogsCollectionQueryList);
			    	   if(jobIsNotRunningOk)
			    	   {
			    		   jsonEntry.put("Group_is_running",false);
			    	   }else
			    	   {
			    		   jsonEntry.put("Group_is_running",true);
			    	   }
			    	   jsonLogsCollection.add(jsonEntry);
				   }
			   }
	    	}else
	    	{
	        
		        for (Job job : new JobsCollection().getJobs()) {
		    	   boolean jobIsNotRunningOk = false;
				   if(job.getConfig().isAutomatic())
				   {
					   Collection<org.easysdi.monitor.biz.job.Query> queries = job.getQueriesList();
					   if(queries.size() == 0)
					   {
						   continue;
					   }
					   ObjectNode jsonEntry = mapper.createObjectNode();
					   int seconds =job.getConfig().getTestInterval();
					   jsonEntry.put("GroupId", job.getJobId());
			    	   jsonEntry.put("GroupName", job.getConfig().getJobName());
			 		   jsonEntry.put("TestInterval_sec",seconds);
					   
			    	   Iterator iter = queries.iterator();
			    	   final ArrayNode jsonLogsCollectionQueryList = mapper.createArrayNode();
			    	   while(iter.hasNext()) {
			    		   	  ObjectNode jsonEntryQuery = mapper.createObjectNode();
			    		   	  org.easysdi.monitor.biz.job.Query query = (Query) iter.next();
					    	  LogManager queryLogManager = new LogManager(query);
					    	  RawLogEntry log = queryLogManager.getLogFetcher().fetchLastLogBeforeDate(Calendar.getInstance());
					    	  if(checkstatus(log.getRequestTime(),seconds))
					    	  {
					    		  jsonEntryQuery.put("Request", query.getConfig().getQueryName());
					    		  jsonEntryQuery.put("LastTest",dateFormat.format(log.getRequestTime().getTime()));
					    		  
					    	  }else
					    	  {
					    		  jsonEntryQuery.put("Request", query.getConfig().getQueryName());
					    		  jsonEntryQuery.put("LastTest",dateFormat.format(log.getRequestTime().getTime()));
					    		  jobIsNotRunningOk = true;
					    	  }
					    	  jsonLogsCollectionQueryList.add(jsonEntryQuery);
					    }
			    	   jsonEntry.put("Requests",jsonLogsCollectionQueryList);
			    	   if(jobIsNotRunningOk)
			    	   {
			    		   jsonEntry.put("Group_is_running",false);
			    	   }else
			    	   {
			    		   jsonEntry.put("Group_is_running",true);
			    	   }
			    	   jsonLogsCollection.add(jsonEntry);
				   }
		    	 }
	    	}
    		dataOut = jsonLogsCollection.toString();
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
    
    private boolean checkstatus(Calendar logdate,int timeinverval)
    {
    	int extratime = 1800; // ½ hour
    	Calendar current = Calendar.getInstance();
    	long timeDifference = current.getTimeInMillis() - logdate.getTimeInMillis();
    	int timeDifferenceInSeconds =  (int)( timeDifference / 1000);
    	int maxInterval = timeinverval+extratime;
    	//System.out.println("Test: "+timeDifferenceInSeconds+" "+maxInterval); 
    	if(timeDifferenceInSeconds <= maxInterval)
    	{
    		return true;
    	}else
    	{
    		return false;
    	}
    }
}

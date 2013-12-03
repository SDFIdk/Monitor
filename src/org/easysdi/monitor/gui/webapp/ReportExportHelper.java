package org.easysdi.monitor.gui.webapp;

/**
 * Helper class for exporting logs
 * 
 * @author Peter Koch, Atkins Denmark
 * @version 1.0, 2013-04-22
 *
 */

import java.io.File; 
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.easysdi.monitor.biz.job.Job;
import org.easysdi.monitor.biz.job.Query;
import org.easysdi.monitor.biz.logging.AbstractAggregateHourLogEntry;
import org.easysdi.monitor.biz.logging.AbstractAggregateLogEntry;
import org.easysdi.monitor.biz.logging.RawLogEntry;
import org.easysdi.monitor.gui.webapp.views.json.serializers.AggregHourLogSerializer;
import org.easysdi.monitor.gui.webapp.views.json.serializers.AggregLogSerializer;
import org.easysdi.monitor.gui.webapp.views.json.serializers.RawLogSerializer;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.transform.*;

import org.easysdi.monitor.biz.logging.LogManager;
import org.springframework.web.servlet.ModelAndView;

public class ReportExportHelper {

	public static String ConvertJSONReportDataToXml(ArrayNode jsonLogData) {
		String jsonDataStr = jsonLogData.toString();
		jsonDataStr = "{\"data\": " + jsonDataStr + "}"; 
		JSONObject jsonObj = JSONObject.fromObject(jsonDataStr);
	    String xml = new XMLSerializer().write(jsonObj);  
		
		return xml;
	}
	
	public static String DoTransformation(String sourceXml, String xsltFileIdent) throws TransformerException, UnsupportedEncodingException, NamingException
	{
		File xsltFile = new File(GetXsltFromIdent(xsltFileIdent));
		
		java.io.InputStream sourceFile = new java.io.ByteArrayInputStream( sourceXml.getBytes("UTF-8" )); 
		java.io.StringWriter resultFile = new java.io.StringWriter();
				
		javax.xml.transform.Source xmlSource = new javax.xml.transform.stream.StreamSource(sourceFile);
	    javax.xml.transform.Source xsltSource = new javax.xml.transform.stream.StreamSource(xsltFile);
	    javax.xml.transform.Result result = new javax.xml.transform.stream.StreamResult(resultFile);

	    javax.xml.transform.TransformerFactory transFact = javax.xml.transform.TransformerFactory.newInstance();
	 
	    javax.xml.transform.Transformer trans = transFact.newTransformer(xsltSource);
	    trans.transform(xmlSource, result);
	    
	    String outResult = resultFile.toString();
	    
	    return outResult; 
	}
		
	@SuppressWarnings("unchecked")
	public static ArrayNode CreateJSONData_AggLog(LogManager logManager, Map<Date, AbstractAggregateLogEntry> logEntries, LogSearchParams searchParams, Job job, Query query, String slaName) 
	{
		final ModelAndView result = new ModelAndView("");
	
		String jobName = job.getConfig().getJobName();
		String queryName;
		if (query != null){ 
			queryName = query.getConfig().getQueryName();
			}
		else
		{
			queryName = "";
		}
		
		result.addObject("noPagingCount", logManager.getAggregateLogsItemsNumber(
        searchParams.getMinDate(), searchParams.getMaxDate(), null, null));
		result.addObject("message", "log.details.success");
		result.addObject("aggregLogsCollection", logEntries.values());
   
		result.addObject("getExport", true);
		result.addObject("Jobname", jobName);
		result.addObject("Queryname",queryName);
		result.addObject("Slaname",slaName);
	
		Map<String, ?> model = result.getModel();
 
		final Collection<AbstractAggregateLogEntry> logsCollection = (Collection<AbstractAggregateLogEntry>) model.get("aggregLogsCollection");
		final Long noPagingCount = (Long) model.get("noPagingCount");
	
		final ObjectMapper mapper = AppContext.getContext().getBean("jsonMapper", ObjectMapper.class);
	
		mapper.createObjectNode().put("noPagingCount", noPagingCount);
	
		final ArrayNode jsonLogsCollection = mapper.createArrayNode();

		for (AbstractAggregateLogEntry logEntry : logsCollection) {
			jsonLogsCollection.add(AggregLogSerializer.serialize(logEntry, mapper, jobName, queryName));
		}
		
		return jsonLogsCollection;
	
	}

	@SuppressWarnings("unchecked")
	public static ArrayNode CreateJSONData_AggHourLog(LogManager logManager, Map<Date, AbstractAggregateHourLogEntry> logEntries, LogSearchParams searchParams, Job job, Query query, String slaName) 
	{
		final ModelAndView result = new ModelAndView("");
	
		String jobName = job.getConfig().getJobName();
		String queryName;
		if (query != null){ 
			queryName = query.getConfig().getQueryName();
			}
		else
		{
			queryName = "";
		}
		
		result.addObject("noPagingCount", logManager.getAggregateHourLogsItemsNumber(
        searchParams.getMinDate(), searchParams.getMaxDate(), null, null));
		result.addObject("message", "log.details.success");
		result.addObject("aggregLogsCollection", logEntries.values());
   
		result.addObject("getExport", true);
		result.addObject("Jobname", jobName);
		result.addObject("Queryname",queryName);
		result.addObject("Slaname",slaName);
	
		Map<String, ?> model = result.getModel();
 
		final Collection<AbstractAggregateHourLogEntry> logsCollection = (Collection<AbstractAggregateHourLogEntry>) model.get("aggregLogsCollection");
		final Long noPagingCount = (Long) model.get("noPagingCount");
	
		final ObjectMapper mapper = AppContext.getContext().getBean("jsonMapper", ObjectMapper.class);
	
		mapper.createObjectNode().put("noPagingCount", noPagingCount);
	
		final ArrayNode jsonLogsCollection = mapper.createArrayNode();

		for (AbstractAggregateHourLogEntry logEntry : logsCollection) {
			jsonLogsCollection.add(AggregHourLogSerializer.serialize(logEntry, mapper, jobName, queryName, slaName));
		}
		
		return jsonLogsCollection;
	
	}
	
	
	public static ArrayNode CreateJSONData_RawLog(LogManager logManager, Set<RawLogEntry> logEntries, LogSearchParams searchParams, Job job, Query query, String slaName, Boolean isSummary) 
	{
		String jobName = job.getConfig().getJobName();
		String queryName;
		Float normResponseTime = null;
		
		if (query != null) {
			queryName = query.getConfig().getQueryName();
			normResponseTime = query.getQueryValidationSettings().getNormTime();
		} else {
			queryName = "";
		}
				
		final Set<RawLogEntry> logsCollection  = logEntries;
		final Boolean addQueryId = true;
		final Boolean getExport = true;
		
		final ObjectMapper mapper = AppContext.getContext().getBean("jsonMapper", ObjectMapper.class);  
		
		final ArrayNode rowsCollection = mapper.createArrayNode();
		
		List<RawLogEntry> sortLogs = new ArrayList<RawLogEntry>(logsCollection);
		Collections.sort(sortLogs, new RawLogSortingComparator());	 
		
		for (RawLogEntry logEntry : sortLogs) {
			rowsCollection.add(RawLogSerializer.serialize(logEntry,addQueryId,getExport,slaName,jobName,queryName,
                                                      Locale.getDefault(), mapper, isSummary, normResponseTime));
			
			
		}     
		return rowsCollection;
	
	}
		
	public static String GetXsltFromIdent(String xsltIdent) throws NamingException
	{
		Context env;
		env = (Context)new InitialContext().lookup("java:comp/env");
		String xsltFile = (String)env.lookup("xsltPath") + "/" + xsltIdent + ".xsl";
		
		return xsltFile;
	}
	
	public static String GenerateXsltOutputFilename(String xsltIdent)
	{
		return "result." + xsltIdent.substring(xsltIdent.indexOf("_") + 1);
		
	}
	
}

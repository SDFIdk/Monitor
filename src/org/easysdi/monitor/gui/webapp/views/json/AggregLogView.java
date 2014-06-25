package org.easysdi.monitor.gui.webapp.views.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;


import org.easysdi.monitor.biz.logging.AbstractAggregateLogEntry;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;

import org.easysdi.monitor.gui.webapp.views.json.serializers.AggregLogSerializer;

/**
 * Displays a set of aggregate logs in JSON format.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.2, 2010-08-06
 *
 */
public class AggregLogView extends AbstractJsonView {
    
    /**
     * Creates a new view.
     */
    public AggregLogView() {
        
    }
    
    public class MyLogAggComparable implements Comparator<AbstractAggregateLogEntry>{
  	   
    	public int compare(AbstractAggregateLogEntry obj1, AbstractAggregateLogEntry obj2) 
    	{   
    		int result = 0;
    		if(obj1.getLogDate().getTime().before(obj2.getLogDate().getTime()))
    		{
    			result = -1;
    		}else if(obj1.getLogDate().getTime().after(obj2.getLogDate().getTime()))
    		{
    			result = 1;
    		}else
    		{
    			result = 0;
    		}
    		return  result;  
    	}
    } 
   
    
    /**
     * Generates the aggregate logs JSON.
     * 
     * @param   model                       the model data to output
     * @param   locale                      the locale indicating the language 
     *                                      in which localizable data should be 
     *                                      shown
     * @return                              a string containing the JSON code 
     *                                      for the aggregate logs
     * @throws  MonitorInterfaceException   <ul>
     *                                      <li>the model data is invalid</li>
     *                                      <li>an error occurred while 
     *                                      converting the aggregate logs to
     *                                      JSON</li>
     *                                      </ul>                   
     */
    @SuppressWarnings("unchecked")
    @Override
    public JsonNode getResponseData(Map<String, ?> model, Locale locale)
        throws MonitorInterfaceException {

        if (model.containsKey("aggregLogsCollection")) {
            
        	if(model.containsKey("getExport") && model.containsKey("Jobname") 
        			&& model.containsKey("Queryname"))
            {
            	final Collection<AbstractAggregateLogEntry> logsCollection 
                = (Collection<AbstractAggregateLogEntry>) model.get(
                        "aggregLogsCollection");
            	final Long noPagingCount = (Long) model.get("noPagingCount");
        		final String jobName = (String)model.get("Jobname");
    			final String queryName = (String)model.get("Queryname");
    			
            	final ObjectMapper mapper = this.getObjectMapper();
            	this.getRootObjectNode().put("noPagingCount", noPagingCount);
            	final ArrayNode jsonLogsCollection = mapper.createArrayNode();
            	List<AbstractAggregateLogEntry> sortLogs = new ArrayList<AbstractAggregateLogEntry>(logsCollection);
        		Collections.sort(sortLogs, new MyLogAggComparable());
            
            	for (AbstractAggregateLogEntry logEntry : logsCollection) {
            		jsonLogsCollection.add(AggregLogSerializer.serialize(logEntry, 
                                                       mapper, jobName, queryName));
            	}    
            	return jsonLogsCollection;
            }else
            {
            	final Collection<AbstractAggregateLogEntry> logsCollection 
                = (Collection<AbstractAggregateLogEntry>) model.get(
                        "aggregLogsCollection");
            	final Long noPagingCount = (Long) model.get("noPagingCount");
            	final ObjectMapper mapper = this.getObjectMapper();
            	this.getRootObjectNode().put("noPagingCount", noPagingCount);
            	final ArrayNode jsonLogsCollection = mapper.createArrayNode();

            	for (AbstractAggregateLogEntry logEntry : logsCollection) {
            		jsonLogsCollection.add(AggregLogSerializer.serialize(logEntry, 
                                                                     mapper));
            	}    
            	return jsonLogsCollection;
            }
        }
        if (model.containsKey("aggregDayLogsCollection")) {
        	final Collection<AbstractAggregateLogEntry> logsCollection = (Collection<AbstractAggregateLogEntry>) model.get("aggregDayLogsCollection");
        	final ObjectMapper mapper = this.getObjectMapper();
        	final ArrayNode jsonLogsCollection = mapper.createArrayNode();
         	List<AbstractAggregateLogEntry> sortLogs = new ArrayList<AbstractAggregateLogEntry>(logsCollection);
    		Collections.sort(sortLogs, new MyLogAggComparable());
        	for (AbstractAggregateLogEntry logEntry : sortLogs) {
        		jsonLogsCollection.add(AggregLogSerializer.serialize(logEntry,mapper,true));
        	}    
        	return jsonLogsCollection;
        }
        throw new MonitorInterfaceException("An internal error occurred",
                                            "internal.error");
    }
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Boolean isSuccess() {
        return true;
    }
}

/**
 * 
 */
package org.easysdi.monitor.gui.webapp.views.json.serializers;

import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.logging.AbstractAggregateHourLogEntry;
import org.easysdi.monitor.biz.logging.AggregateStats;

/**
 * @author berg3428
 *
 */
public class AggregHourLogSerializer {

	   /**
     * Dummy constructor used to prevent instantiation.
     */
    private AggregHourLogSerializer() {
        throw new UnsupportedOperationException(
                                            "This class can't be instantiated");
    }
    
    

    /**
     * Generates the JSON representation of an aggregate log entry.
     * 
     * @param   entry   the aggregate log entry to represent
     * @param   mapper  the JSON object used to map the data to JSON nodes
     * @return          the JSON node containing the data for the entry
     */
    public static JsonNode serialize(AbstractAggregateHourLogEntry entry, 
                                     ObjectMapper mapper) {
        final ObjectNode jsonEntry = mapper.createObjectNode();

        final AggregateStats h1Stats = entry.getH1Stats();
        final AggregateStats inspireStats = entry.getInspireStats();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jsonEntry.put("inspireNbConnErrors", inspireStats.getNbConnErrors());
        jsonEntry.put("h1NbConnErrors", h1Stats.getNbConnErrors());
        jsonEntry.put("inspireNbBizErrors", inspireStats.getNbBizErrors());
        jsonEntry.put("h1NbBizErrors", h1Stats.getNbBizErrors());
        jsonEntry.put("inspireAvailability", inspireStats.getAvailability());
        jsonEntry.put("h1Availability", h1Stats.getAvailability());
        jsonEntry.put("inspireMeanRespTime", inspireStats.getMeanRespTime());
        jsonEntry.put("h1MeanRespTime", h1Stats.getMeanRespTime());
        
        jsonEntry.put("inspireMaxRespTime", inspireStats.getMaxRespTime());
        jsonEntry.put("h1MaxRespTime", h1Stats.getMaxRespTime());
        jsonEntry.put("inspireMinRespTime", inspireStats.getMinRespTime());
        jsonEntry.put("h1MinRespTime", h1Stats.getMinRespTime());
        
        jsonEntry.put("h1Unavailability", h1Stats.getUnavailability());
        jsonEntry.put("inspireUnavailability",inspireStats.getUnavailability());
        jsonEntry.put("h1Failure", h1Stats.getFailure());
        jsonEntry.put("inspireFailure",inspireStats.getFailure());
        jsonEntry.put("h1Untested", h1Stats.getUntested());
        jsonEntry.put("inspireUntested",inspireStats.getUntested());
        
        jsonEntry.put("date", dateFormat.format(entry.getLogDate().getTime()));

        return jsonEntry;
    }
    
    /**
     * Generates the JSON representation of an aggregate log entry.
     * 
     * @param   entry   the aggregate log entry to represent
     * @param   mapper  the JSON object used to map the data to JSON nodes
     * @return          the JSON node containing the data for the entry
     */
    public static JsonNode serialize(AbstractAggregateHourLogEntry entry, 
                                     ObjectMapper mapper,String jobName,
                                     String queryName,String slaName) {
        
    	final ObjectNode jsonEntry = mapper.createObjectNode();

        final AggregateStats h1Stats = entry.getH1Stats();
        final AggregateStats inspireStats = entry.getInspireStats();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(slaName.toLowerCase().equals("default"))
        {
        	jsonEntry.put("jobname",jobName);
        	jsonEntry.put("slaname",slaName);
        	jsonEntry.put("date", dateFormat.format(entry.getLogDate().getTime()));
        	if(queryName != "")
        	{
        		jsonEntry.put("requestname",queryName);
        	}else
        	{
        		jsonEntry.put("requestname","Summary");
        	}
        	jsonEntry.put("availability", h1Stats.getAvailability());
        	jsonEntry.put("unavailability", h1Stats.getUnavailability());
        	jsonEntry.put("failure", h1Stats.getFailure());
        	jsonEntry.put("untested", h1Stats.getUntested());
        	jsonEntry.put("maxresponsetime", h1Stats.getMaxRespTime());
        	jsonEntry.put("minresponsetime", h1Stats.getMinRespTime());
        	jsonEntry.put("meanresponsetime", h1Stats.getMeanRespTime());
        	jsonEntry.put("connerrors", h1Stats.getNbConnErrors());
        	jsonEntry.put("bizerrors", h1Stats.getNbBizErrors());
        }else
        {
        	
           	jsonEntry.put("jobname",jobName);
        	jsonEntry.put("slaname",slaName);
        	jsonEntry.put("date", dateFormat.format(entry.getLogDate().getTime()));
        	if(queryName != "")
        	{
        		jsonEntry.put("requestname",queryName);
        	}else
        	{
        		jsonEntry.put("requestname","Summary");
        	}
        	jsonEntry.put("availability", inspireStats.getAvailability());
        	jsonEntry.put("unavailability",inspireStats.getUnavailability());
        	jsonEntry.put("failure",inspireStats.getFailure());
        	jsonEntry.put("untested",inspireStats.getUntested());
        	jsonEntry.put("maxresponsetime", inspireStats.getMaxRespTime());
        	jsonEntry.put("minresponsetime", inspireStats.getMinRespTime());
        	jsonEntry.put("meanresponsetime", inspireStats.getMeanRespTime());
        	jsonEntry.put("connerrors", inspireStats.getNbConnErrors());	
        	jsonEntry.put("bizerrors", inspireStats.getNbBizErrors());
        }
        return jsonEntry;
    }
}

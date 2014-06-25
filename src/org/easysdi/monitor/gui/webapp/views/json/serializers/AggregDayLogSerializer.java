package org.easysdi.monitor.gui.webapp.views.json.serializers;

import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.logging.AbstractAggregateHourLogEntry;
import org.easysdi.monitor.biz.logging.AggregateStats;

public class AggregDayLogSerializer {
    
	 /**
	 * Dummy constructor used to prevent instantiation.
	 */
	 private AggregDayLogSerializer() {
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
        jsonEntry.put("unavailability", h1Stats.getUnavailability());
        jsonEntry.put("inspireunavailability",inspireStats.getUnavailability());
        jsonEntry.put("date", dateFormat.format(entry.getLogDate().getTime()));

        return jsonEntry;
    }
    
}

/**
 * 
 */
package org.easysdi.monitor.gui.webapp.views.json.serializers;


import java.util.Locale;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.job.Period;
import org.easysdi.monitor.biz.job.Sla;


/**
 * @author berg3428
 *
 */
public class SlaSerializer {

	private Sla sla;
	
	/**
	 * Creates the a sla instance 
	 */
	public SlaSerializer(Sla sla) {
		this.setSla(sla);
	}
	
	/**
     * Defines the sla to represent.
     * 
     * @param   newSla the to represent in JSON
    */
    private void setSla(Sla newSla) {
        if (null == newSla) {
            throw new IllegalArgumentException("Sla can't be null");
        }
        this.sla = newSla;
    }
	
	/**
	 * Serialize a sla instance
	 * @param includeSlaPeriods
	 * @param mapper
	 * @return
	 */
	public JsonNode serialize(boolean includeSlaPeriods, Locale locale, ObjectMapper mapper) 
    { 
	        final ObjectNode jsonSla = mapper.createObjectNode();        
	        jsonSla.put("id", this.getSla().getSlaId());
	        jsonSla.put("name",this.getSla().getName());
	        jsonSla.put("isExcludeWorst",this.getSla().isExcludeWorst());
	        jsonSla.put("isMeasureTimeToFirst",this.getSla().isMeasureTimeToFirst());
	        if(includeSlaPeriods)
	        {
	        	 jsonSla.put("slaPeriods", this.buildPeriodArray(locale, mapper));
	        }
	        return jsonSla;
    }
	 
	/**
	* Gets the overview to represent.
	* 
	* @return  the overview to represent in JSON
	*/
    private Sla getSla() {
        return this.sla;
    }
    
    /**
     * Generates the JSON representation the period
     * 
     * @param Period
     * @param locale
     * @param mapper
     * @return
     */
	private ArrayNode buildPeriodArray(Locale locale, ObjectMapper mapper) {
		final ArrayNode periodArray = mapper.createArrayNode();
        for (Period slaPeriod : this.getSla().getSlaPeriodList()) {
        	PeriodSerializer periodserializer = new PeriodSerializer(slaPeriod);
        	periodArray.add(periodserializer.serialize(locale, mapper));
        }
        return periodArray;

	}

}

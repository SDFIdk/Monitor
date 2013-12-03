/**
 * 
 */
package org.easysdi.monitor.gui.webapp.views.json.serializers;

import java.util.Locale;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.job.Period;
import java.text.SimpleDateFormat;

/**
 * @author berg3428
 *
 */
public class PeriodSerializer {
	
	private Period period;
	
	/**
	 * 
	 */
	public PeriodSerializer(Period period) {
		this.setPeriod(period);
	}
	
	private void setPeriod(Period period) {
		if(period == null)
		{
			throw new IllegalArgumentException("Period can't be null");
		}
		this.period = period;
	}

	public Period getPeriod() {
		return period;
	}
	
	
	public JsonNode serialize(Locale locale, ObjectMapper mapper) {
		final ObjectNode jsonPeriod = mapper.createObjectNode();
		final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
		jsonPeriod.put("id",this.period.getPeriodId());
		jsonPeriod.put("name",this.period.getName());
		jsonPeriod.put("isMonday",this.period.isMonday());
		jsonPeriod.put("isTuesday",this.period.isTuesday());
		jsonPeriod.put("isWednesday",this.period.isWednesday());
		jsonPeriod.put("isThursday",this.period.isThursday());
		jsonPeriod.put("isFriday",this.period.isFriday());
		jsonPeriod.put("isSaturday",this.period.isSaturday());
		jsonPeriod.put("isSunday",this.period.isSunday());
		jsonPeriod.put("isHolidays",this.period.isHolidays());
		jsonPeriod.put("slaStartTime",timeFormat.format( this.period.getSlaStartTime().getTime()));
		jsonPeriod.put("slaEndTime",timeFormat.format( this.period.getSlaEndTime().getTime()));
		jsonPeriod.put("isInclude",this.period.isInclude());
		if(this.period.getDate() != null)
		{
			jsonPeriod.put("date",dateFormat.format(this.period.getDate().getTime()));			
		}else
		{
			jsonPeriod.put("date",(String) null);
		}
		return jsonPeriod;
	}


}

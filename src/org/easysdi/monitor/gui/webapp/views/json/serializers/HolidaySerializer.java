/**
 * 
 */
package org.easysdi.monitor.gui.webapp.views.json.serializers;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.job.Holiday;
/**
 * @author BERG3428
 *
 */
public class HolidaySerializer {

	private Holiday holiday;
	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/**
	 * 
	 */
	public HolidaySerializer(Holiday holiday) {
		 this.setHoliday(holiday);
	}
	
	/**
	 * Serialize a holiday instance
	 * @param locale
	 * @param mapper
	 * @return
	 */
	public JsonNode serialize(Locale locale, ObjectMapper mapper) 
    { 
			final SimpleDateFormat dateFormat = new SimpleDateFormat(HolidaySerializer.DATE_TIME_FORMAT);       
	        final ObjectNode jsonSla = mapper.createObjectNode();        
	        jsonSla.put("id", this.getHoliday().getHolidayId());
	        jsonSla.put("name",this.getHoliday().getName());
	        jsonSla.put("date",dateFormat.format(this.getHoliday().getDate().getTime()));
	        return jsonSla;
    }
	
	/**
	 * @return the holiday
	 */
	private Holiday getHoliday() {
		return holiday;
	}
	/**
	 * @param holiday the holiday to set
	 */
	private void setHoliday(Holiday holiday) {
		if(holiday == null)
		{
			throw new IllegalArgumentException("Holiday can't be null");
		}
		this.holiday = holiday;
	}
	
	

}

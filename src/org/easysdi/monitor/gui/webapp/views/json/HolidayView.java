/**
 * 
 */
package org.easysdi.monitor.gui.webapp.views.json;

import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.easysdi.monitor.biz.job.Holiday;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.HolidaySerializer;

/**
 * @author BERG3428
 *
 */
public class HolidayView extends AbstractJsonView {

	/**
	 * 
	 */
	public HolidayView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected JsonNode getResponseData(Map<String, ?> model, Locale locale)
			throws MonitorInterfaceException {
		
		if (model.containsKey("holiday")) {
            final Holiday holidayview = (Holiday) model.get("holiday");
            return new HolidaySerializer(holidayview).serialize(locale,this.getObjectMapper());
		}   
		throw new MonitorInterfaceException("An internal error occurred",
                                            "internal.error");
	}

	@Override
	protected Boolean isSuccess() {
		return true;
	}

}

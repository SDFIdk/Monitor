/**
 * 
 */
package org.easysdi.monitor.gui.webapp.views.json;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.HolidaySerializer;
import org.easysdi.monitor.biz.job.Holiday;

/**
 * @author BERG3428
 *
 */
public class HolidayCollectionView extends AbstractJsonView{

	/**
	 * 
	 */
	public HolidayCollectionView() {
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	protected JsonNode getResponseData(Map<String, ?> model, Locale locale)
			throws MonitorInterfaceException {
		if(model.containsKey("holidayList"))
		{
		   final ObjectMapper mapper = this.getObjectMapper();
           final ArrayNode holidayList = mapper.createArrayNode();
           
           for (Holiday holiday : (List<Holiday>) model.get("holidayList")) {
            	holidayList.add(new HolidaySerializer(holiday).serialize(locale, mapper));
           }
           return holidayList;
		}
		
		throw new MonitorInterfaceException("An internal error occurred",
        "internal.error");
	}

	@Override
	protected Boolean isSuccess() {
		return true;
	}
	
}

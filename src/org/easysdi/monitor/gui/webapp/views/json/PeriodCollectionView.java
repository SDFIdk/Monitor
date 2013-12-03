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
import org.easysdi.monitor.biz.job.Period;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.PeriodSerializer;

/**
 * @author berg3428
 *
 */
public class PeriodCollectionView extends AbstractJsonView {

	/**
	 * 
	 */
	public PeriodCollectionView() {
	}

	@SuppressWarnings("unchecked")
	@Override
	protected JsonNode getResponseData(Map<String, ?> model, Locale locale)
			throws MonitorInterfaceException {
		 if (model.containsKey("periodList")) {
	            final ObjectMapper mapper = this.getObjectMapper();
	            final ArrayNode periodList = mapper.createArrayNode();     
	            for (Period period : (List<Period>) model.get("periodList")) {
	            	periodList.add(new PeriodSerializer(period).serialize(locale, mapper));
	            }
	            return periodList;
	        }
	        throw new MonitorInterfaceException("An internal error occurred",
	                                            "internal.error");
	}

	@Override
	protected Boolean isSuccess() {
		return true;
	}

}

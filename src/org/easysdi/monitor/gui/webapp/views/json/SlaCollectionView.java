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
import org.easysdi.monitor.biz.job.Sla;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.SlaSerializer;

/**
 * Class that can serialize a Sla list
 * @author BERG3428
 * 
 */
public class SlaCollectionView extends AbstractJsonView {

	@SuppressWarnings("unchecked")
	@Override
	protected JsonNode getResponseData(Map<String, ?> model, Locale locale)
			throws MonitorInterfaceException {
		
		  if (model.containsKey("slaList")) {
	            final ObjectMapper mapper = this.getObjectMapper();
	            final ArrayNode slaList = mapper.createArrayNode();
	           
	            for (Sla sla : (List<Sla>) model.get("slaList")) {
	            	slaList.add(new SlaSerializer(sla).serialize(false, locale, mapper));
	            }
	            return slaList;
	        }
	        throw new MonitorInterfaceException("An internal error occurred",
	                                            "internal.error");
	}

	@Override
	protected Boolean isSuccess() {
		return true;
	}

}

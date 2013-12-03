package org.easysdi.monitor.gui.webapp.views.json;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.easysdi.monitor.biz.job.Overview;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.OverviewSerializer;

public class OverviewsCollectionView extends AbstractJsonView {

	public OverviewsCollectionView(){
	
	}

	@Override
	protected Boolean isSuccess() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected JsonNode getResponseData(Map<String, ?> model, Locale locale)
			throws MonitorInterfaceException {
        if (model.containsKey("overviewList")) {
            final ObjectMapper mapper = this.getObjectMapper();
            final ArrayNode overviewList = mapper.createArrayNode();

            for (Overview overview : (List<Overview>) model.get("overviewList")) {
            	overviewList.add(new OverviewSerializer(overview).serialize(true, true, 
                                                              locale, mapper));

            }

            return overviewList;

        }
         
        throw new MonitorInterfaceException("An internal error occurred",
                                            "internal.error");
	}

}

package org.easysdi.monitor.gui.webapp.views.json;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.easysdi.monitor.biz.job.OverviewQueryView;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.serializers.OverviewQuerySerializer;

public class OverviewQueryViewCollectionView extends AbstractJsonView {

	public OverviewQueryViewCollectionView() {
	}

	@Override
	protected Boolean isSuccess() {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected JsonNode getResponseData(Map<String, ?> model, Locale locale)
			throws MonitorInterfaceException {
        if (model.containsKey("overviewQueryCollection")) {
            final ObjectMapper mapper = this.getObjectMapper();
            final ArrayNode overviewQueryList = mapper.createArrayNode();
            if((List<OverviewQueryView>) model.get("overviewQueryCollection")!= null){
            	for (OverviewQueryView overviewQuery : (List<OverviewQueryView>) model.get("overviewQueryCollection")) {
            		overviewQueryList.add(new OverviewQuerySerializer(overviewQuery).serialize(locale, mapper));

            	}
            }

            return overviewQueryList;

        }
         
        throw new MonitorInterfaceException("An internal error occurred",
                                            "internal.error");
	}

}

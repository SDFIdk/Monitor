package org.easysdi.monitor.gui.webapp.views.json.serializers;

import java.util.Locale;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.easysdi.monitor.biz.job.OverviewQueryView;

public class OverviewQuerySerializer {
	private OverviewQueryView overviewQuery;
	
    public OverviewQuerySerializer(OverviewQueryView overviewQuery) {
        this.setOverviewQuery(overviewQuery);
    }

    public JsonNode serialize(Locale locale, ObjectMapper mapper) 
    {
     
        final ObjectNode jsonOverview = mapper.createObjectNode();
        
        jsonOverview.put("overviewId", this.getOverviewQuery().getId().getOverviewId());
        jsonOverview.put("queryId",this.getOverviewQuery().getId().getQueryId());
        jsonOverview.put("isPublic",this.getOverviewQuery().isQueryIsPublic());
       
        jsonOverview.put("query", buildQuery(locale, mapper));
       	jsonOverview.put("lastQueryResult", buildLastQuery(locale, mapper));
        
        return jsonOverview;
    }
   
	private ObjectNode buildLastQuery(Locale locale, ObjectMapper mapper) {

		QueryLastResultSerializer lastResultSerializer 
			= new QueryLastResultSerializer(this.getOverviewQuery().getLastQueryResult());

		return (ObjectNode) lastResultSerializer.serialize(locale, mapper);
	}

	private ObjectNode buildQuery(Locale locale, ObjectMapper mapper) {

	return	(ObjectNode)QuerySerializer.serialize(this.getOverviewQuery().getQuery(), false, locale, mapper);
	}
	private void setOverviewQuery(OverviewQueryView overviewQuery) {
		this.overviewQuery = overviewQuery;
		
	}
    private OverviewQueryView getOverviewQuery() {
        return this.overviewQuery;
    }
    
}

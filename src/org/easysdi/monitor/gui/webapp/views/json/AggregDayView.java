package org.easysdi.monitor.gui.webapp.views.json;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.deegree.framework.util.DateUtil;
import org.easysdi.monitor.biz.logging.AbstractAggregateHourLogEntry;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.json.AggregHourLogView.MyLogAggHourComparable;
import org.easysdi.monitor.gui.webapp.views.json.serializers.AggregDayLogSerializer;
import org.easysdi.monitor.gui.webapp.views.json.serializers.AggregHourLogSerializer;

import com.sun.javafx.css.CalculatedValue;

public class AggregDayView extends AbstractJsonView {

	public AggregDayView()
	{
		
	}
	
   public class MyLogAggHourComparable implements Comparator<AbstractAggregateHourLogEntry>{
 	   
    	public int compare(AbstractAggregateHourLogEntry obj1, AbstractAggregateHourLogEntry obj2) 
    	{   
    		int result = 0;
    		if(obj1.getLogDate().getTime().before(obj2.getLogDate().getTime()))
    		{
    			result = -1;
    		}else if(obj1.getLogDate().getTime().after(obj2.getLogDate().getTime()))
    		{
    			result = 1;
    		}else
    		{
    			result = 0;
    		}
    		return  result;  
    	}
    } 
	    
		@SuppressWarnings("unchecked")
		@Override
		protected JsonNode getResponseData(Map<String, ?> model, Locale locale)
	    throws MonitorInterfaceException {
	        if (model.containsKey("aggregDayCollection")) {
	        		final Collection<AbstractAggregateHourLogEntry> logsCollection = (Collection<AbstractAggregateHourLogEntry>) model.get("aggregDayCollection");           
	            	final ObjectMapper mapper = this.getObjectMapper();
	            	final ArrayNode jsonLogsCollection = mapper.createArrayNode();
	            	List<AbstractAggregateHourLogEntry> sortLogs = new ArrayList<AbstractAggregateHourLogEntry>(logsCollection);
	        		Collections.sort(sortLogs, new MyLogAggHourComparable());
	        		sortLogs = calcUnaviablePerDay(sortLogs);
	            	for (AbstractAggregateHourLogEntry logEntry : sortLogs) {
	            		jsonLogsCollection.add(AggregDayLogSerializer.serialize(logEntry,mapper));
	            	}
	            	return jsonLogsCollection;
	        }   
	        throw new MonitorInterfaceException("An internal error occurred","internal.error");
		}
		
		private List<AbstractAggregateHourLogEntry> calcUnaviablePerDay(List<AbstractAggregateHourLogEntry> collection)
		{
			List<AbstractAggregateHourLogEntry> log = new ArrayList<AbstractAggregateHourLogEntry>();
			Calendar current = null;
			AbstractAggregateHourLogEntry logelement = null;
			int currentCount = 0;
			float currentunAvailability = 0.0F;
			float currentInspireunAvailability = 0.0F;
			
			for (AbstractAggregateHourLogEntry logEntry : collection) {
				
				if(current == null)
				{
					current = logEntry.getLogDate();
					logelement = logEntry;
					currentCount++;
					currentInspireunAvailability += logEntry.getInspireStats().getUnavailability();
					currentunAvailability += logEntry.getH1Stats().getUnavailability();
					continue;
				}
				if(DateUtil.compareWithoutTime(logEntry.getLogDate(), current) == 0)
				{
					currentCount++;
					currentInspireunAvailability += logEntry.getInspireStats().getUnavailability();
					currentunAvailability += logEntry.getH1Stats().getUnavailability();
				}else
				{
					// Save old day
					Calendar temp = logelement.getLogDate();
					temp.set(Calendar.HOUR_OF_DAY, 0);
					temp.set(Calendar.MINUTE, 0);
					temp.set(Calendar.SECOND, 0);  
					temp.set(Calendar.MILLISECOND, 0);
					
					// Calc
					logelement.getH1Stats().setUnavailability(currentunAvailability / currentCount);
					logelement.getInspireStats().setUnavailability(currentInspireunAvailability / currentCount);
					logelement.setLogDate(temp);
					log.add(logelement);
					
					// New Day
					current = logEntry.getLogDate();
					logelement = logEntry;
					currentCount = 1;
					currentunAvailability = logEntry.getH1Stats().getUnavailability();
					currentInspireunAvailability = logEntry.getInspireStats().getUnavailability();
				}
			}
			// Fix last data
			if(currentCount > 0)
			{
				Calendar temp = logelement.getLogDate();
				temp.set(Calendar.HOUR_OF_DAY, 0);
				temp.set(Calendar.MINUTE, 0);
				temp.set(Calendar.SECOND, 0);  
				temp.set(Calendar.MILLISECOND, 0);
				
				// Calc
				logelement.getH1Stats().setUnavailability(currentunAvailability / currentCount);
				logelement.getInspireStats().setUnavailability(currentInspireunAvailability / currentCount);
				logelement.setLogDate(temp);
				log.add(logelement);
			}
				
			return log;
		}
	
	@Override
	protected Boolean isSuccess() {
		return true;
	}
}

/**
 * 
 */
package org.easysdi.monitor.gui.webapp.views.csv;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.easysdi.monitor.biz.logging.AbstractAggregateHourLogEntry;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.easysdi.monitor.gui.webapp.views.csv.serializers.AggregHourLogSerializer;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * @author berg3428
 *
 */
public class AggregHourLogView  extends AbstractCsvView {

    /**
    * Creates a new view.
    */
    public AggregHourLogView() {
        super();
    }
	
	@SuppressWarnings("unchecked")
	@Override
	protected String generateViewContent(Map<String, ?> model,
			HttpServletRequest request) throws MonitorInterfaceException {
	    final List<String> fileLines = new LinkedList<String>();

        if (model.containsKey("aggregHourLogsCollection")) {
            final Collection<AbstractAggregateHourLogEntry> logsCollection 
                = (Collection<AbstractAggregateHourLogEntry>) model.get(
                     "aggregHourLogsCollection");
            
            final Locale locale = RequestContextUtils.getLocale(request);

            fileLines.add(AggregHourLogSerializer.generateHeaderLine(
                 FIELD_SEPARATOR,
                 locale));

            for (AbstractAggregateHourLogEntry logEntry : logsCollection) {
                fileLines.add(AggregHourLogSerializer.serialize(logEntry,
                                                            FIELD_SEPARATOR));
            }
            return StringUtils.join(fileLines.iterator(), ENTRY_SEPARATOR);
            
        }
        
        throw new MonitorInterfaceException("An internal error occurred",
                                            "internal.error");
	}
}

package org.easysdi.monitor.gui.webapp.views.csv.serializers;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.easysdi.monitor.biz.logging.RawLogEntry;
import org.easysdi.monitor.gui.i18n.Messages;

/**
 * Helper functions to create a CSV document displaying response times entries.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public final class ResponseTimeSerializer {

    /**
     * Hidden constructor (non-instantiable class).
     */
    private ResponseTimeSerializer() {
        
    }
    
    
    
    /**
     * Transform a raw log entry in a CSV repsonse time entry.
     * 
     * @param logEntry          the raw log entry containing the response time
     *                          to serialize
     * @param fieldSeparator    the string used to separate the columns data
     * @param queryIdDisplayed  <code>true</code> to add the query identifier
     *                          to each CSV entry
     * @return                  a string containing the response time 
     *                          informations in CSV format
     */
    public static String serialize(RawLogEntry logEntry, String fieldSeparator,
                                   boolean queryIdDisplayed) {
        final List<String> elements = new LinkedList<String>();
        final SimpleDateFormat dateFormat 
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        elements.add(dateFormat.format(logEntry.getRequestTime().getTime()));

        if (queryIdDisplayed) {
            elements.add(Long.toString(logEntry.getQueryId()));
        }

        elements.add(Float.toString(logEntry.getResponseDelay()));

        return StringUtils.join(elements.iterator(), fieldSeparator);
    }



    /**
     * Creates a columns titles entry.
     * 
     * @param fieldSeparator    the string used to separate the columns titles
     * @param queryIdDisplayed  <code>true</code> to add the query identifier
     *                          column title
     * @param locale            the locale used to display the columns titles
     * @return                  a string containing the columns titles entry
     */
    public static String generateHeaderLine(String fieldSeparator,
                                            boolean queryIdDisplayed, 
                                            Locale locale) {
        final List<String> headers = new LinkedList<String>();
        final Messages i18nMessages = new Messages(locale);

        headers.add(i18nMessages.getMessage("respTime.header.dateTime"));

        if (queryIdDisplayed) {
            headers.add(i18nMessages.getMessage("respTime.header.queryId"));
        }

        headers.add(i18nMessages.getMessage("respTime.header.respDelay"));

        return StringUtils.join(headers.iterator(), fieldSeparator);
    }
}

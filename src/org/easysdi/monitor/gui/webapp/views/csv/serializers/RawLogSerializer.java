package org.easysdi.monitor.gui.webapp.views.csv.serializers;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.easysdi.monitor.biz.logging.RawLogEntry;
import org.easysdi.monitor.gui.i18n.Messages;

/**
 * Helper functions to create a CSV document displaying raw log entries.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public final class RawLogSerializer {
    
    /**
     * Hidden construction (non-instantiable class).
     */
    private RawLogSerializer() {
        
    }

    
    
    /**
     * Transforms a raw log entry into a CSV entry.
     * 
     * @param entry             the raw log entry to serialize
     * @param fieldSeparator    the string used to separate columns data
     * @param queryIdDisplayed  <code>true</code> to add the query identifier
     *                          to each CSV entry
     * @param locale            the locale used to display the data
     * @return                  a string containing the raw log informations in
     *                          CSV format
     */
    public static String serialize(RawLogEntry entry, String fieldSeparator,
                    boolean queryIdDisplayed, Locale locale) {
        final SimpleDateFormat dateFormat 
            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final List<String> elements = new LinkedList<String>();
        elements.add(dateFormat.format(entry.getRequestTime().getTime()));

        if (queryIdDisplayed) {
            elements.add(Long.toString(entry.getQueryId()));
        }

        elements.add(entry.getStatus().getDisplayString(locale));
        elements.add(Float.toString(entry.getResponseDelay()));
        elements.add(Float.toString(entry.getResponseSize()));
        elements.add(entry.getMessage());

        return StringUtils.join(elements.iterator(), fieldSeparator);
    }



    /**
     * Creates a columns titles entry.
     * 
     * @param   fieldSeparator      the string used to separate the columns 
     *                              titles
     * @param   queryIdDisplayed    <code>true</code> to add the query 
     *                              identifier column title
     * @param   locale              the locale used to display the columns 
     *                              titles in the correct language
     * @return                      a string containing the columns titles entry
     */
    public static String generateHeaderLine(String fieldSeparator,
                                            boolean queryIdDisplayed, 
                                            Locale locale) {

        final List<String> headers = new LinkedList<String>();
        final Messages i18nMessages = new Messages(locale);

        headers.add(i18nMessages.getMessage("log.header.dateTime"));

        if (queryIdDisplayed) {
            headers.add(i18nMessages.getMessage("log.header.queryId"));
        }

        headers.add(i18nMessages.getMessage("log.header.status"));
        headers.add(i18nMessages.getMessage("log.header.respDelay"));
        headers.add(i18nMessages.getMessage("log.header.size"));
        headers.add(i18nMessages.getMessage("log.header.message"));

        return StringUtils.join(headers.iterator(), fieldSeparator);
    }
}

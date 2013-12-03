/**
 * 
 */
package org.easysdi.monitor.gui.webapp.views.csv.serializers;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.easysdi.monitor.biz.logging.AbstractAggregateHourLogEntry;
import org.easysdi.monitor.biz.logging.AggregateStats;
import org.easysdi.monitor.gui.i18n.Messages;

/**
 * @author berg3428
 *
 */
public class AggregHourLogSerializer {

    /**
     * Hidden constructor (non instantiable class).
     */
    private AggregHourLogSerializer() {
        
    }
    
    /**
     * Transforms an aggregate log entry into a CSV entry.
     * 
     * @param entry             the aggregate log entry to serialize
     * @param fieldSeparator    the string used to separate the columns data
     * @return                  a string containing the aggregate log in CSV
     *                          format 
     */
    public static String serialize(AbstractAggregateHourLogEntry entry,
                            String fieldSeparator) {

        final AggregateStats h1Stats = entry.getH1Stats();
        final AggregateStats inspireStats = entry.getInspireStats();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final List<String> elements = new LinkedList<String>();
        elements.add(dateFormat.format(entry.getLogDate().getTime()));
        elements.add(Float.toString(h1Stats.getMeanRespTime()));
        elements.add(Float.toString(inspireStats.getMeanRespTime()));
        elements.add(Float.toString(h1Stats.getAvailability()));
        elements.add(Float.toString(inspireStats.getAvailability()));
        elements.add(Float.toString(h1Stats.getNbBizErrors()));
        elements.add(Float.toString(inspireStats.getNbBizErrors()));
        elements.add(Float.toString(h1Stats.getNbConnErrors()));
        elements.add(Float.toString(inspireStats.getNbConnErrors()));
        elements.add(Float.toString(h1Stats.getMaxRespTime()));
        elements.add(Float.toString(inspireStats.getMaxRespTime()));
        elements.add(Float.toString(h1Stats.getMinRespTime()));
        elements.add(Float.toString(inspireStats.getMinRespTime()));
        elements.add(Float.toString(h1Stats.getUnavailability()));
        elements.add(Float.toString(inspireStats.getUnavailability()));
        elements.add(Float.toString(h1Stats.getFailure()));
        elements.add(Float.toString(inspireStats.getFailure()));
        elements.add(Float.toString(h1Stats.getUntested()));
        elements.add(Float.toString(inspireStats.getUntested()));
        return StringUtils.join(elements.iterator(), fieldSeparator);
    }

    /**
     * Creates a columns titles entry.
     * 
     * @param fieldSeparator    the string used to separate the columns titles
     * @param locale            the locale used to display the columns titles
     *                          in the correct language
     * @return                  a string containing the columns titles entry
     */
    public static String generateHeaderLine(String fieldSeparator, 
                                            Locale locale) {

        final List<String> headers = new LinkedList<String>();
        final Messages i18nMessages = new Messages(locale);

        headers.add(i18nMessages.getMessage("log.header.date"));
        headers.add(i18nMessages.getMessage("log.header.h1.meanRespTime"));
        headers.add(i18nMessages.getMessage("log.header.inspire.meanRespTime"));
        headers.add(i18nMessages.getMessage("log.header.h1.availability"));
        headers.add(i18nMessages.getMessage("log.header.inspire.availability"));
        headers.add(i18nMessages.getMessage("log.header.h1.nbBizErrors"));
        headers.add(i18nMessages.getMessage("log.header.inspire.nbBizErrors"));
        headers.add(i18nMessages.getMessage("log.header.h1.nbConnErrors"));
        headers.add(i18nMessages.getMessage("log.header.inspire.nbConnErrors"));

        return StringUtils.join(headers.iterator(), fieldSeparator);
    }
}

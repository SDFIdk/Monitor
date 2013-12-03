package org.easysdi.monitor.gui.webapp.views.rss;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Item;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easysdi.monitor.biz.alert.Alert;
import org.easysdi.monitor.gui.i18n.Messages;
import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Displays an alert collection as a RSS feed.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public class AlertsCollectionView extends AbstractRssView {

    /**
     * Creates a new view.
     */
    public AlertsCollectionView() {
        super();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    protected void buildFeedMetadata(Map<String, ?> model, Channel channel,
                                     HttpServletRequest request) {

        final Messages i18n 
            = new Messages(RequestContextUtils.getLocale(request));
        final Long jobId = (Long) model.get("jobId");
        
        channel.setTitle(String.format(i18n.getMessage("alert.rss.title"),
                                       jobId));
        channel.setDescription(String.format(
                 i18n.getMessage("alert.rss.description"), jobId));
        channel.setLink(this.buildFeedUrl(request));
        channel.setEncoding("UTF-8");
    }



    /**
     * Builds the URL for the RSS feed from the request.
     * 
     * @param   request the request that this view responds to
     * @return          the feed's Url
     */
    private String buildFeedUrl(HttpServletRequest request) {
        final StringBuffer feedLink = new StringBuffer();
        
        feedLink.append(request.getScheme());
        feedLink.append("://");
        feedLink.append(request.getLocalName().toLowerCase());
       
        if (0 < request.getLocalPort()) {
            feedLink.append(":");
            feedLink.append(request.getLocalPort());
        }
        
        feedLink.append(request.getRequestURI());
        feedLink.append("?");
        feedLink.append(request.getQueryString());
        
        return feedLink.toString();
    }



    /**
     * Generates RSS feed items for the alert collection.
     * 
     * @param   model                       the model data to output in the feed
     * @param   request                     the request that this view responds
     *                                      to
     * @param   response                    the response to the request
     * @return                              a list of RSS items
     * @throws  MonitorInterfaceException   the model data is invalid
     */
    @Override
    @SuppressWarnings("unchecked")
    protected List<Item> buildFeedItems(Map<String, ?> model,
                                        HttpServletRequest request, 
                                        HttpServletResponse response)
        throws MonitorInterfaceException {

        if (model.containsKey("alertsCollection")
            && model.containsKey("jobId")) {
            
            final List<Item> itemsList = new LinkedList<Item>();
            final List<Alert> alertsList 
                = (List<Alert>) model.get("alertsCollection");
            final Locale locale = RequestContextUtils.getLocale(request);
            final Messages i18n = new Messages(locale);

            for (Alert alert : alertsList) {
                itemsList.add(this.buildAlertItem(alert, request, i18n));
            }

            return itemsList;
            
        }
        
        throw new MonitorInterfaceException("An internal error occurred",
                                            "internal.error");
    }



    /**
     * Builds a RSS item from an alert.
     * 
     * @param   alert       the alert to show in the RSS feed
     * @param   request     the request that this view responds to
     * @param   i18n        a message ressource object
     * @return              a RSS item for the given alert
     */
    private Item buildAlertItem(Alert alert, HttpServletRequest request, 
                                Messages i18n) {
        
        final Item rssItem = new Item();
        rssItem.setTitle(i18n.getMessage("alert.rss.item.title"));
        rssItem.setLink(this.buildLogsUrl(request));
        rssItem.setDescription(this.buildAlertDescription(alert, i18n));
        
        return rssItem;
    }



    /**
     * Builds the RSS item description of an alert. 
     * 
     * @param   alert   the alert to describe
     * @param   i18n    a message ressource object
     * @return          a RSS item description
     */
    private Description buildAlertDescription(Alert alert, Messages i18n) {
        final Description desc = new Description();
        final String descMessage 
            = i18n.getMessage("alert.rss.item.description");
        final Locale locale = i18n.getLocale();
        desc.setType("text/html");
        desc.setValue(String.format(
                descMessage, alert.getParentJob().getJobId(),
                alert.getParentJob().getConfig().getJobName(),
                alert.getOldStatus().getDisplayString(locale),
                alert.getNewStatus().getDisplayString(locale),
                alert.getCause()));
        
        return desc;
    }



    /**
     * Builds the URL for the logs collection from the request. 
     * 
     * @param   request the request that this view responds to
     * @return          the logs collection URL
     */
    private String buildLogsUrl(HttpServletRequest request) {
        final StringBuffer itemLink = new StringBuffer();
        
        itemLink.append(request.getScheme());
        itemLink.append("://");
        itemLink.append(request.getLocalName().toLowerCase());
        
        if (0 < request.getLocalPort()) {
            itemLink.append(":");
            itemLink.append(request.getLocalPort());
        }
        
        final String requestUri = request.getRequestURI();
        itemLink.append(requestUri.substring(0, 
                                             requestUri.lastIndexOf("/") + 1));
        itemLink.append("logs");
        
        return itemLink.toString();
    }

}

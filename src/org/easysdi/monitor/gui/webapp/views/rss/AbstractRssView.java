package org.easysdi.monitor.gui.webapp.views.rss;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Item;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.WireFeedOutput;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.easysdi.monitor.gui.webapp.MonitorInterfaceException;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.AbstractView;

/**
 * Outline for displaying data in a RSS feed.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public abstract class AbstractRssView extends AbstractView {
    
    /**
     * Creates a new view.
     */
    protected AbstractRssView() {
        this.setContentType("application/rss+xml;charset=UTF-8");
    }
    
    
    /**
     * Defines the metadata for the RSS feed.
     * 
     * @param model     the model data to output in the feed
     * @param channel   the feed's channel
     * @param request   the request that this view responds to
     */
    protected abstract void buildFeedMetadata(Map<String, ?> model, 
                                              Channel channel,
                                              HttpServletRequest request);
    
    
    
    /**
     * Generates the feed's items.
     * 
     * @param   model                       the model data to output
     * @param   request                     the request that this view 
     *                                      responds to
     * @param   response                    the response to the request
     * @return                              a list of feed items
     * @throws  MonitorInterfaceException   the model data is invalid
     */
    protected abstract List<Item> buildFeedItems(Map<String, ?> model,
                                                 HttpServletRequest request, 
                                                 HttpServletResponse response)
        throws MonitorInterfaceException; 
    

    /**
     * Generates the feed's content.
     * 
     * @param   model                       the model data to output in the feed
     * @param   channel                     the feed's channel
     * @param   request                     the request that this view 
     *                                      responds to
     * @param   response                    the response to the request
     * @throws  MonitorInterfaceException   the model data is invalid
     */
    protected final void buildFeedEntries(Map<String, ?> model,
            Channel channel, HttpServletRequest request, 
            HttpServletResponse response) 
        throws MonitorInterfaceException {

        channel.setItems(this.buildFeedItems(model, request, response));
    }



    /**
     * Renders the RSS feed from the model data.
     * 
     * @param   model                       the model data to output in the feed
     * @param   request                     the servlet request that this view
     *                                      responds to
     * @param   response                    the response to the request
     * @throws  MonitorInterfaceException   the model data is invalid
     */
    @Override
    protected void renderMergedOutputModel(Map<String, Object> model,
                                           HttpServletRequest request, 
                                           HttpServletResponse response)
        throws MonitorInterfaceException {
        
        final Channel wireFeed = this.newFeed();
        this.buildFeedMetadata(model, wireFeed, request);
        this.buildFeedEntries(model, wireFeed, request, response);

        response.setContentType(this.getContentType());

        if (!StringUtils.hasText(wireFeed.getEncoding())) {
            wireFeed.setEncoding("UTF-8");
        }

        final WireFeedOutput feedOutput = new WireFeedOutput();
        
        try {
            final ServletOutputStream out = response.getOutputStream();
            feedOutput.output(wireFeed,
                              new OutputStreamWriter(out, 
                                                     wireFeed.getEncoding()));
            out.flush();
            
        } catch (IOException e) {
            
            throw new MonitorInterfaceException(
                    "An error occurred while writing the RSS feed", 
                    "alert.rss.error");
            
        } catch (FeedException e) {

            throw new MonitorInterfaceException(
                    "An error occurred while writing the RSS feed", 
                    "alert.rss.error");
        }
    }


    
    /**
     * Creates a new RSS 2.0 feed.
     * 
     * @return  the newly created feed
     */
    protected Channel newFeed() {
        return new Channel("rss_2.0");
    }
}

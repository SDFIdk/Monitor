package org.easysdi.monitor.biz.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provides helper tools to deal with HTTP codes.
 * 
 * @author Yves Grasset - arx iT
 * @version 1.0, 2010-03-19
 *
 */
public final class HttpCodeUtils {
    
    private static Map<Integer, String> codes;
    private static final int LOWEST_VALID_CODE = 100;
    private static final int HIGHEST_VALID_CODE = 599;
    private static final int LOWEST_SUCCESS_CODE = 100;
    private static final int HIGHEST_SUCCESS_CODE = 299;
    
    
    
    static {
        final Map<Integer, String> map = new LinkedHashMap<Integer, String>();
        map.put(100, "Continue");
        map.put(101, "Switching protocols");
        map.put(102, "Processing (WebDAV)");
        map.put(200, "OK");
        map.put(201, "Created");
        map.put(202, "Accepted");
        map.put(203, "Non-authoritative information");
        map.put(204, "No content");
        map.put(205, "Reset content");
        map.put(206, "Partial content");
        map.put(300, "Multiple choices");
        map.put(301, "Moved permanently");
        map.put(302, "Moved temporarily");
        map.put(303, "See other");
        map.put(304, "Modified");
        map.put(305, "Use proxy");
        map.put(306, "Temporary redirect");
        map.put(400, "Bad request");
        map.put(401, "Unauthorized");
        map.put(402, "Payment required");
        map.put(403, "Forbidden");
        map.put(404, "Not found");
        map.put(405, "Method not allowed");
        map.put(406, "Unacceptable");
        map.put(407, "Proxy authentication required");
        map.put(408, "Request time-out");
        map.put(409, "Conflict");
        map.put(410, "Gone");
        map.put(411, "Length required");
        map.put(412, "Precondition failed");
        map.put(413, "Request entity too large");
        map.put(414, "Request URI too long");
        map.put(415, "Unsupported media type");
        map.put(416, "Request range unsatisfiable");
        map.put(417, "Expectation failed");
        map.put(500, "Internal server error");
        map.put(501, "Not implemented");
        map.put(502, "Bad gateway");
        map.put(503, "Service unavailable");
        map.put(504, "Gateway time-out");
        map.put(505, "HTTP version not supported");
        
        HttpCodeUtils.codes = Collections.unmodifiableMap(map);
    }
    
    /**
     * Dummy constructor to prevent instantiation.
     */
    private HttpCodeUtils() {
        
        throw new UnsupportedOperationException(
                "This class can't be instantiated.");
        
    }
    
    
    
    /**
     * Gets the message corresponding to an HTTP code.
     * 
     * @param   code    the HTTP code
     * @return          the message
     */
    public static String getCodeMessage(Integer code) {
        
        if (null == code || !HttpCodeUtils.codes.containsKey(code)) {
            return "Unable to connect to server";
        }
        
        return HttpCodeUtils.codes.get(code);
    }
    
    
    
    /**
     * Checks if the given HTTP code signals the success of the request.
     * 
     * @param   code    the HTTP code
     * @return          <code>true</code> if the code means success
     */
    public static boolean isSuccess(Integer code) {
        
        if (!HttpCodeUtils.isCodeValid(code)) {
            return false;
        }
        
        return HttpCodeUtils.LOWEST_SUCCESS_CODE <= code
                && HttpCodeUtils.HIGHEST_SUCCESS_CODE >= code;
        
    }
    
    
    
    /**
     * Checks if an HTTP code is valid.
     * 
     * @param   code    the HTTP code
     * @return          <code>true</code> if the code is valid
     */
    public static boolean isCodeValid(Integer code) {

        if (null == code) {
            return false;
        }
        
        return HttpCodeUtils.LOWEST_VALID_CODE <= code
                && HttpCodeUtils.HIGHEST_VALID_CODE >= code;

    }

}

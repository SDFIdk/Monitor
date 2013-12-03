package org.easysdi.monitor.gui.webapp;

import java.util.Comparator;

import org.easysdi.monitor.biz.logging.RawLogEntry;

public class RawLogSortingComparator implements Comparator<RawLogEntry> {
 	   
	public int compare(RawLogEntry obj1, RawLogEntry obj2) 
	{   
		int result = 0;
		if(obj1.getRequestTime().getTime().after(obj2.getRequestTime().getTime()))
		{
			result = -1;
		}else if(obj1.getRequestTime().getTime().before(obj2.getRequestTime().getTime()))
		{
			result = 1;
		}else
		{
			result = 0;
		}
		return  result;  
	}
} 

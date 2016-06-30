package org.easysdi.monitor.dat.dao;

import org.easysdi.monitor.biz.logging.LastLog;

public interface ILastLogQueryDao {
	
	boolean delete(LastLog lastlog);
	
	boolean create(LastLog lastlog);
	
	LastLog getLastlogByQueryId(long queryid);
	
}

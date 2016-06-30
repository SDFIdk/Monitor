package org.easysdi.monitor.dat.dao;

public class LastLogQueryDaoHelper {

	private static ILastLogQueryDao lastlogquerydao;
	
	private LastLogQueryDaoHelper() {
		throw new UnsupportedOperationException("This class can't be instantiated.");
	}
	
	public static void setLastLogQuery(ILastLogQueryDao lastlogQueryDao){
		LastLogQueryDaoHelper.lastlogquerydao = lastlogQueryDao;
	}
	
	public static ILastLogQueryDao getLastLogQueryDao(){
		return LastLogQueryDaoHelper.lastlogquerydao;
	}
}

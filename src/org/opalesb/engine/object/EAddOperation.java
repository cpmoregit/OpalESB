package org.opalesb.engine.object;

import net.spy.memcached.MemcachedClient;

import org.opalesb.common.Log;
import org.opalesb.engine.object.IOperation;
import org.opalesb.engine.queue.QueueManager;


public class EAddOperation {
	
	private MemcachedClient assigneddataCache;
	private String newKey;
	private String newStringToBeParsed;
	private QueueManager newQueueManager; 
	/**
	 * @return the newQueueManager
	 */
	public QueueManager getNewQueueManager() {
		return newQueueManager;
	}
	/**
	 * @param newQueueManager the newQueueManager to set
	 */
	public void setNewQueueManager(QueueManager newQueueManager) {
		this.newQueueManager = newQueueManager;
	}
	/**
	 * @return the newKey
	 */
	public String getNewKey() {
		return newKey;
	}
	/**
	 * @param newKey the newKey to set
	 */
	public void setNewKey(String newKey) {
		this.newKey = newKey;
	}
	/**
	 * @return the newStringToBeParsed
	 */
	public String getNewStringToBeParsed() {
		return newStringToBeParsed;
	}
	/**
	 * @param newStringToBeParsed the newStringToBeParsed to set
	 */
	public void setNewStringToBeParsed(String newStringToBeParsed) {
		this.newStringToBeParsed = newStringToBeParsed;
	}
	
	public MemcachedClient getAssigneddataCache() {
		return assigneddataCache;
	}
	
	public void setAssigneddataCache(MemcachedClient assigneddataCache) {
		this.assigneddataCache = assigneddataCache;
	}
	
	public static Runnable getInstance(
			MemcachedClient assigneddataCache,
			String newKey,
			String newStringToBeParsed,
			QueueManager newQueueManager
			){
		
		AddMovieInfoObjectService addMovieInfoObjectService = null;
		
		try {
			addMovieInfoObjectService = new AddMovieInfoObjectService(
					assigneddataCache, newQueueManager, newKey, newStringToBeParsed);

		} catch (Exception exp) {
			exp.printStackTrace();
			Log.log(Log.FATAL, "Cannot process the record for key" + newKey);
		}
		
		return (Runnable)addMovieInfoObjectService;
	}
	
	
}

package org.opalesb.engine.queue;

import java.util.LinkedList;
import java.util.Queue;
import net.spy.memcached.MemcachedClient;

public class QueueManager{
		
	private MemcachedClient _memcachedClient = null;
	private String _queueName = "_default";
	private int _timeOut = 3600; //3600 s
		
	public QueueManager(String newQueueName, MemcachedClient memcachedClient){
	
		_queueName = newQueueName;
		_memcachedClient = memcachedClient;
		ObjectQueue dataQueue = new ObjectQueue();
		 _memcachedClient.set(newQueueName, _timeOut, dataQueue);
	
	}
	
	public void add(Object queuePushData){
	
		ObjectQueue dataQueue = ( ObjectQueue ) _memcachedClient.get( _queueName );
		dataQueue.add(queuePushData);
		_memcachedClient.set(_queueName, _timeOut, dataQueue);
	
	}
	
	public Object remove(){
		
		ObjectQueue dataQueue = ( ObjectQueue ) _memcachedClient.get( _queueName );
		Object returnQueueObject = (Object) dataQueue.remove();
		_memcachedClient.set(_queueName, _timeOut, dataQueue);
		
		return returnQueueObject;
	}
	
	public void removeQueue(){
		_memcachedClient.delete(_queueName);
	}
	
}

package org.opalesb.engine.queue;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;


public class ObjectQueue implements Serializable {

	private  Queue<Object> _dataQueue = new LinkedList<Object>();
	
	public void add(Object newObject){
		_dataQueue.add(newObject);
	}
	
	public Object remove(){
		return _dataQueue.poll();
	}
}

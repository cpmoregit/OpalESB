package org.opalesb.engine.object;

import java.io.Serializable;

public class EObject implements Serializable {
	
	private boolean _isLocked=false;
	private int _timeOut = 30;
	
	public boolean isLocked() {
		return _isLocked;
	}

	public void Lock() {
		_isLocked = true;
	}
	
	public void clear(){
		_isLocked = false;
	}

}

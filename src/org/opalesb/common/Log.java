package org.opalesb.common;

import org.apache.log4j.Logger;

public class Log {
	
	public static final int TRACE = 1;
	public static final int DEBUG = 2;
	public static final int INFO = 3;
	public static final int WARNING = 4;
	public static final int ERROR = 5;
	public static final int FATAL = 6;
	
	private static String LOGGER_NAME = "OPALESB";
	
	private static Logger logger = Logger.getLogger( LOGGER_NAME );
	
	public static void log(int level, String Message){
		
		switch(level){
			case TRACE:{					
				logger.trace( Message );				
			}break;
			
			case DEBUG:{
				logger.debug( Message );
			}break;
			
			case INFO:{
				logger.info( Message );
			}break;
			
			case WARNING:{
				logger.warn( Message );
			}break;
			
			case ERROR:{
				logger.error( Message );
			}break;
			
			case FATAL:{
				logger.fatal( Message );
			}break;
			
		}
		
	}

}

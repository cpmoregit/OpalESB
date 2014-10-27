package org.opalesb.engine.object;

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;

import org.opalesb.engine.queue.QueueManager;

import net.spy.memcached.MemcachedClient;

public class AddMovieInfoObjectService implements Runnable {
	
	private QueueManager _queueManager = null;
	private MemcachedClient _dataCache = null;

	private String _stringToBeParsed;
	private String _key = null;
	private int _expiry = 3600;
	
	public AddMovieInfoObjectService(
			                  MemcachedClient assigneddataCache, 
			                  QueueManager newQueueManager, 
			                  String newKey,
			                  String newStringToBeParsed){
		_dataCache = assigneddataCache;
		_queueManager = newQueueManager;
		_key = newKey;
		_stringToBeParsed = newStringToBeParsed;
	}
	
	private void parse(){
		
		int count =0;
		StringTokenizer thirdTokenizer = null;
		
		byte[] data;
		int len =0, i=0;
		int hashtagfieldCount = 0;
		int parenthesisCount = 0;
		int curlybracketCount = 0;
		int lastfieldCount = 0;
		
		int ihashtagfield = 0;
		int iparenthesis = 0;
		int icurlybracket = 0;
		int ilastfield = 0;
		
		byte[] hashTag = null;
		byte[] parenthesis = null;
		byte[] curly_bracket = null;
		byte[] last_field = null;
		
		hashTag = new byte[300];
		parenthesis = new byte[300];
		curly_bracket = new byte[400];
		last_field = new byte[300];
		
		data = _stringToBeParsed.getBytes();
		len = data.length;
		
		MovieInfo movieInfo = new MovieInfo();
		
		for(i=0;i<len;i++){
			
			if(hashtagfieldCount <2){
				if( data[i] != '"' ){
					if(hashtagfieldCount>0){
						if( data[i]!='\t'){
							hashTag[ihashtagfield] = data[i];
							ihashtagfield++;
						}
					}else{
						hashtagfieldCount++;
					}
				}else{
					hashtagfieldCount++;
				}
			} else if(parenthesisCount <2){
				if( data[i] != '(' ){
					if( data[i] != ')' ){
						if( parenthesisCount>0 ){
							parenthesis[iparenthesis] = data[i];
							iparenthesis++;
						}
					}else{
						parenthesisCount++;
					}
				}else{
					parenthesisCount++;
				}
			} else if(curlybracketCount <2){
				if( data[i] != '{' ){
					if( data[i] != '}' ){
						if(curlybracketCount>0){
							if( data[i]!='\t'){
								curly_bracket[icurlybracket] = data[i];
								icurlybracket++;
							}
						}
					}else{
						curlybracketCount++;
					}
				}else{
					curlybracketCount++;
				}
			} else if( data[i]!='\t'){					
				last_field[ilastfield]=data[i];
				ilastfield++;
			}
			}
				
		// Add to Movie Info
		movieInfo.setId(_key);
		movieInfo.setHashTag((new String(hashTag)).trim());
		movieInfo.setYear( (new String(parenthesis)).trim());
		movieInfo.setMovieName((new String(curly_bracket)).trim());
		movieInfo.setReleasedAgainOn((new String(last_field)).trim());	
		
		_dataCache.set(_key, 3600, data);
		_queueManager.add( _key);
		
	}
	
	
	@Override
	public void run() {
		parse();
	}

}

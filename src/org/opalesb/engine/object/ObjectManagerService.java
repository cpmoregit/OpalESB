package org.opalesb.engine.object;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import org.opalesb.common.Log;
import org.opalesb.engine.queue.QueueManager;

public class ObjectManagerService extends ThreadPoolExecutor {

	private MemcachedClient _dataMemcachedClient = null;
	private MemcachedClient _queueMemcachedClient = null;
	private QueueManager _queueManager = null;
	private String _queueName = "_dataProcessQueue";
	boolean _autoIndex;
	long _startIndex;
	long _nextIndex;

	public ObjectManagerService(int corePoolSize, int maximumPoolSize,
			long keepAliveTime, TimeUnit timeUnit,
			BlockingQueue<Runnable> workQueue, boolean autoIndex,
			long startIndex) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, timeUnit, workQueue);
		_autoIndex = autoIndex;
		_startIndex = startIndex;
		_nextIndex = _startIndex;
	}

	public void add(String newKey, String data) {

		/*
		 * EAddOperation eAddOperation = new EAddOperation();
		 * eAddOperation.setAssigneddataCache( _dataMemcachedClient );
		 * eAddOperation.setNewKey(newKey);
		 * eAddOperation.setNewQueueManager(_queueManager);
		 * eAddOperation.setNewStringToBeParsed(data);
		 * 
		 * if(getActiveCount() > 2500){ for(int i=0;i<2500;i++); // Do Nothing -
		 * Wait System.out.println("Process Busy..."); }
		 */
		AddMovieInfoObjectService addMovieInfoObjectService = null;

		try {
			addMovieInfoObjectService = new AddMovieInfoObjectService(
					_dataMemcachedClient, _queueManager, newKey, data);
			boolean tryAgain = true;

			while (tryAgain) {
				try {
					execute(addMovieInfoObjectService);
					tryAgain = false;
				} catch (Exception exp) {
					Log.log(Log.ERROR, "Unable to process data for " + newKey
							+ " trying again ....");
					for (int i = 0; i < 500; i++)
						; // Do Nothing -
					System.out.println("Process Busy...");
				}
			}

		} catch (Exception exp) {
			exp.printStackTrace();
			Log.log(Log.FATAL, "Cannot process the record for key" + newKey);
		}

	}

	private static void delete(String key) {

		EObject newObject = (EObject) get(key);

	}

	private static void update(String key) {

		EObject newObject = (EObject) get(key);

	}

	private static EObject get(String key) {
		return null;
	}

	protected void finalize() {

		if (_queueMemcachedClient != null) {
			_queueMemcachedClient.shutdown();
		}

		if (_dataMemcachedClient != null) {
			_dataMemcachedClient.shutdown();
		}

	}

	public void process(String newfileName) {

		String fromFileName = newfileName;
		FileReader fromFileReader = null;
		BufferedReader fromReadFileBuffer = null;

		String readLine = null;

		try {

			if (_dataMemcachedClient == null) {
				_dataMemcachedClient = new MemcachedClient(
						AddrUtil.getAddresses("127.0.0.1:11211"));
			}

			if (_queueMemcachedClient == null) {
				_queueMemcachedClient = new MemcachedClient(
						AddrUtil.getAddresses("127.0.0.1:11211"));
			}

			if (_queueManager == null) {
				_queueManager = new QueueManager(_queueName,
						_queueMemcachedClient);
			}

			fromFileReader = new FileReader(fromFileName);
			fromReadFileBuffer = new BufferedReader(fromFileReader);
			long before = System.currentTimeMillis();

			while ((readLine = fromReadFileBuffer.readLine()) != null) {
				if (getActiveCount() >= 2500) {
					for (int i = 0; i < 10000; i++)
						; // Do Nothing - Wait
					System.out.println("Process Busy...");
				}
				processData(readLine);
			}

			long after = System.currentTimeMillis();

			Log.log(Log.INFO, "File : " + newfileName
					+ (_nextIndex - _startIndex) + " lines were processed in  "
					+ ((after - before) / 3600) + " minutes.");

		} catch (Exception exp) {
			// exp.printStackTrace();
			Log.log(Log.FATAL, exp.getMessage());
		} finally {
			try {
				fromReadFileBuffer.close();
				fromFileReader.close();
			} catch (Exception exp) {
				Log.log(Log.FATAL, exp.getMessage());
			}
		}
	}

	private void processData(String readLine) {

		if (_autoIndex == true) {
			add(String.valueOf(_nextIndex), readLine);
			System.out.println(" Finished Processing " + _nextIndex);

			_nextIndex++;
		} else {
			add("", readLine);
		}

	}

	public static void main(String[] args) {

		int corePoolSize = 2500;
		int maximumPoolSize = 3000;
		long keepAliveTime = 300;
		TimeUnit timeUnit = TimeUnit.SECONDS;

		BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(10000);

		ObjectManagerService objectManagerService = new ObjectManagerService(
				corePoolSize, maximumPoolSize, keepAliveTime, timeUnit,
				workQueue, true, 0);

		objectManagerService.process("/home/cpmore/imdb/data/movies.list");

	}

}

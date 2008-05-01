package metaface.server;
import java.util.*;
/**
 *  Handler class for perform work requested by the Pool.
 *
 *@author     Simon Beard 
 *@version    1.0
 *@see        metaface.client.HTTPClient
 *@see        Pool
 *@see        Worker
 *@see        HTTPServerWorker
 *@see        metaface.client.MetaFaceHTTPClient
 *@see        MetaFaceHTTPServer
 */
class WorkerThread extends Thread {
	/**
	 *  Current worker
	 */
	private Worker _worker;
	/**
	 *  Current data
	 */
	private Object _data;
	/**
	 *  Pool of workers
	 */
	private Pool pool;


	/**
	 *  Creates a new WorkerThread
	 *
	 *@param  id      Thread ID
	 *@param  worker  Worker instance associated with the WorkerThread
	 *@param  p       Description of the Parameter
	 */
	WorkerThread(String id, Worker worker, Pool p) {
		super(id);
		_worker = worker;
		_data = null;
		pool = p;
	}


	/**
	 *  Wakes the thread and does some work
	 *
	 *@param  data  Data to send to the Worker
	 */
	synchronized void wake(Object data) {
		_data = data;
		notify();
	}


	/**
	 *  WorkerThread's thread routine
	 */
	public synchronized void run() {
		boolean stop = false;
		while (!stop) {
			if (_data == null) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
					continue;
				}
			}
			if (_data != null) {
				_worker.run(_data);
			}
			_data = null;
			stop = !(pool._push(this));
		}
	}
}


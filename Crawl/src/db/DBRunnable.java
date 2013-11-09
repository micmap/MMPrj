package db;

import java.util.concurrent.LinkedBlockingQueue;

import utils.Logger;
import data.ZvzListItem;

public class DBRunnable implements Runnable {

	public static LinkedBlockingQueue<ZvzListItem> dbInsertQueue;
	
	@Override
	public void run() {
		try {
			// TODO: indicate done
			while (true) {
				ZvzListItem item = dbInsertQueue.poll();
				if (item != null) {
					DB.saveToDB(item);
				}
			}
		} catch (Exception e) {
			Logger.log("error calling save item to db", e);			
		}
	}

	public DBRunnable() {
		dbInsertQueue = new LinkedBlockingQueue<>();
	}
	
	public static void queueSaveToDB(ZvzListItem item) {
		try {
			dbInsertQueue.put(item);
		} catch (InterruptedException e) {
			Logger.log("error queueing a save to db", e);
			Logger.log(item.toString());
		}
	}
	
}

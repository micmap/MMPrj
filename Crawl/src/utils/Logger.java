package utils;

import java.util.logging.Level;

/**
 * Log utilities
 * @author Inbal
 *
 */
public class Logger {

	private static java.util.logging.Logger log = java.util.logging.Logger.getLogger("Crawl");
	
	public static void log(String msg) {
		log.info(msg);
	}
	
	public static void log(String msg, Exception e) {
		log.log(Level.WARNING, msg, e);
	}

}

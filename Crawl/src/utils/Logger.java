package utils;

/**
 * Log utilities
 * @author Inbal
 *
 */
public class Logger {

	public static void log(String msg) {
		System.out.println(msg);
	}
	
	public static void log(String msg, Exception e) {
		System.out.println(msg);
		e.printStackTrace();
	}

}

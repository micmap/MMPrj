package utils;

/**
 * Sleep utilities
 * @author Inbal
 *
 */
public class Sleeper {

	public static void sleep(int seconds) {

		//Logger.log("sleeping...");
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			Logger.log("Error sleeping", e);
		}
	}
}

package main;

import utils.Logger;
import extract.zvz.ZvzExtractor;

public class ExtractMain {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		/** Works with jre7 and didn't work with jdk1.7 **/
		
		ZvzExtractor extract = new ZvzExtractor();
		
		try {
			extract.extract();
		} catch (Exception e) {
			Logger.log("Main program error", e);
		}
	}

}

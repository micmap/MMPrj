package utils;

/**
 * Config utilities
 * @author Inbal
 *
 */
public class Config {

	// TODO read this from xml
	
	public static boolean isWriteImagesToFile() {
		return true;
	}
	
	public static String dirToWriteImages() {
		return "C:\\Crawl\\Images";
	}
	
	/**
	 * url of the first page to start extracting from
	 * @return
	 */
	public static String getStartPage() {

		// all rent
		// "http://www.zvz.co.il/AppartmentResult.aspx?t=3&search=%D7%9C%D7%94%D7%A9%D7%9B%D7%A8%D7%94"

		// some pages in rent 
		return "http://www.zvz.co.il/AppartmentResult.aspx?t=3&search=%D7%9C%D7%94%D7%A9%D7%9B%D7%A8%D7%94-%D7%90%D7%96%D7%95%D7%A8-%D7%94%D7%A9%D7%A8%D7%95%D7%9F-%D7%A8%D7%9E%D7%AA-%D7%94%D7%A9%D7%A8%D7%95%D7%9F---%D7%94%D7%A8%D7%A6%D7%9C%D7%99%D7%94-%D7%A8%D7%9E%D7%AA-%D7%94%D7%A9%D7%A8%D7%95%D7%9F---%D7%94%D7%A8%D7%A6%D7%9C%D7%99%D7%94-%D7%94%D7%A8%D7%A6%D7%9C%D7%99%D7%94-3-%D7%97%D7%93%D7%A8%D7%99%D7%9D#SR_top";
	}
}
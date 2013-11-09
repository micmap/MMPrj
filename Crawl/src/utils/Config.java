package utils;

import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Config utilities
 * @author Inbal
 *
 */
public class Config {

	private static final String CONF_FILE_NAME = "config.properties";

	public final static Config INSTANCE = new Config();
	
	private boolean isWriteImagesToFile;
	private String dirToWriteImages;
	private String startPage;
	private boolean isCheckForDuplicates;
	private boolean isIgnoreDuplicates;
	private boolean isRealtors;
	private String dbConnection;
	private String dbUser;
	private String dbPswd;
	
	private Config() {
		
		PropertiesConfiguration configuration;
		try {
			configuration = new PropertiesConfiguration(CONF_FILE_NAME);
			
			isWriteImagesToFile = 
					configuration.getBoolean("isWriteImagesToFile");
			dirToWriteImages =
					configuration.getString("dirToWriteImages");
			startPage = 
					configuration.getString("startPage");
			isCheckForDuplicates = 
					configuration.getBoolean("isCheckForDuplicates");
			isIgnoreDuplicates = 
					configuration.getBoolean("isIgnoreDuplicates");
			isRealtors =
					configuration.getBoolean("isRealtors");
			dbConnection =
					configuration.getString("dbConnection");
			dbUser =
					configuration.getString("dbUser");
			dbPswd =
					configuration.getString("dbPswd");
		} catch (Exception e) {
			Logger.log("Error reading configuration", e);
		}
	}
	
	public boolean isWriteImagesToFile() {
		return isWriteImagesToFile;
	}
	
	public String dirToWriteImages() {
		return dirToWriteImages;
	}
	
	/**
	 * url of the first page to start extracting from
	 * @return
	 */
	public String getStartPage() {
		return startPage;
	}

	/**
	 * should the process check for duplicate items
	 * @return
	 */
	public boolean isCheckForDuplicates() {
		return isCheckForDuplicates;
	}

	/**
	 * if a duplicate item is encountered should it be saved or not.
	 * only relevant if duplicate items are checked for.
	 * @return
	 */
	public boolean isIgnoreDuplicates() {
		return isIgnoreDuplicates;
	}

	/**
	 * should realtor items be extracted or regular items.
	 * can't do both..
	 * @return
	 */
	public boolean isRealtors() {
		return isRealtors;
	}

	public String getDbConnection() {
		return dbConnection;
	}

	public String getDbUser() {
		return dbUser;
	}

	public String getDbPswd() {
		// TODO: use something safer?
		return dbPswd;
	}

}

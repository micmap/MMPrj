package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;

/**
 * IO utilities
 * @author Inbal
 *
 */
public class IO {

	public static void writeToFile(String fileName, String txt) {

		try {
			File file = new File(fileName);
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(txt);
			out.close();		
		} catch (Exception e) {
			Logger.log("Error writing text to file", e);
		}
	}
	
	public static void writeToFile(String fileName, byte[] data) {

		try {
			OutputStream out = new FileOutputStream(fileName);
			out.write(data);
			out.close();
		} catch (Exception e) {
			Logger.log("Error writing img to file", e);
		}
	}
	
	public static void createDir(String path) {
		File dir = new File(path);
		dir.mkdir();
	}
}

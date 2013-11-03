package db;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Parser;

import utils.Logger;

public class GeoRequest {

	private static final String USER_AGENT = "Mozilla/5.0";
	
	public static Double[] sendRequest(String address, String city, String country) {
		 
		Double[] retVal = null;
		
		try {
			
			URI uri = new URI(
				    "http", 
				    "maps.googleapis.com", 
				    "/maps/api/geocode/json",
				    "address=" + address + "," + city + "," + country + "&sensor=false",
				    null);
				
			URL obj = new URL(uri.toASCIIString());
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	 
			// optional default is GET
			con.setRequestMethod("GET");
	 
			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);
	 
			int responseCode = con.getResponseCode();
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	 
			retVal = parseResponse(response.toString());
			
		} catch (Exception e) {
			Logger.log("error using google maps api", e);
		}
		
		return retVal;
	}
	
	private static Double[] parseResponse(String response) throws ParseException {
		
		JSONObject json = (JSONObject)new JSONParser().parse(response);
		JSONArray resArray = (JSONArray) json.get("results");
		JSONObject firstRes = (JSONObject) resArray.get(0);
		JSONObject geo = (JSONObject) firstRes.get("geometry");
		JSONObject location = (JSONObject) geo.get("location");
		
		Double[] retVal = new Double[2];
		retVal[0] = (Double) location.get("lng");
		retVal[1] = (Double) location.get("lat");
		
		return retVal;
	}
}

package db;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgresql.geometric.PGpoint;
import org.postgresql.largeobject.BlobInputStream;

import utils.Logger;
import data.ZvzListItem;

public class DB {

	private static Connection con;
	private static PreparedStatement userStmt;
	private static PreparedStatement propStmt;
	private static PreparedStatement selectUserStmt;

	public static void save(ZvzListItem item) {

		try {
			saveToUserTbl(item);
			saveToPropertyTbl(item);
		} catch (Exception e) {
			Logger.log("error saving item to db", e);
			Logger.log(item.toString());
		}
	}

	/**
	 * @param item
	 */
	private static void saveToUserTbl(ZvzListItem item) {

		// TODO: fill all columns in user table

		try {
			userStmt.setString(1, item.getSellerName());
			userStmt.setString(2, item.getSellerPhone());
			userStmt.setString(3, item.getSellerPhone2());
			userStmt.execute();
		} catch (SQLException e) {
			Logger.log("error saving user to db", e);
			Logger.log(item.toString());
		}
	}

	/**
	 * @param item
	 */
	private static void saveToPropertyTbl(ZvzListItem item) {

		// TODO: fill all columns in property table
		// TODO: handle relation to user
		
		try {
			propStmt.setString(1, item.getCountry());
			propStmt.setString(2, item.getCity());
			propStmt.setString(3, item.getAddress());
			propStmt.setInt(4, getInt(item.getPrice()));
			propStmt.setInt(5, getInt(item.getPayments()));
			propStmt.setInt(6, getInt(item.getSize()));
			propStmt.setInt(7, getInt(item.getNumOfRooms()));
			propStmt.setInt(8, getInt(item.getFloor()));
			// TODO: why is apartment type an integer
			propStmt.setInt(9, -1);
			propStmt.setString(10, item.getNotes());
			propStmt.setInt(11, getInt(item.getBalcony()));
			propStmt.setBoolean(12, getBool(item.getForDisabled()));
			propStmt.setBoolean(13, getBool(item.getRoomates()));
			propStmt.setBoolean(14, getBool(item.getProtectedSpace()));
			propStmt.setBoolean(15, getBool(item.getSoragim()));
			propStmt.setBoolean(16, getBool(item.getParking()));
			propStmt.setBoolean(17, getBool(item.getAc()));
			propStmt.setBoolean(18, getBool(item.getElevator()));
			propStmt.setInt(19, -1); // TODO: flagschecksum
			
			// image
			byte[] img = null;
			if (item.getImagesBlob() != null && item.getImagesBlob().length > 0) {
				img = item.getImagesBlob()[0];				
			} 
			propStmt.setBytes(20, img);
			
			// geo
			Double[] pnt = GeoRequest.sendRequest(item.getAddress(), item.getCity(), item.getCountry());
			String center = null;
			if (pnt != null) {
				center = "POINT(" + pnt[0] + " " + pnt[1] + ")";				
			}
			propStmt.setString(21, center);
			
			// use relation
			BigDecimal userId = BigDecimal.ZERO;
			selectUserStmt.setString(1, item.getSellerPhone());
			ResultSet res = selectUserStmt.executeQuery();
			if (res != null && res.next()) {
				userId = res.getBigDecimal(1);
				propStmt.setBigDecimal(22, userId);
			}
			
			propStmt.execute();
			
		} catch (SQLException e) {
			Logger.log("error saving property to db", e);
			Logger.log(item.toString());
		} 

	}

	private static boolean getBool(String s) {
		// TODO: consts
		boolean isTrue = s != null && (s.equals("ιω") || s.equals("λο")); 
		return isTrue;
	}

	private static int getInt(String s) {
		int retVal = -1;
		if (s != null) {
			try {
				retVal = Integer.parseInt(s.replaceAll("\\D+", ""));
			} catch (Exception e) {

			}
		}
		return retVal;
	}

	public static void init() throws SQLException, ClassNotFoundException {
		
		// TODO: to config
		String url = "jdbc:postgresql://localhost:5432/MM";
		String user = "dbuser";
		String password = "dbuser";

		con = DriverManager.getConnection(url, user, password);
		 
		userStmt = con
				.prepareStatement("insert into mmuser(name,phonenumber,phonenumber2) values(?,?,?)");

		// TODO: add columns
		// apartmentnumber
		// maplocation
		// exteriorimageurl
		// numberoffloorsinbuilding
		// publishdate
		// propertytax
		// housecommittee
		// isrenovated
		// ispetsallowed
		// isfurnished
		// apartmentrating
		// ownerrating
		// entrydate
		propStmt = con
				.prepareStatement("insert into mmproperty(" +
						"country,city,address,price,numberofpayments," +
						"size,numberofrooms,floor,apartmenttype,propertysummary," +
						"balcony,ishandicapaccessible,isroommatesuitable,hasaps,haswindowbars," +
						"hasparking,hasairconditioning,haselevator,flagschecksum,exteriorimageurl," + 
						"maplocation,useruniqueid) "
						+ "values(" +
						"?,?,?,?,?," +
						"?,?,?,?,?," +
						"?,?,?,?,?," +
						"?,?,?,?,?," +
						"ST_GeographyFromText(?), ?)");
		
		selectUserStmt = con.prepareStatement("SELECT useruniqueid FROM mmuser WHERE phonenumber LIKE ?");
	}

	public static void cleanUp() {

		try {
			if (userStmt != null) {
				userStmt.close();
			}
			if (propStmt != null) {
				propStmt.close();
			}
			if (con != null) {
				con.close();
			}
		} catch (SQLException e) {
			Logger.log("error cleaning up db", e);
		}
	}
}

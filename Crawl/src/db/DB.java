package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import utils.Logger;
import data.ZvzListItem;

public class DB {

	private static Connection con;
	private static PreparedStatement userStmt;
	private static PreparedStatement propStmt;

	public static void save(ZvzListItem item) {

		try {
			saveToUserTbl(item);
			saveToPropertyTbl(item);
		} catch (Exception e) {
			Logger.log("error saving item to db", e);
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
			Logger.log("error saving item to db", e);
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
			
			propStmt.execute();
			
		} catch (SQLException e) {
			Logger.log("error saving item to db", e);
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

	public static void init() throws SQLException {
		
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
				.prepareStatement("insert into mmproperty(country,city,address,"
						+ "price,numberofpayments,size,numberofrooms,floor,apartmenttype,propertysummary,"
						+ "balcony,ishandicapaccessible,isroommatesuitable,hasaps,"
						+ "haswindowbars,hasparking,hasairconditioning,haselevator,flagschecksum) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
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

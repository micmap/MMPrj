package db;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import utils.Config;
import utils.Logger;
import data.ZvzListItem;
import extract.zvz.ZvzItemExtractor;

public class DB {	
	
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	private static final String IMMEDIATE_DATE = "מיידי";
	private static final String NO_1 = "אין";
	private static final String IS_RENOVATED_2 = "משופצת";
	private static final String IS_RENOVATED_1 = "משופץ/כחדש";
	private static final String TRUE_BOOL_2 = "כן";
	private static final String TRUE_BOOL_1 = "יש";
	
	private static Connection con;
	private static PreparedStatement userStmt;
	private static PreparedStatement companyStmt;
	private static PreparedStatement propStmt;
	private static PreparedStatement selectUserStmt;
	private static PreparedStatement selectCompanyStmt;	
	
	static void saveToDB(ZvzListItem item) {

		// TODO: check working with isRealtors=true (never checked because it doesn't work in zvz)
		// private should only have user id. realtor should have user and company (that will be the same for now)
		
		try {
			if (item.getIsRealtor()) {
				saveToCompanyTbl(item);
			} else {
				saveToUserTbl(item);
			}
			
			saveToPropertyTbl(item);
			
		} catch (Exception e) {
			Logger.log("error saving item to db", e);
			Logger.log(item.toString());
		}
	}

	private static void saveToCompanyTbl(ZvzListItem item) {

		try {
			companyStmt.setString(1, item.getSellerName());
			companyStmt.setString(2, item.getSellerPhone());
			
			companyStmt.execute();
			
		} catch (SQLException e) {
			Logger.log("error saving company to db", e);
			Logger.log(item.toString());
		}
	}

	/**
	 * @param item
	 */
	private static void saveToUserTbl(ZvzListItem item) {

		try {
			userStmt.setString(1, item.getSellerName());
			userStmt.setString(2, item.getSellerPhone());
			
			String phone2;
			if (item.getSellerPhone2() == null || item.getSellerPhone2().isEmpty()) {
				phone2 = item.getSellerPhone();
			} else {
				phone2 = item.getSellerPhone2();
			}
			userStmt.setString(3, phone2);
			
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

		try {
			propStmt.setString(1, item.getCountry());
			propStmt.setString(2, item.getCity());
			propStmt.setString(3, item.getAddress());
			propStmt.setInt(4, getInt(item.getPrice()));
			propStmt.setInt(5, getInt(item.getPayments()));
			propStmt.setInt(6, getInt(item.getSize()));
			propStmt.setInt(7, getInt(item.getNumOfRooms()));
			propStmt.setInt(8, getInt(item.getFloor()));
			propStmt.setInt(9, getApartmentType(item.getPropType()));
			propStmt.setString(10, getItemSummary(item));
			propStmt.setInt(11, getInt(item.getBalcony()));
			propStmt.setBoolean(12, getBool(item.getForDisabled()));
			propStmt.setBoolean(13, getBool(item.getRoomates()));
			propStmt.setBoolean(14, getBool(item.getProtectedSpace()));
			propStmt.setBoolean(15, getBool(item.getSoragim()));
			propStmt.setBoolean(16, getBool(item.getParking()));
			propStmt.setBoolean(17, getBool(item.getAc()));
			propStmt.setBoolean(18, getBool(item.getElevator()));
			// TODO: flagschecksum
			propStmt.setInt(19, -1); 
			
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
			
			fillRelationToSeller(item);
			
			propStmt.setInt(23, getInt(item.getFloorInBuilding()));
			propStmt.setBoolean(24, getBool(item.getIsPetsAllowed()));
			propStmt.setBoolean(26, getIsRenovated(item.getPropCondition()));
			propStmt.setBoolean(27, getIsFurnished(item.getFurniture()));
			propStmt.setDate(28, getEntryDate(item.getEntDate()));
			
			propStmt.execute();
			
		} catch (SQLException e) {
			Logger.log("error saving property to db", e);
			Logger.log(item.toString());
		} 

	}

	private static int getApartmentType(String propType) {
		MMApartmentTypeEnum retval;
		switch (propType) {
		case "דירה":
			retval = MMApartmentTypeEnum.APARTMENT_APRT_TYPE;
			break;
		case "פנטהאוז / דירת גג":
			retval = MMApartmentTypeEnum.PENTHOUSE_APRT_TYPE;
			break;
		case "בית פרטי / וילה / קוטג`":
			retval = MMApartmentTypeEnum.HOUSE_APRT_TYPE;
			break;
		case "לופט / סטודיו":
			retval = MMApartmentTypeEnum.STUDIO_APRT_TYPE;
			break;
		case "דופלקס":
			retval = MMApartmentTypeEnum.DUPLEX_APRT_TYPE;
			break;
		case "טריפלקס":
			retval = MMApartmentTypeEnum.TRIPLEX_APRT_TYPE;
			break;
		case "דירת גן":
			retval = MMApartmentTypeEnum.PARTERRE_APRT_TYPE;
			break;
		case "יחידת דיור":
			retval = MMApartmentTypeEnum.UNIT_APRT_TYPE;
			break;
		case "קרוואן":
			retval = MMApartmentTypeEnum.TRAILER_APRT_TYPE;
			break;
		case "תקופות קצרות":
			retval = MMApartmentTypeEnum.SHORT_PERIOD_TIME_TYPE;
			break;
		case "משק / נחלה":
			retval = MMApartmentTypeEnum.FARM_APRT_TYPE;
			break;
		default:
			Logger.log("unknown prop type: " + propType);
			retval = MMApartmentTypeEnum.APARTMENT_APRT_TYPE;
			break;
		}
		return retval.ordinal();
	}

	private static Date getEntryDate(String entDate) {
		Date retval = null;
		if (entDate != null && entDate.isEmpty() == false) {
			if (entDate.equals(IMMEDIATE_DATE)) {
				retval = new Date(new java.util.Date().getTime());
			} else {
				try {
					retval = new Date(DATE_FORMAT.parse(entDate).getTime());
				} catch (Exception e) {
					retval = null;
				}
			}
		}
		return retval;
	}

	/**
	 * @param item
	 * @return
	 */
	private static boolean getIsFurnished(String furniture) {
		return furniture != null && furniture.isEmpty() == false && furniture.equals(NO_1) == false;
	}

	private static String getItemSummary(ZvzListItem item) {
		StringBuilder bldr = new StringBuilder();
		if (item.getNotes() != null) {
			bldr.append(item.getNotes());
		}
		if (item.getPropCondition() != null && item.getPropCondition().isEmpty() == false) {
			bldr.append("\n");
			bldr.append(ZvzItemExtractor.PROP_CONDITION + item.getPropCondition());
		}
		if (item.getFurniture() != null && item.getFurniture().isEmpty() == false) {
			bldr.append("\n");
			bldr.append(ZvzItemExtractor.FURNITURE_STRING + item.getFurniture());
		}
		return bldr.toString();	
	}

	private static boolean getIsRenovated(String propCondition) {
		boolean retval = false;
		if (propCondition != null) {
			retval = propCondition.equals(IS_RENOVATED_1) || propCondition.equals(IS_RENOVATED_2);
		}
		return retval;
	}

	/**
	 * @param item
	 * @throws SQLException
	 */
	private static void fillRelationToSeller(ZvzListItem item) throws SQLException {
		
		BigDecimal userId = null;
		BigDecimal companyId = null;
		
		ResultSet res;
		if (item.getIsRealtor()) {
			selectCompanyStmt.setString(1, item.getSellerPhone());
			res = selectCompanyStmt.executeQuery();
			if (res != null && res.next()) {
				companyId = res.getBigDecimal(1);
			}
		} else {
			selectUserStmt.setString(1, item.getSellerPhone());
			res = selectUserStmt.executeQuery();
			if (res != null && res.next()) {
				userId = res.getBigDecimal(1);
			}
		}

		propStmt.setBigDecimal(22, userId);
		propStmt.setBigDecimal(25, companyId);
	}

	private static boolean getBool(String s) {
		boolean isTrue = s != null && (s.equals(TRUE_BOOL_1) || s.equals(TRUE_BOOL_2)); 
		return isTrue;
	}

	private static int getInt(String s) {
		int retVal = -1;
		if (s != null) {
			try {
				int decPnt = s.indexOf(".");
				if (decPnt > -1) {
					s = s.substring(0, decPnt);
				}
				retVal = Integer.parseInt(s.replaceAll("\\D+", ""));
			} catch (Exception e) {

			}
		}
		return retVal;
	}

	public static void init() throws SQLException, ClassNotFoundException {
		
		String url = Config.INSTANCE.getDbConnection();
		String user = Config.INSTANCE.getDbUser();
		String password = Config.INSTANCE.getDbPswd();

		con = DriverManager.getConnection(url, user, password);
		 
		userStmt = con
				.prepareStatement("insert into mmuser(name,phonenumber,phonenumber2) values(?,?,?)");
		
		companyStmt = con
				.prepareStatement("insert into mmcompany(name,phonenumber) values(?,?)");

		propStmt = con
				.prepareStatement("insert into mmproperty(" +
						"country,city,address,price,numberofpayments," +
						"size,numberofrooms,floor,apartmenttype,propertysummary," +
						"balcony,ishandicapaccessible,isroommatesuitable,hasaps,haswindowbars," +
						"hasparking,hasairconditioning,haselevator,flagschecksum,exteriorimageurl," + 
						"maplocation,useruniqueid,numberoffloorsinbuilding,ispetsallowed,companyuniqueid," +
						"isrenovated,isfurnished,entrydate) " +						
						"values(" +
						"?,?,?,?,?," +
						"?,?,?,?,?," +
						"?,?,?,?,?," +
						"?,?,?,?,?," +
						"ST_GeographyFromText(?),?,?,?,?," +
						"?,?,?)");
		
		selectUserStmt = con.prepareStatement("SELECT useruniqueid FROM mmuser WHERE phonenumber LIKE ?");
		selectCompanyStmt = con.prepareStatement("SELECT companyuniqueid FROM mmcompany WHERE phonenumber LIKE ?");
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

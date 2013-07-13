package extract.zvz;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utils.Logger;

import data.ZvzListItem;
import extract.base.IExtractor;

/**
 * extract data from a zvz item in a zvz search page
 * @author Inbal
 *
 */
public class ZvzItemExtractor implements IExtractor {

	private static final String PROP_TYPE_STRING = "סוג הנכס: ";
	private static final String NUM_OF_ROOMS_STRING = "חדרים: ";
	private static final String ARAE_STRING = "אזור: ";
	private static final String CITY_STRING = "ישוב: ";
	private static final String ADDR_STRING = "כתובת: ";
	private static final String MAP_STRING = "  מפה";
	private static final String SIZE_STRING = "שטח: ";
	private static final String FLOOR_STRING = "קומה: ";
	private static final String PAYMENTS_STRING = "מס' תשלומים: ";
	private static final String PROP_STATE_STRING = "מצב הנכס: ";
	private static final String ENT_DATE_STRING = "תאריך כניסה: ";
	private static final String PARKING_STRING = "חניה: ";
	private static final String FURNITURE_STRING = "ריהוט: ";
	private static final String AC_STRING = "מיזוג אוויר: ";
	private static final String SORAGIM_STRING = "סורגים: ";
	private static final String BALCONY_STRING = "מרפסת: ";
	private static final String ROOMATES_STRING = "מתאים לשותפים: ";
	private static final String ELEVATOR_STRING = "מעלית: ";
	private static final String FOR_DISABLED_STRING = "גישה לנכים: ";
	private static final String NEIGHBORHOOD_STRING = "שכונה: ";
	private static final String FURNITURE_NOTES_STRING = "פירוט הריהוט: ";
	private static final String NOTES_STRING = "הערות: ";
	
	private static final String DATA_CONTAINER_ID_SUFFIX = "_container";
	private static final String NOTES_ID = "ctl00_lblNotes";
	private static final String FURNITURE_NOTES_ID = "ctl00_lblapp_Furniture";
	private static final String SELLER_PHONE_ID = "ctl00_lblPhone1";
	private static final String SELLER_NAME_ID = "ctl00_lblContactPerson";
	private static final String PRICE_ID = "ctl00_lblCostTotal";
	private static final String PROPS_2_SUFFIX = "_props_2";
	private static final String PROPS_2_FORMAT = "tbody[1]/tr[%s]/td[1]/div[1]";
	private static final String PROPS_1_SUFFIX = "_props_1";
	private static final String PROPS_1_FORMAT = "tbody[1]/tr[%s]/td[1]/div[1]";
	
	WebElement itemDataContainer;
	WebDriver webDriver;
	ZvzListItem listItem;
	
	public ZvzItemExtractor(WebDriver webDriver, ZvzListItem listItem) {

		String itemId = listItem.getOrigItemId();
		this.itemDataContainer = webDriver.findElement(
				By.id(itemId + DATA_CONTAINER_ID_SUFFIX));
		this.webDriver = webDriver;
		this.listItem = listItem;
	}

	/**
	 * this method will extract all of the data available in a certain
	 * item. a zvz item is composed of 2 dinamic property containers and some const
	 * data that. data that wasn't filled in will be null.
	 */
	public boolean extract() {
		
		// the first dinamic container
		extractProp1Data();
		// the second dinamic container
		extractProp2Data();
		
		// const data
		extractImages();		
		extractPrice();
		extractSellerName();
		extractSellerPhone();
		extractFurnitureNotes();
		extractNotes();
		// const data
		
		return true;
	}

	private void extractImages() {

		IExtractor imageExtractor = 
				new ZvzImageExtractor(itemDataContainer, webDriver, listItem);
		imageExtractor.extract();
	}

	private void extractNotes() {

		try {
			WebElement elem = itemDataContainer.findElement(
					By.id(NOTES_ID));
			if (elem != null) {
				
				String fullText = elem.getText();
				String txt = delFromTxt(fullText, NOTES_STRING);
				
				listItem.setNotes(txt);
			}
		
		} catch (Exception e) {
			
		}	
	}

	private void extractFurnitureNotes() {

		try {
			WebElement elem = itemDataContainer.findElement(
					By.id(FURNITURE_NOTES_ID));
			if (elem != null) {
				
				String fullText = elem.getText();
				String txt = delFromTxt(fullText, FURNITURE_NOTES_STRING);
				
				listItem.setFurnitureNotes(txt);
			}
		
		} catch (Exception e) {
			
		}	
	}

	private void extractSellerPhone() {

		try {
			WebElement elem = itemDataContainer.findElement(
					By.id(SELLER_PHONE_ID));
			if (elem != null) {
				listItem.setSellerPhone(elem.getText());
			}
		
		} catch (Exception e) {
			
		}	
	}

	private void extractSellerName() {
		
		try {
			WebElement elem = itemDataContainer.findElement(
					By.id(SELLER_NAME_ID));
			if (elem != null) {
				listItem.setSellerName(elem.getText());
			}
		
		} catch (Exception e) {
			
		}		
	}

	private void extractPrice() {
		try {
			WebElement priceElem = itemDataContainer.findElement(
					By.id(PRICE_ID));
			if (priceElem != null) {
				listItem.setPrice(priceElem.getText());
			}
		
		} catch (Exception e) {
			
		}
	}

	private void extractProp2Data() {

		WebElement props2Elem =	itemDataContainer.findElement(
				By.id(listItem.getOrigItemId() + PROPS_2_SUFFIX));
		
		int i = 1;
		while (true) {
			
			try {
				
				String xPath = String.format(PROPS_2_FORMAT, i);
				WebElement entDateElem = props2Elem.findElement(By.xpath(xPath));
				
				String fullText = entDateElem.getText();

				if (fullText.startsWith(ENT_DATE_STRING)) {
					String txt = delFromTxt(fullText, ENT_DATE_STRING);
					listItem.setEntDate(txt);
				} else if (fullText.startsWith(PARKING_STRING)) {
					String txt = delFromTxt(fullText, PARKING_STRING);
					listItem.setParking(txt);
				} else if (fullText.startsWith(FURNITURE_STRING)) {
					String txt = delFromTxt(fullText, FURNITURE_STRING);
					listItem.setFurniture(txt);
				} else if (fullText.startsWith(AC_STRING)) {
					String txt = delFromTxt(fullText, AC_STRING);
					listItem.setAc(txt);
				} else if (fullText.startsWith(SORAGIM_STRING)) {
					String txt = delFromTxt(fullText, SORAGIM_STRING);
					listItem.setSoragim(txt);
				} else if (fullText.startsWith(BALCONY_STRING)) {
					String txt = delFromTxt(fullText, BALCONY_STRING);
					listItem.setBalcony(txt);
				} else if (fullText.startsWith(ROOMATES_STRING)) {
					String txt = delFromTxt(fullText, ROOMATES_STRING);
					listItem.setRoomates(txt);
				} else if (fullText.startsWith(ELEVATOR_STRING)) {
					String txt = delFromTxt(fullText, ELEVATOR_STRING);
					listItem.setElevator(txt);
				} else if (fullText.startsWith(FOR_DISABLED_STRING)) {
					String txt = delFromTxt(fullText, FOR_DISABLED_STRING);
					listItem.setForDisabled(txt);
				} else {
					Logger.log("*** Unknown prop in prop2: " + fullText + " " + i);
				}
				
			} catch (Exception e) {
				break;
			}
			
			i++;
		}
	}

	private void extractProp1Data() {

		WebElement props2Elem =	itemDataContainer.findElement(
				By.id(listItem.getOrigItemId() + PROPS_1_SUFFIX));
		
		int i = 1;
		while (true) {
			
			try {
				
				String xPath = String.format(PROPS_1_FORMAT, i);
				WebElement entDateElem = props2Elem.findElement(By.xpath(xPath));
				
				String fullText = entDateElem.getText();							
				
				if (fullText.startsWith(PROP_TYPE_STRING)) {
					String txt = delFromTxt(fullText, PROP_TYPE_STRING);
					listItem.setPropType(txt);
				} else if (fullText.startsWith(NUM_OF_ROOMS_STRING)) {
					String txt = delFromTxt(fullText, NUM_OF_ROOMS_STRING);
					listItem.setNumOfRooms(txt);
				} else if (fullText.startsWith(ARAE_STRING)) {
					String txt = delFromTxt(fullText, ARAE_STRING);
					listItem.setArea(txt);
				} else if (fullText.startsWith(CITY_STRING)) {
					String txt = delFromTxt(fullText, CITY_STRING);
					listItem.setCity(txt);
				} else if (fullText.startsWith(ADDR_STRING)) {
					String txt = extractAddress(fullText);
					listItem.setAddress(txt);
				} else if (fullText.startsWith(SIZE_STRING)) {
					String txt = delFromTxt(fullText, SIZE_STRING);
					listItem.setSize(txt);
				} else if (fullText.startsWith(FLOOR_STRING)) {
					String txt = delFromTxt(fullText, FLOOR_STRING);
					listItem.setFloor(txt);
				} else if (fullText.startsWith(PAYMENTS_STRING)) {
					String txt = delFromTxt(fullText, PAYMENTS_STRING);
					listItem.setPayments(txt);
				} else if (fullText.startsWith(PROP_STATE_STRING)) {
					String txt = delFromTxt(fullText, PROP_STATE_STRING);
					listItem.setPropState(txt);
				} else if (fullText.startsWith(NEIGHBORHOOD_STRING)) {
					String txt = delFromTxt(fullText, NEIGHBORHOOD_STRING);
					listItem.setNeighborhood(txt);
				} else {
					Logger.log("Unknown prop in prop1: " + fullText + " " + i);
				}
				
			} catch (Exception e) {
				break;
			}
			
			i++;
		}
	}

	private String delFromTxt(String fullText, String toDel) {
		String txt = fullText.replace(toDel, "");
		return txt;
	}

	/**
	 * @param fullText
	 * @return
	 */
	private String extractAddress(String fullText) {
		String txt = delFromTxt(fullText, ADDR_STRING);
		txt = delFromTxt(txt, MAP_STRING).trim();
		return txt;
	}
	
}

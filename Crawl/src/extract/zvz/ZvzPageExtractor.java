package extract.zvz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utils.Config;
import utils.Logger;
import utils.Sleeper;
import data.ZvzListItem;
import db.DB;
import db.DBRunnable;
import extract.base.IExtractor;

/**
 * Extract all data in a zvz search page
 * @author Inbal
 *
 */
public class ZvzPageExtractor implements IExtractor {

	private static final String ZVZ_ITEMS_TABLE_ID = "SR_grv";
	private static final String ZVZ_LIST_ITEM_PREFIX_ID = "list_item_";
	
	private WebDriver webDriver;
	private String firstRow;
	
	private Map<String, Integer> idsFrequency;
	private Map<String, List<String>> itemsFrequency;
	
	public ZvzPageExtractor(WebDriver webDriver) {
		this.webDriver = webDriver;
		idsFrequency = new HashMap<String, Integer>();
		itemsFrequency = new HashMap<String, List<String>>();
	}

	/**
	 * extract data from the zvz items in the current page
	 * @return
	 * @throws Exception
	 */
	public boolean extract() {
		
		boolean isKeepGoing = false;
		
		if (webDriver != null) {
			
	    	WebElement itemsTable = webDriver.findElement(By.id(ZVZ_ITEMS_TABLE_ID));
	    	List<WebElement> allRows = itemsTable.findElements(By.tagName("tr"));
	    	
	    	boolean isFirstRow = true;
	    	for (final WebElement row : allRows) {
	    		
	    		if (isValidRow(row)) {
	    			
	    			//Logger.log("---");
	    			
	    			ZvzListItem listItem = new ZvzListItem();
	    			String origItemId = row.getAttribute("id").
	    					replace(ZVZ_LIST_ITEM_PREFIX_ID, "");
	    			listItem.setOrigItemId(origItemId);
	    			
	    			// check if this page should be handled
	    			if (isFirstRow) {
	    				String id = listItem.getOrigItemId();
	    				if (firstRow != null && firstRow.equals(id)) {
	    					// no point to keep going - this page first row is the same as the 
	    					// prev's page first row
	    					break;
	    				} else {
	    					firstRow = id;
	    					isKeepGoing = true;
	    				}
	    			}
    				isFirstRow = false;
	        			
    				try {
    					
    					extractItemData(row, listItem);
    					
    					boolean isDuplicate = checkForDuplicates(listItem);
    					
    					boolean isSave =
    							isDuplicate == false ||
    							Config.INSTANCE.isIgnoreDuplicates();
    					
    					if (isSave) {
    						saveListItem(listItem);
    					}
    					
    				} catch (Exception e) {
    					Logger.log("Error extracting item data", e);
					}
    				
    				//Logger.log("---");
	    		}
	    	}
	    	
	    }
		
		return isKeepGoing;
	}
	
	/**
	 * 
	 * @param listItem
	 * @return is the item a duplicate
	 */
	private boolean checkForDuplicates(ZvzListItem listItem) {

		if (Config.INSTANCE.isCheckForDuplicates() == false) {
			return false;
		}
		
		boolean retVal = false;
		
		int freq = 0;
		String id = listItem.getOrigItemId();
		if (idsFrequency.containsKey(id)) {
			freq = idsFrequency.get(id);
			Logger.log("Duplicate Id!" + id);
			retVal = true;
		}
		idsFrequency.put(id, freq + 1);
		
		String itemKey = 
				listItem.getAddress() + 
				listItem.getSellerPhone() + 
				listItem.getSellerName();
		
		List<String> currIds;
		if (itemsFrequency.containsKey(itemKey)) {
			currIds = itemsFrequency.get(itemKey);
			Logger.log("Duplicate Item! " + itemKey);
			for (String string : currIds) {
				Logger.log(string);
			}
			retVal = true;
		} else {
			currIds = new ArrayList<String>();
		}
		currIds.add(id);
		itemsFrequency.put(itemKey, currIds);
		
		return retVal;
	}

	private void saveListItem(ZvzListItem listItem) {

		Logger.log("Saving list item: " + listItem.getOrigItemId());
		//Logger.log(listItem.toString());
		DBRunnable.queueSaveToDB(listItem);
	}

	private boolean isValidRow(WebElement row) {
		String rowId = row.getAttribute("id");
		return rowId.startsWith(ZVZ_LIST_ITEM_PREFIX_ID);
	}

	private void extractItemData(WebElement item, ZvzListItem listItem) throws Exception {
		
		clickItem(item);		
		
		IExtractor itemExtractor = 
				new ZvzItemExtractor(webDriver, listItem);
		itemExtractor.extract();
		
	}

	private void clickItem(WebElement item) throws Exception {

		String itemId = item.getAttribute("id");
		
		Logger.log("clicking row " + itemId);
		
		WebElement anchor = webDriver.findElement(By.id(itemId));
		
		if (anchor != null) {
			
			anchor.click();
			Sleeper.sleep(2);
		}
	}
}

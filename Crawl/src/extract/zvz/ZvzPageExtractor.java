package extract.zvz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import utils.Logger;
import utils.Sleeper;

import data.ZvzListItem;
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
	
	public ZvzPageExtractor(WebDriver webDriver) {
		this.webDriver = webDriver;
		idsFrequency = new HashMap<>();
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
	    			
	    			Logger.log("---");
	    			
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
    					saveListItem(listItem);
    					updateIdsFrequency(listItem);
    				} catch (Exception e) {
    					Logger.log("Error extracting item data", e);
					}
    				
    				Logger.log("---");
	    		}
	    	}
	    	
	    }
		
		return isKeepGoing;
	}
	
	private void updateIdsFrequency(ZvzListItem listItem) {

		int freq = 0;
		String id = listItem.getOrigItemId();
		if (idsFrequency.containsKey(id)) {
			freq = idsFrequency.get(id);
		}
		idsFrequency.put(id, freq + 1);
	}

	private void saveListItem(ZvzListItem listItem) {

		Logger.log("Saving list item: " + listItem.getOrigItemId());
		Logger.log(listItem.toString());
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

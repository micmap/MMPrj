package extract.zvz;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import extract.base.IExtractor;


import utils.Config;
import utils.Logger;
import utils.Sleeper;

/**
 * extract all items in a zvz search
 * @author Inbal
 *
 */
public class ZvzExtractor implements IExtractor {

	private static final String REALTOR_RESULT_VIEW = "SR_lb_realtorResultView";

	private static final String ZVZ_NEXT_BTN_ID = "SR_btnNextTop";
		
	private WebDriver webDriver;
	
	private String getNextBtnId() {
		return ZVZ_NEXT_BTN_ID;
	}
	
	/**
	 * extract all items in a page and keep extracting the next page
	 * untill the last page is reached
	 * @throws Exception
	 */
	public boolean extract() {
		
		try {
			webDriver = new FirefoxDriver();
		
	    	// the first page
			webDriver.get(Config.INSTANCE.getStartPage());
			
			IExtractor pageExtractor = new ZvzPageExtractor(webDriver);
			
			if (Config.INSTANCE.isRealtors()) {
				changeToRealtors();
			}
			
		    while (true) {
		    	boolean isKeepGoing = pageExtractor.extract();
		    	if (!isKeepGoing) {
		    		break;
		    	}
		    	changeToNextPage();
		    }
		    
		    int i = 0;
		    i++;
		    System.out.println(i);
		    
		} catch (Exception e) {
	    	Logger.log("error when changing to next page", e);
	    }
		finally {
			if (webDriver != null) {
				webDriver.quit();
			}
		}
		
		return true;
	}

	/**
	 * 
	 */
	private void changeToRealtors() {
		
		WebElement realtorTabItem = webDriver.findElement(
				By.id(REALTOR_RESULT_VIEW));
		realtorTabItem.click();
		Sleeper.sleep(10);
	}	
	
	public void changeToNextPage() {

		Logger.log("CHANGING TO NEXT PAGE");
		
		WebElement anchor = webDriver.findElement(By.id(getNextBtnId()));
		
		if (anchor != null) {
			
			anchor.click();
			Sleeper.sleep(5);
		}
	}		

}

package extract.zvz;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import utils.Config;
import utils.IO;
import utils.Logger;
import utils.Sleeper;
import data.ZvzListItem;
import extract.base.IExtractor;

/**
 * extract the images in a zvzitem. 
 * extracts up to 4 images (I didn't find any items with more than 4)
 * @author Inbal
 *
 */
public class ZvzImageExtractor implements IExtractor {

	private static final String ITEM_IMAGE_PREFIX = "img_b_";
	private static final String IMAGE_SRC_ATTR = "src";
	private static final String SMALL_IMAGES_ID_FORMAT = "s_img_%s_%s";
	private static final String DEFAULT_IMG_CONTAINS_STR = "default";
	private static final String SCROLL_IMAGES_PREFIX = "scr_r_";
	
	private WebElement itemDataContainer;
	private WebDriver webDriver;
	private ZvzListItem listItem;
	
	public ZvzImageExtractor(WebElement itemDataContainer,
			WebDriver webDriver, ZvzListItem listItem) {
		
		this.itemDataContainer = itemDataContainer;
		this.webDriver = webDriver;
		this.listItem = listItem;
	}
	
	/**
	 * extract the item's images
	 */
	public boolean extract() {
		
		try {
			
			List<String> images = new ArrayList<String>();
			extractImagesSrcs(images);
		
			Logger.log("got images: "+ images.size());
			
			extractBlobs(images);
			
		} catch (Exception e) {
			Logger.log("Error extracting images", e);
		}
		
		return true;
	}

	private void extractImagesSrcs(List<String> images) {
		
		boolean isContinue;
		
		// the main image
		isContinue = addMainImgSrc(images);
		if (!isContinue) { return; }
		
		// mouse over the smaller images (the first one will be the same as the
		// main so i don't need it)
		isContinue = mouseOverImg(2);
		if (!isContinue) { return; }		
		isContinue = addMainImgSrc(images);
		if (!isContinue) { return; }
		
		isContinue = mouseOverImg(3);
		if (!isContinue) { return; }		
		isContinue = addMainImgSrc(images);
		if (!isContinue) { return; }
		
		// scroll to the next image
		isContinue = scrollImages();
		if (!isContinue) { return; }
		
		// after the scroll the third image changes to the new one
		isContinue = mouseOverImg(3);
		if (!isContinue) { return; }		
		isContinue = addMainImgSrc(images);
		if (!isContinue) { return; }
	}

	private boolean scrollImages() {
		
		boolean isContinue = false;
		
		try {
			WebElement scrollElement = itemDataContainer.findElement(
					By.id(SCROLL_IMAGES_PREFIX + listItem.getOrigItemId()));
			if (scrollElement != null) {
				scrollElement.click();
				Sleeper.sleep(2);
				isContinue = true;
			}
		} catch (Exception e) {
		}
		
		return isContinue;
	}

	private boolean addMainImgSrc(List<String> images) {
		
		boolean isContinue = false;
		
		try {
			WebElement imageElement = itemDataContainer.findElement(
					By.id(ITEM_IMAGE_PREFIX + listItem.getOrigItemId()));
			if (imageElement != null) {
				String src = imageElement.getAttribute(IMAGE_SRC_ATTR);
				if (!src.contains(DEFAULT_IMG_CONTAINS_STR) && !images.contains(src) ) {
					// a new image that isn't the default one
					images.add(src);
					isContinue = true;
				}
			}
		} catch (Exception e) {
		}
		
		return isContinue;
	}

	private boolean mouseOverImg(int index) {
		
		boolean isContinue = false;
		
		try {
			String imgId = String.format(SMALL_IMAGES_ID_FORMAT, index, listItem.getOrigItemId());
			WebElement img = itemDataContainer.findElement(By.id(imgId));
			if (img != null) {
				Actions actions = new Actions(webDriver);
				actions.moveToElement(img);
				actions.perform();
				Sleeper.sleep(1);
				isContinue = true;
			}
		} catch (Exception e) {
		}
		
		return isContinue;
	}

	private void extractBlobs(List<String> srcs) {

		if (srcs == null || srcs.size() == 0) {
			return;
		}
		
		byte[][] blobs = new byte[srcs.size()][];
		
		int i = 0;
		for (String src : srcs) {
			
			try {
				
				URL url = new URL(src);
	
		    	InputStream inputStream = url.openStream();
		    	ByteArrayOutputStream output = new ByteArrayOutputStream();
		    	byte[] buffer = new byte[1024];
	
		        int n = 0;
		        while (-1 != (n = inputStream.read(buffer))) {
		           output.write(buffer, 0, n);
		        }
		    	inputStream.close();
	
		    	byte[] data = output.toByteArray();
		    	blobs[i] = data;
		    	
		    	writeToFile(i, data);
	
			} catch (Exception e) {
				Logger.log("Error reading image", e);
	        }
			
			i++;
		}
		
		listItem.setImagesBlob(blobs);
	}

	/**
	 * Write the image's raw data to file
	 * @param imgIndex
	 * @param data
	 */
	private void writeToFile(int imgIndex, byte[] data) {

		if (Config.INSTANCE.isWriteImagesToFile()) {
			
			IO.createDir(Config.INSTANCE.dirToWriteImages());
			
			String fileName = 
					Config.INSTANCE.dirToWriteImages() + "\\" +
					listItem.getOrigItemId() + "_" + imgIndex + ".png";
			
			IO.writeToFile(fileName, data);
		}
	}
}

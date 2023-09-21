package com.cognizant.framework.selenium;

import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.TimeoutException;
import com.cognizant.framework.ALMFunctions;
import com.cognizant.framework.Settings;

/**
 * Class containing useful WebDriver utility functions
 * @author Cognizant
 */
public class WebDriverUtil {
	private CraftDriver driver;
	
	private static Properties properties = Settings.getInstance();
	private ALMFunctions ALMFunctions;
	
	public static final long pageloadTimeout = Long.parseLong(properties.getProperty("PageLoadTimeout"));
	public static final long invisibilityTimeout = Long.parseLong(properties.getProperty("InvisibilityTimeout"));
	public final long staleTimeOut = Long.parseLong(properties.getProperty("StaleTimeOut"));
	
	/**
	 * Constructor to initialize the {@link WebDriverUtil} object
	 * @param driver The {@link WebDriver} object
	 */
	public WebDriverUtil(CraftDriver driver,SeleniumReport report, ALMFunctions almFunctions) {
		this.driver = driver;
		this.ALMFunctions = almFunctions;
	}
	
	/**
	 * Function to pause the execution for the specified time period
	 * @param milliSeconds The wait time in milliseconds
	 */
	public void waitFor(long milliSeconds) {
		try {
			Thread.sleep(milliSeconds);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
	}
	
	/**
	 * Function to wait until the page loads completely
	 * @param timeOutInSeconds The wait timeout in seconds
	 */
	public void waitUntilPageLoaded(long timeOutInSeconds) {
		WebElement oldPage = driver.findElement(By.tagName("html"));
		
		(new WebDriverWait(driver.getWebDriver(), timeOutInSeconds))
									.until(ExpectedConditions.stalenessOf(oldPage));
		
	}
	
	/**
	 * Function to wait until the page readyState equals 'complete'
	 * @param timeOutInSeconds The wait timeout in seconds
	 */
	public void waitUntilPageReadyStateComplete(long timeOutInSeconds) {
		for (long stop=System.nanoTime()+TimeUnit.SECONDS.toNanos(1);stop>System.nanoTime();) {                 }
		ExpectedCondition<Boolean> pageReadyStateComplete =
			new ExpectedCondition<Boolean>() {
	            public Boolean apply(WebDriver driver) {
	                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
	            }
	        };
		    
		(new WebDriverWait(driver.getWebDriver(), timeOutInSeconds)).until(pageReadyStateComplete);
	}
	
	/**
	 * Function to wait until the specified element is located
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds The wait timeout in seconds
	 */
	public void waitUntilElementLocated(By by, long timeOutInSeconds) {
		(new WebDriverWait(driver.getWebDriver(), timeOutInSeconds))
							.until(ExpectedConditions.presenceOfElementLocated(by));
	}
	
	/**
	 * Function to wait until the specified element is located
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds The wait timeout in seconds
	 */
	public boolean waitUntilElementClickable(By by, long timeOutInSeconds) {
		try {
		(new WebDriverWait(driver.getWebDriver(), timeOutInSeconds))
							.until(ExpectedConditions.elementToBeClickable(by));
		return true;
		}catch(TimeoutException e) {
			return false;
		}
		
	}
	
	/**
	 * Function to wait until the specified element is located
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds The wait timeout in seconds
	 */
	public boolean waitUntilElementClickable(By by, long timeOutInSeconds, String elementName, String elementType, String pageName,Boolean LogIndicator) {
		try {
		(new WebDriverWait(driver.getWebDriver(), timeOutInSeconds))
							.until(ExpectedConditions.elementToBeClickable(by));
		return true;
		}catch(TimeoutException e)
		{
			if(LogIndicator)
			{
				ALMFunctions.ThrowException(elementName, elementName+" should be clickable in the page: "+"\""+pageName+"\"",
						"Error - "+elementName+" "+elementType+" is not clickable in the page: "+"\""+pageName+"\""+" even after waiting for "+timeOutInSeconds+" Seconds", true);
			}
			return false;
		}
		catch(Exception e)
		{
			if(LogIndicator)
			{
				ALMFunctions.ThrowException(elementName, elementName+" should be clickable in the page: "+"\""+pageName+"\"",
						"Error - Unable to wait for the "+elementName+" "+elementType+" in the page: "+"\""+pageName+"\"", true);
			}
			return false;
		}
	}
	
	/**
	 * Function to wait until the specified element is visible
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds The wait timeout in seconds
	 */
	public boolean waitUntilElementVisible(By by, long timeOutInSeconds) {
		try {
			(new WebDriverWait(driver.getWebDriver(), timeOutInSeconds))
								.until(ExpectedConditions.visibilityOfElementLocated(by));
			return true;
		}
		catch(TimeoutException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Function to wait until the specified element is visible
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds The wait timeout in seconds
	 */
	public boolean waitUntilElementDisplayed(By by) {
		try {
			(new WebDriverWait(driver.getWebDriver(), 3))
								.until(ExpectedConditions.visibilityOfElementLocated(by));
			if(waitUntilElementClickable(by, 1)) {
			return true;
			}else {return false;}
		}
		catch(TimeoutException e) {
			return false;
		}
	}
	
	/**
	 * Function to wait until the specified element is enabled
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds The wait timeout in seconds
	 */
	public void waitUntilElementEnabled(By by, long timeOutInSeconds) {
		(new WebDriverWait(driver.getWebDriver(), timeOutInSeconds))
							.until(ExpectedConditions.elementToBeClickable(by));
	}
	
	/**
	 * Function to wait until the specified element is disabled
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds The wait timeout in seconds
	 */
	public void waitUntilElementDisabled(By by, long timeOutInSeconds) {
		(new WebDriverWait(driver.getWebDriver(), timeOutInSeconds))
			.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(by)));
	}
	
	/**
	 * Function to select the specified value from a listbox
	 * @param by The {@link WebDriver} locator used to identify the listbox
	 * @param item The value to be selected within the listbox
	 */
	public void selectListItem(By by, String item) {
		Select dropDownList = new Select(driver.findElement(by));
		dropDownList.selectByVisibleText(item);
	}
	
	/**
	 * Function to do a mouseover on top of the specified element
	 * @param by The {@link WebDriver} locator used to identify the element
	 */
	public void mouseOver(By by) {
		Actions actions = new Actions(driver.getWebDriver());
		actions.moveToElement(driver.findElement(by)).build().perform();
	}
	
	/**
	 * Function to verify whether the specified object exists within the current page
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @return Boolean value indicating whether the specified object exists
	 */
	public Boolean objectExists(By by) {
		return !driver.findElements(by).isEmpty();
	}
	
	/**
	 * Function to verify whether the specified object exists within the current page
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @return Boolean value indicating whether the specified object exists
	 */
	public Boolean objectExists(By by, long timeOutInSeconds) {
		waitFor(timeOutInSeconds*100);
		return !driver.findElements(by).isEmpty();
	}
	
	/**
	 * Function to verify whether the specified object exists within the current page
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @return Boolean value indicating whether the specified object exists
	 */
	public boolean objectExists(String xPath) {
		// TODO Auto-generated method stub
		return !driver.findElements(By.xpath(xPath)).isEmpty();
	}
	
	/**
	 * Function to verify whether the specified text is present within the current page
	 * @param textPattern The text to be verified
	 * @return Boolean value indicating whether the specified test is present
	 */
	public Boolean isTextPresent(String textPattern) {
		return driver.findElement(By.cssSelector("BODY")).getText().matches(textPattern);
	}
	
	/**
	 * Function to check if an alert is present on the current page
	 * @param timeOutInSeconds The number of seconds to wait while checking for the alert
	 * @return Boolean value indicating whether an alert is present
	 */
	public Boolean isAlertPresent(long timeOutInSeconds) {
		try {
			new WebDriverWait(driver.getWebDriver(), timeOutInSeconds).until(ExpectedConditions.alertIsPresent());
			return true;
		} catch (TimeoutException ex) {
			return false;
		}
	}
	
	//Gilead-Customized Functions
	/**
	 * Constructor to initialize the {@link WebDriverUtil} object
	 * @param driver The {@link WebDriver} object
	 */
	
	
	/**
	 * Function to pause the execution for the specified time period
	 * @param milliSeconds The wait time in milliseconds
	 * @param elementName The name of the element Name for which the execution is paused 
	 * @param pageName Page Name in which Element is available 
	 */
	public void waitFor(long milliSeconds, String elementName, String elementType,String pageName) {
		try {
			Thread.sleep(milliSeconds);
			} catch (InterruptedException e) {
				ALMFunctions.UpdateReportLogAndALMForFailStatus("Wait for Element", "Script should be able to set to pause for the mentioned duration",
						"Error - Unable to wait for elementName "+elementType+ " in page: "+"\""+pageName+"\"", true);
			}	
	}
	
	/**
	 * Function to wait until the page loads completely
	 * @param timeOutInSeconds The wait timeout in seconds
	 * @param pageName Page to be loaded
	 * @return Boolean value indicating whether page is loaded or not
	 */
	public boolean waitUntilPageLoaded(String pageName) {
		
		try{
		
		WebElement oldPage = driver.findElement(By.tagName("html"));
		
		(new WebDriverWait(driver.getWebDriver(), pageloadTimeout))
									.until(ExpectedConditions.stalenessOf(oldPage));
		return true;
		
		}
		catch(TimeoutException e)
		{
			ALMFunctions.ThrowException("Page Load", pageName+" page should be loaded in "+pageloadTimeout+" Seconds",
					"Error - Page: "+"\""+pageName+"\""+ " is not loaded even after waiting for "+pageloadTimeout+" Seconds", true);
			return false;
		}
		catch(Exception e)
		{
			ALMFunctions.ThrowException("Page Load", pageName+" page should be loaded in "+pageloadTimeout+" Seconds",
					"Error - in waiting for Page: "+"\""+pageName+"\""+ " to load", true);
			return false;
		}
		
	}
	
	/**
	 * Function to wait until the specified element is visible
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds The wait timeout in seconds
	 * @param elementName The name of the element to be visible
	 * @param elementType Type of the element
	 * @param pageName Page in which Element to be visible
	 * @return Boolean value indicating whether element is visible or not
	 */
	public boolean waitUntilElementVisible(WebElement element, long timeOutInSeconds,String elementName, String elementType, String pageName,boolean logIndicator) {
		
		try{
			(new WebDriverWait(driver.getWebDriver(), timeOutInSeconds))
							.until(ExpectedConditions.visibilityOf(element));
			return true;
		}
		catch(TimeoutException e)
		{
			if(logIndicator){
				ALMFunctions.ThrowException("Element Visibility Property Check", elementName+" should be visible in the page: "+"\""+pageName+"\"",
						"Error - "+elementName+" "+elementType+" is not visible in the page: "+"\""+pageName+"\""+" even after waiting for "+timeOutInSeconds+" Seconds", true);
			}
			return false;
		}
		catch(Exception e)
		{
			if(logIndicator){
				ALMFunctions.ThrowException("Element Visibility Property Check", elementName+" should be visible in the page: "+"\""+pageName+"\"",
						"Error - Unable to wait for the "+elementName+" "+elementType+" to become visible in the page: "+"\""+pageName+"\"", true);
			}
			return false;
		}
		
	}
	
	/**
     * Function to wait until the number of browser window available
     * @param timeOutInSeconds The wait timeout in seconds
     * @param NewWindowPageName Page name of new window
     * @return Boolean value indicating whether expected window count exists
     */
     public boolean waitUntilWindowCountAvailable(int windowCount, String NewWindowPageName,long milliSeconds) {
            
            try{
            
            new WebDriverWait(driver.getWebDriver(), milliSeconds)
                                                            .until(ExpectedConditions.numberOfWindowsToBe(windowCount));
            return true;
            
            }
            catch(TimeoutException e)
            {
            	ALMFunctions.ThrowException(NewWindowPageName, "New window "+"\""+NewWindowPageName+"\""+"should be available",
            			"\""+NewWindowPageName+"\""+" window is not available", true);
                return false;
            }
            catch(Exception e)
            {
            	ALMFunctions.ThrowException(NewWindowPageName, "New window "+"\""+NewWindowPageName+"\""+"should be available",
            			"Error - in waiting for the desired window to be available", true);
            	return false;
            }
            
     }

	/**
	 * Function to wait until the page readyState equals 'complete'
	 * @param timeOutInSeconds The wait timeout in seconds
	 * @param pageName Page Name for which wait to be applied until the page readyState equals 'complete'
	 * @return Boolean value indicating whether page reached 'complete' state or not
	 */
     public boolean waitUntilPageReadyStateComplete(long timeOutInSeconds, String pageName) 
     {			
		try{
			new WebDriverWait(driver.getWebDriver(), timeOutInSeconds).until(ExpectedConditions.jsReturnsValue("return document.readyState==\"complete\";"));
			return true;
		}
		catch(TimeoutException e)
		{
			ALMFunctions.ThrowException("Page State", pageName+" page Ready State should be reached as Complete",
					"Error - State of the page: "+"\""+pageName+"\""+ " is not reached as \"Complete\" even after waiting for "+timeOutInSeconds+" Seconds", true);
			return false;
		}
		catch(Exception e)
		{
			ALMFunctions.ThrowException("Page State", pageName+" page Ready State should be reached as Complete",
					"Error - Unable to get the state of Page: "+"\""+pageName+"\"", true);
			return false;
		}
	}
	
	/**
	 * Function to wait until the specified element is located
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds The wait timeout in seconds
	 * @param elementName The name of the element to be located
	 * @param elementType Type of the element
	 * @param pageName Page in which Element to be located
	 * @return Boolean value indicating whether element is located or not
	 */
	public boolean waitUntilElementLocated(By by, long timeOutInSeconds, String elementName, String elementType, String pageName,Boolean LogIndicator) {
		
		try{
		(new WebDriverWait(driver.getWebDriver(), timeOutInSeconds))
							.until(ExpectedConditions.presenceOfElementLocated(by));
		return true;
		}
		catch(TimeoutException e)
		{
			if(LogIndicator)
			{
				ALMFunctions.ThrowException(elementName, elementName+" should be found in the page: "+"\""+pageName+"\"",
						"Error - "+elementName+" "+elementType+" is not found in the page: "+"\""+pageName+"\""+" even after waiting for "+timeOutInSeconds+" Seconds", true);
			}
			return false;
		}
		catch(Exception e)
		{
			if(LogIndicator)
			{
				ALMFunctions.ThrowException(elementName, elementName+" should be found in the page: "+"\""+pageName+"\"",
						"Error - Unable to wait for the "+elementName+" "+elementType+" in the page: "+"\""+pageName+"\"", true);
			}
			return false;
		}
		
	}

	
	/**
	 * Function to wait until the specified element is visible
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds The wait timeout in seconds
	 * @param elementName The name of the element to be visible
	 * @param elementType Type of the element
	 * @param pageName Page in which Element to be visible
	 * @return Boolean value indicating whether element is visible or not
	 */
	public boolean waitUntilElementVisible(By by, long timeOutInSeconds,String elementName, String elementType, String pageName,boolean logIndicator) {
		
		try{
			(new WebDriverWait(driver.getWebDriver(), timeOutInSeconds))
							.until(ExpectedConditions.visibilityOfElementLocated(by));
			return true;
		}
		catch(TimeoutException e)
		{
			if(logIndicator){
				ALMFunctions.ThrowException("Element Visibility Property Check", elementName+" should be visible in the page: "+"\""+pageName+"\"",
						"Error - "+elementName+" "+elementType+" is not visible in the page: "+"\""+pageName+"\""+" even after waiting for "+timeOutInSeconds+" Seconds", true);
			}
			return false;
		}
		catch(Exception e)
		{
			if(logIndicator){
				ALMFunctions.ThrowException("Element Visibility Property Check", elementName+" should be visible in the page: "+"\""+pageName+"\"",
						"Error - Unable to wait for the "+elementName+" "+elementType+" to become visible in the page: "+"\""+pageName+"\"", true);
			}
			return false;
		}
		
	}
	/**
	 * Function to wait until the specified element is enabled
	 * @param WebElement The {@link WebDriver} element used to click
	 * @param timeOutInSeconds The wait timeout in seconds
	 * @param elementName The name of the element to be enabled
	 * @param elementType Type of the element
	 * @param pageName Page in which Element to be enabled
	 * @return Boolean value indicating whether element is enabled or not
	 */
	public boolean waitUntilElementVisible(WebElement element, long timeOutInSeconds,String elementName, String elementType, String pageName) {
		
		try{
			
			(new WebDriverWait(driver.getWebDriver(), timeOutInSeconds))
			.until(ExpectedConditions.visibilityOf(element));
			return true;
		}
		catch(TimeoutException e)
		{
			ALMFunctions.ThrowException("Element Enable Property Check", elementName+" should be enabled in the page: "+"\""+pageName+"\"",
					"Error - "+elementName+" "+elementType+" is not enabled in the page: "+"\""+pageName+"\""+" even after waiting for "+timeOutInSeconds+" Seconds", true);
			return false;
		}
		catch(Exception e)
		{
			ALMFunctions.ThrowException("Element Enable Property Check", elementName+" should be enabled in the page: "+"\""+pageName+"\"",
					"Error - Unable to wait for the "+elementName+" "+elementType+" to be enabled in the page: "+"\""+pageName+"\"", true);
			return false;
		}
			
	}
	/**
	 * Function to wait until the expected title matches with the specified page
	 * title
	 * 
	 * @param lngTimeOutInSeconds
	 *            The wait timeout in seconds
	 * @param Title
	 *            Expected title to be compared with the page title
	 * @param LogIndicator
	 *            boolean to indicate whether log has to be written or not
	 * @param blnThrowException
	 *            boolean to indicate whether exception has to be thrown incase
	 *            of failure
	 * @return Boolean value indicating whether text is present in page title or
	 *         not
	 */
	public Boolean waituntilContainsPresentInPageTitle(long lngTimeOutInSeconds, String Title, boolean LogIndicator,
			boolean blnThrowException) {
		try {
			new WebDriverWait(driver.getWebDriver(), lngTimeOutInSeconds)
					.until(ExpectedConditions.titleContains(Title));
			if (LogIndicator) {
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Page Title Comparison",
						"Page Title should contains " + Title,
						"Page Title is containing Expected text. <br>Expected Page Title Text: " + Title
								+ ", <br>Actual Page Title Text: " + driver.getTitle(),
						true);
			}
			return true;
		} catch (TimeoutException e) {
			if (LogIndicator) {
				if (blnThrowException) {
					ALMFunctions.ThrowException("Page Title Comparison", "Page Title should contains " + Title,
							"Error - Page Title does not contains expected text even after waiting for "
									+ lngTimeOutInSeconds + " Seconds." + " " + "Expected: Title Contains - " + Title
									+ ", Actual Title: " + driver.getTitle(),
							true);
				} else {
					ALMFunctions.UpdateReportLogAndALMForFailStatus("Page Title Comparison",
							"Page Title should contains " + Title,
							"Error - Page Title does not contains expected text even after waiting for "
									+ lngTimeOutInSeconds + " Seconds." + " " + "Expected: Title Contains - " + Title
									+ ", Actual Title: " + driver.getTitle(),
							true);
				}
			}
			return false;
		} catch (Exception e) {
			if (LogIndicator) {
				if (blnThrowException) {
					ALMFunctions.ThrowException("Page Title Comparison", "Page Title should contains " + Title,
							"Below Exception is while trying to wait for page title to contain expected text " + Title
									+ "<br><br>" + e.getLocalizedMessage(),
							true);
				} else {
					ALMFunctions.UpdateReportLogAndALMForFailStatus("Page Title Comparison",
							"Page Title should contains " + Title,
							"Below Exception is while trying to wait for page title to contain expected text " + Title
									+ "<br><br>" + e.getLocalizedMessage(),
							true);
				}
			}
			return false;
		}
	}
	
	/**
	 * Function to wait until the specified element is enabled
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds The wait timeout in seconds
	 * @param elementName The name of the element to be enabled
	 * @param elementType Type of the element
	 * @param pageName Page in which Element to be enabled
	 * @return Boolean value indicating whether element is enabled or not
	 */
	public boolean waitUntilElementEnabled(By by, long timeOutInSeconds,String elementName, String elementType, String pageName) {
		
		try{
			(new WebDriverWait(driver.getWebDriver(), timeOutInSeconds))
							.until(ExpectedConditions.elementToBeClickable(by));
			return true;
		}
		catch(TimeoutException e)
		{
			ALMFunctions.ThrowException("Element Enable Property Check", elementName+" should be enabled in the page: "+"\""+pageName+"\"",
					"Error - "+elementName+" "+elementType+" is not enabled in the page: "+"\""+pageName+"\""+" even after waiting for "+timeOutInSeconds+" Seconds", true);
			return false;
		}
		catch(Exception e)
		{
			ALMFunctions.ThrowException("Element Enable Property Check", elementName+" should be enabled in the page: "+"\""+pageName+"\"",
					"Error - Unable to wait for the "+elementName+" "+elementType+" to be enabled in the page: "+"\""+pageName+"\"", true);
			return false;
		}
			
	}
	
	/**
	 * Function to wait until the specified element is enabled
	 * @param WebElement The {@link WebDriver} element used to click
	 * @param timeOutInSeconds The wait timeout in seconds
	 * @param elementName The name of the element to be enabled
	 * @param elementType Type of the element
	 * @param pageName Page in which Element to be enabled
	 * @return Boolean value indicating whether element is enabled or not
	 */
	public boolean waitUntilElementEnabled(WebElement element, long timeOutInSeconds,String elementName, String elementType, String pageName) {
		
		try{
			(new WebDriverWait(driver.getWebDriver(), timeOutInSeconds))
							.until(ExpectedConditions.elementToBeClickable(element));
			return true;
		}
		catch(TimeoutException e)
		{
			ALMFunctions.ThrowException("Element Enable Property Check", elementName+" should be enabled in the page: "+"\""+pageName+"\"",
					"Error - "+elementName+" "+elementType+" is not enabled in the page: "+"\""+pageName+"\""+" even after waiting for "+timeOutInSeconds+" Seconds", true);
			return false;
		}
		catch(Exception e)
		{
			ALMFunctions.ThrowException("Element Enable Property Check", elementName+" should be enabled in the page: "+"\""+pageName+"\"",
					"Error - Unable to wait for the "+elementName+" "+elementType+" to be enabled in the page: "+"\""+pageName+"\"", true);
			return false;
		}
			
	}
	
	
	/**
	 * Function to wait until the specified element is disabled
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds The wait timeout in seconds
	 * @param elementName The name of the element to be disabled
	 * @param elementType Type of the element
	 * @param pageName Page in which Element to be disabled
	 * @return Boolean value indicating whether element is disabled or not
	 */
	public boolean waitUntilElementDisabled(By by, long timeOutInSeconds, String elementName, String elementType, String pageName) {
		
		try{
		
			(new WebDriverWait(driver.getWebDriver(), timeOutInSeconds))
				.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(by)));
			return true;
		}
		catch(TimeoutException e)
		{
			ALMFunctions.ThrowException("Element Disable Property Check",  elementName+" should be disabled in the page: "+"\""+pageName+"\"",
					"Error - "+elementName+" "+elementType+" is not disabled in the page: "+"\""+pageName+"\""+" even after waiting for "+timeOutInSeconds+" Seconds", true);
			return false;
		}
		catch(Exception e)
		{
			ALMFunctions.ThrowException("Element Disable Property Check",  elementName+" should be disabled in the page: "+"\""+pageName+"\"",
					"Error - Unable to wait for the "+elementName+" "+elementType+" to be disabled in the page: "+"\""+pageName+"\"", true);
			return false;
		}
	
		}
	
	
	/**
	 * Function to wait until the specified element is invisible
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds The wait timeout in seconds
	 * @param elementName The name of the element to be invisible
	 * @param elementType Type of the element
	 * @param pageName Page in which Element to be invisible
	 * @return Boolean value indicating whether element is invisible or not
	 */
	public boolean waitUntilElementInVisible(By by,String elementName, String elementType, String pageName) {
		
		try{
			new WebDriverWait(driver.getWebDriver(), invisibilityTimeout)
							.until(ExpectedConditions.invisibilityOfElementLocated(by));
			return true;
		}
		catch(TimeoutException e)
		{
			ALMFunctions.ThrowException("Element Invisibility",  elementName+" should be invisible in the page: "+"\""+pageName+"\"",
					"Error - "+elementName+" "+elementType+" is visible in the page: "+"\""+pageName+"\""+" even after waiting for "+invisibilityTimeout+" Seconds", true);
			return false;
		}
		catch(Exception e)
		{
			ALMFunctions.ThrowException("Element Invisibility",  elementName+" should be invisible in the page: "+"\""+pageName+"\"",
					"Error - Unable to wait for the "+elementName+" "+elementType+" to become invisible in the page: "+"\""+pageName+"\"", true);
			return false;
		}
		
	}
	
	/**
	 * Function to wait until the expected text matches with text in specified element
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds The wait timeout in seconds
	 * @param elementName The name of the element in which text to be compared with expected text
	 * @param elementType Type of the element
	 * @param Text Expected Text to be compared with element text
	 * @param pageName Page in which Element is located
	 * @return Boolean value indicating whether text is present in element or not
	 */
	public Boolean waituntilTextPresentInElement(By by, long timeOutInSeconds,String elementName, String elementType, String Text, String pageName)
	{
		try{
			(new WebDriverWait(driver.getWebDriver(), timeOutInSeconds))
								.until(ExpectedConditions.textToBePresentInElementLocated(by, Text));
			return true;
		}
		catch(TimeoutException e)
		{
			ALMFunctions.ThrowException("Element Text Comparison",  "Text present in "+elementName+" should be matched with the expected text "+Text+" in the page: "+"\""+pageName+"\"",
					"Error - Text in "+elementName+" "+elementType+" is not matching with the expected text on the page: "+"\""+pageName+"\""+" even after waiting for "+timeOutInSeconds+" Seconds. Expected Text: "+Text+", Actual Text: "+driver.findElement(by).getText(), true);
			return false;
		}
		catch(Exception e)
		{
			ALMFunctions.ThrowException("Element Text Comparison",  "Text present in "+elementName+" should be matched with the expected text "+Text+" in the page: "+"\""+pageName+"\"",
					"Error - Unable to wait for the text to be available in "+elementName+" "+elementType+" on the page: "+"\""+pageName+"\"", true);
			return false;
		}
	}
	
	
	/**
	 * Function to wait until the expected title matches with the specified page title
	 * @param timeOutInSeconds The wait timeout in seconds
	 * @param Title Expected title to be compared with the page title
	 * @param pageName Page in which title is to be compared with expected title
	 * @return Boolean value indicating whether text is present in page title or not
	 */
	public Boolean waituntilTextPresentInPageTitle(long timeOutInSeconds,String Title, String pageName,boolean LogIndicator)
	{
		try{
			(new WebDriverWait(driver.getWebDriver(), timeOutInSeconds))
								.until(ExpectedConditions.titleIs(Title));
			if(LogIndicator)
			{
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Page Title Comparison", pageName+" Page Title should be displayed as "+Title,
						"Page Title is displaying as expected. <br>Expected Page Title: "+Title
						+"<br> Actual Page Title: "+driver.getTitle(), true);
			}
			return true;
			}
		catch(TimeoutException e)
		{
			ALMFunctions.ThrowException("Page Title Comparison", pageName+" Page Title should be displayed as "+Title,
					"Error - Page Title is not matching with the expected title on the page: "+"\""+pageName+"\""+" even after waiting for "+
							timeOutInSeconds+" Seconds."+"<br> Expected Title: "+Title+"<br>Actual Title: "+driver.getTitle(), true);
			return false;
		}
		catch(Throwable e)
		{
			ALMFunctions.ThrowException("Page Title Comparison", pageName+" Page Title should be displayed as "+Title,
					"Error - Unable to wait for the expected title to be available in the page: "
							+"\""+pageName+"\"", true);
			return false;
		}
	}
	/**
	 * Function to wait until the expected title matches with the specified page title
	 * @param timeOutInSeconds The wait timeout in seconds
	 * @param Title Expected title to be compared with the page title
	 * @param pageName Page in which title is to be compared with expected title
	 * @return Boolean value indicating whether text is present in page title or not
	 */
	public Boolean waituntilContainsPresentInPageTitle(long timeOutInSeconds,String Title, String pageName,boolean LogIndicator)
	{
		try{
			(new WebDriverWait(driver.getWebDriver(), timeOutInSeconds))
								.until(ExpectedConditions.titleContains(Title));
			if(LogIndicator)
			{
				ALMFunctions.UpdateReportLogAndALMForPassStatus("Page Title Comparison", pageName+" Page Title should contains "+Title,
						"Page Title is containing Expected text. <br>Expected Page Title Text: "+Title
						+", <br>Actual Page Title Text: "+driver.getTitle(), true);
			}
			return true;
			}
		catch(TimeoutException e)
		{
			ALMFunctions.ThrowException("Page Title Comparison", pageName+" Page Title should contains "+Title,
					"Error - Page Title is not matching with the expected title on the page: "+"\""+pageName+"\""+" even after waiting for "+timeOutInSeconds+" Seconds."+" "
							+ "Expected Title: "+Title+", Actual Title: "+driver.getTitle(), true);
			return false;
		}
		catch(Throwable e)
		{
			ALMFunctions.ThrowException("Page Title Comparison", pageName+" Page Title should contains "+Title,
					"Error - Unable to wait for the expected title to be available in the page: "+"\""+pageName+"\"", true);
			return false;
		}
	}
	/**
	 * Function to wait until the frame available and switch to it
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds The wait timeout in seconds
	 * @param pageName Page in which frame is available
	 * @return Boolean value indicating whether frame is available and switched or not
	 */
	public Boolean waitUntilFrameAvailableAndSwitch(By locator,long timeOutInSeconds,String pageName)
	{
		try{
			(new WebDriverWait(driver.getWebDriver(), timeOutInSeconds))
								.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(locator));
			return true;
			}
		catch(TimeoutException e)
		{
			ALMFunctions.ThrowException("Frame Switch", "User should be swithced to the expected frame",
					"Error - Frame is not available on the page: "+"\""+pageName+"\""+" even after waiting for "+timeOutInSeconds+" Seconds.", true);
			return false;
		}
		catch(Exception e)
		{
			ALMFunctions.ThrowException("Frame Switch", "User should be swithced to the expected frame",
					"Error - Unable to wait for the frame to be available in the page: "+"\""+"\""+pageName+"\""+"\"", true);
			return false;
		}
	}
	
	/**
	 * Function to get out of the iFrame
	 */
	public void switchBackToDefaultContent() {
		driver.switchTo().defaultContent();
	}
	
	/**
	 * Function to wait until the page loads completely
	 * @param timeOutInSeconds The wait timeout in seconds
	 * @param pageName Page to be loaded
	 * @return Boolean value indicating whether page is loaded or not
	 */
	public void waitUntilStalenessOfElement(By locator,String pageName) {
		
		try{
		
		WebElement oldElement = driver.findElement(locator);
		long StaleTimeout = Long.parseLong(properties.getProperty("StaleTimeOut"));
		
		new WebDriverWait(driver.getWebDriver(), StaleTimeout).until(ExpectedConditions.stalenessOf(oldElement));
		
		}
		catch(Exception e)
		{
			
		}
		
	}
	
	public void waitUntilStalenessOfElement(WebElement element,String pageName){
		try{
			long StaleTimeout = Long.parseLong(properties.getProperty("StaleTimeOut"));
			new WebDriverWait(driver.getWebDriver(), StaleTimeout).until(ExpectedConditions.stalenessOf(element));
		}
		catch(Exception e){
			
		}
	}
	
	public void waitUntilStalenessOfTableElement(WebElement element,String pageName){
		try{
			long StaleTimeout = Long.parseLong(properties.getProperty("StaleTableTimeOut"));
			new WebDriverWait(driver.getWebDriver(), StaleTimeout).until(ExpectedConditions.stalenessOf(element));
		}
		catch(Exception e){
			
		}
	}
	/**
	 * Function to wait until the page loads completely
	 * @param timeOutInSeconds The wait timeout in seconds
	 * @param pageName Page to be loaded
	 * @return Boolean value indicating whether page is loaded or not
	 */
	public void waitUntilStalenessOfElement(By locator,long StaleTimeout,String pageName) {
		
		try{
		
		WebElement oldElement = driver.findElement(locator);
		new WebDriverWait(driver.getWebDriver(), StaleTimeout).until(ExpectedConditions.stalenessOf(oldElement));
		
		}
		catch(Exception e)
		{
			
		}
		
	}
	/**
	 * Function to Wait until attribute of element contains expected text
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds The wait timeout in seconds
	 * @param elementName The name of the element in which attribute is to be compared
	 * @param elementType Type of the element
	 * @param blnLog - boolean to indicate whether log to be captured if attribute not matches
	 * @param strAttribute - Name of the attribute
	 * @param strValue - Expected value of the attribute
	 * @return boolean - Attribute matches or not
	 */
	public boolean WaitUntilElementAtributeContainsText(By by,long timeOutInSeconds,String elementName, String elementType,boolean blnLog,
			String strAttribute,String strValue){
		
		try{
			new WebDriverWait(driver.getWebDriver(),timeOutInSeconds).until(ExpectedConditions.attributeContains(by, strAttribute, strValue));
			return true;
		}
		catch(Exception e) {
			if(blnLog){
				ALMFunctions.ThrowException("Attribute Text Comparison", "Atribute "+strAttribute+" should contain expected text "+strValue,
						"Error - "+elementName+" "+elementType+" does not contains "+strValue+" in attribute "+ strAttribute
						+"<br> Expected value in Atribute - "+strAttribute+": "+strValue+"<br>Actual value in Atribute - "+strAttribute+": "+
						driver.findElement(by).getAttribute(strAttribute).trim(), true);
			}
			return false;
		}
	}
	
	public boolean WaitUntilElementAtributeNotContainsText(WebElement element,long timeOutInSeconds,String elementName, String elementType,boolean blnLog,
			String strAttribute,String strValue){
		
		try{
			new WebDriverWait(driver.getWebDriver(),timeOutInSeconds).until(ExpectedConditions.not(ExpectedConditions.attributeContains(element, strAttribute, strValue)));
			return true;
		}
		catch(Exception e) {
			if(blnLog){
				ALMFunctions.ThrowException("Attribute Text Comparison", "Atribute "+strAttribute+" should contain expected text "+strValue,
						"Error - "+elementName+" "+elementType+" does not contains "+strValue+" in attribute "+ strAttribute
						+"<br> Expected value in Atribute - "+strAttribute+": "+strValue+"<br>Actual value in Atribute - "+strAttribute+": "+
						element.getAttribute(strAttribute).trim(), true);
			}
			return false;
		}
	}
	
	/**
	 * Function to Wait until attribute of element to be an expected text
	 * @param by The {@link WebDriver} locator used to identify the element
	 * @param timeOutInSeconds The wait timeout in seconds
	 * @param elementName The name of the element in which attribute is to be compared
	 * @param elementType Type of the element
	 * @param blnLog - boolean to indicate whether log to be captured if attribute not matches
	 * @param strAttribute - Name of the attribute
	 * @param strValue - Expected value of the attribute
	 * @return boolean - Attribute matches or not
	 */
	public boolean WaitUntilElementAtributeText(By by,long timeOutInSeconds,String elementName, String elementType,boolean blnLog,
			String strAttribute,String strValue){
		
		try{
			new WebDriverWait(driver.getWebDriver(),timeOutInSeconds).until(ExpectedConditions.attributeToBe(by, strAttribute, strValue));
			return true;
		}
		catch(Exception e) {
			if(blnLog){
				ALMFunctions.ThrowException("Attribute Text Comparison", "Atribute "+strAttribute+" of "+elementName+" should be an expected text "+strValue,
						"Error - "+elementName+" "+elementType+" does not equals "+strValue+" in attribute "+ strAttribute
						+"<br> Expected value in Atribute - "+strAttribute+": "+strValue+"<br>Actual value in Atribute - "+strAttribute+": "+
						driver.findElement(by).getAttribute(strAttribute).trim(), true);
			}
			return false;
		}
	}
/**
 	 * Function to wait until the Jquery Ajax calls to get completed
 	 * @param lngPageLoadTimeOutInSeconds The wait timeout in seconds
 	 * @param strPageName Page Name for which wait to be applied until Jquery Ajax calls to get completed
 	 * @return Boolean value indicating whether Ajax calls are completed or not
 	 */
      public boolean waitUntilAjaxLoadingComplete(long lngPageLoadTimeOutInSeconds, String strPageName) {			
 		try{
 			Boolean blnJQueryDefined = false;
 			try{
 				new WebDriverWait(driver.getWebDriver(), 2).until(ExpectedConditions.
 						jsReturnsValue("return typeof jQuery != 'undefined' && jQuery !== 'null'"));
 				
 				blnJQueryDefined = true;
 			} catch(Exception e) {
 				//No impact on Execution.
 			}
 			if(blnJQueryDefined) {
 				
				new WebDriverWait(driver.getWebDriver(), lngPageLoadTimeOutInSeconds).until(ExpectedConditions.jsReturnsValue("return jQuery.active ==\"0\";"));
			}
 			return true;
 		} catch(TimeoutException e) {
 			ALMFunctions.ThrowException("Ajax Calls", strPageName+" page Ajax Calls should be completed",
 					"Error - page: "+"\""+strPageName+"\""+ " Ajax Calls are not completed even after waiting for "+lngPageLoadTimeOutInSeconds+" Seconds", true);
 			return false;
 		} catch(Exception e) {
 			ALMFunctions.ThrowException("Ajax Calls", strPageName+" page Ajax Calls should be completed",
 					"Below exception is thrown while trying to get the status of Ajax Calls in Page: "+"\""+strPageName+"\""+"<br><br>"+e.getLocalizedMessage(), true);
 			return false;
 		}
 	} 
      /**
       * Function to wait until the number of browser window available
       * @param timeOutInSeconds The wait timeout in seconds
       * @param NewWindowPageName Page name of new window
       * @return Boolean value indicating whether expected window count exists
       */
       public boolean waitUntilWindowCountAvailable(int windowCount, String NewWindowPageName,long milliSeconds, boolean blnFlagReport) {
              
              try{            
              new WebDriverWait(driver.getWebDriver(), milliSeconds)
                                                              .until(ExpectedConditions.numberOfWindowsToBe(windowCount));
              
              return true;
              
              }
              catch(TimeoutException e)
              {                 
                    if(blnFlagReport){
                    ALMFunctions.UpdateReportLogAndALMForFailStatus(NewWindowPageName, "New window "+"\""+NewWindowPageName+"\""+"should be available",
                                "\""+NewWindowPageName+"\""+" window is not available", true);
                 }
                    return false;
              }
              catch(Exception e)
              {                 
                    if(blnFlagReport){
                    ALMFunctions.UpdateReportLogAndALMForFailStatus(NewWindowPageName, "New window "+"\""+NewWindowPageName+"\""+"should be available",
                                "Error - in waiting for the desired window to be available", true);
                    }
                    return false;
              }
              
       }
       /**
      	 * Function to switch window with preferred URL
      	 * 
      	 * @param partialUrl
      	 * @param expectedCount
      	 * @param windowTitle
      	 * @param milliSeconds
      	 * @return
      	 */
      	public boolean waitAndSwitchToWindowContainsUrl(String partialUrl, int expectedCount, String windowTitle,
      			long milliSeconds) {

      		try {

      			new WebDriverWait(driver.getWebDriver(), milliSeconds)
      					.until(ExpectedConditions.numberOfWindowsToBe(expectedCount));

      			int currentWindowNumbers = driver.getWindowHandles().size();

      			if (currentWindowNumbers < expectedCount) {
      				return false;
      			} else {
      				currentWindowNumbers = driver.getWindowHandles().size();

      				// System.out.println("After while " + currentWindowNumbers);
      				Set<String> currentWindows = driver.getWindowHandles();
      				for (String window : currentWindows) {
      					driver.switchTo().window(window);
      					System.out.println("page url is " + driver.getCurrentUrl());
      					if (driver.getCurrentUrl().contains(partialUrl)) {
      						driver.manage().window().maximize();
      						new WebDriverWait(driver.getWebDriver(), milliSeconds)
      								.until(ExpectedConditions.urlContains(windowTitle));

      						return true;
      					}
      				}
      				return false;
      			}

      		} catch (TimeoutException e) {
      			ALMFunctions.ThrowException(windowTitle, "New window " + "\"" + windowTitle + "\"" + "should be available",
      					"\"" + windowTitle + "\"" + " window is not available", true);
      			return false;
      		} catch (Exception e) {
      			ALMFunctions.ThrowException(windowTitle, "New window " + "\"" + windowTitle + "\"" + "should be available",
      					"Error - in waiting for the desired window to be available", true);
      			return false;
      		}

      	}
      	
      	
}
package com.tr.util;

import java.awt.Toolkit;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import com.tr.common.TimesJobUtil;

public class BasePageObject {

	//protected static WebDriver uiDriver;
	
	public WebDriver driver;
	public WebElement element;
	
	public BasePageObject(WebDriver driver){
	this.driver = driver;	

	}
	
	
	public void waitForAnElement(final By theElement,int timeoutInSeconds) {
		try {
			WebElement element = driver.findElement(theElement);
			WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
			wait.until(ExpectedConditions.visibilityOf(element));
		} catch (Exception e) {
			Reporter.log("There was an issue in finding the webelement, " + theElement.getClass() + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void javaScriptClick(By theElement) {
		
		WebElement element = driver.findElement(theElement);
		((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
	}
	
	public boolean isElementPresent(By by) throws InterruptedException {
	    boolean flag = false;
        try {
        	TimesJobUtil.explicitWait(1000);
        	flag = driver.findElement(by).isDisplayed();
            return flag;
        } catch (NoSuchElementException e) {
            return flag;
        }
    }
	
	public WebElement setElement(By by) throws Exception {
        try {
        	element = driver.findElement(by);
        } catch (NoSuchElementException e) {
        	
        	throw new Exception("Element is located while:"+element+e.getLocalizedMessage());
        }
        
        return element;
    }
	
	
	/**
	 * 
	 * @param locator
	 */
	public  String getText(By theElement) {
		
		WebElement element= driver.findElement(theElement);
		return element.getText();
	}

    public  String getAttributeValue(By theElement,String attributeValue)
    {
		WebElement element= driver.findElement(theElement);
		return element.getAttribute(attributeValue);
	}
    
    
	public  String getCurrentLocation() {
		return driver.getCurrentUrl().trim();
	}
	
	public  void switchToDefaultFrame() {
		driver.switchTo().defaultContent();
	}

	public  void switchToFrame(String id) {
		driver.switchTo().frame(id);
	}

	public  void switchToFrame(By theElement) {
		WebElement element = driver.findElement(theElement);
		driver.switchTo().frame(element);
	}

	public  void refreshPage() {
		driver.navigate().refresh();
	}
	
	public boolean isTextPresent(String textValue) {
		// Reporter.log("Looking for the element...: " + textValue);
		System.out.println("Looking for the element...: " + textValue);
		WebElement webElement = driver.findElement(By.tagName("html"));
		if (webElement.getText().contains(textValue)) {
			return true;
		} else {
			System.out.println("Element not found : " + textValue);
			return false;
		}
	}
	
	public void selectWindow(int window) {
		Set<String> winNames = driver.getWindowHandles();
		driver.switchTo().window((String) winNames.toArray()[window - 1]);
	}
	
	public  void mouseover(By theElement) throws Exception{
		Actions act=new Actions(driver);
		WebElement webelement=driver.findElement(theElement);
		act.moveToElement(webelement).build().perform();
		//new Actions(driver).moveToElement((WebElement) theElement).build().perform();
	}
	/*public  WebElement mouseover(WebElement theElement) {
		new Actions(driver).moveToElement(theElement).build().perform();
		return theElement;
	}*/
	public void selectDropDown(By theSelectElement, String valToSelect) throws InterruptedException {
		WebElement element = driver.findElement(theSelectElement);
		Select select = new Select(element);
		// Get a list of the options
		List<WebElement> options = select.getOptions();
		// For each option in the list, verify if it's the one you want and then
		// click it
		for (WebElement we : options) {
			if (we.getText().equals(valToSelect)) {
				we.click();
				Thread.sleep(2000);
				break;
			}
		}

	}

	public String selectedItem(By theSelectElement) {
		WebElement element = driver.findElement(theSelectElement);
		Select select = new Select(element);
		WebElement anElement = select.getFirstSelectedOption();
		return anElement.getText();
	}

	/**
	 * This api can be used to verify if a UIElement like checkbox, radio button
	 * is checked or not
	 * 
	 * @param theElement
	 * @return
	 */
	public boolean isElementChecked(By theElement) {
		WebElement element = driver.findElement(theElement);
		boolean retValue = false;
		if (element.isSelected()) {
			retValue = true;
		}
		return retValue;
	}

	public  void maxWindow() {

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenResolution = new Dimension((int) toolkit.getScreenSize().getWidth(), (int) toolkit.getScreenSize().getHeight());
		driver.manage().window().setSize(screenResolution);

	}

	public  void selectWindowCancel(int window) throws Exception {
		//Logger.info("Inside the select window cancel");
		String parentWindowHandle = driver.getWindowHandle();
		Set<String> winNames = driver.getWindowHandles();
		driver.switchTo().alert().dismiss();
		Thread.sleep(3000);
		driver.switchTo().window(parentWindowHandle); 
	}

	public void waitImplicit() {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	public void dragDrop(By element, By target) {
		WebElement elementSource = driver.findElement(element);
		WebElement elementDestination = driver.findElement(target);
		(new Actions(driver)).dragAndDrop(elementSource, elementDestination).perform();

	}
	
	public  void deleteCookies() {
		driver.manage().deleteAllCookies();
	}

	public  void closeBrowser() {

		driver.quit();
	}

	public  void alert() {
		Alert alert = driver.switchTo().alert();
		alert.accept();

	}
	
	 /**
     * This method Verifies click on close button
     * 
     * @return
     * @throws Exception
     */
    public void closePresentWindow() throws Exception {
        try {
            //driver.close();
        } catch (Exception e) {
            throw new Exception(
                    "ISSUE IN CLOSING THE 'window'" + "\nMETHOD:clickOnCloseWindow\n" + e
                            .getLocalizedMessage());
        }
    }

	public void closePopUp() throws InterruptedException{
		String parent = driver.getWindowHandle();
		Set<String>pops=driver.getWindowHandles();
        {
        Iterator<String>it = pops.iterator();
        while (it.hasNext()) {
            String popupHandle=it.next().toString();
            if(!popupHandle.contains(parent))
            {
            driver.switchTo().window(popupHandle);
            System.out.println("Popu Up Title: "+ driver.switchTo().window(popupHandle).getTitle());
            driver.close();
            Thread.sleep(5000);
            }
        }
	}
	}

	public String getSelectedValue(By element) {
		WebElement element1 = driver.findElement(element);
		Select select = new Select(element1);
		String selectedValue = select.getFirstSelectedOption().getText();
		return selectedValue;
	}

	public void selectByIndex(By element, int index) {
		WebElement element1 = driver.findElement(element);
		Select select = new Select(element1);
		List<WebElement> options = select.getOptions();
		int size = options.size();
		if (size > 0) {
			select.selectByIndex(index);
		}
	}

	public String getSelectedValueFromCombo(By element) {
		WebElement element1 = driver.findElement(element);
		Select select = new Select(element1);
		String selectedValue = select.getFirstSelectedOption().getText();
		return selectedValue;
	}

	public  void clearAndEnterValueInTextBox(By by, String value) {
		WebElement textBox = driver.findElement(by);
		Assert.assertTrue(textBox.isEnabled(), "Text Box is enabled");
		textBox.sendKeys(Keys.CONTROL + "a");
		textBox.sendKeys(Keys.DELETE);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
		}
		textBox.sendKeys(value);
		

	}
	
	public void handleAlert() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			boolean alertPresent = false;
			if (wait.until(ExpectedConditions.alertIsPresent()) == null) {
				alertPresent = false;
			} else {
				alertPresent = true;
			}
			if (alertPresent) {
				Alert alert = driver.switchTo().alert();
				alert.accept();
			}
		} catch (UnhandledAlertException e) {

		}
	}

	public  void waitImplicit(int millisecods) {
		driver.manage().timeouts().implicitlyWait(millisecods,
		TimeUnit.MILLISECONDS);
		try {
			Thread.sleep(millisecods);
		} catch (InterruptedException e) {
		}
	}
	
	
	public void switchToNewWindow() throws Exception
	{
		for (String winHandle : driver.getWindowHandles())
		{
			driver.switchTo().window(winHandle);
		}
	}

	public  String getWindowName() throws Exception 
	{
		String windowName = driver.getWindowHandle();
		return windowName;
	}

	public  boolean isChildWindowPresent() throws Exception {
		Set<String> windows = driver.getWindowHandles();
		int size = windows.size();
		return size > 1;
	}

	public  void switchToParentWindow(String windowName) throws Exception {
		windowName = windowName.toString();
		driver.switchTo().window(windowName);
		waitImplicit(3000);
	}
public  String getPageTitle()
{
		return driver.getTitle().trim();
}
	
	public  void compareTwoStrings(String Actual, String Expected, String Message) {
		Assert.assertEquals(Actual, Expected, Message);
	}
	
//	public String getExcelTest(int row,int column,String sheetName) throws Exception {
//				ExcelutilObject.setExcelFile(MonsterIndiaConstants.Path_TestData, sheetName);
//				return ExcelutilObject.getCellData(row, column);
//				
//			}


}

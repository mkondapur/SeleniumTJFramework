package com.tr.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.tr.common.TimesJobUtil;
import com.tr.util.BasePageObject;

public class SignInPage extends BasePageObject {

	public SignInPage(WebDriver driver) {
		super(driver);
	}
	
	public static Logger logger = Logger.getLogger(SignInPage.class);
	
	/*Variables*/
	boolean flag = false;
	String txt = null;
	
/*Web elements*/
	
	By popLoginHeader = By.id("GB_caption");
	By txtEmailId = By.id("j_username");
	By txtPassword = By.id("j_password");
	By btnLogin = By.id("//div[@id='loginForm']//input[@value='SIGN IN']");
	By btnCloseIcon = By.id("closeId");
	
	public boolean isSignInPopDisplayed() throws Exception
	{
		try 
		{
			flag = isElementPresent(popLoginHeader);
			
			if (flag) 
			{
				logger.info("Site logo is displayed in Home Page");	
			}			
			else
			{
				logger.info("Site logo is not displayed in Home Page");
			}
		} 
		catch (Exception e) 
		{
			throw new Exception("Site logo is not present in Home page::"+e.getLocalizedMessage());
		}
		return flag;
	}
	
	public String getPopUpHeader() throws Exception{
		try {
			flag = isElementPresent(popLoginHeader);
			Assert.assertTrue(flag, "Pop header is not displayed");
			txt = getText(popLoginHeader);
		} catch (Exception e) {
			throw new Exception("Failed while getting Sign In pop header text::"+e.getLocalizedMessage());
		}
		
		return txt;
	}
public HomePage closeIcon() throws Exception{
		
		try {
			
			flag = isElementPresent(btnCloseIcon);
			Assert.assertTrue(flag, "Login Close icon is not displayed");
			setElement(btnCloseIcon).click();
			TimesJobUtil.explicitWait(2000);
		} catch (Exception e) {
			throw new Exception("Failed to click on close icon");
		}
		
		return new HomePage(driver);
	}



}

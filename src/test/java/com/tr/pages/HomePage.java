package com.tr.pages;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import com.tr.common.TimesJobUtil;
import com.tr.util.BasePageObject;

public class HomePage extends BasePageObject



{
	WebElement element=null;
	
	//Constructor
		public HomePage(WebDriver driver) 
		{
		super(driver);
		}
		
		public static Logger logger = Logger.getLogger(HomePage.class);
	/*Variables*/
		
	boolean flag = false;
	List<WebElement> list=null;

	/*Web elements*/
	
	By imgSiteLogo = By.xpath("//img[@alt='TimesJobs']");
	By lnkSignIn = By.xpath("//*[@id='site']//a[contains(.,'Sign In')]");
	By lnkRegister = By.xpath("//*[@id='site']//a[contains(.,'Register')]");
	
	public boolean isSiteLogoDisplayed() throws Exception
	
	{
		try 
		{
			flag = isElementPresent(imgSiteLogo);
			
			
			if (flag) 
			{
			System.out.println("Site logo is displayed in Home Page");	
			}			
			else
			{
				System.out.println("Site logo is not displayed in Home Page");
			}
		} 
		catch (Exception e) 
		{
			throw new Exception("Site logo is not present in Home page::"+isSiteLogoDisplayed()+e.getLocalizedMessage());
		}
		return flag;
	}
	
	
	
	
	/**
	 * This method helps us click on Login
	 * @return boolean
	 * @param No param
	 * @throws Exception
	 */
	public SignInPage clickOnSignIn() throws Exception
	{
		try 
		{
			logger.info("Verifying SignIn link");
			
			flag = isElementPresent(lnkSignIn);
			Assert.assertTrue(flag, "SignIn link is not displayed");
			setElement(lnkSignIn).click();
			TimesJobUtil.explicitWait(3000);
			
		} 
		catch (Exception e) 
		{
			throw new Exception("Login link is not present in Home page::"+e.getLocalizedMessage());
		}
		
		
		return new SignInPage(driver);
	}
	
	
	
	
}

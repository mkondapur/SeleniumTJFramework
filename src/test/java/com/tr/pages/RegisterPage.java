package com.tr.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.tr.common.TimesJobUtil;
import com.tr.util.BasePageObject;

public class RegisterPage extends BasePageObject{

	public RegisterPage(WebDriver driver) {
		super(driver);
	}
	
	public static Logger logger = Logger.getLogger(SignInPage.class);
	
	/*Variables*/
	
	public boolean flag = false;
	public String txt = null;
	
	/*Webelements*/
	
	By txtEmail = By.id("emailAdd");
	By txtPassword = By.id("passwordField");
	By txtConfirmPassword = By.id("retypePassword");
	By txtMobileNumber = By.id("mobNumber");
	By txtExpYear = By.id("cboWorkExpYear");
	By txtExpMonth = By.id("cboWorkExpMonth");
	By scltWorkLocation = By.id("curLocation");
	By uploadResume = By.name("resumeFile_basic");
	By btnContunue = By.id("basicSubmit");
	
	
	public RegisterPage enterEmail(String emailId) throws Exception{
		try {
			logger.info("Entering email id as :"+emailId);
			flag = isElementPresent(txtEmail);
			Assert.assertTrue(flag, "Email id is not displayed");
			element = setElement(txtEmail);
			element.clear();
			element.sendKeys(emailId);
			element.sendKeys(Keys.TAB);
		} catch (Exception e) {
			
			throw new Exception("Failed while entering email id in register page"+e.getLocalizedMessage());
		}
		
		return this;
	}
	
	public RegisterPage enterPassword(String password) throws Exception{
		try {
			logger.info("Entering password as :"+password);
			flag = isElementPresent(txtPassword);
			Assert.assertTrue(flag, "Password field is not displayed");
			setElement(txtPassword).sendKeys(password);
			element.sendKeys(Keys.TAB);
		} catch (Exception e) {
			
			throw new Exception("Failed while entering password in register page"+e.getLocalizedMessage());
		}
		
		return this;
	}

	public RegisterPage enterConfirmPassword(String confirmPassword) throws Exception{
		try {
			logger.info("Entering confirm password as :"+confirmPassword);
			flag = isElementPresent(txtConfirmPassword);
			Assert.assertTrue(flag, "Confirm password field is not displayed");
			setElement(txtConfirmPassword).sendKeys(confirmPassword);
			element.sendKeys(Keys.TAB);
		} catch (Exception e) {
			
			throw new Exception("Failed while entering confirm password in register page"+e.getLocalizedMessage());
		}
		
		return this;
	}
	
	public RegisterPage enterMobileNumber(String mobileNumber) throws Exception{
		try {
			logger.info("Entering mobile number as :"+mobileNumber);
			flag = isElementPresent(txtMobileNumber);
			Assert.assertTrue(flag, "Mobile number field is not displayed");
			setElement(txtMobileNumber).sendKeys(mobileNumber);
		} catch (Exception e) {
			
			throw new Exception("Failed while entering mobile number in register page"+e.getLocalizedMessage());
		}
		
		return this;
	}
	
	public RegisterPage enterExpYear(String expYear) throws Exception{
		try {
			logger.info("Entering work experience year as :"+expYear);
			flag = isElementPresent(txtExpYear);
			Assert.assertTrue(flag, "Work experience year field is not displayed");
			setElement(txtExpYear).sendKeys(expYear);
		} catch (Exception e) {
			
			throw new Exception("Failed while Work experience year in register page"+e.getLocalizedMessage());
		}
		
		return this;
	}
	
	public RegisterPage enterExpMonth(String expMonth) throws Exception{
		try {
			logger.info("Entering work experience month as :"+expMonth);
			flag = isElementPresent(txtExpYear);
			Assert.assertTrue(flag, "Work experience month field is not displayed");
			setElement(txtExpYear).sendKeys(expMonth);
		} catch (Exception e) {
			
			throw new Exception("Failed while Work experience month in register page"+e.getLocalizedMessage());
		}
		
		return this;
	}
	
	public RegisterPage selectCurrentWorkLocation(String workLocation) throws Exception{
		try {
			logger.info("Selecting work Location as :"+workLocation);
			flag = isElementPresent(scltWorkLocation);
			Assert.assertTrue(flag, "Work location  field is not displayed");
			TimesJobUtil.selectItemByVisibleText(scltWorkLocation, workLocation);
		} catch (Exception e) {
			
			throw new Exception("Failed while work Location in register page"+e.getLocalizedMessage());
		}
		
		return this;
	}
	
	public RegisterPage uploadResume(String resumePath) throws Exception{
		try {
			logger.info("Uploading resume :");
			flag = isElementPresent(uploadResume);
			Assert.assertTrue(flag, "Upload resume field is not displayed");
			setElement(uploadResume).sendKeys(resumePath);
		} catch (Exception e) {
			
			throw new Exception("Failed while uploading resume in register page"+e.getLocalizedMessage());
		}
		
		return this;
	}
	
	public CandidateDashboardPage clickOnContinue() throws Exception{
		try {
			
			logger.info("Clicking on Register continue button");
			flag = isElementPresent(btnContunue);
			Assert.assertTrue(flag, "Continue button is not displayed");
			setElement(btnContunue).click();
			TimesJobUtil.explicitWait(3000);
			
		} catch (Exception e) {
			
			throw new Exception("Failed while clickin on contune button in register page"+e.getLocalizedMessage());
		}
		
		return new CandidateDashboardPage(driver);
	}
}

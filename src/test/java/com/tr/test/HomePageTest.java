package com.tr.test;

import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.tr.common.TimesJobConstants;
import com.tr.pages.CandidateDashboardPage;
import com.tr.pages.HomePage;
import com.tr.pages.RegisterPage;
import com.tr.pages.SignInPage;
import com.tr.util.BasePageObject;
import com.tr.util.BaseTestObject;

public class HomePageTest extends BaseTestObject{

	HomePage objHomePage = null;
	SignInPage objSignInpage = null;
	RegisterPage objRegisterPage = null;
	CandidateDashboardPage objDashboardPage = null;
	boolean flag = false;
	String text = null;
	
	@Parameters({"browserType"})
	@Test(priority=0, enabled=true,groups="Sanity")
	public void verifyHomePage() throws Exception
	{
	try 
	{
		objHomePage = new HomePage(driver);
		flag = objHomePage.isSiteLogoDisplayed();
	    Assert.assertTrue(flag, "Site logo is not displayed");
	    objSignInpage = objHomePage.clickOnSignIn();
	    text = objSignInpage.getPopUpHeader();
	    Assert.assertEquals(text, TimesJobConstants.TITLE_SIGNIN);
	} 
	catch (Exception e) 
	{
		throw new Exception("FAILED CLICK ON SITELOGO AND VERFIY PAGETITLE TESTCASE" + "\n verifyHomePage" +e.getLocalizedMessage());
	}

	}
	
	//@Parameters({"browserType"})
	//@Test(priority=0, enabled=false,groups="Sanity")
	public void verifyRegister() throws Exception
	{
	try 
	{
		objHomePage = new HomePage(driver);
		flag = objHomePage.isSiteLogoDisplayed();
		Assert.assertTrue(flag, "Site logo doesnot exist");
		objRegisterPage = objHomePage.clickOnRegister();
		objRegisterPage.enterEmail("");
		objRegisterPage.enterPassword("");
		objRegisterPage.enterConfirmPassword("");
		objRegisterPage.enterMobileNumber("");
		objRegisterPage.selectCurrentWorkLocation("");
		objRegisterPage.uploadResume("");
		objDashboardPage = objRegisterPage.clickOnContinue();
		
	} 
	catch (Exception e) 
	{
		throw new Exception("FAILED CLICK ON SITELOGO AND VERFIY PAGETITLE TESTCASE" + "\n verifyHomePage" +e.getLocalizedMessage());
	}

	}
}

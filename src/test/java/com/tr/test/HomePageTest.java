package com.tr.test;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.tr.common.TimesJobConstants;
import com.tr.common.TimesJobUtil;
import com.tr.pages.CandidateDashboardPage;
import com.tr.pages.HomePage;
import com.tr.pages.RegisterPage;
import com.tr.pages.SignInPage;
import com.tr.util.BasePageObject;
import com.tr.util.BaseTestObject;
import com.tr.util.ExcelutilObject;

public class HomePageTest extends BaseTestObject{

	HomePage objHomePage = null;
	SignInPage objSignInpage = null;
	RegisterPage objRegisterPage = null;
	CandidateDashboardPage objDashboardPage = null;
	boolean flag = false;
	String text = null;
	int step =0;
	String stepTxt ="STEP:";
	
	@Parameters({"browserType"})
	@Test(priority=0, enabled=true,groups="Sanity")
	public void verifyHomePage() throws Exception
	{
	try 
	{
		
		String userName = getHomePageDataFromPropertyFile("username");//Read test data from Property file
		String password = getHomePageDataFromPropertyFile("password");
		
		System.out.println(userName+password);
		Reporter.log(step+":Verifying Home page site logo");
		objHomePage = new HomePage(driver);
		flag = objHomePage.isSiteLogoDisplayed();
		Reporter.log(stepTxt+step+"Site logo status:"+flag);
	    Assert.assertTrue(flag, "Site logo is not displayed");
	    step++;
	    Reporter.log(stepTxt+step+":Clicking on Sign in link");
	    objSignInpage = objHomePage.clickOnSignIn();
	    step++;
	    Reporter.log(stepTxt+step+"Verifying Sign in pop up");
	    text = objSignInpage.getPopUpHeader();
	    step++;
	    Reporter.log(stepTxt+step+"Pop header text:"+text+":"+TimesJobConstants.TITLE_SIGNIN);
	    Assert.assertEquals(text, TimesJobConstants.TITLE_SIGNIN);
	} 
	catch (Exception e) 
	{
		throw new Exception("FAILED CLICK ON SITELOGO AND VERFIY PAGETITLE TESTCASE" + "\n verifyHomePage" +e.getLocalizedMessage());
	}

	}
	
	@Parameters({"browserType"})
	@Test(priority=1, enabled=true,groups="Sanity")
	public void verifyRegister() throws Exception
	{
	try 
	{
		String emailId = getHomePageSheetData(1, 0);//Read test data from excel file
		String password = getHomePageSheetData(1, 1);
		String mobileNumber = getHomePageSheetData(1, 2);
		String expYear = getHomePageSheetData(1, 3);
		String expMonth = getHomePageSheetData(1, 4);
		String workLocation = getHomePageSheetData(1, 5);
		String resumePath = getHomePageSheetData(1, 6);
		
		objHomePage = new HomePage(driver);
		flag = objHomePage.isSiteLogoDisplayed();
		Assert.assertTrue(flag, "Site logo doesnot exist");
		objRegisterPage = objHomePage.clickOnRegister();
		objRegisterPage.enterEmail(emailId);
		TimesJobUtil.explicitWait(1000);
		objRegisterPage.enterPassword(password);
		TimesJobUtil.explicitWait(1000);
		objRegisterPage.enterConfirmPassword(password);
		TimesJobUtil.explicitWait(1000);
		objRegisterPage.enterMobileNumber(mobileNumber);
		TimesJobUtil.explicitWait(1000);
		objRegisterPage.enterExpYear(expYear);
		TimesJobUtil.explicitWait(1000);
		objRegisterPage.enterExpMonth(expMonth);
		TimesJobUtil.explicitWait(1000);
		objRegisterPage.selectCurrentWorkLocation(workLocation);
		TimesJobUtil.explicitWait(1000);
		objRegisterPage.uploadResume(resumePath);
		TimesJobUtil.explicitWait(1000);
		objDashboardPage = objRegisterPage.clickOnContinue();
		
	} 
	catch (Exception e) 
	{
		throw new Exception("FAILED CLICK ON SITELOGO AND VERFIY PAGETITLE TESTCASE" + "\n verifyHomePage" +e.getLocalizedMessage());
	}

	}
	
	public String getHomePageSheetData(int row,int coloumn) throws Exception{
		
		ExcelutilObject.setExcelFile(TimesJobUtil.Path_TestData, "RegisterTestData");
		String cellVal = ExcelutilObject.getCellData(row, coloumn);
		return cellVal;
		
		
	}
	
	public  String getHomePageDataFromPropertyFile(String key){
		
		String value = getPropertyContents().getProperty(key);
		return value;
		
	}
}

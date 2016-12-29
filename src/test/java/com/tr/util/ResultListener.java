package com.tr.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.TestListenerAdapter;


public class ResultListener extends TestListenerAdapter implements IHookable {

	public static boolean m_quitExecution = false;
	public static long m_totalDuration = 0;
	private int m_passCount = 0;
	private int m_failCount = 0;
	private int m_skipCount = 0;
	public static Map<String, List<String>> m_testResultMap = new LinkedHashMap<String, List<String>>();
	public static Logger logger = Logger.getLogger(ResultListener.class);

	@Override
	public void onTestStart(ITestResult result) {
		logger.info("\n\n****************** START OF TEST CASE  " + result.getMethod().getMethodName() + " ****************** \n");
	}

	@Override
	public void onTestFailure(ITestResult tr) {

		getTestResultDetails(tr);

		m_failCount++;
		printCurrentStatus();

		
	}

	@Override
	public void onTestSkipped(ITestResult tr) {

		getTestResultDetails(tr);

		m_skipCount++;
		printCurrentStatus();

	}

	@Override
	public void onTestSuccess(ITestResult tr) {

		getTestResultDetails(tr);

		m_passCount++;
		printCurrentStatus();
	}

	@Override
	public void onFinish(ITestContext testContext) {

		/*if (null != SeleniumConnector.getWebDriver()) {
			SeleniumConnector.getWebDriver().quit();
		}*/

		//generateAndMailReport(testContext);
	}

	public void run(IHookCallBack paramIHookCallBack, ITestResult paramITestResult) {

		if (!m_quitExecution)
			paramIHookCallBack.runTestMethod(paramITestResult);
		else
			throw new SkipException("Test Skipped due to Invalid Login Credentials");

	}

	public void printCurrentStatus() {
		logger.info("\n CURRENT TEST EXECUTION STATUS -> PASS: " + m_passCount + "  FAIL: " + m_failCount + "  SKIP: " + m_skipCount + "\n");
	}

	/*private void generateAndMailReport(ITestContext testContext) {

		//if (null != JenkinsConnector.getJobName()) {
			//ReportGenerator email = new ReportGenerator(testContext);
		CustomReport_New email=new CustomReport_New(testContext);
			try {
				email.generateReport();
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	*/

	private void getTestResultDetails(ITestResult tr) {

		long testMethodDuration = 0;
		List<String> testDetails = new ArrayList<String>();

		testMethodDuration = tr.getEndMillis() - tr.getStartMillis();
		m_totalDuration += testMethodDuration;
		testDetails.add(String.valueOf(tr.getStatus()));
		//testDetails.add(AppCenterUtil.millisecondsToMS(testMethodDuration));

		m_testResultMap.put(tr.getName(), testDetails);
	}

}


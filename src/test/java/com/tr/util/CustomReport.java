package com.tr.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.IInvokedMethod;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestNGException;
import org.testng.collections.Lists;
import org.testng.internal.Utils;
import org.testng.xml.XmlSuite;

import com.tr.common.TimesJobConstants;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;



public class CustomReport extends ResultListener implements IReporter{

	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat(" MMM d 'at' hh:mm a");
	private PrintWriter m_out;
	private int m_row;
	private Integer m_testIndex;
	private int m_methodIndex;
	private Scanner scanner;
	public static Logger logger = Logger.getLogger(CustomReport.class);
	private StringBuffer sb = new StringBuffer();
	public String propertyFilePath = System.getProperty("user.dir")
			+ "\\src\\test\\resources\\testdata\\testData.properties";
	/**
	 *  Author : Manjunath Reddy
	 * This method is the entry point of this class. TestNG calls this listener
	 * method to generate the report.
	 
	 */
		
	public void generateReport(List<XmlSuite> xml,List<ISuite> suites,String outdir) {
		Reporter.log("", true);
		Reporter.log("-------------------------------------", true);
		Reporter.log("-- Generating test HTML report...  --", true);
		Reporter.log("-------------------------------------", true);
		// Iterating over each suite included in the test
		for (ISuite suite : suites) {
			// Following code gets the suite name
			String suiteName = suite.getName();
			// Getting the results for the said suite
			Map<String, ISuiteResult> suiteResults = suite.getResults();
			for (ISuiteResult sr : suiteResults.values()) {
				ITestContext tc = sr.getTestContext();
				System.out.println(
						"Passed tests for suite " + suiteName + " is:" + tc.getPassedTests().getAllResults().size());
				System.out.println(
						"Failed tests for suite " + suiteName + " is:" + tc.getFailedTests().getAllResults().size());
				System.out.println(
						"Skipped tests for suite " + suiteName + " is:" + tc.getSkippedTests().getAllResults().size());
			}
		}
		System.out.println();
		try {
			//reportContent(suites);
			reportContent(suites);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			m_out = createWriter(outdir);
		} catch (IOException e) {
			Reporter.log("Error relating to report output file.", true);
			e.printStackTrace();
		}
		startHtml(m_out);
		try {
			generateSuiteSummaryReport(suites);
		} catch (Exception e) {
			e.printStackTrace();
		}
		generateMethodSummaryReport(suites);
		generateMethodDetailReport( suites );
		
		endHtml(m_out);
		try {
			for (ISuite suite : suites) {
				String suiteName = suite.getName();
//			List<String> lst = new ArrayList<String>();
//			lst.add("mkondapur@gmail.com");
			sendEmail(sb.toString(), suiteName + " Report", "mkondapur@gmail.com");
			//sendMailViaExchnageService("Manjunath.Reddy@apollo.edu", "Itcinfotech8*", suiteName + " Report",sb.toString(), lst);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		m_out.flush();

		m_out.close();

	}

	private void reportContent(List<ISuite> suites) throws Exception {

		sb.append(
				"<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Aynax Execution</title><style type=\"text/css\">table caption,table.info_table,table.param,table.passed,table.failed {margin-bottom:10px;border:1px solid #000099;border-collapse:collapse;empty-cells:show;}table.info_table td,table.info_table th,table.param td,table.param th,table.passed td,table.passed th,table.failed td,table.failed th {border:1px solid #000099;padding:.25em .5em .25em .5em}table.param th {vertical-align:bottom}td.numi,th.numi,td.numi_attn {text-align:center}tr.total td {font-weight:bold}table caption {text-align:center;font-weight:bold;}table.passed tr.stripe td,table tr.passedodd td {background-color: #00AA00;}table.passed td,table tr.passedeven td {background-color: #33FF33;}table.passed tr.stripe td,table tr.skippedodd td {background-color: #cccccc;}table.passed td,table tr.skippedodd td {background-color: #dddddd;}table.failed tr.stripe td,table tr.failedodd td,table.param td.numi_attn {background-color: #FF3333;}table.failed td,table tr.failedeven td,table.param tr.stripe td.numi_attn {background-color: #DD0000;}tr.stripe td,tr.stripe th {background-color: #E6EBF9;}p.totop {font-size:85%;text-align:center;border-bottom:2px black solid}div.shootout {padding:2em;border:3px #4854A8 solid}</style></head><body>");
		
		for (ISuite suite : suites) {
			String suiteName = suite.getName();
			sb.append("Hi Everybody,");
			sb.append("<p> Please find the " + suiteName + " Report" + " </p>");
			sb.append(printEnvironmentDetails(suites));
			sb.append(printTestExecutionSummary(suites));
			sb.append(printMethodResult());
			sb.append("</body></html>");
			
		}
	}

	protected PrintWriter createWriter(String outdir) throws IOException {
		new File(outdir).mkdirs();
		return new PrintWriter(new BufferedWriter(new FileWriter(new File(outdir, "TimesJobTestNGReport.html"))));
	}

	/**
	 * Creates a table showing the highlights of each test method with links to
	 * the method details
	 */
	protected void generateMethodSummaryReport(List<ISuite> suites) {
		m_methodIndex = 0;
		startResultSummaryTable("methodOverview");
		int testIndex = 1;
		for (ISuite suite : suites) {
			if (suites.size() > 1) {
				titleRow(suite.getName(), 5);
			}
			Map<String, ISuiteResult> r = suite.getResults();
			for (ISuiteResult r2 : r.values()) {
				ITestContext testContext = r2.getTestContext();
				String testName = testContext.getName();
				m_testIndex = testIndex;
				resultSummary(suite, testContext.getFailedConfigurations(), testName, "failed",
						" (configuration methods)");
				resultSummary(suite, testContext.getFailedTests(), testName, "failed", "");
				resultSummary(suite, testContext.getSkippedConfigurations(), testName, "skipped",
						" (configuration methods)");
				resultSummary(suite, testContext.getSkippedTests(), testName, "skipped", "");
				resultSummary(suite, testContext.getPassedTests(), testName, "passed", "");
				testIndex++;
			}
		}
		m_out.println("</table>");
	}
	
	/**
	 * Creates a section showing known results and console output for each
	 * method
	 */
	protected void generateMethodDetailReport(List<ISuite> suites) {
		Reporter.log("Size of suites: " + suites.size(), true);
		m_out.println("<br/><b align=\"center\">Method Summaries</b><br/>");
		m_methodIndex = 0;
		for (ISuite suite : suites) {
			m_out.println("<h1>" + suite.getName() + "</h1>");
			Map<String, ISuiteResult> r = suite.getResults();
			Reporter.log("Size of suite: " + r.size(), true);
			for (ISuiteResult r2 : r.values()) {
				ITestContext testContext = r2.getTestContext();
				if (r.values().size() > 0) {
					m_out.println("<h3>" + testContext.getName() + "</h3>");
				}
				Reporter.log("Generating method detail for failed configurations...", true);
				resultDetail(testContext.getFailedConfigurations());
				Reporter.log("Generating method detail for failed tests...", true);
				resultDetail(testContext.getFailedTests());
				Reporter.log("Generating method detail for skipped configurations...", true);
				resultDetail(testContext.getSkippedConfigurations());
				Reporter.log("Generating method detail for skipped tests...", true);
				resultDetail(testContext.getSkippedTests());
				Reporter.log("Generating method detail for passed tests...", true);
				resultDetail(testContext.getPassedTests());
			}
		}
	}

	public String convertLongToCanonicalLengthOfTime(long timeLength) {
		System.out.println("Time passed in is: " + timeLength);
		if (timeLength >= 86400000) {
			throw new IllegalArgumentException("Duration must be greater than zero or less than 24 hours!");
		}

		long hours = TimeUnit.MILLISECONDS.toHours(timeLength);
		timeLength -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(timeLength);
		timeLength -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(timeLength);

		StringBuilder sb = new StringBuilder(64);
		sb.append(hours);
		sb.append("h:");
		sb.append(minutes);
		sb.append("m:");
		sb.append(seconds);
		sb.append("s");

		return (sb.toString());
	}

	/**
	 * 
	 * @param tests
	 */
	private void resultSummary(ISuite suite, IResultMap tests, String testname, String style, String details) {
		if (tests.getAllResults().size() > 0) {
			StringBuffer buff = new StringBuffer();
			String lastClassName = "";
			int mq = 0;
			int cq = 0;
			for (ITestNGMethod method : getMethodSet(tests, suite)) {
				m_row += 1;
				m_methodIndex += 1;
				ITestClass testClass = method.getTestClass();
				String className = testClass.getName();
				if (mq == 0) {
					String id = (m_testIndex == null ? null : "t" + Integer.toString(m_testIndex));
					m_testIndex = null;
				}
				if (!className.equalsIgnoreCase(lastClassName)) {
					if (mq > 0) {
						cq += 1;
						m_out.print("<tr class=\"" + style + (cq % 2 == 0 ? "even" : "odd") + "\">" + "<td");
						if (mq > 1) {
							m_out.print(" rowspan=\"" + mq + "\"");
						}
						m_out.println(">" + lastClassName + "</td>" + buff);
					}
					mq = 0;
					buff.setLength(0);
					lastClassName = className;
				}
				Set<ITestResult> resultSet = tests.getResults(method);
				long end = Long.MIN_VALUE;
				long start = Long.MAX_VALUE;
				for (ITestResult testResult : tests.getResults(method)) {
					if (testResult.getEndMillis() > end) {
						end = testResult.getEndMillis();
					}
					if (testResult.getStartMillis() < start) {
						start = testResult.getStartMillis();
					}
				}
				mq += 1;
				if (mq > 1) {
					buff.append("<tr class=\"" + style + (cq % 2 == 0 ? "odd" : "even") + "\">");
				}
				Date d = new Date(start);
				String formattedDate = dateFormatter.format(d);
				String description = method.getDescription();
				String testInstanceName = resultSet.toArray(new ITestResult[] {})[0].getTestName();
				buff.append("<td><a href=\"#m" + m_methodIndex + "\">" + qualifiedName(method) + " "
						+ (description != null && description.length() > 0 ? "(\"" + description + "\")" : "") + "</a>"
						+ (null == testInstanceName ? "" : "<br>(" + testInstanceName + ")") + "</td>"
						+ "<td class=\"numi\">" + resultSet.size() + "</td>" + "<td>" + formattedDate + "</td>"
						+ "<td class=\"numi\">" + convertLongToCanonicalLengthOfTime(end - start) + "</td>");
				// + "<td><a href='emailable-report.html'></a></td></tr>");
			}
			if (mq > 0) {
				cq += 1;
				m_out.print("<tr class=\"" + style + (cq % 2 == 0 ? "even" : "odd") + "\">" + "<td");
				if (mq > 1) {
					m_out.print(" rowspan=\"" + mq + "\"");
				}
				m_out.println(">" + lastClassName + "</td>" + buff);
			}
		}
	}

	/** Starts and defines columns result summary table */
	private void startResultSummaryTable(String style) {
		tableStart(style, "summary");
		m_out.println("<b align=\"center\">Test Result Summary Table</b>");
		// m_out.println("<tr><th>Class</th>"
		// + "<th>Method</th><th>#
		// of<br>Scenarios</th><th>Start</th><th>Time<br>elapsed</th><th>Custom</th></tr>");
		m_row = 0;
	}

	private String qualifiedName(ITestNGMethod method) {
		StringBuilder addon = new StringBuilder();
		String[] groups = method.getGroups();
		int length = groups.length;
		if (length > 0 && !"basic".equalsIgnoreCase(groups[0])) {
			addon.append("(");
			for (int i = 0; i < length; i++) {
				if (i > 0) {
					addon.append(", ");
				}
				addon.append(groups[i]);
			}
			addon.append(")");
		}
		return "<b>" + method.getMethodName() + "</b> " + addon;
	}

	/**
	 * Called by method generateMethodDetailReport
	 * 
	 * @param tests
	 */
	private void resultDetail(IResultMap tests) {
		if (tests.size() > 0) {
			for (ITestResult result : tests.getAllResults()) {
				ITestNGMethod method = result.getMethod();
				m_methodIndex++;
				String packname = method.getTestClass().getName();
				m_out.println("<h2 id=\"m" + m_methodIndex + "\">" + packname + " : " + method.getMethodName() + "</h2>");
				Set<ITestResult> resultSet = tests.getResults(method);
				generateForResult(result, method, resultSet.size());
				m_out.println("<p class=\"totop\"><a href=\"#summary\">back to summary</a></p>");
			}
		} else {
			Reporter.log("Result map was empty.", true);
		}
	}

	/**
	 * Write the first line of the stack trace
	 *
	 * @param tests
	 */
	@SuppressWarnings("unused")
	private void getShortException(IResultMap tests) {
		for (ITestResult result : tests.getAllResults()) {
			m_methodIndex++;
			Throwable exception = result.getThrowable();
			List<String> msgs = Reporter.getOutput(result);
			boolean hasReporterOutput = msgs.size() > 0;
			boolean hasThrowable = exception != null;
			if (hasThrowable) {
				boolean wantsMinimalOutput = result.getStatus() == ITestResult.SUCCESS;
				if (hasReporterOutput) {
					m_out.print("<h3>" + (wantsMinimalOutput ? "Expected Exception" : "Failure") + "</h3>");
				}
				// Getting first line of the stack trace
				String str = Utils.stackTrace(exception, true)[0];
				scanner = new Scanner(str);
				String firstLine = scanner.nextLine();
				m_out.println(firstLine);
			}
		}
	}

	/**
	 * Write all parameters
	 *
	 * @param tests
	 */
	@SuppressWarnings("unused")
	private void getParameters(IResultMap tests) {
		for (ITestResult result : tests.getAllResults()) {
			m_methodIndex++;
			Object[] parameters = result.getParameters();
			boolean hasParameters = parameters != null && parameters.length > 0;
			if (hasParameters) {
				for (Object p : parameters) {
					m_out.println(Utils.escapeHtml(org.testng.internal.Utils.toString(p, String.class)) + " | ");
				}
			}
		}
	}

	/**
	 * Called by resultDetail method to show detailed information about each
	 * test including the console log.
	 * 
	 * @param ans
	 * @param method
	 * @param resultSetSize
	 */
	private void generateForResult(ITestResult ans, ITestNGMethod method, int resultSetSize) {
		Object[] parameters = ans.getParameters();
		boolean hasParameters = parameters != null && parameters.length > 0;
		tableStart("result", null);
		if (hasParameters) {

			m_out.print("<tr class=\"param\">");
			for (int x = 1; x <= parameters.length; x++) {
				m_out.print("<th>Param." + x + "</th>");
			}
			m_out.println("</tr>");
			m_out.print("<tr class=\"param stripe\">");
			for (Object p : parameters) {
				m_out.println("<td>" + Utils.escapeHtml(Utils.toString(p, String.class)) + "</td>");
			}
			m_out.println("</tr>");
		} else {
			//m_out.println("<tr><td><i>Test did not have parameters.</i></td></tr> ");
		}
		List<String> msgs = Reporter.getOutput(ans);
		boolean hasReporterOutput = msgs.size() > 0;
		Throwable exception = ans.getThrowable();
		boolean hasThrowable = exception != null;
		if (hasReporterOutput || hasThrowable) {
			if (hasParameters) {
				m_out.print("<tr><td");
				if (parameters.length > 1) {
					m_out.print(" colspan=\"" + parameters.length + "\"");
				}
				m_out.println(">");
			} else {
				m_out.println("<div>");
			}
			if (hasReporterOutput) {
				if (hasThrowable) {
					m_out.println("<h3>Test Messages</h3>");
				}
				for (String line : msgs) {
					m_out.println(line + "<br>");
				}
			}
			if (hasThrowable) {
				boolean wantsMinimalOutput = ans.getStatus() == ITestResult.SUCCESS;
				if (hasReporterOutput) {
					m_out.println("<h3>" + (wantsMinimalOutput ? "Expected Exception" : "Failure") + "</h3>");
				}
				generateExceptionReport(exception, method);
			}
			if (hasParameters) {
				m_out.println("</td></tr>");
			} else {
				m_out.println("</div>");
			}
		} else {
			//m_out.println("<tr><td><i>Test did not have report output.</i></td></tr>");
		}
		m_out.println("</table>");
	}

	protected void generateExceptionReport(Throwable exception, ITestNGMethod method) {
		m_out.print("<div class=\"stacktrace\">");
		m_out.print(Utils.stackTrace(exception, true)[0]);
		m_out.println("</div>");
	}

	/**
	 * Since the methods will be sorted chronologically, we want to return the
	 * ITestNGMethod from the invoked methods.
	 */
	private Collection<ITestNGMethod> getMethodSet(IResultMap tests, ISuite suite) {
		List<IInvokedMethod> r = Lists.newArrayList();
		List<IInvokedMethod> invokedMethods = suite.getAllInvokedMethods();
		for (IInvokedMethod im : invokedMethods) {
			if (tests.getAllMethods().contains(im.getTestMethod())) {
				r.add(im);
			}
		}
		Arrays.sort(r.toArray(new IInvokedMethod[r.size()]), new TestSorter());
		List<ITestNGMethod> result = Lists.newArrayList();
		// Add all the invoked methods
		for (IInvokedMethod m : r) {
			result.add(m.getTestMethod());
		}
		// Add all the methods that weren't invoked (e.g. skipped) that we
		// haven't added yet
		for (ITestNGMethod m : tests.getAllMethods()) {
			if (!result.contains(m)) {
				result.add(m);
			}
		}
		return result;
	}


	public void generateSuiteSummaryReport(List<ISuite> suites) throws Exception {
		printExecutionParameters(suites);
		m_out.println("<b align=\"center\">Test Execution Summary</b>");
		tableStart("testOverview", null);
		m_out.print("<tr>");
		tableColumnStart("Test");
		tableColumnStart("Methods<br>Passed");
		tableColumnStart("Scenarios<br>Passed");
		tableColumnStart("# skipped");
		tableColumnStart("# failed");
		tableColumnStart("Total Time");
		/*tableColumnStart("Included<br>Groups");
		tableColumnStart("Excluded<br>Groups");*/
		m_out.println("</tr>");

		int qty_tests = 0;
		int qty_pass_m = 0;
		int qty_pass_s = 0;
		int qty_skip = 0;
		int qty_fail = 0;
		long testStart;
		long testEnd;
		m_testIndex = 1;
		if (suites.size() == 0)
			throw new TestNGException("You need to have at lease one suite to generate a report.");
		for (ISuite suite : suites) {
			titleRow(suite.getName(), 8);
			Map<String, ISuiteResult> tests = suite.getResults();
			for (ISuiteResult r : tests.values()) {
				qty_tests += 1;
				ITestContext overview = r.getTestContext();
				startSummaryRow(overview.getName());
				int q = getMethodSet(overview.getPassedTests(), suite).size();
				qty_pass_m += q;
				summaryCell(q, Integer.MAX_VALUE);
				q = overview.getPassedTests().size();
				qty_pass_s += q;
				summaryCell(q, Integer.MAX_VALUE);
				q = getMethodSet(overview.getSkippedTests(), suite).size();
				qty_skip += q;
				summaryCell(q, 0);
				q = getMethodSet(overview.getFailedTests(), suite).size();
				qty_fail += q;
				summaryCell(q, 0);
				testStart = overview.getStartDate().getTime();
				testEnd = overview.getEndDate().getTime();
				String passedTime = convertLongToCanonicalLengthOfTime(testEnd - testStart);
				summaryCell(passedTime, true);
				/*summaryCell(overview.getIncludedGroups());
				summaryCell(overview.getExcludedGroups());*/
				m_out.println("</tr>");
				m_testIndex++;
			}
			m_out.println("<tr class=\"total\"><td>Total</td>");
			summaryCell(qty_pass_m, Integer.MAX_VALUE);
			summaryCell(qty_pass_s, Integer.MAX_VALUE);
			summaryCell(qty_skip, 0);
			summaryCell(qty_fail, 0);
			for (ISuite suite1 : suites) {
				Map<String, ISuiteResult> suiteResults = suite1.getResults();
				for (ISuiteResult sr : suiteResults.values()) {
					ITestContext context = sr.getTestContext();
			testStart = context.getStartDate().getTime();
			testEnd = context.getEndDate().getTime();
			String passedTime = convertLongToCanonicalLengthOfTime( testEnd - testStart );
			summaryCell(passedTime, true);
			}
			}
			// String passedTime = convertLongToCanonicalLengthOfTime( testEnd -
			// testStart );
			//summaryCell("9999", true); 
			//m_out.println("<td colspan=\"1\"></td>");
			//m_out.println("<td colspan=\"2\">&nbsp;</td></tr>");
			m_out.println("</table>");
			//m_out.println("<p></p>");
		}
	}

	public void printExecutionParameters(List<ISuite> suites) throws Exception {
		for (ISuite suite : suites) {
			String suiteName = suite.getName();
			Capabilities caps = ((RemoteWebDriver) BaseTestObject.driver).getCapabilities();
			String browsername = caps.getBrowserName();
			String browserVersion = caps.getVersion();
			String OS = caps.getPlatform().toString();
			Properties prop = new Properties();
			InputStream input = new FileInputStream(propertyFilePath);
			prop.load(input);
			String url = prop.getProperty("url");
			m_out.println("<b>Test Environment Details</b>");
			tableStart("testOverview", null);
			m_out.print("<tr>");
			tableColumnStart("Suite Name");
			tableColumnStart("BaseUrl");
			tableColumnStart("OSType");
			tableColumnStart("Browser");
			tableColumnStart("BrowserVersion");
			m_out.println("</tr>");
			summaryCell(suiteName, true);
			summaryCell(url, true);
			summaryCell(OS, true);
			summaryCell(browsername, true);
			summaryCell(browserVersion, true);
			m_out.println("</table>");
			m_out.println("<p></p>");
		}
	}

	public String printEnvironmentDetails(List<ISuite> suites) throws Exception {
		StringBuffer sb = new StringBuffer();
			Capabilities caps = ((RemoteWebDriver) BaseTestObject.driver).getCapabilities();
			String browsername = caps.getBrowserName();
			String browserVersion = caps.getVersion();
			String OS = caps.getPlatform().toString();
			Properties prop = new Properties();
			InputStream input = new FileInputStream(propertyFilePath);
			prop.load(input);
			String url = prop.getProperty("url");
			String buildUrl=JenkinsConnector.getBuildUrl();
			String buildNo=JenkinsConnector.getBuildNo();
			String VideoUrl=buildUrl.replaceAll(buildNo, "ws/src/test/resources/Recordings");
			String reportLink=buildUrl.replaceAll(buildNo, "ws/target/surefire-reports/AynaxExtentReport.html");
			String logLink=buildUrl.replaceAll(buildNo, "ws/target/logs/ALEFELog.txt");
			sb.append("<table width='40%' border=1 >");
			sb.append(
					"<th bgcolor='#5D7B9D'  colspan=2><col width=\"40%\">  <col width=\"60\"><font color='#fff' size=3> Environment Details </font></th>");
			sb.append("<tr>");
			sb.append("<td ><b><font  size=2 >APPLICATION URL</font></td><td align='center' size=2> <a href="+ url + "><font  color=green > " + url + "</a></font></b></td></tr>");
			sb.append("<td><b><font  size=2>OS EXECUTED </font></td><td width=20 align='center' size=2> <font  color=green > " + OS+ "</b></td> </font></tr>");
			sb.append("<td><b><font  size=2>DATE OF EXECUTION </font></td><td width=20 align='center' size=2> <font  color=green > " + new Date()+ " </b></td> </font></tr>");
			sb.append("<td><b><font  size=2>BROWSER NAME </font></td><td width=20 align='center' size=2> <font  color=green > " + browsername+ "</b></td> </font></tr>");
			sb.append("<td><b><font  size=2>BROWSER VERSION </font></td><td width=20 align='center' size=2><font  color=green >  " + browserVersion
					+ " </b></td> </font></tr>");
			sb.append("<td ><b><font  size=2>REPORT LINK</font> </td><td align='center' size=2> <a href="+ reportLink + "><font  color=green > " + reportLink + "</a></b></td></tr>");
			sb.append("<td ><b><font  size=2>VIDEO URL</font> </td><td align='center' size=2> <a href="	+ VideoUrl + "> <font  color=green >" + VideoUrl + "</a></b></td></tr>");
			sb.append("<td ><b><font  size=2>LOG URL</font> </td><td align='center' size=2> <a href="	+ logLink + "> <font  color=green >" + logLink + "</a></b></td></tr>");

			sb.append("</table>");
		
		return sb.toString();
	}
	
	public String printTestExecutionSummary(List<ISuite> suites) {
		 StringBuffer sbuffer = new StringBuffer();
		 long testStart;
			long testEnd;
		 for (ISuite suite : suites) {
				Map<String, ISuiteResult> suiteResults = suite.getResults();
				for (ISuiteResult sr : suiteResults.values()) {
					ITestContext context = sr.getTestContext();
		 int passedCount=context.getPassedTests().getAllResults().size();
		 int skippedCount=context.getSkippedTests().getAllResults().size();
		 int failedCount=context.getFailedTests().getAllResults().size();
        int totalCount = passedCount + skippedCount + failedCount;
        testStart = context.getStartDate().getTime();
		 testEnd = context.getEndDate().getTime();
		 String totalTime = convertLongToCanonicalLengthOfTime( testEnd - testStart );
		 sbuffer.append("<table width='40%' border=1 >");
		 sbuffer.append(
					"<th bgcolor='#5D7B9D'  colspan=2><col width=\"40%\">  <col width=\"60\"><font color='#fff' size=3> Test Execution Summary </font></th>");
		 sbuffer.append("<tr>");
        sbuffer.append("<table cellspacing=\"0\" cellpadding=\"0\" width=30% border=3 class=\"param\"> ");
        sbuffer.append("<td align='center'><font  color=green >Pass Count</td>");
        sbuffer.append("<td align='center'><font  color=red >Fail Count</td>");
        sbuffer.append("<td align='center'><font  color=grey >Skip Count</td>");
        sbuffer.append("<td align='center'><font  color=blue >Total Count</td>");
        sbuffer.append("<td align='center'><font  color=olive >Time Duration (hh:mm:ss)</td></tr>");
        /*List<ITestNGMethod> lst=suite.getAllMethods();
        for(int i=0;i<lst.size();i++){
        sbuffer.append("<tr><td align='center'><font  color=blue > <a href="+ lst.get(i).getMethodName() + ">"+ lst.get(i).getMethodName() + "</a></td></tr>");
        }*/
      
        sbuffer.append("<td align='center'><font  color=green >" + passedCount + "</td>");
        sbuffer.append("<td align='center'><font  color=red >" + failedCount + "</td>");
        sbuffer.append("<td align='center'><font  color=grey >" + skippedCount + "</td>");
        sbuffer.append("<td align='center'><font  color=blue >" + totalCount + "</td>");
        sbuffer.append("<td align='center'><font  color=olive >" + totalTime + "</td>");
        sbuffer.append("</table>");
        }
				}
		 
		  return sbuffer.toString();
	 }
	
	 private String printMethodResult() {
		 StringBuffer sbuffer1 = new StringBuffer();
         String body = "";
         Map<String, List<String>> resultMap = ResultListener.m_testResultMap;

       
         for (Map.Entry<String, List<String>> map : resultMap.entrySet()) {

                String font = "";
                String testMethodName = map.getKey();
                String status = map.getValue().get(0);
               

                if (status.equalsIgnoreCase("1"))
                      font = "<font color='green'>Pass</font>";
                else if (status.equalsIgnoreCase("2"))
                      font = "<font color='red'>Fail</font>";
                else if (status.equalsIgnoreCase("3"))
                      font = "<font color='grey'>Skip</font>";

                body = body + "<tr>" + "<td>" + testMethodName + "</td><td>" + font + "</td>" + "</tr>";
               
         }
         sbuffer1.append("<table width='40%' border=1 >");
		 sbuffer1.append(
					"<th bgcolor='#5D7B9D'  colspan=2><col width=\"40%\">  <col width=\"60\"><font color='#fff' size=3> Test Details </font></th>");
		 sbuffer1.append("<tr>");
		 
         sbuffer1.append("<table width='60%' border='1' cellpadding='0' cellspacing='0' >"
                      + "<tr bgcolor='99CCFF'><td> TestCase</td><td> Result</td></tr>" + body + "</table>");
		  return sbuffer1.toString();

  }
	
	@SuppressWarnings("unused")
	private void summaryCell(String[] val) {
		StringBuffer b = new StringBuffer();
		for (String v : val) {
			b.append(v + " ");
		}
		summaryCell(b.toString(), true);
	}

	private void summaryCell(String v, boolean isgood) {
		m_out.print("<td class=\"numi" + (isgood ? "" : "_attn") + "\">" + v + "</td>");
	}

	private void startSummaryRow(String label) {
		m_row += 1;
		m_out.print("<tr" + (m_row % 2 == 0 ? " class=\"stripe\"" : "")
				+ "><td style=\"text-align:left;padding-right:2em\"><a href=\"#t" + m_testIndex + "\">" + label + "</a>"
				+ "</td>");
	}

	private void summaryCell(int v, int maxexpected) {
		summaryCell(String.valueOf(v), v <= maxexpected);
	}

	private void tableStart(String cssclass, String id) {
		m_out.println("<table cellspacing=\"0\" cellpadding=\"0\""
				+ (cssclass != null ? " class=\"" + cssclass + "\"" : " style=\"padding-bottom:2em\"")
				+ (id != null ? " id=\"" + id + "\"" : "") + ">");
		m_row = 0;
	}

	private void tableColumnStart(String label) {
		m_out.print("<th>" + label + "</th>");
	}

	private void titleRow(String label, int cq) {
		titleRow(label, cq, null);
	}

	private void titleRow(String label, int cq, String id) {
		m_out.print("<tr");
		if (id != null) {
			m_out.print(" id=\"" + id + "\"");
		}
		m_out.println("><th colspan=\"" + cq + "\">" + label + "</th></tr>");
		m_row = 0;
	}

	/** Starts HTML stream */
	protected void startHtml(PrintWriter out) {
		out.println(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
		out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		out.println("<head>");
		out.println("<title>Aynax Automation Execution Report</title>");
		out.println("<style type=\"text/css\">");
		out.println("table {margin-bottom:10px;border-collapse:collapse;empty-cells:show}");
		out.println("td,th {border:1px solid #009;padding:.25em .5em}");
		out.println(".result th {vertical-align:bottom}");
		out.println(".param th {padding-left:1em;padding-right:1em}");
		out.println(".param td {padding-left:.5em;padding-right:2em}");
		out.println(".stripe td,.stripe th {background-color: #E6EBF9}");
		out.println(".numi,.numi_attn {text-align:right}");
		out.println(".total td {font-weight:bold}");
		out.println(".passedodd td {background-color: #0A0}");
		out.println(".passedeven td {background-color: #3F3}");
		out.println(".skippedodd td {background-color: #CCC}");
		out.println(".skippedodd td {background-color: #DDD}");
		out.println(".failedodd td,.numi_attn {background-color: #F33}");
		out.println(".failedeven td,.stripe .numi_attn {background-color: #D00}");
		out.println(".stacktrace {white-space:pre;font-family:monospace}");
		out.println(".totop {font-size:85%;text-align:center;border-bottom:2px solid #000}");
		out.println("</style>");
		out.println("</head>");
		out.println("<body>");
	}

	/** Finishes HTML stream */
	protected void endHtml(PrintWriter out) {
		out.println("<center><h4>=Customized TestNG Report=</h4></center>");
		out.println("</body></html>");
	}

	// ~ Inner Classes --------------------------------------------------------
	/** Arranges methods by classname and method name */
	private class TestSorter implements Comparator<IInvokedMethod> {
		/** Arranges methods by classname and method name */
		public int compare(IInvokedMethod o1, IInvokedMethod o2) {
			// System.out.println("Comparing " + o1.getMethodName() + " " +
			// o1.getDate()
			// + " and " + o2.getMethodName() + " " + o2.getDate());
			return (int) (o1.getDate() - o2.getDate());
			// int r = ((T) o1).getTestClass().getName().compareTo(((T)
			// o2).getTestClass().getName());
			// if (r == 0) {
			// r = ((T) o1).getMethodName().compareTo(((T) o2).getMethodName());
			//
		}
		// return r;
	}

	/*
	 * public void sendreport(){
	 * 
	 * Properties props = new Properties(); props.put("mail.smtp.host",
	 * "smtp.gmail.com"); props.put("mail.smtp.socketFactory.port", "465");
	 * props.put("mail.smtp.socketFactory.class",
	 * "javax.net.ssl.SSLSocketFactory"); props.put("mail.smtp.auth", "true");
	 * props.put("mail.smtp.port", "465"); Session session =
	 * Session.getDefaultInstance(props, new javax.mail.Authenticator() {
	 * protected PasswordAuthentication getPasswordAuthentication() { return new
	 * PasswordAuthentication("akshaya.panigrahi85@gmail.com","hiwelcome"); }
	 * }); try { String htmlPath =
	 * System.getProperty("user.dir")+"\\test-output\\AynaxReport.html"; String
	 * fromMail="akshaya.panigrahi85@gmail.com"; String
	 * toMail="Akshaya.Panigrahi@itcinfotech.com"; MimeMessage message = new
	 * MimeMessage(session);
	 * 
	 * message.setFrom(new InternetAddress(fromMail));
	 * message.addRecipient(Message.RecipientType.TO, new
	 * InternetAddress(toMail));
	 * 
	 * message.setSubject("HTML Message");
	 * 
	 * message.setContent(htmlPath,"text/html" ); Transport.send(message);
	 * System.out.println("message sent...."); } catch (MessagingException ex) {
	 * throw new RuntimeException(ex); } }
	 */

	public void sendMailViaExchnageService(String username, String password, String subject, String body,
			List<String> toAddressList) throws Exception {

		logger.info("Mail sending Inprogress...");
		ExchangeService service;
		

		service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		ExchangeCredentials credentials = new WebCredentials(username, password);
		service.setCredentials(credentials);
		try {
			service.setUrl(new URI("https://outlook.office365.com/EWS/Exchange.asmx"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		EmailMessage msg;
		try {

			msg = new EmailMessage(service);
			msg.setSubject(subject);
			msg.setBody(MessageBody.getMessageBodyFromText(body));
			Iterator<String> mailList = toAddressList.iterator();
			msg.getToRecipients().addSmtpAddressRange(mailList);

			msg.send();
			logger.info("Mail sending Success...Please check your inbox");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void sendEmail(String source, String subject, String mailIds) throws EmailException, MessagingException {
		if (!"".equals(source)) {
			
			HtmlEmail email = new HtmlEmail();
			
			email.setHostName("smtp.gmail.com");

			String to = TimesJobConstants.MAILING_LIST_STAKEHOLDER;
			email.addTo(to);

			if (mailIds != null && mailIds.contains(",")) {
				String[] ccArr = mailIds.split(",");
				for (String t : ccArr) {
					email.addCc(t);
				}
			}

			email.setFrom(TimesJobConstants.MAILING_LIST_STAKEHOLDER);
			email.setSubject(subject);
			email.setHtmlMsg(source);
			email.send();
		}
	}
	
}
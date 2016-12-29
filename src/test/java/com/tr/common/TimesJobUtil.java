package com.tr.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;


public class TimesJobUtil {

	static RemoteWebDriver driver;
	 static String txt=null;
	 boolean flag = false;
	 public static Logger logger = Logger.getLogger(TimesJobUtil.class);
	 
	 
	 public static RemoteWebDriver getWebDriver() {
			return driver;
		}
	 
	 
	public static void implicitWait(int seconds){
		
		driver.manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
	}
	
	public static void explicitWait(int miliSeconds) throws InterruptedException{
		
		Thread.sleep(miliSeconds);
	}
	
	public static void waitForElementLaod(WebElement element) throws InterruptedException {
		try {
			int sec = 0;
			for (sec = 0; sec <= 30; sec++) {
				try {
					Thread.sleep(1000);
					sec++;
					if (element.isEnabled()) {
						//System.out.println(" : ELEMENT IS ENABLED : " + element.getAttribute("name"));
						break;
					}
				} catch (Exception e) {
				}
				//System.out.println(" : ELEMENT NOT YET ENABLED : " + element.getAttribute("name"));
			}
		} catch (Exception e) {
		}
	}
	
	
	String TXT_LOGIN_MSG = "";
	public static final String Path_TestData = System.getProperty("user.dir")+"\\src\\test\\resources\\testdata\\testDataSheet.xlsx";
	
	
	public String getExcelRowData(String sheetName , int rowNum , int colNum) throws InvalidFormatException, IOException{
		FileInputStream fis = new FileInputStream(Path_TestData);
		Workbook wb = WorkbookFactory.create(fis);
		Sheet sh = wb.getSheet(sheetName);
		Row row = sh.getRow(rowNum);
		String data = row.getCell(colNum).getStringCellValue();
		return data;
	}
	
	public int getRowcount(String sheetName) throws InvalidFormatException, IOException{
		FileInputStream fis = new FileInputStream(Path_TestData);
		Workbook wb = WorkbookFactory.create(fis);
		Sheet sh = wb.getSheet(sheetName);
		int rowCount = sh.getLastRowNum();
		return rowCount;
	}
	
	public void setExcelData(String sheetName , int rowNum , int colNum , String data) throws InvalidFormatException, IOException{
		FileInputStream fis = new FileInputStream(Path_TestData);
		Workbook wb = WorkbookFactory.create(fis);
		Sheet sh = wb.getSheet(sheetName);
		Row row = sh.getRow(rowNum);
		Cell cel = row.createCell(colNum);
		cel.setCellType(cel.CELL_TYPE_STRING);
}
	
	
	public static String todaysDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM");
		Date date = new Date();
		return (dateFormat.format(date));
	}

	public static String todaysDateYear() {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");
		Date date = new Date(System.currentTimeMillis() + 5 * 1000L * 60 * 60 * 24);
		return (dateFormat.format(date));
	}

	public static String getSpecificDate(int days) {
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");
		Date date = new Date(System.currentTimeMillis() + days * 1000L * 60 * 60 * 24);
		System.out.println(date);
		return (dateFormat.format(date));
	}

	public static String getPreviousYearDate(int years) {

		DateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -years);
		String result = dateFormat.format(cal.getTime());

		return result;
	}

	public static String getFutureMonth() {

		Calendar cal = GregorianCalendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy");

		Date currentMonth = new Date();
		cal.setTime(currentMonth);

		// current month
		// String currentMonthAsSting = df.format(cal.getTime());

		// Add next month
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
		String nextMonthAsString = df.format(cal.getTime());
		nextMonthAsString = nextMonthAsString.substring(0, nextMonthAsString.indexOf(" "));

		return nextMonthAsString;
	}

	public static String getCurrentMonth() {

		Calendar cal = GregorianCalendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("MMMM yyyy");

		Date currentMonth = new Date();
		cal.setTime(currentMonth);

		// current month
		String currentMonthAsSting = df.format(cal.getTime());
		currentMonthAsSting = currentMonthAsSting.substring(0, currentMonthAsSting.indexOf(" "));

		return currentMonthAsSting;
	}

	public static String getCurrentYear() {

		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		String yearInString = String.valueOf(year);
		return yearInString;
	}

	public static String getFutureDateYear(int years) {


		DateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, +years);
		String result = dateFormat.format(cal.getTime());

		return result;
	}

	public static String monFormat(String dateValue) {
		DateFormat dateFormat = new SimpleDateFormat("MMM d, YYYY");
		Date date = new Date(dateValue);
		String format = dateFormat.format(date);
		return format;
	}
	
	
	public static String convertRGBAToHexa(String rgb) {
		String[] numbers = rgb.replace("rgba(", "").replace(")", "").split(",");
		int number1 = Integer.parseInt(numbers[0]);
		numbers[1] = numbers[1].trim();
		int number2 = Integer.parseInt(numbers[1]);
		numbers[2] = numbers[2].trim();
		int number3 = Integer.parseInt(numbers[2]);
		numbers[3] = numbers[3].trim();
		String hexcColorCode = String.format("#%02x%02x%02x", number1, number2, number3).toString();
		return hexcColorCode;
	}

	public static String getColor(WebElement element, String cssAtributeVal) {
		String actualColor = null;
		boolean flag = false;
		try {
			flag = element.isDisplayed();
			logger.info("Header/Footer Color Element Dipalyed:: " + flag);
			Assert.assertTrue(flag, "Expected color element is not displayed");
			txt = element.getCssValue(cssAtributeVal);
			logger.info("Header/Footer Color Element Value:: " + txt);
			actualColor = convertRGBAToHexa(txt);
		} catch (Exception e) {
			logger.info("EXPECTED COLOR IS NOT MATCHING WITH ACTUAL COLOR" + e.getLocalizedMessage());
		}

		return actualColor;
	}
	
	/**
	 * This Method is sued to convert to milliseconds to MM:SS (Minutes:Seconds)
	 * format
	 * 
	 * @param ms
	 * @return MM:SS
	 */
	public static String millisecondsToMS(long ms) {

		String hms = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(ms) % TimeUnit.HOURS.toMinutes(1), TimeUnit.MILLISECONDS.toSeconds(ms) % TimeUnit.MINUTES.toSeconds(1));
		return hms;
	}

	/**
	 * This Method is sued to convert to milliseconds to HH:MM:SS
	 * (Hours:Minutes:Seconds) format
	 * 
	 * @param ms
	 * @return HH:MM:SS
	 */
	public static String millisecondsToHMS(long ms) {
		String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(ms), TimeUnit.MILLISECONDS.toMinutes(ms) % TimeUnit.HOURS.toMinutes(1),
				TimeUnit.MILLISECONDS.toSeconds(ms) % TimeUnit.MINUTES.toSeconds(1));

		return hms;
	}

	/**
	 * This method is used to GET response for the provided URL
	 * 
	 * @param url
	 * @return result
	 * @throws Exception
	 */
	public static String getHttpResponse(String url) throws Exception {

		String result = "";
		try {
			URL connect = new URL(url);
			URLConnection yc = connect.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
				result = inputLine;
			in.close();
		} catch (Exception e) {
			logger.error("GET HTTP Response Failed!! " + e.getMessage());
		}
		return result;

	}

	public static String getHTTPResponseByTrustCertificates(String url) throws Exception {

		doTrustToCertificates();

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		int responseCode = con.getResponseCode();

		logger.info("\nSending 'GET' request to URL : " + url);
		logger.info("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();

	}

	/**
	 * This method is sued to get the http Response code for the service
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static int getHttpResponseCode(String url) throws Exception {

		doTrustToCertificates();

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		int responseCode = con.getResponseCode();

		logger.info("\nSending 'GET' request to URL : " + url);
		logger.info("Response Code : " + responseCode);

		return responseCode;
	}

	

	/**
	 * This method is sued to handle all the trust certificates while making
	 * connection with the web services
	 * 
	 * @throws Exception
	 */
	public static void doTrustToCertificates() throws Exception {
		Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
				return;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
				return;
			}
		} };

		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		HostnameVerifier hv = new HostnameVerifier() {

			public boolean verify(String urlHostName, SSLSession session) {
				if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
					System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
				}
				return true;
			}

		};

		HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}
	
	
	/**
	 * This method is used to get the Browser Name
	 * 
	 * @return
	 */
	public static String getBrowserName() {

		RemoteWebDriver webDriver = getWebDriver();
		return webDriver.getCapabilities().getBrowserName();
	}

	/**
	 * This method is used to get the Browser Version
	 * 
	 * @return
	 */
	public static String getBrowserVersion() {

		RemoteWebDriver webDriver = getWebDriver();
		return webDriver.getCapabilities().getVersion();
	}

	/**
	 * This method is used to get the Browser Version
	 * 
	 * @return
	 */
	public static String getPlatform() {

		RemoteWebDriver webDriver = getWebDriver();
		return webDriver.getCapabilities().getPlatform().name();
	}

	/**
	 * This method is used to get the Browser Version using JS
	 * 
	 * @return
	 */
	public static String getBrowserVersionByJS() {

		RemoteWebDriver webDriver =getWebDriver();
		JavascriptExecutor js = webDriver;
		String useragent = (String) js.executeScript("return navigator.userAgent;");

		String[] strArray = useragent.split(" ");
		String version = "";

		for (String s : strArray) {

			if (s.contains("Version")) {
				version = s.split("/")[1];
				break;
			}
		}

		return version;
	}


	/**
	 * This method is used to get the Browser Name using JS
	 * 
	 * @return
	 */
	public static String getBrowserNameByJS() {

		RemoteWebDriver webDriver = getWebDriver();
		JavascriptExecutor js = webDriver;
		String useragent = (String) js.executeScript("return navigator.userAgent;");

		String[] strArray = useragent.split(" ");
		String browser = strArray[strArray.length - 1].split("/")[0];

		return browser;
	}
	
	public static String getApplicationId(String AppURL) {
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(AppURL);
		String applicationId = null;
		while (m.find()) {
			applicationId = m.group();
		}
		return applicationId;
	}

	public static void openNewWindow(String URL) throws Exception {
		WebDriver driver = getWebDriver();
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.open()");
		switchToNewWindow();
		driver.navigate().to(URL);
	}
	
	public static void switchToNewWindow() throws Exception {
		RemoteWebDriver webdriver = getWebDriver();
		for (String winHandle : webdriver.getWindowHandles()) {
			webdriver.switchTo().window(winHandle);
		}
	}

	/**
	 * This method takes the screenshot of the WebPage and saves it in the
	 * output directory.
	 * 
	 * @param driver
	 * @param outputDir
	 * @param methodName
	 * @throws Exception
	 */
	public static void takeScreenShot(WebDriver driver, String outputDir, String methodName) {

		try {
			String fileNameWithPath = null;
			String fileName = null;

			if (driver != null) {

				// Augment the driver only if the driver instance
				// is of type RemoteWebDriver(When tests are running in Grid)
				// if (driver.getClass() == RemoteWebDriver.class) {
				// driver = new Augmenter().augment(driver);
				// }

				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

				fileName = methodName + ".jpeg";
				fileNameWithPath = outputDir + "/" + fileName;

				File scrShot = new File(fileNameWithPath);

				/*
				 * if( null != scrFile && scrShot.exists() ){ scrShot.delete();
				 * }
				 */
				FileUtils.moveFile(scrFile, scrShot);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static String getCurrentTime() {

		Calendar cal = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH) + 1;
		StringBuffer dateTime = new StringBuffer();
		dateTime.append(Integer.toString(month));
		dateTime.append(Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));
		dateTime.append(Integer.toString(cal.get(Calendar.YEAR)));
		dateTime.append('_');
		dateTime.append(Integer.toString(cal.get(Calendar.HOUR_OF_DAY)));
		dateTime.append(Integer.toString(cal.get(Calendar.MINUTE)));
		return dateTime.toString();
	}

	public static String getCurrentDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String formattedDate = dateFormat.format(new Date()).toString();
		return formattedDate;
	}

	public static String getCurrentTimeWith24HRS() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMMMMMMM dd, yyyy h:mm aa");
		String formattedDate = dateFormat.format(new Date()).toString();
		return formattedDate;
	}

	public static String getDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
		String formattedDate = dateFormat.format(new Date()).toString();
		return formattedDate;
	}

	public static String convertTimeWith24HRSFormat(String timeFormate) {

		SimpleDateFormat dateFormat = new SimpleDateFormat(timeFormate);
		String formattedDate = dateFormat.format(new Date()).toString();
		return formattedDate;
	}

	public static String retrieveUserName(String userName) {
		int index = userName.indexOf(", ") + 2;
		String name = userName.substring(index, userName.length() - 1);

		return name;
	}
	
	
	public static void selectItemByIndex(By by, int index) {
		WebElement element = getWebDriver().findElement(by);
		Select select = new Select(element);
		int size = select.getOptions().size();
		logger.info("Total values ::" + size);
		if (size >= index) {
			select.selectByIndex(index);
		}
		Assert.assertTrue(element.isDisplayed(), "Element is displayed?");
	}

	public static void selectItemByValue(By by, String value) {
		WebElement element = getWebDriver().findElement(by);
		Select select = new Select(element);
		select.selectByValue(value);
	}

	public static void selectItemByVisibleText(By by, String value) {
		WebElement element = getWebDriver().findElement(by);
		Select select = new Select(element);
		select.deselectByVisibleText(value);
	}
}
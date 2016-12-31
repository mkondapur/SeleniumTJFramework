package com.tr.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;


public class BaseTestObject {

	protected static WebDriver driver;
	public static String propertyFilePath = System.getProperty("user.dir")+"\\src\\test\\resources\\testdata\\testData.properties";
	public static String chromeDriverPath = System.getProperty("user.dir")+"\\src\\test\\resources\\drivers\\chromedriver.exe";
	
	FileInputStream fileInput =null;
	//Properties ObjProperty = getPropertyContents();
	public Properties ObjProperty=getPropertyContents();
	
	public String browser = ObjProperty.getProperty("browser");

	public String url = ObjProperty.getProperty("url");

	
	/**
	 
     * This function will execute before each Test tag in testng.xml
 
     * @param browser
 
     * @throws Exception
 
     */
	private static final Properties prop = new Properties();

	private static void loadPropertiesFile() 
	{
		InputStream input = null;

		try
		{
			input = new FileInputStream(propertyFilePath);
			// load a properties file
			prop.load(input);
		} 
		catch (IOException ex) 
		{
			ex.printStackTrace();
		} 
		finally 
		{
			if (input != null) 
			{
				try
				{
					input.close();
				} 
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}

	public static Properties getPropertyContents() {
		loadPropertiesFile();
		return prop;
	}
	
	
	@Parameters({"browserType"})
	@BeforeSuite(alwaysRun = true)
	
    public void setup(String browser) throws Exception
	{
        if(browser.equalsIgnoreCase("FF"))
        {
            driver = new FirefoxDriver();
        }
        else if(browser.equalsIgnoreCase("GC"))
        {
        	ChromeOptions options = new ChromeOptions();
        	options.addArguments("chrome.switches","--disable-extensions");
            System.setProperty("webdriver.chrome.driver",chromeDriverPath);
            driver = new ChromeDriver(options);
        }
        else if(browser.equalsIgnoreCase("IE")){
            System.setProperty("webdriver.ie.driver","C:\\IEdriver.exe");
            driver = new InternetExplorerDriver();
        }
        else
        {
        	throw new Exception("Browser is not correct");
        }
       
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); 
        driver.manage().window().maximize();
        
}
	
	@AfterSuite(alwaysRun = true)
	public void tearDown() throws Exception{
		//ReportSending.execute("testaynaxdemo@gmail.com","testaynaxdemo@gmail.com");
		driver.quit();
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
}

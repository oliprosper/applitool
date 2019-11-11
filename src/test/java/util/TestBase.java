package util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class TestBase {
	public static WebDriverWait wait;
	public static ExtentReports reports;
	public static ExtentHtmlReporter htmlReporter;
	private static ThreadLocal<ExtentTest> parentTest = new ThreadLocal<ExtentTest>();
	public static ThreadLocal<ExtentTest> testInfo = new ThreadLocal<ExtentTest>();
	private static OptionsManager optionsManager = new OptionsManager();
	public static String myURL = System.getProperty("instance-url", "");

	@BeforeSuite
	@Parameters({ "groupReport", "server", "device" })
	public void setUp(String groupReport, String server, String device) throws Exception {

		htmlReporter = new ExtentHtmlReporter(new File(System.getProperty("user.dir") + groupReport));
		reports = new ExtentReports();
		reports.setSystemInfo("STAGING", myURL);
		if (device.equalsIgnoreCase("desktop")) {
			reports.setSystemInfo("Test Device", "Laptop");
		} else {
			reports.setSystemInfo("Test Device", device);
		}
		reports.attachReporter(htmlReporter);

	}

	public static ThreadLocal<WebDriver> driver = new ThreadLocal<WebDriver>();

	@Parameters({ "myBrowser", "device" })
	@BeforeClass
	public void beforeClass(String myBrowser, String device) throws Exception {
		ExtentTest parent = reports.createTest(getClass().getName());
		parentTest.set(parent);

		if (device.equalsIgnoreCase("desktop")) {
			if (myBrowser.equalsIgnoreCase("chrome")) {
				File classpathRoot = new File(System.getProperty("user.dir"));
				File chromeDriver = new File(classpathRoot, "chromedriver.exe");
				System.setProperty("webdriver.chrome.driver", chromeDriver.getAbsolutePath());
				driver.set(new ChromeDriver(optionsManager.getChromeOptions()));
				getDriver().manage().window().maximize();
				getDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				getDriver().get(myURL);

			} else if (myBrowser.equalsIgnoreCase("firefox")) {

				File classpathRoot = new File(System.getProperty("user.dir"));
				File firefoxDriver = new File(classpathRoot, "geckodriver.exe");
				System.setProperty("webdriver.gecko.driver", firefoxDriver.getAbsolutePath());
				driver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
				getDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				getDriver().get(myURL);
			}
		} else {
			if (myBrowser.equalsIgnoreCase("chrome")) {
				File classpathRoot = new File(System.getProperty("user.dir"));
				File chromeDriver = new File(classpathRoot, "chromedriver.exe");
				System.setProperty("webdriver.chrome.driver", chromeDriver.getAbsolutePath());
				driver.set(new ChromeDriver(optionsManager.getChromeEmulatorOptions(device)));
				getDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				getDriver().get(myURL);

			} else if (myBrowser.equalsIgnoreCase("firefox")) {

				File classpathRoot = new File(System.getProperty("user.dir"));
				File firefoxDriver = new File(classpathRoot, "geckodriver.exe");
				System.setProperty("webdriver.gecko.driver", firefoxDriver.getAbsolutePath());
				driver.set(new FirefoxDriver(optionsManager.getFirefoxOptions()));
				getDriver().manage().window().setSize(TestUtils.setDimension(device));
				getDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
				getDriver().get(myURL);
			}
		}
	}

	@AfterClass
	public void afterClass() {
		getDriver().close();
	}

	public static WebDriver getDriver() {

		return driver.get();
	}

	@BeforeMethod(description = "fetch test cases name")
	public void register(Method method) {

		ExtentTest child = parentTest.get().createNode(method.getName());
		testInfo.set(child);
		testInfo.get().getModel().setDescription(TestUtils.CheckBrowser());

	}

	@AfterMethod(description = "to display the result after each test method")
	public void captureStatus(ITestResult result) throws IOException {

		for (String group : result.getMethod().getGroups())
			testInfo.get().assignCategory(group);

		if (result.getStatus() == ITestResult.FAILURE) {
			testInfo.get().fail(result.getThrowable());
			testInfo.get().info(TestUtils.addScreenshot());

		} else if (result.getStatus() == ITestResult.SKIP)
			testInfo.get().skip(result.getThrowable());
		else
			testInfo.get().pass(result.getName() + " Test passed");
	}

	@Parameters({ "toMails", "groupReport" })
	@AfterSuite(description = "clean up report after test suite")
	public void cleanup(String toMails, String groupReport) {

		reports.flush();
		SendMail.ComposeGmail(toMails, groupReport);

	}

}

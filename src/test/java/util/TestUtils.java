package util;

import com.aventstack.extentreports.Status;

import enums.TargetTypeEnum;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

public class TestUtils extends TestBase {

	public static void assertSearchText(String type, String element, String value) {

		StringBuffer verificationErrors = new StringBuffer();
		TargetTypeEnum targetTypeEnum = TargetTypeEnum.valueOf(type);
		String ttype = null;

		switch (targetTypeEnum) {
		case ID:
			ttype = getDriver().findElement(By.id(element)).getText();
			break;
		case NAME:
			ttype = getDriver().findElement(By.name(element)).getText();
			break;
		case CSSSELECTOR:
			ttype = getDriver().findElement(By.cssSelector(element)).getText();
			break;

		case XPATH:
			ttype = getDriver().findElement(By.xpath(element)).getText();
			break;

		default:
			ttype = getDriver().findElement(By.id(element)).getText();
			break;
		}

		try {
			Assert.assertEquals(ttype, value);
			testInfo.get().log(Status.INFO, value + " found");
		} catch (Error e) {
			verificationErrors.append(e.toString());
			String verificationErrorString = verificationErrors.toString();
			testInfo.get().error(value + " not found");
			testInfo.get().error(verificationErrorString);
		}
	}

	public static String addScreenshot() {

		TakesScreenshot ts = (TakesScreenshot) getDriver();
		File scrFile = ts.getScreenshotAs(OutputType.FILE);

		String encodedBase64 = null;
		FileInputStream fileInputStreamReader = null;
		try {
			fileInputStreamReader = new FileInputStream(scrFile);
			byte[] bytes = new byte[(int) scrFile.length()];
			fileInputStreamReader.read(bytes);
			encodedBase64 = new String(Base64.encodeBase64(bytes));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "data:image/png;base64," + encodedBase64;
	}

	public static String generatePhoneNumber() {

		int num1, num2, num3;
		int set2, set3;
		Random generator = new Random();
		num1 = generator.nextInt(7) + 1;
		num2 = generator.nextInt(9);
		num3 = generator.nextInt(8);
		set2 = generator.nextInt(9993) + 100;
		set3 = generator.nextInt(8999) + 1000;
		String phone = num1 + "" + num2 + "" + num3 + "" + set2 + "" + set3;
		return phone;
	}

	public static String CheckBrowser() {
		// Get Browser name and version.
		Capabilities caps = ((RemoteWebDriver) getDriver()).getCapabilities();
		String browserName = caps.getBrowserName();
		String browserVersion = caps.getVersion();

		// return browser name and version.
		String os = browserName + " " + browserVersion;

		return os;
	}

	/**
	 * @return number
	 * @description to generate a 11 digit number.
	 */
	public static String generatePhoneNumber1() {

		long y = (long) (Math.random() * 100000 + 0222220000L);
		String Surfix = "080";
		String num = Long.toString(y);
		String number = Surfix + num;
		return number;

	}

	public static String generatePhoneNumber2() {

		long y = (long) (Math.random() * 100000 + 0224440000L);
		String Surfix = "080";
		String num = Long.toString(y);
		String number = Surfix + num;
		return number;

	}

	@SuppressWarnings("unused")
	public static Dimension setDimension(String device) {

		Dimension dimension = null;

		if (device.equalsIgnoreCase("iPhone 8")) {
			dimension = new Dimension(375, 667);

		} else if (device.equalsIgnoreCase("iPhone X")) {
			dimension = new Dimension(375, 812);

		} else if (device.equalsIgnoreCase("iPhone 8 Plus")) {
			dimension = new Dimension(414, 736);

		} else if (device.equalsIgnoreCase("iPad Mini")) {
			dimension = new Dimension(768, 1024);

		} else if (device.equalsIgnoreCase("Galaxy S5")) {
			dimension = new Dimension(360, 640);

		} else if (device.equalsIgnoreCase("Galaxy S9")) {
			dimension = new Dimension(360, 740);

		} else if (device.equalsIgnoreCase("Nexus 5X")) {
			dimension = new Dimension(412, 732);

		} else if (device.equalsIgnoreCase("Galaxy Tab 10")) {
			dimension = new Dimension(800, 1280);

		} else {

			System.out.println(device + " not found");
		}
		return dimension;

	}

	public static void clickElement(String type, String element) {
		JavascriptExecutor ex = (JavascriptExecutor) getDriver();
		WebElement clickThis = null;
		TargetTypeEnum targetTypeEnum = TargetTypeEnum.valueOf(type);
		switch (targetTypeEnum) {
		case ID:
			clickThis = getDriver().findElement(By.id(element));
			break;
		case NAME:
			clickThis = getDriver().findElement(By.name(element));
			break;
		case CSSSELECTOR:
			clickThis = getDriver().findElement(By.cssSelector(element));
			break;
		case XPATH:
			clickThis = getDriver().findElement(By.xpath(element));
			break;
		default:
			clickThis = getDriver().findElement(By.id(element));
		}
		ex.executeScript("arguments[0].click()", clickThis);
	}

}

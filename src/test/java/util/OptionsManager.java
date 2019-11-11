package util;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

public class OptionsManager {

	//Get Chrome Options
    public ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-popup-blocking");
        return options;
    }
    
  //Get Chrome Options
    public ChromeOptions getChromeEmulatorOptions(String device) {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--allow-running-insecure-content");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--disable-popup-blocking");
        Map<String, String> mobileEmulation = new HashMap<>();
		mobileEmulation.put("deviceName", device);
		options.setExperimentalOption("mobileEmulation", mobileEmulation);
        return options;
    }
    
 
    //Get Firefox Options
    public FirefoxOptions getFirefoxOptions () {
        FirefoxOptions options = new FirefoxOptions();
        FirefoxProfile profile = new FirefoxProfile();
        profile.setAcceptUntrustedCertificates(true);
        profile.setAssumeUntrustedCertificateIssuer(false);
        profile.setPreference("network.proxy.type", 0);
        profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "application/csv, text/csv, image/jpeg, data:image/jpg, image/jpg, data:image/png, image/png, application/zip, application/x-zip, application/x-zip-compressed, application/download, application/octet-stream");
        profile.setPreference("browser.download.manager.showWhenStarting", false);
        profile.setPreference("browser.download.manager.focusWhenStarting", false);
        profile.setPreference("browser.download.useDownloadDir", true);
        profile.setPreference("browser.helperApps.alwaysAsk.force", false);
        profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
        profile.setPreference("browser.download.manager.closeWhenDone", true);
        profile.setPreference("browser.download.manager.showAlertOnComplete", false);
        profile.setPreference("browser.download.manager.useWindow", false);
        profile.setPreference("browser.download.panel.shown",false);
        profile.setPreference( "pdfjs.disabled", true );
        options.setCapability(FirefoxDriver.PROFILE, profile);
        return options;
    }
    
}

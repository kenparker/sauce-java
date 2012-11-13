package com.saucelabs.junit;

import com.saucelabs.common.SauceOnDemandAuthentication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

/**
 * Demonstrates how to write a JUnit test that runs tests against Sauce OnDemand in parallel.
 *
 * @author Ross Rowe
 */
@RunWith(Parallelized.class)
public class WebDriverParallelTest {

    public SauceOnDemandAuthentication authentication = new SauceOnDemandAuthentication();

    private String browser;
    private String os;
    private String version;

    public WebDriverParallelTest(String os, String version, String browser) {
        super();
        this.os = os;
        this.version = version;
        this.browser = browser;
    }

    @Parameterized.Parameters
    public static LinkedList browsersStrings() throws Exception {
        LinkedList browsers = new LinkedList();
        browsers.add(new String[]{Platform.MAC.toString(), "5.0", "iPhone"});
        return browsers;
    }

    private WebDriver driver;

    @Before
    public void setUp() throws Exception {

        DesiredCapabilities capabillities = new DesiredCapabilities();
        capabillities.setCapability(CapabilityType.BROWSER_NAME, browser);
        capabillities.setCapability(CapabilityType.VERSION, version);
        capabillities.setCapability(CapabilityType.PLATFORM, Platform.valueOf(os));
        this.driver = new RemoteWebDriver(
                new URL("http://" + authentication.getUsername() + ":" + authentication.getAccessKey() + "@ondemand.saucelabs.com:80/wd/hub"),
                capabillities);
    }

    @Test
    public void webDriver() throws Exception {
        printSessionId("webDriver");
        driver.get("http://www.amazon.com/");
        assertEquals("Amazon.com: Online Shopping for Electronics, Apparel, Computers, Books, DVDs & more", driver.getTitle());
    }

    private void printSessionId(String testName) {

        String message = String.format("SauceOnDemandSessionID=%1$s job-name=%2$s", (((RemoteWebDriver) driver).getSessionId()).toString(), testName);
        System.out.println(message);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }
}

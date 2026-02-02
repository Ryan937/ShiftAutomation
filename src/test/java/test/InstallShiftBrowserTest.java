package test;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

public class InstallShiftBrowserTest extends BaseTest {

    private final String DOWNLOAD_BUTTON = ".portable-text .link-button";
    private final String SHIFT_BROWSER_QUICK_SETTINGS = "Quick Settings";
    private final String SHIFT_BROWSER_ADVANCED_SETTINGS = "Advanced Settings";
    private final String SHIFT_BROWSER_VERSION = "//*[contains(@Name, 'Version')]";

    @Test
    public void downloadAndInstallShiftBrowserTest() throws Exception {

        initChromeDriver();

        // 1. Go to Shift website and download Shift installer file
        navigateToShiftHomepage();
        clickShiftBrowserDownloadButton();

        File shiftInstallerFile = waitForDownload("Shift");

        stopChromeDriver();

        // 2. Verify file version (TO-DO)

        // 3. Install Shift browser
        String installPath = System.getProperty("user.home") + SHIFT_BROWSER_FILE_PATH;
        launchExe(shiftInstallerFile.getAbsolutePath());
        waitForSeconds(10); // waiting for installer to finish TODO: make it an explicit wait

        // Initialize Windows driver with Shift browser
        initWindowsDriver(installPath);

        // 4. Navigate to About page and verify the version
        goToAboutShiftPage();
        String browserVersion = getShiftBrowserVersion();
        Assert.assertNotNull(browserVersion, "Could not find browser version text");
        Assert.assertTrue(browserVersion.contains("142.0.0.3349 (Official Build) (64-bit)"),
                "Browser version is incorrect. Expecting 142.0.0.3349 (Official Build) (64-bit) but got " + browserVersion);
    }

    private void navigateToShiftHomepage() {
        chromeDriver.get("https://shift.com");
    }

    private void clickShiftBrowserDownloadButton() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(DOWNLOAD_BUTTON))).click();
    }

    private void goToAboutShiftPage() {
        wait.until(ExpectedConditions.elementToBeClickable(By.name(SHIFT_BROWSER_QUICK_SETTINGS))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.name(SHIFT_BROWSER_ADVANCED_SETTINGS))).click();
    }

    private String getShiftBrowserVersion() {
        WebElement browserVersionElement = windowsDriver.findElement(By.xpath(SHIFT_BROWSER_VERSION));
        wait.until(ExpectedConditions.visibilityOf(browserVersionElement));

        return browserVersionElement.getText();
    }
}

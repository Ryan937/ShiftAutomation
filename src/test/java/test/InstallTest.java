package test;

import base.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Objects;

public class InstallTest extends BaseTest {

    private final String DOWNLOAD_BUTTON = ".portable-text .link-button";
    private final String SHIFT_BROWSER_QUICK_SETTINGS = "Quick Settings";
    private final String SHIFT_BROWSER_ADVANCED_SETTINGS = "Advanced Settings";
    private final String SHIFT_BROWSER_VERSION = "//*[contains(@Name, 'Version')]";

    @Test
    public void downloadAndInstallShiftBrowserTest() throws Exception {

        // 1. Go to Shift website and download Shift installer file
        initChrome();
        chromeDriver.get("https://shift.com");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(DOWNLOAD_BUTTON))).click();

        File shiftInstallerFile = waitForDownload("Shift");

        chromeDriver.quit();
        chromeDriver = null;

        // 2. Verify file version (TO-DO)

        // 3. Install Shift browser
        String installPath = System.getProperty("user.home") + "\\AppData\\Local\\Shift\\chromium\\shift.exe";
        launchExe(shiftInstallerFile.getAbsolutePath());
        waitForSeconds(10);

        // 4. Start Shift browser and check the version
        initWindowsDriver(installPath);
        String browserVersion = goToAboutShiftPage();
        Assert.assertNotNull(browserVersion, "Could not find browser version text");
        Assert.assertTrue(browserVersion.contains("142.0.0.3349 (Official Build) (64-bit)"),
                "Browser version is incorrect. Expecting 142.0.0.3349 (Official Build) (64-bit) but got " + browserVersion);
    }

    private File waitForDownload(String fileName) throws InterruptedException {
        File dir = new File(System.getProperty("user.home") + "/Downloads");

        for (int i = 0; i < 30; i++) {
            File[] matches = dir.listFiles((d, name) -> name.startsWith(fileName) && name.endsWith(".exe"));

            if (matches != null && matches.length > 0) {
                boolean stillDownloading = false;

                for (File f : Objects.requireNonNull(dir.listFiles())) {
                    if (f.getName().endsWith(".crdownload")) {
                        stillDownloading = true;
                        break;
                    }
                }

                if (!stillDownloading)
                    return matches[0];
            }
            Thread.sleep(1000);
        }

        throw new RuntimeException("Download failed or timed out");
    }

    private WebElement findInstaller(String shiftInstallerFileName) throws InterruptedException {
        for (int i = 0; i < 30; i++) {
            try {
                return windowsDriver.findElement(By.name(shiftInstallerFileName));
            } catch(Exception e) {
                Thread.sleep(1000);
            }
        }

        return null;
    }

    private String goToAboutShiftPage() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.name(SHIFT_BROWSER_QUICK_SETTINGS))).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.name(SHIFT_BROWSER_ADVANCED_SETTINGS))).click();

            WebElement browserVersionElement = windowsDriver.findElement(AppiumBy.xpath(SHIFT_BROWSER_VERSION));
            wait.until(ExpectedConditions.visibilityOf(browserVersionElement));

            return browserVersionElement.getText();
        } catch (Exception e) {
            System.out.println("Could not navigate to verify version.");
        }

        return null;
    }
}

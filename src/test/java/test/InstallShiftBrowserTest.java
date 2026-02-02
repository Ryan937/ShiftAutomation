package test;

import base.BaseTest;
import browsers.ChromeBrowser;
import browsers.WindowsController;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;

public class InstallShiftBrowserTest extends BaseTest {

    @Test
    public void downloadAndInstallShiftBrowserTest() throws Exception {

        chromeBrowser = new ChromeBrowser();

        // 1. Go to Shift website and download Shift installer file
        chromeBrowser.navigateToShiftHomepage();
        chromeBrowser.clickShiftBrowserDownloadButton();

        File shiftInstallerFile = waitForDownload("Shift");

        stopChromeDriver();

        // 2. Verify file version (TO-DO)

        // 3. Install Shift browser
        String installPath = System.getProperty("user.home") + SHIFT_BROWSER_FILE_PATH;
        launchExe(shiftInstallerFile.getAbsolutePath());
        waitForSeconds(10); // waiting for installer to finish TODO: make it an explicit wait

        // Initialize Windows driver with Shift browser
        windowsController = new WindowsController(installPath);

        // 4. Navigate to About page and verify the version
        windowsController.goToAboutShiftPage();
        String browserVersion = windowsController.getShiftBrowserVersion();
        Assert.assertNotNull(browserVersion, "Could not find browser version text");
        Assert.assertTrue(browserVersion.contains("142.0.0.3349 (Official Build) (64-bit)"),
                "Browser version is incorrect. Expecting 142.0.0.3349 (Official Build) (64-bit) but got " +
                        browserVersion);
    }
}

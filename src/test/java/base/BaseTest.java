package base;

import browsers.ChromeBrowser;
import browsers.WindowsController;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;


import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class BaseTest {

    protected ChromeBrowser chromeBrowser;
    protected WindowsController windowsController;

    protected WebDriverWait wait;

    protected final String SHIFT_BROWSER_FILE_PATH = "\\AppData\\Local\\Shift\\chromium\\shift.exe";

    protected void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ignored) {

        }
    }

    protected void launchExe(String folderPath) {
        try {
            java.io.File file = new java.io.File(folderPath);

            if (!file.exists()) {
                throw new RuntimeException("File not found");
            }
            new ProcessBuilder(file.getAbsolutePath()).start();

        } catch (java.io.IOException e) {
            throw new RuntimeException("Failed to start file");
        }
    }

    protected File waitForDownload(String fileName) throws InterruptedException {
        File dir = new File(System.getProperty("user.home") + "/Downloads");

        for (int i = 0; i < 30; i++) {
            File[] matches = dir.listFiles((file, name) -> name.startsWith(fileName) && name.endsWith(".exe"));

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

    protected void stopChromeDriver() {
        chromeBrowser.getDriver().quit();
    }

    @AfterMethod(alwaysRun = true)
    public void teardown() throws IOException {
        if (chromeBrowser != null) chromeBrowser.close();
        if (windowsController != null) windowsController.close();
        Assert.assertNotNull(windowsController);
        windowsController.stopAppiumDriver();
    }
}
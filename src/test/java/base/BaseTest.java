package base;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.windows.WindowsDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.Objects;

public class BaseTest {
    protected WebDriver chromeDriver;
    protected WindowsDriver windowsDriver;
    protected WebDriverWait wait;

    protected int TIME_OUT_IN_SECONDS = 10;

    protected final String SHIFT_BROWSER_FILE_PATH = "\\AppData\\Local\\Shift\\chromium\\shift.exe";

    protected void initChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        chromeDriver = new ChromeDriver(options);

        wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(TIME_OUT_IN_SECONDS));
    }

    protected void initWindowsDriver(String appId) throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("app", appId);
        capabilities.setCapability("platformName", "Windows");
        capabilities.setCapability("deviceName", "WindowsPC");

        windowsDriver = startAppiumService(capabilities);
        windowsDriver.manage().window().maximize();

        wait = new WebDriverWait(windowsDriver, Duration.ofSeconds(TIME_OUT_IN_SECONDS));
    }

    private WindowsDriver startAppiumService(DesiredCapabilities desiredCapabilities) throws IOException {
        killProcess("node");
        AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder()
                .usingAnyFreePort()
                .withTimeout(Duration.ofSeconds(TIME_OUT_IN_SECONDS));

        AppiumDriverLocalService appiumDriverLocalService = AppiumDriverLocalService.buildService(serviceBuilder);
        appiumDriverLocalService.start();

        URL serverUrl = appiumDriverLocalService.getUrl();
        return new WindowsDriver(serverUrl, desiredCapabilities);
    }


    private void killProcess(String process) throws IOException {
        Runtime runtime = Runtime.getRuntime();
        runtime.exec("taskkill /F /IM " + process + ".exe");
    }

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
        chromeDriver.quit();
        chromeDriver = null;
    }

    private void stopAppiumDriver() throws IOException {
        String appiumService = "node";
        killProcess(appiumService);
    }

    @AfterMethod(alwaysRun = true)
    public void teardown() throws IOException {
        if (chromeDriver != null) chromeDriver.quit();
        if (windowsDriver != null) windowsDriver.quit();
        stopAppiumDriver();
    }
}
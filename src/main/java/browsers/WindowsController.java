package browsers;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

public class WindowsController {

    private WebDriver windowsDriver;
    private final WebDriverWait wait;

    protected int TIME_OUT_IN_SECONDS = 10;

    private final String SHIFT_BROWSER_QUICK_SETTINGS = "Quick Settings";
    private final String SHIFT_BROWSER_ADVANCED_SETTINGS = "Advanced Settings";
    private final String SHIFT_BROWSER_VERSION = "//*[contains(@Name, 'Version')]";
    private final String SHIFT_BROWSER_SEARCH_BAR = "//Edit[@AutomationId='omnibox-textbox']";
    private final String SHIFT_BROWSER_BOOKMARK_BUTTON = "//Button[@Name='Add Bookmark']";
    private final String SHIFT_BROWSER_BOOKMARK_PANEL_BOOKMARK_FOLDER = "//TreeItem[@Name='Bookmarks bar']";
    private final String SHIFT_BROWSER_ADD_BOOKMARK_PANEL_SAVE_BUTTON = "//Button[@Name='Save']";
    private final String SHIFT_BROWSER_SETTINGS_BOOKMARKS_BUTTON = "//Button[@Name='Bookmarks']";
    private final String SHIFT_BROWSER_SETTINGS_BOOKMARKS_PAGE_BOOKMARK_ITEMS = "//DataItem[starts-with(@AutomationId, 'bookmark_')]";

    public WindowsController(String appId) throws IOException {
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

    public void stopAppiumDriver() throws IOException {
        String appiumService = "node";
        killProcess(appiumService);
    }

    public WebDriver getDriver() {
        return windowsDriver;
    }

    public void goToAboutShiftPage() {
        wait.until(ExpectedConditions.elementToBeClickable(By.name(SHIFT_BROWSER_QUICK_SETTINGS))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.name(SHIFT_BROWSER_ADVANCED_SETTINGS))).click();
    }

    public String getShiftBrowserVersion() {
        WebElement browserVersionElement = windowsDriver.findElement(By.xpath(SHIFT_BROWSER_VERSION));
        wait.until(ExpectedConditions.visibilityOf(browserVersionElement));

        return browserVersionElement.getText();
    }

    public void navigateToRedbrickHomepage() {
        WebElement searchBarElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(SHIFT_BROWSER_SEARCH_BAR)));

        searchBarElement.click();
        searchBarElement.sendKeys("https://www.rdbrck.com/");
        searchBarElement.sendKeys(Keys.ENTER);
    }

    public void saveCurrentPageAsBookmark() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(SHIFT_BROWSER_BOOKMARK_BUTTON))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(SHIFT_BROWSER_BOOKMARK_PANEL_BOOKMARK_FOLDER))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(SHIFT_BROWSER_ADD_BOOKMARK_PANEL_SAVE_BUTTON))).click();
    }

    public void navigateToBookMarkPage() {
        wait.until(ExpectedConditions.elementToBeClickable(By.name(SHIFT_BROWSER_QUICK_SETTINGS))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(SHIFT_BROWSER_SETTINGS_BOOKMARKS_BUTTON))).click();
    }

    public List<WebElement> getBookmarkedItems() {
        wait.until(ExpectedConditions.visibilityOf(windowsDriver.findElement(By.xpath(SHIFT_BROWSER_SETTINGS_BOOKMARKS_PAGE_BOOKMARK_ITEMS))));

        return windowsDriver.findElements(By.xpath(SHIFT_BROWSER_SETTINGS_BOOKMARKS_PAGE_BOOKMARK_ITEMS));
    }

    public void close() {
        if (windowsDriver != null) {
            windowsDriver.quit();
            windowsDriver = null;
        }
    }
}

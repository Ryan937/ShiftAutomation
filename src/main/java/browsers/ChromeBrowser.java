package browsers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class ChromeBrowser {

    private WebDriver chromeDriver;
    private final WebDriverWait wait;

    protected int TIME_OUT_IN_SECONDS = 10;

    private final String CLICK_SHIFT_BROWSER_DOWNLOAD_BUTTON = ".portable-text .link-button";

    public ChromeBrowser() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        chromeDriver = new ChromeDriver(options);

        wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(TIME_OUT_IN_SECONDS));
    }

    public void navigateToShiftHomepage() {
        chromeDriver.get("https://shift.com");
    }

    public void clickShiftBrowserDownloadButton() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(CLICK_SHIFT_BROWSER_DOWNLOAD_BUTTON))).click();
    }

    public WebDriver getDriver() {
        return chromeDriver;
    }

    public void close() {
        if (chromeDriver != null) {
            chromeDriver.quit();
            chromeDriver = null;
        }
    }
}

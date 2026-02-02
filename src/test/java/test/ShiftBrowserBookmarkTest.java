package test;

import base.BaseTest;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.annotations.Test;

import java.util.List;

public class ShiftBrowserBookmarkTest extends BaseTest {

    private final String SHIFT_BROWSER_SEARCH_BAR = "//Edit[@AutomationId='omnibox-textbox']";
    private final String SHIFT_BROWSER_BOOKMARK_BUTTON = "//Button[@Name='Add Bookmark']";
    private final String SHIFT_BROWSER_BOOKMARK_PANEL_BOOKMARK_FOLDER = "//TreeItem[@Name='Bookmarks bar']";
    private final String SHIFT_BROWSER_ADD_BOOKMARK_PANEL_SAVE_BUTTON = "//Button[@Name='Save']";
    private final String SHIFT_BROWSER_QUICK_SETTINGS = "Quick Settings";
    private final String SHIFT_BROWSER_SETTINGS_BOOKMARKS_BUTTON = "//Button[@Name='Bookmarks']";
    private final String SHIFT_BROWSER_SETTINGS_BOOKMARKS_PAGE_BOOKMARK_ITEMS = "//DataItem[starts-with(@AutomationId, 'bookmark_')]";

    @Test
    public void bookmarkPageTest() throws Exception {
        String installPath = System.getProperty("user.home") + "\\AppData\\Local\\Shift\\chromium\\shift.exe";
        initWindowsDriver(installPath);

        // 1. Navigate to RedBrick homepage
        navigateToRedbrickHomepage();

        // 2. Save the page as a bookmark
        saveCurrentPageAsBookmark();

        // 3. Navigate to the bookmarks page and get all the bookmarked items
        navigateToBookMarkPage();
        List<WebElement> allBookmarks = getBookmarkedItems();

        // 4. Verify bookmark is saved
        Assert.assertFalse(allBookmarks.isEmpty(), "There are no saved bookmarks in bookmarks page");
        Assert.assertEquals(allBookmarks.get(0).getText(),
                "Redbrick | We power a global portfolio of companies",
                "Saved bookmarks does not contain the correct bookmark");
    }

    private void navigateToRedbrickHomepage() {
        WebElement searchBarElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(SHIFT_BROWSER_SEARCH_BAR)));

        searchBarElement.click();
        searchBarElement.sendKeys("https://www.rdbrck.com/");
        searchBarElement.sendKeys(Keys.ENTER);
    }

    private void saveCurrentPageAsBookmark() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(SHIFT_BROWSER_BOOKMARK_BUTTON))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(SHIFT_BROWSER_BOOKMARK_PANEL_BOOKMARK_FOLDER))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(SHIFT_BROWSER_ADD_BOOKMARK_PANEL_SAVE_BUTTON))).click();
    }

    private void navigateToBookMarkPage() {
        wait.until(ExpectedConditions.elementToBeClickable(By.name(SHIFT_BROWSER_QUICK_SETTINGS))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(SHIFT_BROWSER_SETTINGS_BOOKMARKS_BUTTON))).click();
    }

    private List<WebElement> getBookmarkedItems() {
        wait.until(ExpectedConditions.visibilityOf(windowsDriver.findElement(By.xpath(SHIFT_BROWSER_SETTINGS_BOOKMARKS_PAGE_BOOKMARK_ITEMS))));

        return windowsDriver.findElements(By.xpath(SHIFT_BROWSER_SETTINGS_BOOKMARKS_PAGE_BOOKMARK_ITEMS));
    }
}

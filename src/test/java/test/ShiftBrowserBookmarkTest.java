package test;

import base.BaseTest;
import browsers.WindowsController;
import org.testng.Assert;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;

public class ShiftBrowserBookmarkTest extends BaseTest {



    @Test
    public void bookmarkPageTest() throws Exception {
        String installPath = System.getProperty("user.home") + SHIFT_BROWSER_FILE_PATH;
        windowsController = new WindowsController(installPath);

        // 1. Navigate to RedBrick homepage
        windowsController.navigateToRedbrickHomepage();

        // 2. Save the page as a bookmark
        windowsController.saveCurrentPageAsBookmark();

        // 3. Navigate to the bookmarks page and get all the bookmarked items
        windowsController.navigateToBookMarkPage();
        List<WebElement> allBookmarks = windowsController.getBookmarkedItems();

        // 4. Verify bookmark is saved
        Assert.assertFalse(allBookmarks.isEmpty(), "There are no saved bookmarks in bookmarks page");
        Assert.assertEquals(allBookmarks.get(0).getText(),
                "Redbrick | We power a global portfolio of companies",
                "Saved bookmarks does not contain the correct bookmark");
    }
}

# Shift Browser Automation
Automated testing framework for Shift Browser using Selenium WebDriver and Appium WinAppDriver.

## Built With
- Java
- Maven
- Selenium
- Appium
- TestNG

## Getting Started
How to run the tests

### Prerequisites
- Java installed
- Maven installed
- Appium (requires Node.js)
  `npm install -g appium`
- WinAppDriver (download from https://github.com/microsoft/WinAppDriver/releases)
- Windows Developer Mode enabled

## Test Cases

### InstallShiftBrowserTest
- Downloads Shift Browser from https://shift.com/
- Runs the installer
- Verifies the installed browser version

### ShiftBrowserBookmarkTest
- Launches Shift Browser
- Navigates to https://www.rdbrck.com/
- Saves current page as a bookmark
- Verifies the bookmark was saved correctly in the bookmarks page

## Running the Tests

### Option 1: Using preferred IDE

1. Open the project as a Maven project
2. Wait for Maven dependencies to finish
3. Run tests by clicking the green icon next to any tests with @Test tag

### Option 2: Command line

1. Start a terminal in the project root
2. Run all or specific test by running the following commands:
- mvn test
- mvn -Dtest=InstallShiftBrowserTest test
- mvn -Dtest=ShiftBrowserBookmarkTest test

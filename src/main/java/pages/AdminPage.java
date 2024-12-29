package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AdminPage {
    private WebDriver driver;

    private By adminTab = By.xpath("(//A[@data-v-6475d26d=''])[1]");
    private By dynamicRecordsCount = By.xpath("(//BUTTON[@data-v-10d463b7=''])[3]/../..//SPAN[@data-v-7b563373='']");
    private By addButton = By.xpath("(//BUTTON[@data-v-10d463b7=''])[3]");
    private By UsernameField = By.xpath("(//INPUT[@data-v-1f99f73c=''])[2]");
    private By employeeNameField = By.xpath("//INPUT[@data-v-75e744cd='']");
    private By userRoleDropdown = By.xpath("(//DIV[@data-v-67d2aedf=''][text()='-- Select --'])[1]/..//I[@data-v-bddebfba='']");
    private By statusDropdown = By.xpath("(//DIV[@data-v-67d2aedf=''])[4]");
    private By passwordField = By.xpath("(//INPUT[@data-v-1f99f73c=''])[3]");
    private By confirmPasswordField = By.xpath("(//INPUT[@data-v-1f99f73c=''])[4]");
    private By saveButton = By.xpath("(//BUTTON[@data-v-10d463b7=''])[1]/following-sibling::BUTTON");
    private By SearchByUsernameField = By.xpath("(//INPUT[@data-v-1f99f73c=''])[2]");
    private By SearchButton = By.xpath("(//BUTTON[@data-v-10d463b7=''])[1]/following-sibling::BUTTON");
    private By DeleteButton = By.xpath("(//BUTTON[@data-v-f5c763eb=''])[5]/preceding-sibling::BUTTON");
    private By ConfirmDeleteButton = By.xpath("(//BUTTON[@data-v-10d463b7=''])[4]/following-sibling::BUTTON");

    public AdminPage(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateToAdminTab() {
        driver.findElement(adminTab).click();
    }

    public int getNumberOfRecords() {
        String text = driver.findElement(dynamicRecordsCount).getText();
        return Integer.parseInt(text.replaceAll("[^0-9]", ""));
    }

    public void clickAddButton() {
        driver.findElement(addButton).click();
    }

    public void enterUsername(String username) {
        driver.findElement(UsernameField).sendKeys(username);
    }

    /*    public void enterEmployeeName(String employeeName) {
            WebElement employeeField = driver.findElement(employeeNameField);
            employeeField.clear();
            employeeField.sendKeys(employeeName);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String script = "var options = document.querySelectorAll('div.oxd-autocomplete-dropdown span');" +
                    "for (var i = 0; i < options.length; i++) {" +
                    "  if (options[i].innerText.trim() === arguments[0]) {" +
                    "    options[i].click();" +
                    "    break;" +
                    "  }" +
                    "}";
            js.executeScript(script, employeeName);
        }*/
    private String getElementTextUsingJS(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (String) js.executeScript("return arguments[0].innerText;", element);
    }

    public void enterEmployeeName(String employeeName) {
        WebElement employeeField = driver.findElement(employeeNameField);
        employeeField.clear();
        employeeField.sendKeys(employeeName);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'oxd-autocomplete-dropdown')]")
        ));
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//div[contains(@class,'oxd-autocomplete-dropdown')]//span")
        ));

        System.out.println("Available options:");
        for (WebElement option : options) {
            String optionText = getElementTextUsingJS(option).trim();
            System.out.println("Option Text: [" + optionText + "]");
            System.out.println("Comparing with: [" + employeeName + "]");

            if (optionText.equalsIgnoreCase(employeeName.trim())) {
                option.click();
                return;
            }
        }

        throw new RuntimeException("No matching options found for employee name: " + employeeName);
    }


    public void selectAdminRoleOption() {
        WebElement dropdown = driver.findElement(userRoleDropdown);
        dropdown.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class,'oxd-select-dropdown')]//span[contains(text(),'Admin')]")));
        option.click();
    }

    public void selectEnableStatusOption() {
        WebElement dropdown = driver.findElement(statusDropdown);
        dropdown.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement option = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class,'oxd-select-dropdown')]//span[contains(text(),'Enable')]")));
        option.click();
    }


    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        driver.findElement(confirmPasswordField).sendKeys(confirmPassword);
    }

    public void clickSaveButton() {
        driver.findElement(saveButton).click();
    }

    public void addNewUser(String username, String employeeName, String password, String confirmPassword) {
        clickAddButton();
        enterUsername(username);
        enterEmployeeName(employeeName);
        selectAdminRoleOption();
        selectEnableStatusOption();
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
        clickSaveButton();
    }

    public void searchUserByUsername(String username) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchField = wait.until(ExpectedConditions.elementToBeClickable(SearchByUsernameField));
        searchField.clear();
        searchField.sendKeys(username);
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(SearchButton));
        searchButton.click();
    }

    public void deleteUser() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(DeleteButton));
        deleteButton.click();
        WebElement confirmButton = wait.until(ExpectedConditions.elementToBeClickable(ConfirmDeleteButton));
        confirmButton.click();

    }


    public boolean isRecordCountIncreased(int initialCount) {
        return getNumberOfRecords() == initialCount + 1;
    }

    public boolean isRecordCountDecreased(int initialCount) {
        return getNumberOfRecords() == initialCount - 1;
    }

}

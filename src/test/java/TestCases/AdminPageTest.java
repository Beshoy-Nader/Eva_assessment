package TestCases;

import Base.Hooks;
import org.junit.Assert;
import org.junit.Test;
import pages.AdminPage;
import pages.LoginPage;

public class AdminPageTest extends Hooks {

    @Test
    public void testAddAndDeleteUser() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("Admin", "admin123");
        AdminPage adminPage = new AdminPage(driver);
        adminPage.navigateToAdminTab();
        int initialRecordCount = adminPage.getNumberOfRecords();
        System.out.println(initialRecordCount);
        String newUsername = "TestUser123";
        adminPage.addNewUser(newUsername, "Peter Mac Anderson", "Password123", "Password123");
        Assert.assertTrue("Record count did not increase after adding a user.",
                adminPage.isRecordCountIncreased(initialRecordCount));
        adminPage.navigateToAdminTab();
        int afterSearchCount = adminPage.getNumberOfRecords();
        adminPage.searchUserByUsername(newUsername);
        adminPage.deleteUser();
        adminPage.navigateToAdminTab();
        Assert.assertTrue("Record count did not decrease after deleting the user.",
                adminPage.isRecordCountDecreased(afterSearchCount));
    }
}

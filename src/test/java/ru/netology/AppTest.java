package ru.netology;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataUser;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.*;

public class AppTest {

    @Test
    void shouldOpenDashboard() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataUser.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataUser.getVerificationCodeFor(authInfo);
        var dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.checkHeading();
    }

    @Test
    void shouldBlockedSystem() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataUser.getInvalidAuthInfo();
        loginPage.invalidLogin(authInfo);
        $("[data-test-id=password] input").setValue("1");
        $("[data-test-id=password] input").setValue("2");
        loginPage.systemBlocked();
    }

    @AfterAll
    static void shouldCleanDB() {
        DataUser.cleanDB();
    }
}
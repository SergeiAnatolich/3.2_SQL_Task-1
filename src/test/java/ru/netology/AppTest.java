package ru.netology;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataUser;
import ru.netology.page.LoginPage;

import java.util.Locale;

import static com.codeborne.selenide.Selenide.open;

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
        Faker faker = new Faker(new Locale("ru"));
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataUser.getInvalidAuthInfo();
        loginPage.invalidLogin(authInfo);
        loginPage.loginWithOtherInvalidPassword(faker.internet().password());
        loginPage.loginWithOtherInvalidPassword(faker.internet().password());
        loginPage.systemBlocked();
    }

    @AfterAll
     static void shouldCleanDB() {
        DataUser.cleanTables();
    }
}
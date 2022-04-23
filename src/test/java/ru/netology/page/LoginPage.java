package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import org.openqa.selenium.Keys;
import ru.netology.data.DataUser;

import java.util.Locale;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    private SelenideElement loginField = $("[data-test-id=login] input");
    private SelenideElement passwordField = $("[data-test-id=password] input");
    private SelenideElement loginButton = $("[data-test-id=action-login]");
    private SelenideElement notificationTitle = $("[data-test-id=error-notification] .notification__title");
    private SelenideElement notificationContent = $("[data-test-id=error-notification] .notification__content");

    public VerificationPage validLogin(DataUser.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        return new VerificationPage();
    }

    public void invalidLogin(DataUser.AuthInfo info) {
        loginField.setValue(info.getLogin());
        passwordField.setValue(info.getPassword());
        loginButton.click();
        notificationTitle.shouldHave(text("Ошибка"));
        notificationContent.shouldHave(text("Неверно указан логин или пароль"));
    }

    public void systemBlocked() {
        notificationTitle.shouldHave(text("Ошибка"));
        notificationContent.shouldHave(text("Повторный вход возможен через 30 минут"));
    }

    private static final Faker faker = new Faker(new Locale("ru"));

    public void loginWithOtherInvalidPassword() {
        passwordField.sendKeys(Keys.chord(Keys.CONTROL, "a") + Keys.DELETE);
        String pass = faker.internet().password();
        passwordField.setValue(pass);
        loginButton.click();
    }
}

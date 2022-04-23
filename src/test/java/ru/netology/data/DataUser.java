package ru.netology.data;

import lombok.SneakyThrows;
import lombok.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataUser {
    private DataUser() {
    }

    @Value
    public static class AuthInfo {
        private String login;
        private String password;
    }

    public static AuthInfo getAuthInfo() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getInvalidAuthInfo() {
        return new AuthInfo("vasya", "password");
    }

    @Value
    public static class VerificationCode {
        private String code;
    }

    @SneakyThrows
    private static String getIdUserFor(AuthInfo authInfo) {
        var idUserQuery = "SELECT id FROM users WHERE login=" + '"' + authInfo.getLogin() + '"' + ";";
        try (var statement = connection().createStatement();) {
            try (var rs = statement.executeQuery(idUserQuery)) {
                rs.next();
                String idUser = rs.getString("id");
                return idUser;
            }
        }
    }

    @SneakyThrows
    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        var codeUserQuery = "SELECT code FROM auth_codes WHERE user_id=" + '"' + getIdUserFor(authInfo) + '"' + " ORDER BY created DESC;";
        try (var statement = connection().createStatement();) {
            try (var rs = statement.executeQuery(codeUserQuery)) {
                rs.next();
                String codeUser = rs.getString("code");
                return new VerificationCode(codeUser);
            }
        }
    }

    @SneakyThrows
    public static void cleanTables() {
        var cleanAuthCodes = "DELETE FROM auth_codes;";
        var cleanCardTransactions = "DELETE FROM card_transactions;";
        var cleanCards = "DELETE FROM cards;";
        var cleanUsers = "DELETE FROM users;";
        try (var statement = connection().createStatement();) {
            statement.execute(cleanAuthCodes);
            statement.execute(cleanCardTransactions);
            statement.execute(cleanCards);
            statement.execute(cleanUsers);
        }
    }

    public static Connection connection() throws SQLException {
        var connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/alfabank_test", "sergei", "mypassword");
        return connection;
    }
}
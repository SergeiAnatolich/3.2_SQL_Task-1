package ru.netology.data;

import lombok.SneakyThrows;
import lombok.Value;

import java.sql.DriverManager;

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
        String idUser;

        var idUserQuery = "SELECT id FROM users WHERE login=" + '"' + authInfo.getLogin() + '"' + ";";
        try (var connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/alfabank_test", "sergei", "mypassword");
             var statement = connection.createStatement();
        ) {
            try (var rs = statement.executeQuery(idUserQuery)) {
                rs.next();
                idUser = rs.getString("id");
            }
        }
        return idUser;
    }

    @SneakyThrows
    public static VerificationCode getVerificationCodeFor(AuthInfo authInfo) {
        String codeUser;

        var codeUserQuery = "SELECT code FROM auth_codes WHERE user_id=" + '"' + getIdUserFor(authInfo) + '"' + " ORDER BY created DESC;";
        try (var connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/alfabank_test", "sergei", "mypassword");
             var statement = connection.createStatement();
        ) {
            try (var rs = statement.executeQuery(codeUserQuery)) {
                rs.next();
                codeUser = rs.getString("code");
            }
        }
        return new VerificationCode(codeUser);
    }

    @SneakyThrows
    public static void cleanTables() {
        var cleanAuthCodes = "DELETE FROM auth_codes;";
        var cleanCardTransactions = "DELETE FROM card_transactions;";
        var cleanCards = "DELETE FROM cards;";
        var cleanUsers = "DELETE FROM users;";
        try (var connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/alfabank_test", "sergei", "mypassword");
             var statement = connection.createStatement();
        ) {
            statement.execute(cleanAuthCodes);
            statement.execute(cleanCardTransactions);
            statement.execute(cleanCards);
            statement.execute(cleanUsers);
        }
    }
}
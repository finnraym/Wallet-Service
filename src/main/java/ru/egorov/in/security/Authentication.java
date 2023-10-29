package ru.egorov.in.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class Authentication {
    private String login;
    private boolean isAuth;
    private String message;

    public Authentication() {
    }

    public Authentication(String login, boolean isAuth, String message) {
        this.login = login;
        this.isAuth = isAuth;
        this.message = message;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package jwtsecurity.controller;

public class AuthResponse {

    private String login;
    private String expiration;

    public AuthResponse(String login, String expiration) {
        this.login = login;
        this.expiration = expiration;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
}

package jwtsecurity.controller;

public class AuthRequest {

    private String login;
    private char[] password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return new String(password);
    }

    public void setPassword(char[] password) {
        this.password = password;
    }
}

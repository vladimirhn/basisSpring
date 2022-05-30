package jwtsecurity.user;

import kpersistence.v2.annotations.Column;
import kpersistence.v1.mapping.annotations.Entity;
import kpersistence.v2.annotations.Table;
import kpersistence.v2.tables.StringIdTable;

@Entity
@Table(name = "users")
public class User extends StringIdTable {

    @Column(name = "login")
    private String login;

    @Column(name = "role")
    private String role;

    @Column(name = "password")
    private String password;

    public User() {}

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public User(String login, String role, String password) {
        this.login = login;
        this.role = role;
        this.password = password;
    }

    @Override
    public void setDefaults() {}

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

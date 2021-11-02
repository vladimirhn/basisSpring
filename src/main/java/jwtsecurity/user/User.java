package jwtsecurity.user;

import kpersistence.mapping.annotations.Column;
import kpersistence.mapping.annotations.Entity;
import kpersistence.mapping.annotations.Table;
import repository.tables.StringIdTable;

@Entity
@Table(name = "users")
public class User extends StringIdTable {

    @Column(name = "login")
    private String login;

    @Column(name = "role")
    private String role;

    @Column(name = "password")
    private String password;

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

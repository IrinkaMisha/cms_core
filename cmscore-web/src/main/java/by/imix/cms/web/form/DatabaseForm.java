package by.imix.cms.web.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by sedler on 14.04.2015.
 */
public class DatabaseForm {
    @Pattern(regexp="^[a-zA-Z0-9]+$",message = "{registrationUser.login.symbol}")
    @Size(min=3, max=20, message = "{registrationUser.login.size}")
    private String login;
    @Pattern(regexp="^[a-zA-Z0-9]+$",message = "{registrationUser.pass.symbol}")
    @Size(min=3, max=20, message = "{registrationUser.login.size}")
    private String password;
    private String dialect;
    private String driverClassName;
    private String url;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

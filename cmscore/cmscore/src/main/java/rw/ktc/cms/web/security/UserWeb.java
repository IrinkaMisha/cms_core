package rw.ktc.cms.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.io.File;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: miha
 * Date: 17.12.13
 * Time: 15:00
 * To change this template use File | Settings | File Templates.
 */
public class UserWeb extends User{
    private rw.ktc.cms.entity.User user;
    private String avatarPath= File.separator+"resources"+File.separator+"avataruser";

    public UserWeb(String username, String password, Collection<? extends GrantedAuthority> authorities, rw.ktc.cms.entity.User user) {
        super(username,password,authorities);
        this.user = user;
    }

    public UserWeb(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, rw.ktc.cms.entity.User user) {
        super(username,password,enabled,accountNonExpired,credentialsNonExpired,accountNonLocked,authorities);
        this.user = user;
    }

    public rw.ktc.cms.entity.User getUser() {
        return user;
    }

    public void setUser(rw.ktc.cms.entity.User user) {
        this.user = user;
    }
}

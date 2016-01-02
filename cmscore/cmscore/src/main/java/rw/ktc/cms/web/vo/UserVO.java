package rw.ktc.cms.web.vo;

import java.util.List;

/**
 * Created by dima on 24.08.2015.
 */
public class UserVO {
    public long id;
    public boolean active;
    public boolean restore=false;
    public List<Long> roles;

}

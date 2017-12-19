package by.imix.cms.web.security;

import by.imix.cms.dao.IUserDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import by.imix.cms.nodedata.NodeProperty;
import by.imix.cms.entity.Role;
import by.imix.cms.entity.User;
import by.imix.cms.web.image.FileUploaderIface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: miha
 * Date: 16.12.13
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */

@Service("userManServiceForSpringSecurity")
@Transactional(readOnly = true)
public class UserManServiceForSpringSecurity implements UserDetailsManager {
    private static final Logger logger = LoggerFactory.getLogger(UserManServiceForSpringSecurity.class);

    @Autowired
    private IUserDAO userManagerService;

    private FileUploaderIface fileUploader;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Ищем в базе юзера по его имени");
        User user = userManagerService.getUserByName(username);
        if (null == user ) throw new UsernameNotFoundException("Не найден пользователь с таким именем, или не верный пароль");
        return new UserWeb(username, user.getPassword(), true,  user.getActive(), true,
                           true, getGrantedAuthority(user), user);
    }

    public List<SimpleGrantedAuthority> getGrantedAuthority(User user){
        List<NodeProperty> lGr=new ArrayList<>();
        for (Role r:user.getRoles()){
            lGr.addAll(userManagerService.getPropertysValue(r, Role.NAME_FIELD_PERMISSION_FOR_NODE_PROPERTY));
        }

        List<SimpleGrantedAuthority> gal=new ArrayList<>();
        for(NodeProperty np:lGr){
            gal.add(new SimpleGrantedAuthority(np.getValue()));
        }

        return gal;
    }

    public void createUser(UserDetails userDetails) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void updateUser(UserDetails userDetails) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void deleteUser(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void changePassword(String s, String s2) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean userExists(String name) {
        User user = userManagerService.getUserByName(name);
        return (user!=null);
    }

    public FileUploaderIface getFileUploader() {
        return fileUploader;
    }

    @Autowired
    public void setFileUploader(@Qualifier("imageUploadForAvatar") FileUploaderIface fileUploader) {
        this.fileUploader = fileUploader;
    }

    public IUserDAO getUserManagerService() {
        return userManagerService;
    }

    public void setUserManagerService(IUserDAO userManagerService) {
        this.userManagerService = userManagerService;
    }
}

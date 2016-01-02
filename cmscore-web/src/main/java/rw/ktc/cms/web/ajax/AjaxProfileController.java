package rw.ktc.cms.web.ajax;

import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import rw.ktc.cms.dao.IRoleDAO;
import rw.ktc.cms.dao.IUserDAO;
import rw.ktc.cms.nodedata.json.ViewFlag;
import rw.ktc.cms.entity.Role;
import rw.ktc.cms.entity.User;
import rw.ktc.cms.web.security.CurrentUserWeb;
import rw.ktc.cms.web.security.UserWeb;
import rw.ktc.cms.web.vo.RoleForm;
import rw.ktc.cms.web.security.CredentialBox;
import rw.ktc.cms.web.vo.UserVO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("ajax/api1/profile/")
@Secured("ROLE_ADMIN_SETTINGS")
public class AjaxProfileController {
    private static final Logger logger = LoggerFactory.getLogger(AjaxProfileController.class);

    @Autowired
    private IUserDAO daoUser;
    @Autowired
    private IRoleDAO daoRole;

    @JsonView(ViewFlag.UserBriefly.class)
    @ResponseBody
    @RequestMapping(value = "users", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> users() {
        List<User> users = daoUser.getAll();
        return users;
    }

    @JsonView(ViewFlag.UserWithRoles.class)
    @ResponseBody
    @RequestMapping(value = "users-with-roles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> usersWithRoles() {
        List<User> users = daoUser.getAll();
        List<User> usersFull = new ArrayList<>();
        for (User u : users) {
            usersFull.add(getDaoUser().loadFullObject(u));
        }
        return usersFull;
    }

    @JsonView(ViewFlag.UserFull.class)
    @ResponseBody
    @RequestMapping(value = "user/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public User user(@PathVariable("id") Long id) {
        User user = daoUser.getById(id);
        user = daoUser.loadFullObject(user);
        return user;
    }

    @JsonView(ViewFlag.UserFull.class)
    @ResponseBody
    @RequestMapping(value = "user/delete/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public User userDelete(@PathVariable("id") Long id, @CurrentUserWeb UserWeb whoIam) {
        User user = daoUser.getById(id);
        user = daoUser.loadFullObject(user);
        daoUser.removeUser(user, whoIam.getUser());
        return user;
    }

    @ResponseBody
    @JsonView(ViewFlag.UserFull.class)
    @RequestMapping(value = "user/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public User saveUser(@RequestBody UserVO userVO, @CurrentUserWeb UserWeb whoIam) {
        User user = daoUser.getById(userVO.id);
        daoUser.loadFullObject(user);
        if (!userVO.restore) {
            user.setActive(userVO.active);
            List<Role> listForRemove = new ArrayList<>();
            for (Role r : user.getRoles()) { // Удаляем роли которых нет в новом списке.
                boolean removeRole = true;
                for (long idR : userVO.roles) {
                    if (r.getId() == idR) {
                        removeRole = false;
                        break;
                    }
                }
                if (removeRole) {
                    listForRemove.add(r);
                }
            }
            user.getRoles().removeAll(listForRemove);
            for (long idR : userVO.roles) { // добавляем роли которых нет в старом списке
                boolean neddAdd = true;
                for (Role r : user.getRoles()) {
                    if (r.getId() == idR) {
                        neddAdd = false;
                        break;
                    }
                }
                if (neddAdd) {
                    Role r = daoRole.getFullRoleById(idR);
                    if (r != null) {
                        user.getRoles().add(r);
                    } else {
                        logger.warn("что-то случилось - нужно добавить роли , но роли с таким ид в базе не найдено");
                    }

                }
            }
        } else {
            user.setRemoved(false);
        }
        user = daoUser.saveOrUpdateNode(user, whoIam.getUser());
        daoUser.loadFullObject(user);
        return user;
    }

    @JsonView(ViewFlag.RoleFull.class)
    @ResponseBody
    @RequestMapping(value = "role/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Role role(@PathVariable("id") Long id) {
        Role role = getDaoRole().getFullRoleById(id);
        return role;
    }

    @JsonView(ViewFlag.RoleFull.class)
    @ResponseBody
    @RequestMapping(value = "roles", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Role> roles() {
        List<Role> roles = getDaoRole().getAll();
        List<Role> rolesFull = new ArrayList<>();
        for (Role r : roles) {
            rolesFull.add(getDaoRole().getFullRoleById(r.getId()));
        }
        return rolesFull;
    }

    @RequestMapping(value = "permissions", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Set<String> getPermissions() {
        return CredentialBox.getAllPermissions();
    }

    @ResponseBody
    @RequestMapping(value = "role/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public RoleForm saveNewRole(@RequestBody RoleForm roleVO, @CurrentUserWeb UserWeb whoIam) {
        Role r = null;
        Long id = (Long) daoRole.createOrUpdateRole(roleVO, whoIam.getUser());
        RoleForm form = RoleForm.getInstanceFromRole(daoRole.getFullRoleById(id));
        return form;
    }

    @ResponseBody
    @RequestMapping(value = "role/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public RoleForm roleDelete(@RequestBody RoleForm roleVO) {
        Role r = daoRole.getFullRoleById(roleVO.id);
        daoRole.deleteRoleById(roleVO.id);
        RoleForm form = RoleForm.getInstanceFromRole(r);
        return form;
    }


    public IUserDAO getDaoUser() {
        return daoUser;
    }

    public void setDaoUser(IUserDAO daoUser) {
        this.daoUser = daoUser;
    }

    public IRoleDAO getDaoRole() {
        return daoRole;
    }

    public void setDaoRole(IRoleDAO daoRole) {
        this.daoRole = daoRole;
    }

}

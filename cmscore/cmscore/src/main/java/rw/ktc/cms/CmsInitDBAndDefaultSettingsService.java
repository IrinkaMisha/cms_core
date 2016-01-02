package rw.ktc.cms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.ktc.cms.dao.IRoleDAO;
import rw.ktc.cms.dao.IUserDAO;
import rw.ktc.cms.entity.Role;
import rw.ktc.cms.entity.User;
import rw.ktc.cms.material.CmsInfo;
import rw.ktc.cms.model.ICmsInfoDAO;
import rw.ktc.cms.web.security.CredentialBox;
import rw.ktc.cms.web.vo.RoleForm;

import java.util.ArrayList;

@Service
public class CmsInitDBAndDefaultSettingsService implements ApplicationListener<ContextRefreshedEvent> {
    private static final Logger logger = LoggerFactory.getLogger(CmsInitDBAndDefaultSettingsService.class);

    private IUserDAO daoUser;

    private IRoleDAO daoRole;

    private CredentialBox credentialBox;

    private ICmsInfoDAO cmsInfoDAO;


    private CmsInfo cmsInfo;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.info("подготовка к инициализации");
        cmsInfo = cmsInfoDAO.getInstanceFromDataBase();
        createDefaultRoles();
        createDefaultAdminCms();

    }

    public void createDefaultRoles() {
        if (!cmsInfo.isRolesCreated()) {
            logger.info("создаем ролей");
            Role regRole = daoRole.getFullRoleByName(Const.ROLE_NAME_FOR_REGISTER_USER);
            Role adminRole = daoRole.getFullRoleByName(Const.ROLE_NAME_FOR_ADMINSCMS);
            RoleForm form;
            if (null == regRole) {
                form = getInstanceByName(Const.ROLE_NAME_FOR_REGISTER_USER, true);
            }
            if (null == adminRole) {
                form = getInstanceByName(Const.ROLE_NAME_FOR_ADMINSCMS, true);
            }
            cmsInfo.setRolesCreated(true);
            cmsInfoDAO.update();
        }
    }

    /**
     * Метод создает роль с именем roleName, если addAllPermission = true - добавятся все пермишионы из credentialBox
     * @param roleName
     * @param addAllPermission
     * @return
     */
    private RoleForm getInstanceByName(String roleName, boolean addAllPermission) {
        Role r = daoRole.getFullRoleByName(roleName);
        if (null != r) return null;
        RoleForm form = new RoleForm();
        form.setName(roleName);
        if (addAllPermission) {
//            form.permissions = new ArrayList<>(getCredentialBox().getListCredential());

            form.permissions = new ArrayList<>(CredentialBox.getAllPermissions());
        }
        daoRole.createOrUpdateRole(form, cmsInfo.getNode());
        return form;
    }

    @Transactional
    public void createDefaultAdminCms() {
        User user = daoUser.getUserByName(Const.USER_NAME_FOR_ADMINSCMS);
        if (user == null && !cmsInfo.isAdminCreated() && cmsInfo.isRolesCreated()) {
            logger.info("создаем пользователя " + Const.USER_NAME_FOR_ADMINSCMS);
            user = new User(Const.USER_NAME_FOR_ADMINSCMS, Const.PASSWORD_FOR_ADMINSCMS);
            user.setActive(true);
            Role role = daoRole.getFullRoleByName(Const.ROLE_NAME_FOR_ADMINSCMS);
            user.getRoles().add(role);
            user = daoUser.createUser(user, cmsInfo.getNode());
            cmsInfo.setAdminCreated(true);
            cmsInfoDAO.update();
        }
    }

    public IUserDAO getDaoUser() {
        return daoUser;
    }

    @Autowired
    public void setDaoUser(IUserDAO daoUser) {
        this.daoUser = daoUser;
    }

    public IRoleDAO getDaoRole() {
        return daoRole;
    }

    @Autowired
    public void setDaoRole(IRoleDAO daoRole) {
        this.daoRole = daoRole;
    }

    public CredentialBox getCredentialBox() {
        return credentialBox;
    }

    @Autowired
    public void setCredentialBox(CredentialBox credentialBox) {
        this.credentialBox = credentialBox;
    }

    public ICmsInfoDAO getCmsInfoDAO() {
        return cmsInfoDAO;
    }

    @Autowired
    public void setCmsInfoDAO(ICmsInfoDAO cmsInfoDAO) {
        this.cmsInfoDAO = cmsInfoDAO;
    }
}

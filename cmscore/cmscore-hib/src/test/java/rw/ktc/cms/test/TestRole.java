package rw.ktc.cms.test;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rw.ktc.cms.dao.IRoleDAO;
import rw.ktc.cms.dao.IUserDAO;
import rw.ktc.cms.entity.Role;
import rw.ktc.cms.entity.User;
import rw.ktc.cms.web.security.CredentialBox;
import rw.ktc.cms.web.vo.RoleForm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dima on 21.11.2014.
 */
public class TestRole {

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("test-context-d4.xml");
        testRoles(ac);
    }

    private static void testRoles(ApplicationContext ac) {
        IRoleDAO daoRole = ac.getBean(IRoleDAO.class);
        IUserDAO daoUser = ac.getBean(IUserDAO.class);
        Role role;
        CredentialBox credentialBox = ac.getBean(CredentialBox.class);

        role = daoRole.getFullRoleByName("administrator");
        System.out.println(role);
        role = daoRole.getFullRoleById(21751l);
        role = (Role) daoRole.getById(new Long(21751), false);
        if (null == role) System.exit(1);
        List<String> permissions = null;
//        List<String> permissions = new ArrayList<>(credentialBox.getListCredential());
//        System.out.println(permissions);

        RoleForm form = new RoleForm();
        form.id = role.getId();
        form.name = role.getName();
        form.permissions= new ArrayList<>(Arrays.asList(permissions.get(0),permissions.get(1)));


        User u = (User) daoUser.getById(2L, false);
        daoRole.createOrUpdateRole(form, u);

        role = daoRole.getFullRoleById(21751L);
        System.out.println(role);
        System.out.println(role.getNodeProperties().size());


        System.out.println("stop");

    }

    private static void assertThat(boolean value) throws IllegalArgumentException {
        if (! value) throw new IllegalArgumentException("Условие не выполнено");
    }


}

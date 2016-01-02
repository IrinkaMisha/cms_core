package rw.ktc.cms.test;


import org.hibernate.Hibernate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rw.ktc.cms.Const;
import rw.ktc.cms.dao.IRoleDAO;
import rw.ktc.cms.dao.IUserDAO;
import rw.ktc.cms.entity.Role;
import rw.ktc.cms.entity.User;
import rw.ktc.cms.material.CmsInfo;
import rw.ktc.cms.model.ICmsInfoDAO;
import rw.ktc.cms.nodedata.HistoryNodeImpl;
import rw.ktc.cms.nodedata.NodeProperty;
import rw.ktc.cms.nodedata.service.HistoryNodeService;
import rw.ktc.cms.web.security.CredentialBox;

import java.util.UUID;

/**
 * Created by dima on 21.11.2014.
 */
public class TestUser {

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("test-context-d4.xml");
        testUsers(ac);

//        testNodeHistory(ac);
//        testCmsInfo(ac);
//        ctreateDoxuyaUsers(ac);
    }

    private static void testCmsInfo(ApplicationContext ac) {
        ICmsInfoDAO cmsInfoDAO = ac.getBean(ICmsInfoDAO.class);
        IUserDAO daoUser = ac.getBean(IUserDAO.class);
        CmsInfo cmsInfo = cmsInfoDAO.getInstanceFromDataBase();
        System.out.println(cmsInfo);
        System.out.println();
    }

    private static void testUsers(ApplicationContext ac) {
        IUserDAO daoUser = ac.getBean(IUserDAO.class);
        ICmsInfoDAO cmsInfoDAO = ac.getBean(ICmsInfoDAO.class);
        CmsInfo cmsInfo = cmsInfoDAO.getInstanceFromDataBase();
        CredentialBox credentialBox = ac.getBean(CredentialBox.class);

//        List<User> users = daoUser.getAll();
//        System.out.println(users);

        User u = daoUser.getUserByName("test5");
        daoUser.loadFullObject(u);
//        List<User> history = daoUser.getHistoryConcreteNode(u);
//        System.out.println(history);
        NodeProperty np = new NodeProperty();
        np.setKeyt("key" + UUID.randomUUID());
        np.setValue("value" + UUID.randomUUID());
        u.getNodeProperties().add(np);

//        u.setRemoved(true);
//        u.setHistorical(true);
//        u.setActive(false);

        long id1 = u.getId();
        u = daoUser.saveOrUpdateNode(u, cmsInfo.getNode());

        long id2 = u.getId();
        System.out.println("id1:" + id1);
        System.out.println("id2:" + id2);
        System.out.println("stop");
    }


    private static Long createNodeHistory(ApplicationContext ac) {
        HistoryNodeService historyNodeService = ac.getBean("nodedefaulthistservice", HistoryNodeService.class);
        ICmsInfoDAO cmsInfoDAO = ac.getBean(ICmsInfoDAO.class);
        CmsInfo cmsInfo = cmsInfoDAO.getInstanceFromDataBase();

        HistoryNodeImpl testNode = new HistoryNodeImpl();
        NodeProperty np = new NodeProperty();
        np.setKeyt("key" + UUID.randomUUID());
        np.setValue("value" + UUID.randomUUID());
        testNode.getNodeProperties().add(np);
        Long id = (Long) historyNodeService.createNode(testNode, cmsInfo.getNode());
        return id;
    }

    private static void testNodeHistory(ApplicationContext ac) {
        HistoryNodeService historyNodeService = ac.getBean("nodedefaulthistservice", HistoryNodeService.class);
        ICmsInfoDAO cmsInfoDAO = ac.getBean(ICmsInfoDAO.class);
        CmsInfo cmsInfo = cmsInfoDAO.getInstanceFromDataBase();
        long id = createNodeHistory(ac);
        HistoryNodeImpl testNode = (HistoryNodeImpl) historyNodeService.getById(id, false);
        System.out.println(Hibernate.isInitialized(testNode.getNodeProperties()));
        historyNodeService.loadFullObject(testNode);
        NodeProperty np = new NodeProperty();
        np.setKeyt("keyAdd" + UUID.randomUUID());
        np.setValue("valueAdd" + UUID.randomUUID());
        testNode.getNodeProperties().add(np);

        HistoryNodeImpl testNode2 = (HistoryNodeImpl) historyNodeService.saveOrUpdateNode(testNode, cmsInfo.getNode());
        System.out.println(testNode2.equals(testNode));
        System.out.println("stop" + id);
    }

    private static void ctreateDoxuyaUsers(ApplicationContext ac) {
        IRoleDAO daoRole = ac.getBean(IRoleDAO.class);
        IUserDAO daoUser = ac.getBean(IUserDAO.class);
        ICmsInfoDAO cmsInfoDAO = ac.getBean(ICmsInfoDAO.class);
        CmsInfo cmsInfo = cmsInfoDAO.getInstanceFromDataBase();

        String name, password;
        Role r = daoRole.getFullRoleByName(Const.ROLE_NAME_FOR_REGISTER_USER);
        for (int i = 0; i < 5; i++) {

            name = "test" + (i == 0 ? "" : i);
            password = name;
            User user = new User(name, password);
            user.setActive(true);
            user.getRoles().add(r);
            daoUser.createUser(user, cmsInfo.getNode());
        }

    }

    private static void assertThat(boolean value) throws IllegalArgumentException {
        if (!value) throw new IllegalArgumentException("Условие не выполнено");
    }


}

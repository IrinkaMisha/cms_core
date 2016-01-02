package rw.ktc.cms.test.menu;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rw.ktc.cms.nodedata.service.NodeService;
import rw.ktc.cms.entity.Role;
import rw.ktc.cms.entity.User;

/**
 * Created by dima on 21.11.2014.
 */
public class PrepareDBForTest {

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("test-context-d4.xml");
        prepareDB(ac);

    }
    private static void testService1(ApplicationContext ac) {
    }

    /**
     * Метод используется для подготовки базы данных и создания пользователя админ и гость
     * @param ac
     */
    private static void prepareDB(ApplicationContext ac) {
        NodeService nodeService = (NodeService) ac.getBean("nodehibservice", NodeService.class);
        User user = new User();
        user.setName("admin");
        user.setPassword("admin");
        user = (User) nodeService.saveOrUpdate(user);

        User userG = new User();
        userG.setName("guest");
        userG.setPassword("guest");
        userG = (User) nodeService.saveOrUpdateNode(userG, user);

        Role roleA = new Role();
        roleA.setName("administrator");
        roleA = (Role) nodeService.saveOrUpdateNode(roleA, user);
        Role roleU = new Role();
        roleU.setName("unregisteruser");
        roleU = (Role) nodeService.saveOrUpdateNode(roleU, user);
        Role roleR = new Role();
        roleR.setName("registeruser");
        roleR = (Role) nodeService.saveOrUpdateNode(roleR, user);

        user.getRoles().add(roleA);
        user.getRoles().add(roleR);

        userG.getRoles().add(roleU);

        user.setActive(true);
        userG.setActive(true);
        user = (User) nodeService.saveOrUpdate(user);
        userG = (User) nodeService.saveOrUpdate(userG);
        System.out.println(user);

    }
}

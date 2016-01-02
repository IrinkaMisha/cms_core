package rw.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rw.ktc.cms.dao.IUserDAO;
import rw.ktc.cms.entity.User;

/**
 * Created by sedler on 26.03.15.
 */
public class DaoTest {
    public static void main(String[] args) {
        System.out.println("Проверка теста");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("dao-test-config.xml");
        IUserDAO userManagerNodeService= (IUserDAO)ctx.getBean(IUserDAO.class);
        User user=userManagerNodeService.getUserByName("admin");
        System.out.println("завершение теста теста");
    }
}

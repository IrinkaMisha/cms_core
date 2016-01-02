package rw.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by dima on 21.11.2014.
 */
public class TestHib {

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("test-context.xml");
        testService1(ac);

    }
    private static void testService1(ApplicationContext ac) {
    }
}

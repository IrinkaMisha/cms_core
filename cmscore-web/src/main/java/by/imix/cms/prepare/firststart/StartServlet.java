package by.imix.cms.prepare.firststart;

import by.imix.cms.database.DatabaseUtil;
import by.imix.cms.entity.Role;
import by.imix.cms.entity.User;
import by.imix.cms.nodedata.NodeProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.*;

/**
 * Class initialize first loading database and check connect to database
 */
@Service
public class StartServlet implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(StartServlet.class);

    @Autowired
    private ApplicationContext _applicationContext;

    @Autowired
    private ServletContext context;

    public StartServlet() {
        init();
    }

    @Autowired
    public void setServletContext(ServletContext servletContext) {
        this.context = servletContext;
    }

    public void init() {
        System.out.println("StartServlet try load data!!!!!");
        Properties prop = new Properties();
        try {
            //URL path=this.getClass().getClassLoader().getResources("../../").nextElement();
            prop.load(this.getClass().getClassLoader().getResourceAsStream("project.properties"));
//            prop.load(ResourcePathHolder.getServletContext().getResourceAsStream("/WEB-INF/classes/project.properties"));
            String jdbcPropertiesUrl = prop.getProperty("cms.jdbcsettingsurl");
            prop.load(this.getClass().getClassLoader().getResourceAsStream(jdbcPropertiesUrl));
            String database = prop.getProperty("by.imix.cms.database");
            String driverName = prop.getProperty("jdbc.mysql.driverClassName");
            String url = prop.getProperty("jdbc.mysql.url");
            String user = prop.getProperty("jdbc.mysql.username");
            String password = prop.getProperty("jdbc.mysql.password");
            if (!DatabaseUtil.isConnection(driverName, url, user, password)) {
                logger.warn("Try create user for system");
                if (true) {
//                ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
//                IUserDAO userService = (IUserDAO) ctx.getBean(IUserDAO.class);
//                defaultManager = (DefaultDataManager) ctx.getBean(DefaultDataManager.class);

                    User admin = new User("admin", "admin");
                    admin.setDateCreate(new Date());
                    Role adminRole = new Role("administrator");
                    adminRole.setDateCreate(new Date());
                    List<NodeProperty> credentials = new ArrayList<NodeProperty>();
                    credentials.add(new NodeProperty("credential", "ROLE_AUTH_DATA"));
                    credentials.add(new NodeProperty("credential", "ROLE_ADMIN_SETTINGS"));
                    adminRole.setNodeProperties(credentials);

                    Role registerUser = new Role("registeruser");
                    registerUser.setDateCreate(new Date());
                    List<NodeProperty> cred = new ArrayList<NodeProperty>();
                    cred.add(new NodeProperty("credential", "ROLE_AUTH_DATA"));
                    registerUser.setNodeProperties(cred);

//                defaultManager.createRole(adminRole);
//                defaultManager.createRole(registerUser);

                    Set<Role> roles = new HashSet<Role>();
                    roles.add(adminRole);
                    admin.setRoles(roles);
                    admin.setActive(true);
//                defaultManager.createAdminUser(admin);
                }
            } else {
                logger.debug("PostLoadingFullConfiguration loading");
                GenericApplicationContext dynamicContext = new GenericApplicationContext();
                new XmlBeanDefinitionReader(dynamicContext).loadBeanDefinitions(new FileSystemResource(context.getRealPath("/") + "cmsController-servlet.xml"));
                dynamicContext.setParent(_applicationContext);
                dynamicContext.refresh();

                dynamicContext.registerShutdownHook();
//                context.setParent(_applicationContext);
//                _applicationContext.refresh();
//                new AnnotationConfigApplicationContext(PostLoadingFullConfiguration.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        context = servletContextEvent.getServletContext();
        _applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}

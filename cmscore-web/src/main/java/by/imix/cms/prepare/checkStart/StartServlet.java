package by.imix.cms.prepare.checkStart;

import by.imix.cms.database.DatabaseUtil;
import by.imix.cms.entity.Role;
import by.imix.cms.entity.User;
import by.imix.cms.nodedata.NodeProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Class initialize first loading database and check connect to database
 */
//@Service
public class StartServlet implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(StartServlet.class);

//    @Autowired
    private ApplicationContext _applicationContext;

//    @Autowired
    private ServletContext servletContext;

    public StartServlet() {

    }

//    @Autowired
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        init();
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
//                GenericApplicationContext dynamicContext = null;
//                try {
//                    dynamicContext = new GenericApplicationContext();
//                    System.out.println(servletContext.getRealPath("/") + "cmsController-servlet.xml");
//                    new XmlBeanDefinitionReader(dynamicContext).loadBeanDefinitions(new FileSystemResource(servletContext.getRealPath("/") + "/WEB-INF/classes/cmsController-servlet.xml"));
//                    dynamicContext.setParent(_applicationContext);
//                    dynamicContext.refresh();
//                    dynamicContext.registerShutdownHook();
//                } catch (BeansException e) {
//                    e.printStackTrace();
//                } catch (IllegalStateException e) {
//                    e.printStackTrace();
//                }


                AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
                ctx.register(FirstPostLoadingFullConfiguration.class);
                ctx.setParent(_applicationContext);
                ctx.refresh();
                ctx.registerShutdownHook();


//                servletContext.setParent(_applicationContext);
//                _applicationContext.refresh();
//                new AnnotationConfigApplicationContext(PostLoadingFullConfiguration.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        servletContext = servletContextEvent.getServletContext();
        _applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContextEvent.getServletContext());
        init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }


}

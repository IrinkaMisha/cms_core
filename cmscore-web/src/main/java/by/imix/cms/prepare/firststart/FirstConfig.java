package by.imix.cms.prepare.firststart;

import by.imix.cms.database.DatabaseUtil;
import by.imix.cms.entity.Role;
import by.imix.cms.entity.User;
import by.imix.cms.nodedata.NodeProperty;
import by.imix.cms.prepare.firststart.postloading.FirstPostLoadingFullConfiguration;
import by.imix.cms.prepare.firststart.postloading.FullPostLoadingFullConfiguration;
import by.imix.cms.prepare.firststart.postloading.PostLoadingFullConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Created by miha on 29.10.2017.
 */

public class FirstConfig {
    private static final Logger logger = LoggerFactory.getLogger(StartServlet.class);

//    @Autowired
//    private GenericApplicationContext _applicationContext;

//    @Autowired
//    private ServletContext context;

//    @Autowired
//    public void setServletContext(ServletContext servletContext) {
//        this.context = servletContext;
//    }

//    @Bean
//    public PostLoadingFullConfiguration getPostLoadingFullConfiguration2() {
//        return new PostLoadingFullConfiguration();
//    }

    public FirstConfig(){
        getPostLoadingFullConfiguration();
    }

//    @Bean
    public PostLoadingFullConfiguration getPostLoadingFullConfiguration() {
        System.out.println("FirstConfig try to load data");
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
//            JavaConfigApplicationContext context =
//                    new JavaConfigApplicationContext("**/configuration/**/*.class", "**/other/*Config.class");

            if (DatabaseUtil.isConnection(driverName, url, user, password)) {
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
                AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
                ctx.register(FirstPostLoadingFullConfiguration.class);
            } else {
                AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
                ctx.register(FullPostLoadingFullConfiguration.class);
                System.out.println("PostLoadingFullConfiguration loading");
                logger.warn("PostLoadingFullConfiguration loading");
                return new FullPostLoadingFullConfiguration();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new FullPostLoadingFullConfiguration();
    }


//    @Override
//    public void contextInitialized(ServletContextEvent servletContextEvent) {
//        context = servletContextEvent.getServletContext();
//    }
//
//    @Override
//    public void contextDestroyed(ServletContextEvent servletContextEvent) {
//
//    }
}
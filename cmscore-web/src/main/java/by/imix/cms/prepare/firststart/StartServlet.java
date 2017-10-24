package by.imix.cms.prepare.firststart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import by.imix.cms.nodedata.NodeProperty;
import by.imix.cms.entity.Role;
import by.imix.cms.entity.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by sedler on 10.04.2015.
 */

public class StartServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(StartServlet.class);

//    private IUserDAO userService;
//    private DefaultDataManager defaultManager;

//    public IUserDAO userService() {
//        return userService;
//    }
//
//    public void userService(IUserDAO userService) {
//        this.userService = userService;
//    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/cms.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    public void init() throws ServletException {
        Properties prop=new Properties();
        try {
            prop.load(getServletContext().getResourceAsStream("/WEB-INF/classes/jdbc.properties"));
            String database=prop.getProperty("by.imix.cms.database");
            if(database.equals("1")) {
                ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
//                IUserDAO userService = (IUserDAO) ctx.getBean(IUserDAO.class);
//                defaultManager = (DefaultDataManager) ctx.getBean(DefaultDataManager.class);
                User admin = new User("admin","admin");
                admin.setDateCreate(new Date());
                Role adminRole = new Role("administrator");
                adminRole.setDateCreate(new Date());
                List<NodeProperty> credentials= new ArrayList<NodeProperty>();
                credentials.add(new NodeProperty("credential", "ROLE_AUTH_DATA"));
                credentials.add(new NodeProperty("credential", "ROLE_ADMIN_SETTINGS"));
                adminRole.setNodeProperties(credentials);

                Role registerUser = new Role("registeruser");
                registerUser.setDateCreate(new Date());
                List<NodeProperty> cred= new ArrayList<NodeProperty>();
                cred.add(new NodeProperty("credential", "ROLE_AUTH_DATA"));
                registerUser.setNodeProperties(cred);

//                defaultManager.createRole(adminRole);
//                defaultManager.createRole(registerUser);

                Set<Role> roles=new HashSet<Role>();
                roles.add(adminRole);
                admin.setRoles(roles);
                admin.setActive(true);
//                defaultManager.createAdminUser(admin);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

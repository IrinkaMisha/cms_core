package rw.ktc.cms.test;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rw.ktc.cms.dao.IRoleDAO;
import rw.ktc.cms.dao.IUserDAO;
import rw.ktc.cms.material.CmsInfo;
import rw.ktc.cms.model.ICmsInfoDAO;
import rw.ktc.cms.nodedata.NodeImpl;
import rw.ktc.cms.nodedata.NodeProperty;
import rw.ktc.cms.nodedata.json.ViewFlag;
import rw.ktc.cms.entity.Role;
import rw.ktc.cms.entity.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by dima on 21.11.2014.
 */
public class TestJson {

    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("test-context-d4.xml");
        testUserService(ac);
        testRoles(ac);
    }

    private static ObjectMapper getOM() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.enable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return mapper;
    }

    private static ObjectMapper getDefaultSpringOM() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // pretty print for debag

        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }


    private static void testBinary() {
        int i = 157;
        int hexValue0 = 0x1;
        int hexValue1 = 0x4;
        int hexValue2 = 4;
        int v = i & hexValue2;
        System.out.println("v:" + v);
        if ((i & hexValue2) == hexValue2) {
            System.out.println("if:" + v);
        }

    }

    private static void testRoles(ApplicationContext ac) {
//        NodeService nodeService = (NodeService) ac.getBean("nodehibservice", NodeService.class);
//        IUserAdminService userAdminService = ac.getBean(IUserAdminService.class);
//        IUserDAO userManagerNodeService = ac.getBean(IUserDAO.class);
//        IRoleDAO roleDAO = ac.getBean(IRoleDAO.class);
        IRoleDAO roleDAO = ac.getBean(IRoleDAO.class);
        List<Role> roles1 = roleDAO.getAll();
        Role role=null;
        for (Role r: roles1 ){
            roleDAO.loadFullObject(r);
            if (r.getNodeProperties().size() > 0 && r.getNodeProperties().get(0).getKeyt().equals(Role.NAME_FIELD_PERMISSION_FOR_NODE_PROPERTY)){
                role = r;
                break;
            }
        }

//        role = roleDAO.loadFullObject(role);

        ObjectMapper mapper = getOM();
        String json = null;
        try {
            json = mapper.writerWithView(ViewFlag.NodeBriefly.class).writeValueAsString(role);
            assertThat(json.contains("id"));
            assertThat(json.contains("name"));
            System.out.println(json);

            json = mapper.writerWithView(ViewFlag.RoleFull.class).writeValueAsString(role);
            assertThat(json.contains("id"));
            assertThat(json.contains("name"));
            assertThat(json.contains("nodeProperties"));
            assertThat(json.contains("nodeCreator"));
            assertThat(json.contains("nodeCorrector"));
            assertThat(json.contains(Role.NAME_FIELD_PERMISSION_FOR_NODE_PROPERTY));
            System.out.println(json);

            List<Role> roles = roleDAO.getAll();
            List<Role> rolesFull = new ArrayList<>();
            for (Role r : roles) {
                rolesFull.add(roleDAO.getFullRoleById(r.getId()));
            }

            for (Role r : rolesFull) {
                System.out.println(r.getId() + ":" + r.getName());
                json = mapper.writerWithView(ViewFlag.RoleFull.class).writeValueAsString(r);
                System.out.println(json);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println("stop");

    }

    private static void assertThat(boolean value) throws IllegalArgumentException {
        if (!value) throw new IllegalArgumentException("Условие не выполнено");
    }

    private static void testUserService(ApplicationContext ac) {
        IUserDAO userDAO = ac.getBean(IUserDAO.class);
        ICmsInfoDAO cmsInfoDAO = ac.getBean(ICmsInfoDAO.class);
        CmsInfo cmsInfo = cmsInfoDAO.getInstanceFromDataBase();
        try {
            ObjectMapper mapper = getOM();

            User u = (User) userDAO.getAll().get(0);
            u = userDAO.loadFullObject(u);
            if (u.getNodeProperties().size() < 1){
                NodeProperty np = new NodeProperty();
                np.setKeyt("key" + UUID.randomUUID());
                np.setValue("value" + UUID.randomUUID());
                u.getNodeProperties().add(np);
                u = userDAO.saveOrUpdateNode(u, cmsInfo.getNode());
            }

            printUser(u);
            System.out.println("" + ViewFlag.UserBriefly.class);
            String json = mapper.writerWithView(ViewFlag.UserBriefly.class).writeValueAsString(u);
            System.out.println(json);
            assertThat(json.contains("id"));
            assertThat(json.contains("name"));
            assertThat(json.contains("active"));

            System.out.println("" + ViewFlag.UserWithRoles.class);
            json = mapper.writerWithView(ViewFlag.UserWithRoles.class).writeValueAsString(u);
            System.out.println(json);
            assertThat(json.contains("id"));
            assertThat(json.contains("name"));
            assertThat(json.contains("active"));
            assertThat(json.contains("keyt"));
            assertThat(json.contains("roles"));
            assertThat(json.contains("nodeProperties"));

            System.out.println("" + ViewFlag.UserFull.class);
            json = mapper.writerWithView(ViewFlag.UserFull.class).writeValueAsString(u);
            System.out.println(json);
            assertThat(json.contains("id"));
            assertThat(json.contains("name"));
            assertThat(json.contains("active"));
            assertThat(json.contains("keyt"));
            assertThat(json.contains("roles"));
            assertThat(json.contains("nodeProperties"));
            assertThat(json.contains("nodeCreator"));
            assertThat(json.contains("nodeCorrector"));
            System.out.println("");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void printUser(NodeImpl user) {
        System.out.println("id:"+user.getId());
        System.out.println("creator:"+user.getNodeCreator());
        System.out.println("corrector:"+user.getNodeCorrector());
        System.out.println("propertys:"+user.getNodeProperties());
    }


}

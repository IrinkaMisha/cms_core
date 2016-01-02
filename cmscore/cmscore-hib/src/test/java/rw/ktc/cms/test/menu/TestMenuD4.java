package rw.ktc.cms.test.menu;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rw.ktc.cms.dao.IRoleDAO;
import rw.ktc.cms.dao.IUserDAO;
import rw.ktc.cms.entity.CmsMenuItem;
import rw.ktc.cms.entity.MenuWrapper;
import rw.ktc.cms.entity.MenuState;
import rw.ktc.cms.nodedata.NodeState;
import rw.ktc.cms.nodedata.service.StateService;
import rw.ktc.cms.dao.IMenuWrapperDAO;
import rw.ktc.cms.nodedata.state.State;
import rw.ktc.cms.nodedata.state.StateDefault;
import rw.ktc.cms.nodedata.state.StateSimple;
import rw.ktc.cms.entity.Role;
import rw.ktc.cms.entity.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by dima on 21.11.2014.
 */
public class TestMenuD4 {

    public static void main(String[] args) {

        ApplicationContext ac = new ClassPathXmlApplicationContext("test-context-d4.xml");
//        createStateForMenu(ac);
//        saveMenu(ac);
//        changeMenuForStateShow(ac);
//        testMenu(ac);
//        createMenuWrapperUsogdp(ac);
//        changeUsogdpMenuPermition(ac);
        testMenuWrap(ac);


    }

    private static void createMenuWrapperUsogdp(ApplicationContext ac) {
        IMenuWrapperDAO menuWrapper = (IMenuWrapperDAO) ac.getBean(IMenuWrapperDAO.class);
        IUserDAO userManager = ac.getBean(IUserDAO.class);
        User admin = userManager.getUserByName("admin");
        MenuWrapper menu = new MenuWrapper("adminka");
        CmsMenuItem menuItem1 = new CmsMenuItem("Регулировка", "/emptyvagons");
        CmsMenuItem menuItem2 = new CmsMenuItem("График", "/districts");
        menu.getMenuItems().add(menuItem1);
        menu.getMenuItems().add(menuItem2);

        menu = (MenuWrapper) menuWrapper.saveOrUpdateNode(menu, admin);
        System.out.println(menu);
    }

    private static void saveMenu(ApplicationContext ac) {
        IMenuWrapperDAO menuWrapper = (IMenuWrapperDAO) ac.getBean(IMenuWrapperDAO.class);
        IUserDAO userManager = ac.getBean(IUserDAO.class);
//        IUserDAO userManager = (IUserDAO) ac.getBean("userControlService");
        User admin = userManager.getUserByName("admin");
//        MenuWrapper menu = (MenuWrapper) nodeService.getFullNodeById(MenuWrapper.class, 11700L);
        MenuWrapper menu = new MenuWrapper("testmenu");
        menu = (MenuWrapper) menuWrapper.saveOrUpdateNode(menu, admin);
        System.out.println(menu);
    }
    private static void changeMenuForStateShow(ApplicationContext ac) {
        IUserDAO userManager = (IUserDAO) ac.getBean(IUserDAO.class);
        IRoleDAO daoRole = ac.getBean(IRoleDAO.class);
        IMenuWrapperDAO menuWrapper = (IMenuWrapperDAO) ac.getBean( IMenuWrapperDAO.class);
        StateService stateDAO = ac.getBean(StateService.class);

        List<Role> roles= daoRole.getAll();
//        List<State> states = (List<State>)(List<?>) stateDAO.getAll();

        StateDefault stateShowMenu = (StateDefault) stateDAO.getByDesc(MenuState.MENUCHANGE.toString());

        MenuWrapper menu = (MenuWrapper) menuWrapper.getNodeById(MenuWrapper.class, 150L);
//        MenuWrapper menu = new MenuWrapper();
//        menu.setDescription("description  2");
//        menu.setTitle("title  2");
        // добавляем все роли для всех состояний - цикл в цикле )
        List<NodeState> lns = new ArrayList<>();
        for (Role r: roles){
            NodeState nodeStateCM = new NodeState(stateShowMenu, r);
            lns.add(nodeStateCM);
            menu.setListStates(new HashSet<>(lns));
        }
        menuWrapper.saveOrUpdate(menu);
        System.out.println(menu);

//        Rule createMenuRule = new Rule();
//        createMenuRule.setDiscription("Создание Меню");
//        createMenuRule.setRuleFlags(new RuleFlags());
//        createMenuRule.getRuleFlags().getAdd().add(nodeStateCM);
//
//        RuleIface rI= new RulePerformer();
//
//        rI.doRule(menu, createMenuRule);

    }
    private static void changeUsogdpMenuPermition(ApplicationContext ac) {
        IUserDAO userManager = (IUserDAO) ac.getBean(IUserDAO.class);
        IRoleDAO daoRole = ac.getBean(IRoleDAO.class);
        IMenuWrapperDAO menuService = (IMenuWrapperDAO) ac.getBean( IMenuWrapperDAO.class);
        StateService stateService = ac.getBean(StateService.class);
        State mchange = (State) stateService.getByDesc(MenuState.MENUCHANGE.toString());
        State mshow = (State) stateService.getByDesc(MenuState.MENUSHOW.toString());
        State mcreate = (State) stateService.getByDesc(MenuState.MENUCREATE.toString());

        System.out.println(mchange);
        MenuWrapper usogdpMenu = menuService.getByName("adminka");
//        usogdpMenu = menuService.getFullNodeById(usogdpMenu.getId());

        System.out.println(usogdpMenu);
        List<Role> roles= daoRole.getAll();

        List<NodeState> lns = new ArrayList<>();
        NodeState nodeState;
        for (Role r: roles){
            nodeState = new NodeState(mchange, r);
            lns.add(nodeState);
            nodeState = new NodeState(mshow, r);
            lns.add(nodeState);
            nodeState = new NodeState(mcreate, r);
            lns.add(nodeState);
        }
        usogdpMenu.setListStates(new HashSet<>(lns));
        usogdpMenu = (MenuWrapper) menuService.saveOrUpdate(usogdpMenu);
        System.out.println(usogdpMenu);
    }

    private static void testMenu(ApplicationContext ac) {
        IUserDAO userManager =ac.getBean(IUserDAO.class);
        IMenuWrapperDAO menuWrapper = ac.getBean(IMenuWrapperDAO.class);
        StateService stateService = ac.getBean(StateService.class);

        MenuWrapper m = (MenuWrapper) menuWrapper.getAll().get(0);

        for (NodeState ns : m.getListStates()){
            System.out.println(ns.getState().getDescription());
            System.out.println(ns.getNode().getId());
        }
        System.out.println(m);
    }

    private static void printRoles(Set<Role> roles){
        for(Role role:roles){
            System.out.println(role.getName());
        }
    }

    private static void createStateForMenu(ApplicationContext ac){
        StateService stateService= (StateService) ac.getBean("stateservice");
        StateSimple createMenu=new StateSimple();
        createMenu.setDescription(MenuState.MENUCREATE.toString());
        stateService.saveOrUpdate(createMenu);
        StateSimple showMenu = new StateSimple();
        showMenu.setDescription(MenuState.MENUSHOW.toString());
        stateService.saveOrUpdate(showMenu);
        StateSimple menuChange = new StateSimple();
        menuChange.setDescription(MenuState.MENUCHANGE.toString());
        stateService.saveOrUpdate(menuChange);
        System.out.println(stateService.getAll());
    }
    private static void testMenuWrap(ApplicationContext ac) {
//        IUserDAO userManager = (IUserDAO) ac.getBean(IUserDAO.class);
        IMenuWrapperDAO menuService = (IMenuWrapperDAO) ac.getBean(IMenuWrapperDAO.class);
        MenuWrapper usogdpMenu = menuService.getByName("adminka");
        for (NodeState ns : usogdpMenu.getListStates()){
            System.out.println(ns.getState().getDescription());
            System.out.println(ns.getNode().getId());
        }
        System.out.println(usogdpMenu);
    }


}

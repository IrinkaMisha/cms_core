package rw.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import by.imix.cms.isitnecessary.menu.Menu;
import by.imix.cms.isitnecessary.menu.MenuItem;
import by.imix.cms.isitnecessary.menu.MenuNodeService;
import by.imix.cms.nodedata.service.HistoryNodeService;
import by.imix.cms.nodedata.service.StateService;
import by.imix.cms.nodedata.state.StateSimple;

/**
 * Created by miha on 12.05.2015.
 */
public class TestMenuLoadSave {
//    @Autowired
//    @Qualifier("stateservice")
    private StateService stateService;

//    @Autowired
//    @Qualifier("nodedefaulthistservice")
    private HistoryNodeService nodehibservice;



    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("test-menu.xml");
        TestMenuLoadSave t=new TestMenuLoadSave();
        t.testServiceLoad(ac);
//        t.testServiceSave(ac);

    }
    private void testServiceSave(ApplicationContext ac) {
        MenuItem menuItemR=new MenuItem("Главное меню","");

        MenuItem menuItemF=new MenuItem("Файл","javascript:file()");

        MenuItem menuItemO=new MenuItem("Открыть","javascript:file()",menuItemF);
        MenuItem menuItemP=new MenuItem("Вставить","javascript:file()",menuItemF);

        MenuItem menuItemE=new MenuItem("Редактирование","javascript:file()");
        MenuItem menuItemV=new MenuItem("Вид","javascript:file()");
        MenuItem menuItemN=new MenuItem("Навигация","javascript:file()");

        Menu menu=new Menu(menuItemR,"Главное меню");

        MenuNodeService mns= (MenuNodeService) ac.getBean("menuNodeService");


       stateService= (StateService) ac.getBean("stateservice");


        stateService.saveOrUpdate(new StateSimple("Пользователь может корректировать"));

        mns.saveMenu(2L,menu);

        System.out.println("Сохранение завершено");

    }

    private void testServiceLoad(ApplicationContext ac) {
        MenuNodeService mns= (MenuNodeService) ac.getBean("menuNodeService");
        nodehibservice= (HistoryNodeService) ac.getBean("nodedefaulthistservice");

        Menu m= null;
        try {
//            m = mns.getMenuByNode((Node) nodehibservice.getById(11550L,false));
            m = mns.getMenuById(11550L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Считывание меню завершено "+m.toString());
    }
}

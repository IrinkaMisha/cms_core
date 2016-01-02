package rw.ktc.cms.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import rw.ktc.cms.material.CmsInfo;
import rw.ktc.cms.web.dynamiccontent.DynamicContent;
import rw.ktc.cms.model.ICmsInfoDAO;
import rw.ktc.cms.model.IDynamicContentService;

import java.util.List;

/**
 * Created by dima on 28.08.2015.
 */
public class TestDynamicContent {
    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("test-context-d4.xml");
        addNodeProperty(ac);
    }
    private static void addNodeProperty(ApplicationContext ac) {
        IDynamicContentService dynamicContentService = ac.getBean(IDynamicContentService.class);
        ICmsInfoDAO cmsInfoDAO = ac.getBean(ICmsInfoDAO.class);
        CmsInfo cmsInfo = cmsInfoDAO.getInstanceFromDataBase();


//        DynamicContent dc = dynamicContentService.load(13650L);
//        dc.getRedirectUrl().add("loremIpsum.html");
//        dynamicContentService.saveChanges(dc, cmsInfo.getNode());
        List<DynamicContent> list = dynamicContentService.getAllDynamicContent();
        System.out.println(list);
        System.out.println("");


    }
}

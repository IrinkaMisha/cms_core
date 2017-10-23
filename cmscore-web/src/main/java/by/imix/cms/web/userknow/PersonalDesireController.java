package by.imix.cms.web.userknow;

import by.imix.cms.nodedata.service.NodeService;
import by.imix.cms.web.security.CurrentUserWeb;
import by.imix.cms.web.security.UserWeb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import by.imix.cms.nodedata.Node;
import by.imix.cms.nodedata.NodeImpl;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Created by miha on 09.09.2014.
 */
@Controller("personalDesireController")
public class PersonalDesireController {
    @Autowired
    @Qualifier("nodeServiceImpl")
    private NodeService<Node,Long> contentService;

    @RequestMapping(value = "user/content/savepersonaldesire.html" ,method= RequestMethod.POST)
    @ResponseBody
    public ModelAndView addTypeContent(PersonalDesire pkForm,HttpSession httpSession,  @CurrentUserWeb UserWeb userWeb) throws IOException {
        try {
            if(pkForm.getId()!=null) {
                Node node = contentService.getById(pkForm.getId(), false);
                pkForm.setNode(node);
            }
            pkForm.fillObject();//заполняем ноде новыми данными
            contentService.saveOrUpdateNode(pkForm.getNode(), userWeb.getUser());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/user/content/"+((NodeImpl)pkForm.getNode()).getId()+"/viewpersonaldesire.html");
    }

    @RequestMapping(value = "user/content/addpersonaldesire.html")
    public ModelAndView addpage(PersonalDesire pk) throws IOException {
        ModelAndView mav=new ModelAndView("redactPersonalDesire");
        mav.addObject("pk", null);
        return mav;
    }

    @RequestMapping(value = "user/content/{idPage}/editpersonaldesire.html")
    public ModelAndView editpage(@PathVariable("idPage") Long idPage) throws IOException {
        ModelAndView mav=new ModelAndView("redactPersonalDesire");
        try {
            Node node=contentService.getById(idPage, false);
            PersonalDesire pk=new PersonalDesire(node);
            mav.addObject("personalDesire", pk);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "/user/content/{idPk}/viewpersonaldesire.html")
    public ModelAndView nodeView(@PathVariable("idPk") Long idPk) throws IOException {
        ModelAndView mav=new ModelAndView("viewPersonalKnow");
        try {
            Node node= contentService.getById(idPk, false);
            if(node!=null){
                PersonalDesire pk=new PersonalDesire(node);
                mav.addObject("pk", pk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "/user/content/allpersonaldesire.html")
    public ModelAndView getAllpersonalknow() throws IOException {
        ModelAndView mav=new ModelAndView("allpersonalknow");
        try {
            List<Node> listNode= contentService.getAllNodeFromPrKey("type","personaldesire");
            for(Node node:listNode){
                contentService.loadFullObject(node);
            }
            mav.addObject("listNode", listNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }
}
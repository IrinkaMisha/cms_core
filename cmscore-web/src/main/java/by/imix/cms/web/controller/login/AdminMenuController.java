package by.imix.cms.web.controller.login;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import by.imix.cms.web.dynamiccontent.DynamicContent;
import by.imix.cms.nodedata.Node;
import by.imix.cms.model.IDynamicContentService;
import by.imix.cms.web.Const;
import by.imix.cms.web.security.CurrentUserWeb;
import by.imix.cms.web.security.UserWeb;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@Secured("ROLE_DYNCONT_CRUD")
public class AdminMenuController {
    private static final Logger logger = LoggerFactory.getLogger(AdminMenuController.class);

    @Autowired
    private IDynamicContentService dynamicContentService;

//    @RequestMapping(value = "/m-p-c.html", method = RequestMethod.GET)
//    public ModelAndView newDynamicPage222() {
//        ModelAndView mav = new ModelAndView("manager/dynamicpage/editDynamicPage");
//        mav.addObject("dynamicContent", new DynamicContent());
//        return mav;
//    }

    @RequestMapping(value = "content/dc/ndp", method = RequestMethod.GET)
    public ModelAndView newDynamicPage2(@CurrentUserWeb UserWeb userWeb) {
        DynamicContent dc = new DynamicContent();
        dc.setName("Новая страница");
//        dc.setType("page"); // нельзя
        dc.setContent("Описпание новой страницы");
        dc.setPastRoot(false); // это пока не работает, пусть полежит здесь
        dc.setTypeDynCont("page");
        Node n = dynamicContentService.saveChanges(dc, userWeb.getUser());
        return new ModelAndView("redirect:/content/show/" + n.getId());
    }

    @RequestMapping(value = "content/dc/ndc", method = RequestMethod.GET)
    public ModelAndView newDynamicContent( @CurrentUserWeb UserWeb userWeb) {
        DynamicContent dc = new DynamicContent();
        dc.setName("Новый блок");
//        dc.setType("page"); // нельзя
        dc.setContent("Описпание блока");
        dc.setPastRoot(false); // это пока не работает, пусть полежит здесь
        dc.setTypeDynCont("content");
        Node n = dynamicContentService.saveChanges(dc, userWeb.getUser());
        return new ModelAndView("redirect:/content/show/" + n.getId());
    }

    @RequestMapping(value = "content/show/{idPage}")
    public ModelAndView viewDynamicContent(@PathVariable("idPage") Long idPage, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView();
        try {
//            RequestDispatcher rd = request.getServletContext().getRequestDispatcher("/include.jsp");
//            rd.include(request, response);

            DynamicContent dynContent = dynamicContentService.load(idPage);
            if (null != dynContent) {
                mav.addObject(Const.NAME_MAIN_OBJECT_FOR_DYNAMIC_CONTENT, dynContent);
                logger.debug("отображение динамического содержимого {}", dynContent);
                String viewName = dynamicContentService.getViewNameTemplate(dynContent.getNode());
                mav.setViewName(viewName);
            } else {
                throw new ResourceNotFoundException(idPage + "");
            }
        }catch (ResourceNotFoundException nf){
            throw nf; // normal work
        }catch (Exception e) {
            logger.error("Ошибка динамического контента {}", e);
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping(value = "template/show/{idtemplate}")
    public ModelAndView viewDynamicContent(@PathVariable("idtemplate") String idtemplate, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        String viewName = dynamicContentService.getViewNameTemplate(idtemplate);
        mav.setViewName(viewName);
        return mav;
    }

    public IDynamicContentService getDynamicContentService() {
        return dynamicContentService;
    }

    public void setDynamicContentService(IDynamicContentService dynamicContentService) {
        this.dynamicContentService = dynamicContentService;
    }


}

package rw.ktc.cms.web.ajax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import rw.ktc.cms.web.dynamiccontent.DynamicContent;
import rw.ktc.cms.model.IDynamicContentService;
import rw.ktc.cms.nodedata.Node;
import rw.ktc.cms.web.security.CurrentUserWeb;
import rw.ktc.cms.web.security.UserWeb;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("ajax/api1/dc/")
@Secured("ROLE_DYNCONT_CRUD")
public class AjaxDynamicContentController {
    private static final Logger logger = LoggerFactory.getLogger(AjaxDynamicContentController.class);
    @Autowired
    private IDynamicContentService dynamicContentService;

    @Autowired
    private ServletContext servletContext;

    private String webFolder;

    @PostConstruct
    private void init() {
        webFolder = servletContext.getRealPath("/");
        dynamicContentService.setPathToWebFolder(webFolder);
    }

    @ResponseBody
    @RequestMapping(value = "get/{idPage}", method = RequestMethod.GET, produces = "application/json")
    public DynamicContent getDynamicContent(@PathVariable("idPage") Long idPage) {
        DynamicContent dynContent = dynamicContentService.load(idPage);
        return dynContent;
    }

    @ResponseBody
    @RequestMapping(value = "create", method = RequestMethod.POST, produces = "application/json")
    public DynamicContent createNewDynamicContent(@RequestBody DynamicContent dynamicContent, HttpServletRequest request, @CurrentUserWeb UserWeb userWeb) {
        return dynamicContentService.createNew(dynamicContent, userWeb.getUser());
    }

    /**
     * Метод получения имен переменных из шаблона контейнера. Под этими именами будут вставлены новые блоки(контейнеры)
     */
    @ResponseBody
    @RequestMapping(value = "namesContainers", method = RequestMethod.GET, produces = "application/json")
    public List<String> namesContainers(@RequestParam("idCont") Long idCont) throws IOException {
        List<String> strings = dynamicContentService.getNamesBlocks(idCont);
        return strings;
    }

    @ResponseBody
    @RequestMapping(value = "save", method = RequestMethod.POST, produces = "application/json")
    public Boolean saveDynamicContent(@RequestBody DynamicContent dynamicContent, HttpServletRequest request, @CurrentUserWeb UserWeb userWeb) {
        logger.info("save dyncon.cont:{}", dynamicContent.getCont());

        Node node = dynamicContentService.saveChanges(dynamicContent, userWeb.getUser());
        if (null != node) {
            return true;
        } else {
            return false;
        }
    }

//    @ResponseBody
//    @RequestMapping(value = "update", method = RequestMethod.POST, produces = "application/json")
//    public Boolean updateContainers(@RequestBody DynamicContent dynamicContent, HttpServletRequest request) {
//        logger.info("save dyncon.cont:{}", dynamicContent.getCont());
//        return false;
//    }

    @ResponseBody
    @RequestMapping(value = "templates/all", method = RequestMethod.GET, produces = "application/json")
    public List<Map> getTemplates() {
        return dynamicContentService.getAllTemplatesInfo();
    }

    @ResponseBody
    @RequestMapping(value = "templates/all/xml", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
    public List<Map> getTemplates2xml() {
        return dynamicContentService.getAllTemplatesInfo();
    }


    @RequestMapping(value = "all", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<DynamicContent> getAllContainers() {
        return dynamicContentService.getAllDynamicContent();
    }

    @ResponseBody
    @RequestMapping(value = "all/{type}", method = RequestMethod.GET, produces = "application/json")
    public List<DynamicContent> getAllDynamicContentForType(@PathVariable("type") String type) {
        return dynamicContentService.getAllDynamicContentForType(type);
    }

}

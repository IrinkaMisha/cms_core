package by.imix.cms.web.dynamiccontent;

import by.imix.cms.model.IDynamicContentService;
import by.imix.cms.redirect.RedirectViewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import by.imix.cms.redirect.RedirectView;
import by.imix.cms.web.Const;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by miha on 17.10.2014.
 */
@Transactional(readOnly = true)
public class DynamicContentHandler implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(DynamicContentHandler.class);

    @Autowired
    IDynamicContentService dynamicContentService;

    @Autowired
    private RedirectViewService redirectViewService;

    public DynamicContentHandler() {
    }

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        if (modelAndView != null && !modelAndView.getViewName().startsWith("redirect:")) {
            RedirectView rw = redirectViewService.checkRedirect(httpServletRequest.getRequestURI());
            if (rw != null) {
                dynamicContentService.load(rw.getId_nodeView());
                DynamicContent dynContent = dynamicContentService.load(rw.getId_nodeView());

                Container contC = new Container();
                contC.setName("centerPage");
                if (modelAndView.getViewName().contains("/")) {
                    contC.setType("jsp");
                } else {
                    contC.setType("tiles");
                }
                contC.setContent(modelAndView.getViewName());
                modelAndView.addObject(Const.NAME_OBJECT_FOR_CENTER_PAGE_FOR_DYNAMIC_CONTENT, contC);

                modelAndView.addObject(Const.NAME_MAIN_OBJECT_FOR_DYNAMIC_CONTENT, dynContent);
                modelAndView.setViewName(dynamicContentService.getViewNameTemplate(dynContent.getNode()));

                logger.debug("{} and will be redirect to {} for path - {}", rw, modelAndView.getViewName(), httpServletRequest.getServletPath());
            }
        }
    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse response, Object o, Exception e) throws Exception {

    }

    public IDynamicContentService getDynamicContentService() {
        return dynamicContentService;
    }

    public void setDynamicContentService(IDynamicContentService dynamicContentService) {
        this.dynamicContentService = dynamicContentService;
    }

    public RedirectViewService getRedirectViewService() {
        return redirectViewService;
    }

    public void setRedirectViewService(RedirectViewService redirectViewService) {
        this.redirectViewService = redirectViewService;
    }
}


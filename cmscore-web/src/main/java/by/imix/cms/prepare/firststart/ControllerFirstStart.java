package by.imix.cms.prepare.firststart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * Created with IntelliJ IDEA.
 * User: miha
 * Date: 06.05.14
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
@Controller("FirstController")
@SessionAttributes
public class ControllerFirstStart {
    private static final Logger logger = LoggerFactory.getLogger(ControllerFirstStart.class);

    private ApplicationContext context;
    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @RequestMapping(value = "index1.html")
    public String index() {
//        log.info("Не поподает сюда - беда блин");
        return "firstPage";
    }

//    @RequestMapping(value = "settings.html")
//    public String settings(HttpServletRequest httpServletRequest) throws IOException {
//        log.info("Не поподает сюда - беда блин");
//        return "firstPage";
//    }
}

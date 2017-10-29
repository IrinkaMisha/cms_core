package by.imix.cms;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;


/**
 * Helper for getting Servlet and Application context
 * Created by miha on 29.10.2017.
 */
public class ResourcePathHolder  implements ApplicationContextAware, InitializingBean {
    private WebApplicationContext wac;
    private static String resourcePath;
    private static ServletContext sc;
    private String path = "WEB-INF";

    public static String getResourcePath() {
        return resourcePath;
    }

    public static ServletContext getServletContext() {
        return sc;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        wac = (WebApplicationContext) applicationContext;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ResourcePathHolder.sc = wac.getServletContext();
        ResourcePathHolder.resourcePath = sc.getRealPath(path);
    }
}
package by.imix.cms.prepare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Class is needed for load additional configuration when we have establish connect to database
 *
 */
@Configuration
@ImportResource("classpath:cmsController-servlet.xml")
public class FullPostLoadingFullConfiguration implements PostLoadingFullConfiguration, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private static final Logger logger = LoggerFactory.getLogger(PostLoadingFullConfiguration.class);
    public FullPostLoadingFullConfiguration()
    {
        System.out.println("start FullPostLoadingFullConfiguration");

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    public void initContext() {
        System.out.println("initContext FullPostLoadingFullConfiguration");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        System.out.println(applicationContext);
        context.setParent(applicationContext);
        context.register(FullPostLoadingFullConfiguration.class);
        context.refresh();
    }
}


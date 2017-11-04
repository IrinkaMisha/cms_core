package by.imix.cms.prepare.firststart;

import by.imix.cms.prepare.PostLoadingFullConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by Mikhail_Kachanouski on 11/1/2017.
 */
@Configuration
@ImportResource("classpath:first-start-config.xml")
public class FirstPostLoadingFullConfiguration implements PostLoadingFullConfiguration, ApplicationContextAware
{
    private static final Logger logger = LoggerFactory.getLogger(FirstPostLoadingFullConfiguration.class);

    private ApplicationContext applicationContext;

    public FirstPostLoadingFullConfiguration()
    {
        System.out.println("start FirstPostLoadingFullConfiguration");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void initContext() {
        System.out.println("add FirstPostLoadingFullConfiguration");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.setParent(applicationContext);
        context.register(FirstPostLoadingFullConfiguration.class);
        context.refresh();
    }
}
package by.imix.cms.prepare.checkstart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * Created by miha on 29.10.2017.
 */
@Configuration
public class FirstConfig {
    private static final Logger logger = LoggerFactory.getLogger(StartServlet.class);

//    @Bean
//    @Conditional(FirstStartCondition.class)
    public FirstPostLoadingFullConfiguration loadFirstPostLoadingFullConfiguration() {
        System.out.println("new FirstPostLoadingFullConfiguration");
        FirstPostLoadingFullConfiguration res = new FirstPostLoadingFullConfiguration();
//        if (!FirstStartCondition.isConnect()) {
//            res.initContext();
//        }
        return res;
    }

//    @Bean
//    @Conditional(FullStartCondition.class)
    public FullPostLoadingFullConfiguration loadFullPostLoadingFullConfiguration() {
        System.out.println("new FullPostLoadingFullConfiguration");
        FullPostLoadingFullConfiguration res = new FullPostLoadingFullConfiguration();
//        if (FirstStartCondition.isConnect()) {
//            res.initContext();
//        }
        return res;
    }
}
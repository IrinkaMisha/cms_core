package by.imix.cms.prepare.checkStart;

import by.imix.cms.prepare.firststart.*;
import by.imix.cms.prepare.firststart.FullStartCondition;
import by.imix.cms.prepare.postloading.FirstPostLoadingFullConfiguration;
import by.imix.cms.prepare.postloading.FullPostLoadingFullConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * Created by miha on 29.10.2017.
 */
@Configuration
public class FirstConfig {
    private static final Logger logger = LoggerFactory.getLogger(StartServlet.class);

    public FirstConfig(){

    }

    @Bean
    @Conditional(FirstStartCondition.class)
    public FirstPostLoadingFullConfiguration getFirstPostLoadingFullConfiguration() {
        System.out.println("new FirstPostLoadingFullConfiguration");
        return new FirstPostLoadingFullConfiguration();
    }

    @Bean
    @Conditional(FullStartCondition.class)
    public FullPostLoadingFullConfiguration getFullPostLoadingFullConfiguration() {
        System.out.println("new FullPostLoadingFullConfiguration");
        return new FullPostLoadingFullConfiguration();
    }

    @Bean
    @Conditional(FullStartCondition.class)
    public InitDataBaseController getInitDataBase() {
        return new InitDataBaseController();
    }
}
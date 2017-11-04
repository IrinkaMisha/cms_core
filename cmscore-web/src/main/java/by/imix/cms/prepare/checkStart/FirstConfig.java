package by.imix.cms.prepare.checkStart;

import by.imix.cms.prepare.firststart.InitDataBaseController;
import by.imix.cms.prepare.FirstPostLoadingFullConfiguration;
import by.imix.cms.prepare.FullPostLoadingFullConfiguration;
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
        System.out.println("new FirstConfig");
    }

    @Bean
    @Conditional(FirstStartCondition.class)
    public FirstPostLoadingFullConfiguration getFirstPostLoadingFullConfiguration() {
        System.out.println("getFirstPostLoadingFullConfiguration");
        FirstPostLoadingFullConfiguration res = new FirstPostLoadingFullConfiguration();
//        if (DatabaseUtil.isConnectWithReadProperty()) {
            res.initContext();
//        }
        return res;
    }

    @Bean
    @Conditional(FullStartCondition.class)
    public FullPostLoadingFullConfiguration getFullPostLoadingFullConfiguration() {
        System.out.println("getFullPostLoadingFullConfiguration");
        FullPostLoadingFullConfiguration res = new FullPostLoadingFullConfiguration();
//        if (!DatabaseUtil.isConnectWithReadProperty()) {
            res.initContext();
//        }
        return res;
    }

    @Bean
    @Conditional(FullStartCondition.class)
    public InitDataBaseController getInitDataBase() {
        return new InitDataBaseController();
    }
}
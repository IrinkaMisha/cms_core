package by.imix.cms.prepare.postloading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by Mikhail_Kachanouski on 11/1/2017.
 */
@Configuration
@ImportResource({ "classpath:cmsController-servlet.xml"})
public class FirstPostLoadingFullConfiguration implements PostLoadingFullConfiguration
{
    private static final Logger logger = LoggerFactory.getLogger(FirstPostLoadingFullConfiguration.class);

    public FirstPostLoadingFullConfiguration()
    {
        System.out.println("start FirstPostLoadingFullConfiguration");
    }
}

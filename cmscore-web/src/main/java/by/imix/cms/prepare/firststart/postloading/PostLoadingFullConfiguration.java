package by.imix.cms.prepare.firststart.postloading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Class is needed for load additional configuration when we have establish connect to database
 *
 */
@Configuration
@ImportResource("cmsController-servlet.xml")
public class PostLoadingFullConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(PostLoadingFullConfiguration.class);

}
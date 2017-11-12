package by.imix.cms.prepare;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by miha on 11.11.2017.
 */
public class LogLoadBean1 {
    private static final Logger logger = LoggerFactory.getLogger(LogLoadBean1.class);
    public LogLoadBean1() {
        System.out.println("LogLoadBean1");
        logger.debug("LogLoadBean1");
    }
}

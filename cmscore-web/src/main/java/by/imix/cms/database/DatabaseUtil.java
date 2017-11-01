package by.imix.cms.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Util class for database
 * Created by miha on 29.10.2017.
 */
public class DatabaseUtil {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);

    /**
     * Method for check database
     *
     * @param driverName driverName
     * @param url        url
     * @param user       user
     * @param password   password
     * @return
     */
    public static boolean isConnection(String driverName, String url, String user, String password) {
        try {
            Class.forName(driverName);
            System.out.println("Trying to connect");
            Connection connection = DriverManager.getConnection(url, user, password);

            logger.debug("Connection Established Successfull and the DATABASE NAME IS:"
                    + connection.getMetaData().getDatabaseProductName());
            return true;
        } catch (Exception e) {
            logger.warn("Unable to make connection with DB");
            e.printStackTrace();
            return false;
        }
    }
}

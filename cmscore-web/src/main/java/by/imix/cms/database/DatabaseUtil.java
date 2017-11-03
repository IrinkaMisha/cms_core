package by.imix.cms.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Util class for database
 * Created by miha on 29.10.2017.
 */
public class DatabaseUtil{
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);

    /**
     * Method for check database
     *
     * @param driverName driverName
     * @param url url
     * @param user user
     * @param password password
     */
    public static boolean isConnection(String driverName, String url, String user, String password){
        Connection connection = null;
        try{
            Class.forName(driverName);
            System.out.println("Trying to connect");
            connection = DriverManager.getConnection(url, user, password);

            logger.debug("Connection Established Successfull and the DATABASE NAME IS:" +
                         connection.getMetaData().getDatabaseProductName());
            System.out.println("Connection Established Successfull and the DATABASE NAME IS:" +
                               connection.getMetaData().getDatabaseProductName());
            return true;
        }
        catch (Exception e){
            System.out.println("Unable to make connection with DB");
            logger.warn("Unable to make connection with DB");
            //            e.printStackTrace();
            return false;
        }
        finally{
            if (connection != null){
                try{
                    connection.close();
                }
                catch (SQLException e){
                }
            }
        }
    }
}
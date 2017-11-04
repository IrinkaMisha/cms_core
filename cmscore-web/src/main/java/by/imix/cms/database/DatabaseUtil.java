package by.imix.cms.database;

import by.imix.cms.prepare.checkStart.FullStartCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Util class for database
 * Created by miha on 29.10.2017.
 */
public class DatabaseUtil
{
    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtil.class);
    private static Map<String, Boolean> conectSuccess = new HashMap<>();
    /**
     * State for connect
     */
    public static Boolean state = null;

    /**
     * Method for check connect from property files
     */
    public static boolean isConnectWithReadProperty()
    {
        if (state == null)
        {
            Properties prop = new Properties();
            FullStartCondition ds = new FullStartCondition();
            try
            {
                //URL path=this.getClass().getClassLoader().getResources("../../").nextElement();
                prop.load(ds.getClass().getClassLoader().getResourceAsStream("project.properties"));
                //            prop.load(ResourcePathHolder.getServletContext().getResourceAsStream("/WEB-INF/classes/project.properties"));
                String jdbcPropertiesUrl = prop.getProperty("cms.jdbcsettingsurl");
                prop.load(ds.getClass().getClassLoader().getResourceAsStream(jdbcPropertiesUrl));
                //            String database = prop.getProperty("by.imix.cms.database");
                String driverName = prop.getProperty("jdbc.mysql.driverClassName");
                String url = prop.getProperty("jdbc.mysql.url");
                String user = prop.getProperty("jdbc.mysql.username");
                String password = prop.getProperty("jdbc.mysql.password");
                if (DatabaseUtil.isConnection(driverName, url, user, password))
                {
                    System.out.println("connect is sucsess");
                    state = false;
                }
            }
            catch (IOException e)
            {
                System.out.println("connect is bad");
            }
            state = true;
        }
        return state;
    }

    public static boolean isConnection(String driverName, String url, String user, String password, boolean newCheck)
    {
        String key = driverName + url + user + password;
        if (!newCheck)
        {
            if (conectSuccess.get(key) != null)
            {
                return conectSuccess.get(key);
            }
        }
        Connection connection = null;
        try
        {
            Class.forName(driverName);
            System.out.println("Trying to connect");
            connection = DriverManager.getConnection(url, user, password);

            logger.debug("Connection Established Successfull and the DATABASE NAME IS:" +
                         connection.getMetaData().getDatabaseProductName());
            System.out.println("Connection Established Successfull and the DATABASE NAME IS:" +
                               connection.getMetaData().getDatabaseProductName());
            conectSuccess.put(key, true);
            return true;
        }
        catch (Exception e)
        {
            System.out.println("Unable to make connection with DB");
            logger.warn("Unable to make connection with DB");
            conectSuccess.put(key, false);
            //            e.printStackTrace();
            return false;
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                }
            }
        }
    }

    /**
     * Method for check database
     *
     * @param driverName driverName
     * @param url url
     * @param user user
     * @param password password
     */
    public static boolean isConnection(String driverName, String url, String user, String password)
    {
        return isConnection(driverName, url, user, password, false);
    }
}
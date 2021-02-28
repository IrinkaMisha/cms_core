package by.imix.cms.prepare.checkstart;

import by.imix.cms.database.DatabaseUtil;
import by.imix.cms.database.FirstInicialize;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Class condition for check that all data for full load server is in application and we can start it else we will start
 * subconfiguration that allow indicate this data and save it to settings files or DB
 * Created by Mikhail_Kachanouski on 11/2/2017.
 */
public class FirstStartCondition implements Condition
{
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata)
    {
        System.out.println("Check possible full configuration");
        return isConnect();
    }

    public static boolean isConnect(){
        Properties prop = new Properties();
        FirstStartCondition ds=new FirstStartCondition();
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
                try {
                    Connection con = DriverManager.getConnection(url, user, password);
                    FirstInicialize.checkOrCreateTable(con);
//                    FirstInicialize.checkInitializationTable(con);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return false;
            }
        } catch (IOException e) {
            System.out.println("connect is bad");
        }

        return true;
    }
}

package by.imix.cms.prepare.firststart;

import by.imix.cms.database.DatabaseUtil;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Mikhail_Kachanouski on 11/2/2017.
 */
public class FirstOrFullStateCondition implements Condition
{
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata)
    {
        Properties prop = new Properties();
        try
        {
            //URL path=this.getClass().getClassLoader().getResources("../../").nextElement();
            prop.load(this.getClass().getClassLoader().getResourceAsStream("project.properties"));
            //            prop.load(ResourcePathHolder.getServletContext().getResourceAsStream("/WEB-INF/classes/project.properties"));
            String jdbcPropertiesUrl = prop.getProperty("cms.jdbcsettingsurl");
            prop.load(this.getClass().getClassLoader().getResourceAsStream(jdbcPropertiesUrl));
//            String database = prop.getProperty("by.imix.cms.database");
            String driverName = prop.getProperty("jdbc.mysql.driverClassName");
            String url = prop.getProperty("jdbc.mysql.url");
            String user = prop.getProperty("jdbc.mysql.username");
            String password = prop.getProperty("jdbc.mysql.password");
            if (DatabaseUtil.isConnection(driverName, url, user, password))
            {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}

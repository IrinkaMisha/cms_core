package by.imix.cms.prepare.firststart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import by.imix.cms.web.form.DatabaseForm;

import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class InitDataBase {
    private static final Logger logger = LoggerFactory.getLogger(InitDataBase.class);

    @Autowired
    ServletContext servletContext;

    private final String config="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
            "<beans xmlns=\"http://www.springframework.org/schema/beans\"\n"+
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"+
            "xsi:schemaLocation=\"http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd\">\n"+
            "<!-- after write file -->\n"+
            "<import resource=\"classpath:spring-web-config.xml\"/>\n"+
            "</beans>\n";

    @RequestMapping(value = "/cms.html")
    public String createNewUser(DatabaseForm databaseForm, Model model){
        List<String> database = new ArrayList<String>();
        List<String> dialect =new ArrayList<String>();
        database.add("com.mysql.jdbc.Driver");
        database.add("com.ibm.db2.jcc.DB2Driver");

        dialect.add("org.hibernate.dialect.MySQLDialect");
        dialect.add("org.hibernate.dialect.DB2Dialect");

        model.addAttribute("listDatabase", database);
        model.addAttribute("listDialect", dialect);
        model.addAttribute("databaseForm", databaseForm);
        return "initDataBase";
    }

    @RequestMapping(value = "/cms/initdatabase.html", method = RequestMethod.POST)
    public String saveNewUser(@Valid DatabaseForm databaseForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "initDataBase";
        }
        try {
            Class.forName(databaseForm.getDriverClassName());
            Connection con = DriverManager.getConnection(databaseForm.getUrl(), databaseForm.getLogin(), databaseForm.getPassword());
            con.close();

            File file = new File(servletContext.getRealPath("/WEB-INF/startSpring-servlet.xml"));
            File copyFile = new File(servletContext.getRealPath("/WEB-INF/temp.xml"));
            FileWriter writer = new FileWriter(copyFile, false);
            writer.write(config);
            writer.close();
            file.delete();
            copyFile.renameTo(file);

            Properties prop=new Properties();
            prop.load(servletContext.getResourceAsStream("/WEB-INF/classes/jdbc.properties"));
            prop.setProperty("org.hibernate.dialect", databaseForm.getDialect());
            prop.setProperty("jdbc.username", databaseForm.getLogin());
            prop.setProperty("jdbc.password", databaseForm.getPassword());
            prop.setProperty("jdbc.url", databaseForm.getUrl());
            prop.setProperty("jdbc.driverClassName",databaseForm.getDriverClassName());
            prop.setProperty("rw.ktc.cms.database","1");
            prop.store(new FileOutputStream(servletContext.getRealPath("/WEB-INF/classes/jdbc.properties")), null);

            return "success";
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.debug("Драйвер не найден");
            return "initDataBase";
        } catch (SQLException e) {
            e.printStackTrace();
            logger.debug("Нету соединения с базой");
            return "initDataBase";
        } catch (IOException e) {
            logger.debug("IOException");
            return "error";
        }
    }
}

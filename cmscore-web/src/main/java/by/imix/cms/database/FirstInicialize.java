package by.imix.cms.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * Created by miha on 09.01.2018.
 */
public final class FirstInicialize {
    private static final Logger logger = LoggerFactory.getLogger(FirstInicialize.class);
    private static final String SQL_REQUEST_EXECUTED_ACTIONS_TABLE = "CREATE TABLE IF NOT EXISTS `executed_actions` (\n" +
            "  `id` int(4) unsigned NOT NULL AUTO_INCREMENT,\n" +
            "  `type` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,\n" +
            "  `addition_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=MyISAM  DEFAULT CHARSET=utf8 AUTO_INCREMENT=13 ;";
    private static final String SQL_REQUEST_NEED_INITIALIZE = "SELECT * FROM executed_actions WHERE addition_info='%c' and type='initialize_table'";

    private static final String START_PROCEDURE = "BEGIN %c END;";

    /**
     *
     * @param connection connection
     */
    public static void executeScript(Connection connection, String script, String... pr){

        try{
            PreparedStatement psProcToexecute = connection.prepareStatement(String.format(script, pr));
            psProcToexecute.execute();
        }catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     *
     * @param connection connection
     */
    public static void executeScriptEndCheckResult(Connection connection, String script, String... pr){

        try{
            Statement statement = connection.createStatement();
            String sql = String.format(script, pr);
            statement.execute(sql);
            Boolean isRetrieved = statement.execute(sql);
            System.out.println("Is data retrieved: " + isRetrieved);
//            ResultSet resultSet = statement.executeQuery(sql);
//            while (resultSet.next()) {
        }catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Check Or Create Table EXECUTED_ACTIONS
     * @param connection connection
     */
    public static void checkOrCreateTable(Connection connection){
        System.out.println("Check Or Create Table EXECUTED_ACTIONS");
        executeScript(connection, SQL_REQUEST_EXECUTED_ACTIONS_TABLE);
    }

    /**
     * Check That table nameTable is initialize
     * @param connection connection
     * @param nameTable nameTable that need check
     */
    public static void checkInitializationTable(Connection connection, String nameTable){

        executeScriptEndCheckResult(connection, SQL_REQUEST_EXECUTED_ACTIONS_TABLE, nameTable);
    }

    private void executeSqlFile() {
        try {
            Runtime rt = Runtime.getRuntime();
            String executeSqlCommand = "psql -U (user) -h (domain) -f (script_name) (dbName)";
            Process pr = rt.exec(executeSqlCommand);
            int exitVal = pr.waitFor();
            System.out.println("Exited with error code " + exitVal);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void executeSqlFile2() {
        try {
            String line;
            Process p = Runtime.getRuntime().exec
                    ("psql -U username -d dbname -h serverhost -f scripfile.sql");
            BufferedReader input =
                    new BufferedReader
                            (new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();
        }
        catch (Exception err) {
            err.printStackTrace();
        }
    }
}

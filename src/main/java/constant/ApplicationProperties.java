package constant;

/**
 * Created by niitgyas on 17/9/17.
 * https://github.com/team172011
 */


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.rmi.server.ExportException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationProperties {
    public static String AUTHOR;
    public static String COMPANY;
    public static int MAXTHREAD;
    public static String DATABASE_URL;
    public static String DATABASE_USER;
    public static String DATABASE_PASSWORD;
    private Properties appProperties;
    private static final String propertiesFile = "config.properties";
    final static Logger logger = Logger.getLogger("Properties");

    public ApplicationProperties() {
        try {
            appProperties = new Properties();
            InputStream ins = getClass().getClassLoader().getResourceAsStream(propertiesFile);
            if (ins != null) {
                appProperties.load(ins);
                AUTHOR = appProperties.getProperty("author");
                COMPANY = appProperties.getProperty("company");
                MAXTHREAD = Integer.parseInt(appProperties.getProperty("maxthread"));
                DATABASE_URL = appProperties.getProperty("database_url");
                DATABASE_USER = appProperties.getProperty("database_user");
                DATABASE_PASSWORD = appProperties.getProperty("database_password");
                logger.log(Level.ALL,"This is configured : " + AUTHOR + COMPANY);

            } else {
                throw new FileNotFoundException("property file '" + propertiesFile + "' not found in the classpath");
            }
        }catch (Exception ee){
            System.err.print(ee.getMessage());
        }
    }
}

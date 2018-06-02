package dao;

import custom.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Class for Database Connections.
 */
public class Database {
    private Connection connection = null;
    private String url;
    private String userName;
    private String password;
    private static Logger logger = LoggerFactory.getLogger(Database.class);
    private Properties properties;

    /**
     * class has default access modifier and should
     * not be accessible outside the dao package.
     */

    Database() {
        try {
            initialize();
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, userName, password);
            logger.debug(properties.getProperty("user_name"));
        } catch (Exception e) {
            logger.error(e.toString());
        }

    }

    /**
     *
     */
    private void initialize() {
        try {
            properties = new ApplicationProperties().getPropertyObject("database.properties");
            logger.debug(properties.getProperty("user_name"));
            this.url = "jdbc:mysql://" + properties.getProperty("host_address") + ":" + properties.getProperty("port") + "/" + properties.getProperty("database") + "?autoReconnect=" + properties.getProperty("autoReconnect") + "&useSSL=" + properties.getProperty("useSSL");
            this.userName = properties.getProperty("user_name");
            this.password = properties.getProperty("password");
        } catch (Exception ex) {
            logger.error("database.properties file not found" + ex.toString());
        }
    }

    /**
     * returns connection to the database.
     * @return connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Close the connection.
     */
    @Override
    protected void finalize() throws Throwable {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}

package dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class Database {
    private Connection connection = null;
    private String url="jdbc:mysql://localhost:3306/finapp?autoReconnect=true&useSSL=false";
    private String user_name="root";
    private String password="mysql";
    private static Logger logger = LoggerFactory.getLogger(Database.class);

    /**
     * class has default access modifier and shuld not be accessible outside the dao package
     */
    Database(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user_name, password);
        } catch (SQLException e) {
            logger.error(e.toString());
        } catch (ClassNotFoundException e) {
            logger.error(e.toString());
        }

    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    protected void finalize() throws Throwable {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}

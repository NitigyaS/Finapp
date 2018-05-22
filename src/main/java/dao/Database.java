package application;

import java.sql.*;

public class Database {
    private Connection connection = null;

    public Database(String url , String user_name , String password){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user_name, password);
        } catch (SQLException e) {
            System.err.println("Error Recieved in Database Connection.");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Error Recieved in Database Connection.");
            e.printStackTrace();
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

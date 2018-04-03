package ua.markfolkin.nc.DBUtils;

import oracle.jdbc.driver.OracleDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBProcessor {

    private Connection connection;
    private final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private final String USER_NAME = "mark";
    private final String PASSWORD = "q123123145";

    public DBProcessor() throws SQLException {
        DriverManager.registerDriver(new OracleDriver());
        connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }

    public Connection getConnection() {
        return connection;
    }
}

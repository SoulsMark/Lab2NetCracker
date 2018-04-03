package ua.markfolkin.nc.DBUtils;

import java.sql.*;
import java.util.ArrayList;

public class CommandExecutor {

    private DBProcessor dbProcessor;
    private Connection connection;

    public CommandExecutor() throws SQLException {
        this.dbProcessor = new DBProcessor();
        this.connection = dbProcessor.getConnection();
    }

    public ResultSet getResultSet(String query) throws SQLException {
        Statement st = connection.createStatement();
        PreparedStatement ps = connection.prepareStatement(query);
        return st.executeQuery(query);
    }

    public void executeCommands(ArrayList<String> commands) throws SQLException {
        Statement st = connection.createStatement();
        for (String s : commands) {
            st.addBatch(s);
            System.out.println(s);
        }
        st.addBatch("COMMIT");
        st.executeBatch();
        st.clearBatch();
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

}

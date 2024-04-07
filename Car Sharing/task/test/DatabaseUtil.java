import org.hyperskill.hstest.exception.outcomes.WrongAnswer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class DatabaseUtil {

    private static Connection connection = null;
    private static final String databaseFilePath = "./src/carsharing/db/carsharing";

    public Connection getConnection() {
        if (connection != null) {
            return connection;
        }
        try {
            connection = DriverManager.getConnection("jdbc:h2:" + databaseFilePath);
        } catch (SQLException ignored) {
            System.out.println(ignored.getErrorCode());
            throw new WrongAnswer("Can't connect to the database.");
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ignored) {
                System.out.println(ignored.getErrorCode());
                throw new WrongAnswer("Can't close connection to the database.");
            }
            connection = null;
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            return getConnection().createStatement().executeQuery(query);
        } catch (SQLException exception) {
            throw new WrongAnswer("Can't execute query to the database.\n" +
                    "SQL Message:\n" + exception.getMessage());
        }
    }

    public boolean ifTableExist(String tableName) {
        try {
            tableName = tableName.toUpperCase();
            ResultSet resultSet = executeQuery("SHOW TABLES");
            while (resultSet.next()) {
                if (resultSet.getString("TABLE_NAME").equals(tableName)) {
                    return true;
                }
            }
            return false;
        } catch (SQLException exception) {
            throw new WrongAnswer("Can't execute query to the database.\n" +
                    "SQL Message:\n" + exception.getMessage());
        }
    }

    public void ifColumnsExist(String tableName, String[][] columns) {
        try {
            ResultSet resultSet = getConnection()
                    .createStatement()
                    .executeQuery("SHOW COLUMNS FROM " + tableName.toUpperCase());

            HashMap<String, String> correctColumns = new HashMap<>();
            for (String[] column : columns) {
                correctColumns.put(column[0], column[1]);
            }

            while (resultSet.next()) {
                String columnName = resultSet.getString("FIELD");
                if (correctColumns.containsKey(columnName)) {
                    if (!resultSet.getString("TYPE").contains(correctColumns.get(columnName))) {
                        throw new WrongAnswer("In the '" + tableName.toUpperCase() + "' table '" + columnName
                                + "' column should be of " + correctColumns.get(columnName) + " type.");
                    }
                    correctColumns.remove(columnName);
                }
            }
            if (!correctColumns.isEmpty()) {
                throw new WrongAnswer("Can't find in '" + tableName.toUpperCase() + "' table the following columns: " + correctColumns.toString());
            }
        } catch (SQLException exception) {
            throw new WrongAnswer("Can't execute query to the database.\n" +
                    "SQL Message:\n" + exception.getMessage());
        }
    }
}

import org.mariadb.jdbc.MariaDbDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static DBManager instance;
    private DataSource dataSource;

    DBManager() throws SQLException {
        // First try with a DataSource without pooling:
        MariaDbDataSource dataSource = new MariaDbDataSource();
        /*
         * That should fail (SQLException: too many connections)
         * Try now commenting the previous executable line
         * and using the following DataSource that supports pooling:
         * MariaDbPoolDataSource dataSource = new MariaDbPoolDataSource();
         * That should work!
         */
        dataSource.setUrl("jdbc:mariadb://localhost:3306/company");
        dataSource.setUser("root");
        dataSource.setPassword(null);
        this.dataSource = dataSource;
    }

    public static synchronized DBManager getInstance() throws SQLException {
        if(instance == null) {
            instance = new DBManager();
        }
        return instance;
    }


    public List<String> getAllUsers() throws SQLException {
        var list = new ArrayList<String>();
        // expect a SQLNonTransientConnectionException (too many connections)
        // see README.md
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM users")) {
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    list.add(resultSet.getString("username"));
                }
            }
        } finally {
            if (connection != null) {
                System.out.println("Closing database connection...");
                connection.close();
                System.out.println("Connection valid: " + connection.isValid(5));
            }
        }
        return list;
    }
}

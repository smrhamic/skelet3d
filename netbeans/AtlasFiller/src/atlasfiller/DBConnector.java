package atlasfiller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Michal
 */
public class DBConnector {
    
    static String dbName = "//localhost:1527/atlas";
    static String userName = "app";
    static String password = "superman";
    
    public static Connection getConnection() throws SQLException {

        Connection conn;
        Properties connectionProps = new Properties();
        connectionProps.put("user", userName);
        connectionProps.put("password", password);


        conn = DriverManager.getConnection(
                   "jdbc:derby" + ":" + dbName,
                   connectionProps);
        System.out.println("Connected to database");
        return conn;
    }
}

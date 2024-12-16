import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * A utility class for establishing a database connection.
 *
 * @author User
 */
public class connection {

    // Database connection parameters
    public static final String DB_URL = "jdbc:mysql://localhost:3306/KURTpyuteran";
   public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "";

    // A static connection object
   static Connection con = null;

    /**
     * Retrieves a database connection. If a connection does not exist, a new one will be created.
     *
     * @return a Connection object to the database
     */
    public static Connection getConnection() {
        if (con == null) { // Establish connection if it hasn't been initialized
            try {
                con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Connection established successfully!");
            } catch (SQLException e) {
                System.err.println("Failed to establish a connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return con;
    }
}

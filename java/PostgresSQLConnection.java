
import java.sql.*;

/**
 * @author Nannagaru
 */
public class PostgresSQLConnection {

    private static Connection postgresSQLConnection;

    public static Connection getConnection() {
        if (postgresSQLConnection == null) {
            try {
                Class.forName("org.postgresql.Driver");
                postgresSQLConnection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "postgres");
                System.out.println("Instance successfully created");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error: unable to load driver class!");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return postgresSQLConnection;
    }

    public static ResultSet executeQuery(String selectQuery) {
        System.out.println(selectQuery);
        Connection con = getConnection();
        try {
            Statement stmt = con.createStatement();
            return stmt.executeQuery(selectQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void executeUpdate(String selectQuery) {
        System.out.println(selectQuery);
        Connection con = getConnection();
        try {
            Statement stmt = con.createStatement();
            stmt.executeUpdate(selectQuery);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}



package practice.java;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/atm_banking";

    private static final String USER = "root";

    // Apna actual MySQL password yahan likho.
    private static final String PASSWORD =
            System.getenv("DB_PASSWORD");

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                URL, USER, PASSWORD
        );
    }

    public static void main(String[] args) {

        try (Connection connection = getConnection()) {
            System.out.println(
                    "MySQL connected successfully!"
            );

        } catch (SQLException e) {
            System.out.println(
                    "Database connection failed: "
                            + e.getMessage()
            );
        }
    }
}

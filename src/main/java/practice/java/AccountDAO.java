
package practice.java;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class AccountDAO {

    public boolean createAccount(Account account) {

        String sql = "INSERT INTO accounts " +
                "(account_number, pin_hash, balance) " +
                "VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, account.getAccountNumber());
            statement.setString(2, account.getPinHash());
            statement.setBigDecimal(3, account.getBalance());

            int rows = statement.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Account creation failed: "
                    + e.getMessage());
            return false;
        }
    }

    public Account findByAccountNumber(String accountNumber) {
        String sql = "SELECT account_number, pin_hash, balance " +
                "FROM accounts WHERE account_number = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, accountNumber);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new Account(
                            result.getString("account_number"),
                            result.getString("pin_hash"),
                            result.getBigDecimal("balance")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Account lookup failed: " + e.getMessage());
        }

        return null;
    }


    public boolean deposit(String accountNumber, java.math.BigDecimal amount) {
        if (amount == null ||
                amount.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            System.out.println("Amount must be greater than zero.");
            return false;
        }

        String updateSql =
                "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";

        String transactionSql =
                "INSERT INTO transactions " +
                        "(account_number, transaction_type, amount) VALUES (?, 'DEPOSIT', ?)";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement update =
                         connection.prepareStatement(updateSql);
                 PreparedStatement insert =
                         connection.prepareStatement(transactionSql)) {

                update.setBigDecimal(1, amount);
                update.setString(2, accountNumber);

                if (update.executeUpdate() != 1) {
                    connection.rollback();
                    System.out.println("Account not found.");
                    return false;
                }

                insert.setString(1, accountNumber);
                insert.setBigDecimal(2, amount);
                insert.executeUpdate();

                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Deposit failed: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }


    public boolean withdraw(String accountNumber, java.math.BigDecimal amount) {
        if (amount == null ||
                amount.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            System.out.println("Amount must be greater than zero.");
            return false;
        }

        String updateSql =
                "UPDATE accounts SET balance = balance - ? " +
                        "WHERE account_number = ? AND balance >= ?";

        String transactionSql =
                "INSERT INTO transactions " +
                        "(account_number, transaction_type, amount) " +
                        "VALUES (?, 'WITHDRAWAL', ?)";

        try (Connection connection = DatabaseConnection.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement update =
                         connection.prepareStatement(updateSql);
                 PreparedStatement insert =
                         connection.prepareStatement(transactionSql)) {

                update.setBigDecimal(1, amount);
                update.setString(2, accountNumber);
                update.setBigDecimal(3, amount);

                if (update.executeUpdate() != 1) {
                    connection.rollback();
                    System.out.println(
                            "Account not found or insufficient balance."
                    );
                    return false;
                }

                insert.setString(1, accountNumber);
                insert.setBigDecimal(2, amount);
                insert.executeUpdate();

                connection.commit();
                return true;

            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Withdrawal failed: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }


    public void showTransactionHistory(String accountNumber) {
        String sql = "SELECT transaction_id, transaction_type, amount, " +
                "transaction_time FROM transactions " +
                "WHERE account_number = ? " +
                "ORDER BY transaction_time DESC, transaction_id DESC";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, accountNumber);

            try (ResultSet result = statement.executeQuery()) {
                System.out.println("\n====== TRANSACTION HISTORY ======");

                boolean found = false;

                while (result.next()) {
                    found = true;

                    System.out.println(
                            "ID: " + result.getLong("transaction_id") +
                                    " | Type: " + result.getString("transaction_type") +
                                    " | Amount: ₹" + result.getBigDecimal("amount") +
                                    " | Date: " + result.getTimestamp("transaction_time")
                    );
                }

                if (!found) {
                    System.out.println("No transactions found.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Could not load history: " + e.getMessage());
        }
    }

}

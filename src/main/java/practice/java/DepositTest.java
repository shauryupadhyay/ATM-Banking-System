
package practice.java;

import java.math.BigDecimal;
import java.util.Scanner;

public class DepositTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AccountDAO dao = new AccountDAO();

        System.out.print("Enter account number: ");
        String accountNumber = sc.nextLine();

        System.out.print("Enter deposit amount: ");
        String input = sc.nextLine();

        try {
            BigDecimal amount = new BigDecimal(input);

            if (dao.deposit(accountNumber, amount)) {
                Account account = dao.findByAccountNumber(accountNumber);

                System.out.println("Deposit successful!");
                System.out.println("Updated balance: ₹" +
                        account.getBalance());
            } else {
                System.out.println(
                        "Deposit failed. Check account number and amount."
                );
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid amount.");
        }

        sc.close();
    }
}

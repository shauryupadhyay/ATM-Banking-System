
package practice.java;

import java.math.BigDecimal;
import java.util.Scanner;

public class WithdrawTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AccountDAO dao = new AccountDAO();

        System.out.print("Enter account number: ");
        String accountNumber = sc.nextLine();

        System.out.print("Enter withdrawal amount: ");
        String input = sc.nextLine();

        try {
            BigDecimal amount = new BigDecimal(input);

            if (dao.withdraw(accountNumber, amount)) {
                Account account = dao.findByAccountNumber(accountNumber);

                System.out.println("Withdrawal successful!");
                System.out.println("Remaining balance: ₹"
                        + account.getBalance());
            } else {
                System.out.println("Withdrawal unsuccessful.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid amount.");
        }

        sc.close();
    }
}

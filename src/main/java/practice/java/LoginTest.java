
package practice.java;

import java.util.Scanner;

public class LoginTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AccountDAO dao = new AccountDAO();

        System.out.print("Enter account number: ");
        String accountNumber = sc.nextLine();

        System.out.print("Enter PIN: ");
        String pin = sc.nextLine();

        Account account = dao.findByAccountNumber(accountNumber);

        if (account != null &&
                PasswordUtil.verifyPin(pin, account.getPinHash())) {
            System.out.println("Login successful!");
            System.out.println("Account: " + account.getAccountNumber());
            System.out.println("Balance: ₹" + account.getBalance());
        } else {
            System.out.println("Invalid account number or PIN.");
        }

        sc.close();
    }
}

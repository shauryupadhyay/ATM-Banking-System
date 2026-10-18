
package practice.java;

import java.math.BigDecimal;
import java.util.Scanner;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final AccountDAO dao = new AccountDAO();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n====== ATM BANKING SYSTEM ======");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> createAccount();
                case "2" -> login();
                case "0" -> {
                    System.out.println("Thank you for using our ATM!");
                    sc.close();
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void createAccount() {
        System.out.print("Enter new account number: ");
        String accountNumber = sc.nextLine().trim();

        if (accountNumber.isEmpty()) {
            System.out.println("Account number cannot be empty.");
            return;
        }

        System.out.print("Create a 4-digit PIN: ");
        String pin = sc.nextLine();

        if (!pin.matches("\\d{4}")) {
            System.out.println("PIN must contain exactly 4 digits.");
            return;
        }

        String pinHash = PasswordUtil.hashPin(pin);

        Account account = new Account(
                accountNumber, pinHash, BigDecimal.ZERO
        );

        if (dao.createAccount(account)) {
            System.out.println("Account created successfully!");
        } else {
            System.out.println(
                    "Account creation failed. Check if the account number already exists."
            );
        }
    }

    private static void login() {
        System.out.print("Enter account number: ");
        String accountNumber = sc.nextLine().trim();

        System.out.print("Enter PIN: ");
        String pin = sc.nextLine();

        Account account = dao.findByAccountNumber(accountNumber);

        if (account == null ||
                !PasswordUtil.verifyPin(pin, account.getPinHash())) {
            System.out.println("Invalid account number or PIN.");
            return;
        }

        System.out.println("Login successful!");
        accountMenu(accountNumber);
    }

    private static void accountMenu(String accountNumber) {
        while (true) {
            System.out.println("\n====== ACCOUNT MENU ======");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Transaction History");
            System.out.println("0. Logout");
            System.out.print("Enter choice: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> checkBalance(accountNumber);
                case "2" -> depositMoney(accountNumber);
                case "3" -> withdrawMoney(accountNumber);
                case "4" -> showTransactionHistory(accountNumber);
                case "0" -> {
                    System.out.println("Logged out successfully.");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void checkBalance(String accountNumber) {
        Account account = dao.findByAccountNumber(accountNumber);

        if (account != null) {
            System.out.println("Current balance: ₹" + account.getBalance());
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void depositMoney(String accountNumber) {
        System.out.print("Enter deposit amount: ");
        try {
            BigDecimal amount = new BigDecimal(sc.nextLine().trim());

            if (dao.deposit(accountNumber, amount)) {
                System.out.println("Deposit successful!");
                checkBalance(accountNumber);
            } else {
                System.out.println("Deposit failed. Check the amount.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid amount.");
        }
    }

    private static void withdrawMoney(String accountNumber) {
        System.out.print("Enter withdrawal amount: ");
        try {
            BigDecimal amount = new BigDecimal(sc.nextLine().trim());

            if (dao.withdraw(accountNumber, amount)) {
                System.out.println("Withdrawal successful!");
                checkBalance(accountNumber);
            } else {
                System.out.println(
                        "Withdrawal failed. Check balance and amount."
                );
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid amount.");
        }
    }

    private static void showTransactionHistory(String accountNumber) {
        dao.showTransactionHistory(accountNumber);
    }
}

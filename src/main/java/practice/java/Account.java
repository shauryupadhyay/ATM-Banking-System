
package practice.java;

import java.math.BigDecimal;

public class Account {

    private String accountNumber;
    private String pinHash;
    private BigDecimal balance;

    public Account(String accountNumber, String pinHash, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.pinHash = pinHash;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPinHash() {
        return pinHash;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}

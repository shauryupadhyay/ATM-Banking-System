# ATM Banking System

A console-based ATM Banking System developed using Java and MySQL.

## Features
- Create a bank account
- Secure PIN hashing using PBKDF2
- Login authentication
- Check account balance
- Deposit money
- Withdraw money
- View transaction history
- Prevent invalid transactions
- Store account and transaction data in MySQL
- Database transactions for reliable balance updates

## Technologies Used
- Java
- MySQL
- JDBC
- Maven
- IntelliJ IDEA
- Git and GitHub

## Database Setup
1. Install MySQL and start the MySQL server.
2. Create a database named `atm_banking`.
3. Configure the database connection.
4. Set the `DB_PASSWORD` environment variable to your MySQL password.
5. Run the project from IntelliJ IDEA.

## Security
- PINs are hashed rather than stored as plain text.
- Database credentials should not be committed to GitHub.

## Author
Shaury Upadhyay

GitHub: https://github.com/shauryupadhyay
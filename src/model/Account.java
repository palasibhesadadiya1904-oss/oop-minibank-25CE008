
package model;

import util.WithdrawRule;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import java.io.Serializable;
import java.util.Objects;

public abstract class Account implements Transactable, Serializable {

    private static final long serialVersionUID = 1L;

    private final int accountNumber;
    private String ownerName;
    protected long balance;
    private boolean active;

    private static int accountCounter = 1000;

    public Account(String ownerName, long balance) {
        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = balance;
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }

    private static synchronized int generateAccountNumber() {
        return ++accountCounter;
    }

    @Override
    public synchronized void deposit(long amount)
            throws InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Deposit amount must be positive");
        }

        balance += amount;
    }

    @Override
    public synchronized boolean withdraw(long amount)
            throws InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Withdrawal amount must be positive");
        }

        if (!active || !canWithdraw(amount) || amount > balance) {
            return false;
        }

        balance -= amount;
        return true;
    }

    public synchronized void transfer(Account to, long amount)
            throws InsufficientFundsException, InvalidAmountException {

        if (to == null) {
            throw new IllegalArgumentException(
                    "Destination account cannot be null");
        }

        if (to == this) {
            throw new IllegalArgumentException(
                    "Cannot transfer to the same account");
        }

        if (!withdraw(amount)) {
            throw new InsufficientFundsException(
                    "Withdrawal failed", amount);
        }

        try {
            to.deposit(amount);
        } catch (InvalidAmountException e) {
            try {
                deposit(amount);
            } catch (InvalidAmountException ignored) {
            }

            throw e;
        }
    }

    public static void transferSafely(
            Account from, Account to, long amount)
            throws InsufficientFundsException, InvalidAmountException {

        if (from == null || to == null) {
            throw new IllegalArgumentException(
                    "Accounts cannot be null");
        }

        if (from == to) {
            throw new IllegalArgumentException(
                    "Cannot transfer to the same account");
        }

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Transfer amount must be positive");
        }

        Account first =
                from.accountNumber < to.accountNumber ? from : to;

        Account second = first == from ? to : from;

        synchronized (first) {
            synchronized (second) {

                if (!from.active) {
                    throw new InsufficientFundsException(
                            "Source account is inactive", amount);
                }

                if (!from.canWithdraw(amount)) {
                    throw new InsufficientFundsException(
                            "Withdrawal not allowed", amount);
                }

                if (amount > from.balance) {
                    throw new InsufficientFundsException(
                            "Insufficient balance",
                            amount - from.balance);
                }

                from.balance -= amount;
                to.balance += amount;
            }
        }
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public synchronized long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

    public WithdrawRule getWithdrawRule() {
        return (account, amount) -> account.canWithdraw(amount);
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", ownerName='" + ownerName + '\'' +
                ", balance=" + balance +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Account account = (Account) obj;
        return accountNumber == account.accountNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}

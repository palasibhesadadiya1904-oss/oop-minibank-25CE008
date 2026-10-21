package model;
import model.annotation.Id;
import model.annotation.Positive;
import java.util.Objects;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;


public abstract class Account implements Transactable, InterestBearing  {

    @Id
    private String accountNumber;

    private String ownerName;

    @Positive
    private long balance;
    private boolean active;

    private static long accountCounter = 0;

    private static String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
    }

    public Account(String ownerName, long balance) {

        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = balance;
        this.active = true;
    }

    public Account(String ownerName) {
        this(ownerName, 0);
    }
public void transfer(Account to, long amount) throws BankException {

    try {
        if (to == null) {
            throw new BankException("Destination account not found.");
        }

        withdraw(amount);
        to.deposit(amount);

    } catch (BankException e) {
        throw e;

    } finally {
        System.out.println("Transfer operation completed.");
    }
}
    public abstract double interestRate();

    public abstract boolean canWithdraw(long amount);

   @Override
public synchronized void deposit(long amount) throws InvalidAmountException {
    if (amount <= 0) {
        throw new InvalidAmountException(
                "Deposit amount must be greater than zero.");
    }

    balance += amount;
}

@Override
public   synchronized  boolean withdraw(long amount)
        throws InsufficientFundsException, InvalidAmountException {

    if (amount <= 0) {
        throw new InvalidAmountException(
                "Withdrawal amount must be greater than zero.");
    }

    if (!canWithdraw(amount)) {
        long shortfall = amount - balance;

        throw new InsufficientFundsException(
                "Insufficient funds. Required amount: Rs. " + amount,
                Math.max(0, shortfall));
    }

    balance -= amount;
    return true;
} 

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public long getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "Account Number: " + accountNumber
                + ", Owner: " + ownerName
                + ", Balance: Rs. " + balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Account)) {
            return false;
        }

        Account other = (Account) o;
        return accountNumber.equals(other.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNumber);
    }
}

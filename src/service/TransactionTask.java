
package service;

import model.Account;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;

public class TransactionTask implements Runnable {

    private final Account account;
    private final long amount;
    private final boolean deposit;

    public TransactionTask(Account account, long amount, boolean deposit) {
        this.account = account;
        this.amount = amount;
        this.deposit = deposit;
    }

    @Override
    public void run() {
        try {
            if (deposit) {
                account.deposit(amount);
                System.out.println(Thread.currentThread().getName()
                        + " deposited Rs. " + amount);
            } else {
                account.withdraw(amount);
                System.out.println(Thread.currentThread().getName()
                        + " withdrew Rs. " + amount);
            }
        } catch (InvalidAmountException e) {
            System.out.println("Invalid transaction: " + e.getMessage());
        } catch (InsufficientFundsException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        }
    }
}

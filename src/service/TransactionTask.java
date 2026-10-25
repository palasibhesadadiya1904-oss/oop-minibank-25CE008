
package service;

import model.Account;
import exception.InvalidAmountException;

public class TransactionTask implements Runnable {

    private final Account account;
    private final String transactionType;
    private final long amount;

    public TransactionTask(
            Account account, String transactionType, long amount) {
        this.account = account;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    @Override
    public void run() {
        try {
            if (transactionType.equalsIgnoreCase("DEPOSIT")) {
                account.deposit(amount);
                System.out.println(
                        "Deposit successful: Rs. " + amount);

            } else if (transactionType.equalsIgnoreCase("WITHDRAW")) {
                boolean success = account.withdraw(amount);

                if (success) {
                    System.out.println(
                            "Withdrawal successful: Rs. " + amount);
                } else {
                    System.out.println(
                            "Withdrawal failed: insufficient balance "
                            + "or withdrawal not allowed.");
                }

            } else {
                System.out.println("Invalid transaction type.");
            }

        } catch (InvalidAmountException e) {
            System.out.println(
                    "Transaction failed: " + e.getMessage());
        }
    }
}


package model;

import exception.InsufficientFundsException;
import exception.InvalidAmountException;

public class FixedDepositAccount extends Account {

    public FixedDepositAccount(String ownerName, long balance) {
        super(ownerName, balance);
    }

    @Override
    public double interestRate() {
        return 7.0;
    }

    @Override
    public boolean canWithdraw(long amount) {
        return false;
    }

    @Override
    public boolean withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Withdrawal amount must be greater than zero.");
        }

        throw new InsufficientFundsException(
                "Withdrawal is not allowed for a fixed deposit account.",
                amount);
    }
}

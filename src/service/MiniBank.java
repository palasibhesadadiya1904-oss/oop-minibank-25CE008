
package service;

import java.util.Scanner;
import util.AnnotationValidator;
import model.Account;
import model.Customer;
import model.SavingsAccount;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.Premium;
import util.Validator;
import util.Command;
import exception.InvalidAmountException;
import exception.InsufficientFundsException;
import exception.BankException;
import util.CommandParser;
import util.StatementFormatter;
import util.WithdrawRule;

import static java.lang.System.out;

public class MiniBank {

    record BankInfo(String name, String branch) {}

    enum MenuOption {
        OPEN_ACCOUNT,
        DEPOSIT,
        WITHDRAW,
        TRANSFER,
        EXIT
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BankInfo bank = new BankInfo("MiniBank", "CHARUSAT Branch");

        out.println("==================================");
        out.println("Welcome to " + bank.name());
        out.println("Branch: " + bank.branch());
        out.println("==================================");

        SavingsAccount acc1 = new SavingsAccount("Rahul", 1000, 500);
        CurrentAccount acc2 = new CurrentAccount("Priya", 0, 2000);
        FixedDepositAccount acc3 = new FixedDepositAccount("Amit", 2000);

        Account[] accounts = {acc1, acc2, acc3};

        try {
            acc1.deposit(500);
            acc1.withdraw(200);
        } catch (InvalidAmountException e) {
            out.println("Invalid amount: " + e.getMessage());
        } catch (InsufficientFundsException e) {
            out.println("Withdrawal failed: " + e.getMessage());
            out.println("Shortfall: Rs. " + e.getShortfall());
        } finally {
            out.println("Transaction attempt finished.");
        }

        out.println("\n----- Account Details -----");

        for (Account acc : accounts) {
            out.println(acc);
            out.println("Interest Rate: " + acc.interestRate() + "%");
            out.println();
        }

        out.println("----- Account Comparison -----");
        out.println("Are acc1 and acc2 equal? " + acc1.equals(acc2));

        out.println("\n----- Withdrawal Tests -----");

        try {
            acc1.withdraw(2000);
            out.println("Rahul's withdrawal successful.");
        } catch (InsufficientFundsException e) {
            out.println("Rahul: " + e.getMessage());
            out.println("Shortfall: Rs. " + e.getShortfall());
        } catch (InvalidAmountException e) {
            out.println("Rahul: " + e.getMessage());
        }

        try {
            acc2.withdraw(2500);
            out.println("Priya's withdrawal successful.");
        } catch (InsufficientFundsException e) {
            out.println("Priya: " + e.getMessage());
            out.println("Shortfall: Rs. " + e.getShortfall());
        } catch (InvalidAmountException e) {
            out.println("Priya: " + e.getMessage());
        }

        try {
            acc3.withdraw(500);
            out.println("Amit's withdrawal successful.");
        } catch (InsufficientFundsException e) {
            out.println("Amit: " + e.getMessage());
            out.println("Shortfall: Rs. " + e.getShortfall());
        } catch (InvalidAmountException e) {
            out.println("Amit: " + e.getMessage());
        }

        out.println("\n----- instanceof Check -----");

        for (Account acc : accounts) {
            if (acc instanceof SavingsAccount savings) {
                out.println(savings.getOwnerName()
                        + " has a Savings Account.");
            }
        }

        out.println("\n----- Customer and Address -----");

        Customer.Address address =
                new Customer.Address("Main Road", "Anand", "388001");

        Customer customer1 =
                new Customer("Rahul", "rahul@gmail.com",
                        "9876543210", address);

        Customer customer2 = customer1.clone();

        out.println("Customer ID: " + customer1.getCustomerId());
        out.println("Customer Name: " + customer1.getName());
        out.println("Address: " + customer1.getAddress().getLine());
        out.println("City: " + customer1.getAddress().getCity());
        out.println("Pincode: " + customer1.getAddress().getPincode());
        out.println("Cloned Customer ID: " + customer2.getCustomerId());

        out.println("\n----- Validator Tests -----");

        out.println("Valid mobile: "
                + Validator.isValidMobile("9876543210"));
        out.println("Invalid mobile: "
                + Validator.isValidMobile("12345"));

        out.println("Valid email: "
                + Validator.isValidEmail("rahul@gmail.com"));
        out.println("Invalid email: "
                + Validator.isValidEmail("rahulgmail.com"));

        out.println("Valid PAN: "
                + Validator.isValidPan("ABCDE1234F"));
        out.println("Invalid PAN: "
                + Validator.isValidPan("ABC123"));

        out.println("Valid IFSC: "
                + Validator.isValidIfsc("SBIN0001234"));
        out.println("Invalid IFSC: "
                + Validator.isValidIfsc("SBI123"));

        out.println("\n----- Command Parser -----");

        Command command = CommandParser.parse("DEPOSIT AC0001 500");

        out.println("Type: " + command.type());
        out.println("Account Number: " + command.accountNumber());
        out.println("Amount: " + command.amount());

        out.println("\n" + StatementFormatter.buildStatement(acc1));

        out.println("\n----- Yearly Interest -----");

        for (Account acc : accounts) {
            out.println(acc.getOwnerName()
                    + ": Rs. " + acc.yearlyInterest());
        }

        out.println("\n----- Withdraw Rule -----");

        WithdrawRule rule1 = new WithdrawRule() {
            @Override
            public boolean allow(Account account, long amount) {
                return account.canWithdraw(amount);
            }
        };

        WithdrawRule rule2 =
                (account, amount) -> account.canWithdraw(amount);

        out.println("Anonymous class: " + rule1.allow(acc1, 200));
        out.println("Lambda expression: " + rule2.allow(acc1, 200));

        out.println("\n----- Premium Check -----");

        for (Account acc : accounts) {
            if (acc instanceof Premium) {
                out.println(acc.getOwnerName() + " has Premium status.");
            }
        }

        out.println("\n----- Annotation Validation -----");

        SavingsAccount invalidAccount =
                new SavingsAccount("Test User", -500, 100);

        String[] errors = AnnotationValidator.validate(invalidAccount);

        if (errors.length == 0) {
            out.println("No validation errors.");
        } else {
            for (String error : errors) {
                out.println(error);
            }
        }

        try (BankSession session = new BankSession()) {
            out.println("Performing banking operations.");
        } catch (Exception e) {
            out.println("Session error: " + e.getMessage());
        }

        out.println("\n----- Transfer Test -----");

        try {
            acc1.transfer(acc2, 100);
            out.println("Transfer successful.");
            out.println("Rahul's balance: Rs. " + acc1.getBalance());
out.println("Priya's balance: Rs. " + acc2.getBalance());
        } catch (BankException e) {
            out.println("Transfer failed: " + e.getMessage());
        }
        
System.out.println("\n----- Multithreading Test -----");

SavingsAccount sharedAccount =
        new SavingsAccount("Thread Test", 0, 0);

int threadCount = 10;
int times = 1000;
long amount = 1;

Thread[] threads = new Thread[threadCount];

for (int i = 0; i < threadCount; i++) {
    AccountWorker worker =
            new AccountWorker(sharedAccount, times, amount);

    threads[i] = new Thread(worker, "Worker-" + (i + 1));
    threads[i].start();
}

for (Thread thread : threads) {
    try {
        thread.join();
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.out.println("Main thread was interrupted.");
        return;
    }
}

long expectedBalance = (long) threadCount * times * amount;

System.out.println("Expected balance: Rs. " + expectedBalance);
System.out.println("Actual balance: Rs. " + sharedAccount.getBalance());

System.out.println("\n----- Transaction Processor -----");

SavingsAccount batchAccount =
        new SavingsAccount("Batch User", 1000, 0);

TransactionProcessor processor = new TransactionProcessor();

for (int i = 0; i < 10; i++) {
    processor.submit(new TransactionTask(batchAccount, 100, true));
}

for (int i = 0; i < 5; i++) {
    processor.submit(new TransactionTask(batchAccount, 50, false));
}

processor.stop();

System.out.println("Expected balance: Rs. 1750");
System.out.println("Actual balance: Rs. " + batchAccount.getBalance());

System.out.println("\n----- Deadlock Prevention Test -----");

Thread transfer1 = new Thread(() -> {
    try {
        Account.transferSafely(acc1, acc2, 10);
        out.println("Rahul to Priya transfer completed.");
    } catch (BankException e) {
        out.println("Transfer failed: " + e.getMessage());
    }
}, "Transfer-1");

Thread transfer2 = new Thread(() -> {
    try {
        Account.transferSafely(acc2, acc1, 10);
        out.println("Priya to Rahul transfer completed.");
    } catch (BankException e) {
        out.println("Transfer failed: " + e.getMessage());
    }
}, "Transfer-2");

transfer1.start();
transfer2.start();

try {
    transfer1.join();
    transfer2.join();
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    out.println("Main thread interrupted.");
}

out.println("Both transfer threads finished.");

        int choice;
System.out.println("\n----- Producer Consumer -----");
ProducerConsumerDemo.runDemo();
        do {
            out.println("\n===== MINIBANK MENU =====");
            out.println("1. Open Account");
            out.println("2. Deposit");
            out.println("3. Withdraw");
            out.println("4. Transfer");
            out.println("5. Exit");
            out.print("Enter your choice: ");

            choice = sc.nextInt();

            MenuOption option = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.EXIT;
                default -> null;
            };

            if (option == null) {
                out.println("Invalid choice. Please try again.");
                continue;
            }

            switch (option) {
                case OPEN_ACCOUNT ->
                    out.println("Open Account - to be implemented in a later lab.");

                case DEPOSIT ->
                    out.println("Deposit - to be implemented in a later lab.");

                case WITHDRAW ->
                    out.println("Withdraw - to be implemented in a later lab.");

                case TRANSFER ->
                    out.println("Transfer - to be implemented in a later lab.");

                case EXIT ->
                    out.println("Thank you for using MiniBank. Goodbye!");
            }

        } while (choice != 5);

        sc.close();
    }
}

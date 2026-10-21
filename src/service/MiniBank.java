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

        System.out.println("==================================");
        System.out.println("Welcome to " + bank.name());
        System.out.println("Branch: " + bank.branch());
        System.out.println("==================================");

        SavingsAccount acc1 = new SavingsAccount("Rahul", 1000, 500);
        CurrentAccount acc2 = new CurrentAccount("Priya", 0, 2000);
        FixedDepositAccount acc3 = new FixedDepositAccount("Amit", 2000);

        Account[] accounts = {acc1, acc2, acc3};

        acc1.deposit(500);
        acc2.deposit(1000);
        acc1.withdraw(200);
        acc3.withdraw(500);

        System.out.println("\n----- Account Details -----");

        for (Account acc : accounts) {
            System.out.println(acc);
            System.out.println("Interest Rate: " + acc.interestRate() + "%");
            System.out.println();
        }

        System.out.println("----- Account Comparison -----");
        System.out.println("Are acc1 and acc2 equal? " + acc1.equals(acc2));

        System.out.println("\n----- Withdrawal Tests -----");

        System.out.println("Rahul withdraws Rs. 2000: "
                + acc1.withdraw(2000));

        System.out.println("Priya withdraws Rs. 2500: "
                + acc2.withdraw(2500));

        System.out.println("Amit withdraws Rs. 500: "
                + acc3.withdraw(500));

        System.out.println("\n----- instanceof Check -----");

        for (Account acc : accounts) {
            if (acc instanceof SavingsAccount savings) {
                System.out.println(savings.getOwnerName()
                        + " has a Savings Account.");
            }
        }

        System.out.println("\n----- Customer and Address -----");

        Customer.Address address =
                new Customer.Address("Main Road", "Anand", "388001");

        Customer customer1 =
                new Customer("Rahul", "rahul@gmail.com", "9876543210", address);

        Customer customer2 = customer1.clone();

        System.out.println("Customer ID: " + customer1.getCustomerId());
        System.out.println("Customer Name: " + customer1.getName());
        System.out.println("Address: " + customer1.getAddress().getLine());
        System.out.println("City: " + customer1.getAddress().getCity());
        System.out.println("Pincode: " + customer1.getAddress().getPincode());
        System.out.println("Cloned Customer ID: " + customer2.getCustomerId());

        System.out.println("\n----- Validator Tests -----");

        System.out.println("Valid mobile: "
                + Validator.isValidMobile("9876543210"));
        System.out.println("Invalid mobile: "
                + Validator.isValidMobile("12345"));

        System.out.println("Valid email: "
                + Validator.isValidEmail("rahul@gmail.com"));
        System.out.println("Invalid email: "
                + Validator.isValidEmail("rahulgmail.com"));

        System.out.println("Valid PAN: "
                + Validator.isValidPan("ABCDE1234F"));
        System.out.println("Invalid PAN: "
                + Validator.isValidPan("ABC123"));

        System.out.println("Valid IFSC: "
                + Validator.isValidIfsc("SBIN0001234"));
        System.out.println("Invalid IFSC: "
                + Validator.isValidIfsc("SBI123"));

        System.out.println("\n----- Command Parser -----");

        Command command = CommandParser.parse("DEPOSIT AC0001 500");

        System.out.println("Type: " + command.type());
        System.out.println("Account Number: " + command.accountNumber());
        System.out.println("Amount: " + command.amount());

        System.out.println("\n" + StatementFormatter.buildStatement(acc1));
        
System.out.println("\n----- Yearly Interest -----");

for (Account acc : accounts) {
    System.out.println(acc.getOwnerName()
            + ": Rs. " + acc.yearlyInterest());
}

System.out.println("\n----- Withdraw Rule -----");

WithdrawRule rule1 = new WithdrawRule() {
    @Override
    public boolean allow(Account account, long amount) {
        return account.canWithdraw(amount);
    }
};

WithdrawRule rule2 =
        (account, amount) -> account.canWithdraw(amount);

System.out.println("Anonymous class: "
        + rule1.allow(acc1, 200));

System.out.println("Lambda expression: "
        + rule2.allow(acc1, 200));

System.out.println("\n----- Premium Check -----");

for (Account acc : accounts) {
    if (acc instanceof Premium) {
        System.out.println(acc.getOwnerName() + " has Premium status.");
    }
}

        System.out.println("\n----- Annotation Validation -----");

SavingsAccount invalidAccount =
        new SavingsAccount("Test User", -500, 100);

String[] errors = AnnotationValidator.validate(invalidAccount);

if (errors.length == 0) {
    System.out.println("No validation errors.");
} else {
    for (String error : errors) {
        System.out.println(error);
    }
}
        int choice;

        do {
            System.out.println("\n===== MINIBANK MENU =====");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

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
                System.out.println("Invalid choice. Please try again.");
                continue;
            }

            switch (option) {
                case OPEN_ACCOUNT ->
                    System.out.println("Open Account - to be implemented in a later lab.");

                case DEPOSIT ->
                    System.out.println("Deposit - to be implemented in a later lab.");

                case WITHDRAW ->
                    System.out.println("Withdraw - to be implemented in a later lab.");

                case TRANSFER ->
                    System.out.println("Transfer - to be implemented in a later lab.");

                case EXIT ->
                    System.out.println("Thank you for using MiniBank. Goodbye!");
            }

        } while (choice != 5);

        sc.close();
    }
}

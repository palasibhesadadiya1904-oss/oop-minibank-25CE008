
import java.util.Scanner;

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

        Account acc1 = new Account("Rahul", 1000);
        Account acc2 = new Account("Priya");
        Account acc3 = new Account("Amit", 2000);

        Account[] accounts = {acc1, acc2, acc3};

        acc1.deposit(500);
        acc2.deposit(1000);
        acc1.withdraw(200);
        acc3.withdraw(500);

        System.out.println("\n----- Account Details -----");

        for (Account acc : accounts) {
            System.out.println(acc);
        }

        System.out.println("\n----- Account Comparison -----");
        System.out.println("Are acc1 and acc2 equal? " + acc1.equals(acc2));

        System.out.println("\n----- instanceof Check -----");

        if (acc1 instanceof Account) {
            System.out.println("acc1 is an Account object.");
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

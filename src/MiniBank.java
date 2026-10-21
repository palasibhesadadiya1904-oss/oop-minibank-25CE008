
import java.util.Scanner;

public class MiniBank {

    // Record to store bank information
    record BankInfo(String name, String branch) {}

    // Enum containing all menu options
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
    System.out.println("Account Number: " + acc.getAccountNumber());
    System.out.println("Owner: " + acc.getOwnerName());
    System.out.println("Balance: Rs. " + acc.getBalance());
    System.out.println();
}



        int choice;

        do {
            // Display menu
            System.out.println("\n===== MINIBANK MENU =====");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();

            // Switch expression maps the number to a menu option
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

            // Display placeholder for the selected option
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

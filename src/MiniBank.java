
import java.util.Scanner;

public class MiniBank {

    record BankInfo(String name, String branch) {}

    enum MenuOption {
        OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, EXIT
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BankInfo b = new BankInfo("MiniBank", "CHARUSAT");

        System.out.println("Welcome to " + b.name());
        System.out.println("Branch: " + b.branch());

        int ch;

        do {
            System.out.println("\n----- MiniBank Menu -----");
            System.out.println("1. Open Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            ch = sc.nextInt();

            MenuOption op = switch (ch) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.EXIT;
                default -> null;
            };

            if (op == null) {
                System.out.println("Invalid choice!");
                continue;
            }

            switch (op) {
                case OPEN_ACCOUNT ->
                    System.out.println("Open Account - Coming soon!");

                case DEPOSIT ->
                    System.out.println("Deposit - Coming soon!");

                case WITHDRAW ->
                    System.out.println("Withdraw - Coming soon!");

                case TRANSFER ->
                    System.out.println("Transfer - Coming soon!");

                case EXIT ->
                    System.out.println("Thank you! Visit again.");
            }

        } while (ch != 5);

        sc.close();
    }
}

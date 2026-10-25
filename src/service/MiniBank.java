
package service;

import model.Account;
import model.SavingsAccount;
import model.CurrentAccount;
import model.FixedDepositAccount;
import util.StatePersister;
import util.TransactionLog;
import util.ReportGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

import static java.lang.System.out;

public class MiniBank {

    record BankInfo(String name, String branch) {
    }

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

        out.println("================================");
        out.println("Welcome to " + bank.name());
        out.println("Branch: " + bank.branch());
        out.println("================================");

        Path dataFile = Path.of("data", "accounts.dat");
        Path logsFolder = Path.of("logs");
        Path logFile = logsFolder.resolve("transactions.txt");
        Path reportFile = Path.of("reports", "end-of-day.txt");

        Account[] accounts=null;

        try {
            if (Files.exists(dataFile)) {
                accounts = StatePersister.load(dataFile);
                out.println("Previous account data loaded.");
            } else {
                accounts = new Account[]{
                    new SavingsAccount("Rahul", 1000, 500),
                   new CurrentAccount("Priya", 0, 1000),
                   new FixedDepositAccount("Amit", 2000)
                };

                StatePersister.save(accounts, dataFile);
                out.println("New account data created and saved.");
            }

            out.println("\nAccount Details:");

            for (Account account : accounts) {
                out.println(account);
            }

            out.println("\nPersistence Test:");

            StatePersister.save(accounts, dataFile);

            Account[] loadedAccounts = StatePersister.load(dataFile);

            for (Account account : loadedAccounts) {
                out.println(account);
            }

            out.println("\nTransaction Log Test:");

            TransactionLog.append(logFile, "DEPOSIT,1000");
            TransactionLog.append(logFile, "WITHDRAW,200");
            TransactionLog.append(logFile, "DEPOSIT,500");

            out.println("Transactions appended to log file.");

            ReportGenerator.generate(logsFolder, reportFile);

            out.println("\nEnd-of-Day Report:");

            for (String line : Files.readAllLines(reportFile)) {
                out.println(line);
            }

        } catch (IOException | ClassNotFoundException e) {
            out.println("File operation error: " + e.getMessage());
        }

        boolean running = true;

        while (running) {
            out.println("\n========== MiniBank Menu ==========");
            out.println("1. Open Account");
            out.println("2. Deposit");
            out.println("3. Withdraw");
            out.println("4. Transfer");
            out.println("5. Exit");
            out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                out.println("Please enter a valid number.");
                sc.next();
                continue;
            }

            int choice = sc.nextInt();

            MenuOption option = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.EXIT;
                default -> null;
            };

            if (option == null) {
                out.println("Invalid choice. Try again.");
                continue;
            }

            switch (option) {
                case OPEN_ACCOUNT ->
                    out.println("Open Account - to be implemented.");

                case DEPOSIT ->
                    out.println("Deposit - to be implemented.");

                case WITHDRAW ->
                    out.println("Withdraw - to be implemented.");

                case TRANSFER ->
                    out.println("Transfer - to be implemented.");

                case EXIT -> {
                    try {
                       if (accounts != null) {
    StatePersister.save(accounts, dataFile);
}
                        out.println("Account data saved successfully.");
                    } catch (IOException e) {
                        out.println("Could not save accounts: "
                                + e.getMessage());
                    }

                    out.println("Thank you for using MiniBank!");
                    running = false;
                }
            }
        }

        sc.close();
    }
}

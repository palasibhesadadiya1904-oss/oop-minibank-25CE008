package service;

import model.Account;
import model.SavingsAccount;
import model.CurrentAccount;
import model.FixedDepositAccount;
import util.StatePersister;
import util.TransactionLog;
import util.ReportGenerator;
import util.AccountStore;
import util.Repository;
import exception.InvalidAmountException;
import exception.InsufficientFundsException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.System.out;

public class MiniBank {

record BankInfo(String name, String branch) {
}

enum MenuOption {
    OPEN_ACCOUNT, DEPOSIT, WITHDRAW, TRANSFER, EXIT
}

private static void collectionsTest() {
    AccountStore store = new AccountStore();

    store.add(new SavingsAccount("Rahul", 1000, 500));
    store.add(new CurrentAccount("Priya", 0, 1000));
    store.add(new FixedDepositAccount("Amit", 2000));

    store.addTransaction("DEPOSIT 1000");
    store.addTransaction("WITHDRAW 200");

    out.println("\nAccounts Sorted by Balance:");

    for (Account account : store.sortedByBalance()) {
        out.println(account);
    }

    out.println("\nAccount Count: " + store.size());
    out.println("\nTransaction History:");

    for (String transaction : store.getTransactionHistory()) {
        out.println(transaction);
    }
}

private static void repositoryTest() {
    Repository<Account> repository = new Repository<>();

    Account rahul = new SavingsAccount("Rahul", 1000, 500);
    Account priya = new CurrentAccount("Priya", 0, 1000);
    Account amit = new FixedDepositAccount("Amit", 2000);

    repository.saveAll(rahul, priya, amit);

    out.println("\nRepository Test:");
    String id = String.valueOf(rahul.getAccountNumber());

    out.println("Find by ID " + id + ":");
    out.println(repository.findById(id));

    out.println("\nAll Repository Accounts:");

    for (Account account : repository.findAll()) {
        out.println(account);
    }

    out.println("\nRepository Count: " + repository.findAll().size());

    Integer[] numbers = {10, 20, 30};
    out.println("Average: " + Repository.average(numbers));
}

private static Account findAccount(Account[] accounts, int number) {
    for (Account account : accounts) {
        if (account.getAccountNumber() == number) {
            return account;
        }
    }
    return null;
}

private static int readInt(Scanner sc, String message) {
    while (true) {
        out.print(message);
        if (sc.hasNextInt()) {
            return sc.nextInt();
        }
        out.println("Please enter a valid whole number.");
        sc.next();
    }
}

private static long readLong(Scanner sc, String message) {
    while (true) {
        out.print(message);
        if (sc.hasNextLong()) {
            return sc.nextLong();
        }
        out.println("Please enter a valid amount.");
        sc.next();
    }
}

private static Account[] openAccount(Scanner sc, Account[] accounts,
                                     Path logFile) {
    out.println("\nChoose Account Type:");
    out.println("1. Savings Account");
    out.println("2. Current Account");
    out.println("3. Fixed Deposit Account");

    int type = readInt(sc, "Enter account type: ");
    sc.nextLine();

    out.print("Enter account holder name: ");
    String name = sc.nextLine().trim();

    if (name.isEmpty()) {
        out.println("Name cannot be empty.");
        return accounts;
    }

    Account newAccount;

    switch (type) {
        case 1 -> {
            long balance = readLong(sc, "Enter opening balance: ");
            long limit = readLong(sc, "Enter minimum balance/withdrawal limit: ");
            if (balance < 0 || limit < 0) {
                out.println("Values cannot be negative.");
                return accounts;
            }
            newAccount = new SavingsAccount(name, balance, limit);
        }
        case 2 -> {
            long balance = readLong(sc, "Enter opening balance: ");
            long limit = readLong(sc, "Enter overdraft limit: ");
            if (balance < 0 || limit < 0) {
                out.println("Values cannot be negative.");
                return accounts;
            }
            newAccount = new CurrentAccount(name, balance, limit);
        }
        case 3 -> {
            long balance = readLong(sc, "Enter fixed deposit amount: ");
            if (balance < 0) {
                out.println("Amount cannot be negative.");
                return accounts;
            }
            newAccount = new FixedDepositAccount(name, balance);
        }
        default -> {
            out.println("Invalid account type.");
            return accounts;
        }
    }

    accounts = Arrays.copyOf(accounts, accounts.length + 1);
    accounts[accounts.length - 1] = newAccount;

    out.println("Account opened successfully!");
    out.println(newAccount);

    try {
        TransactionLog.append(logFile,
                "OPEN," + newAccount.getAccountNumber());
    } catch (IOException e) {
        out.println("Could not write transaction log: " + e.getMessage());
    }

    return accounts;
}

private static void deposit(Scanner sc, Account[] accounts, Path logFile) {
    int number = readInt(sc, "Enter account number: ");
    Account account = findAccount(accounts, number);

    if (account == null) {
        out.println("Account not found.");
        return;
    }

    long amount = readLong(sc, "Enter deposit amount: ");

    try {
        account.deposit(amount);
        out.println("Deposit successful. New balance: Rs " + account.getBalance());

        TransactionLog.append(logFile,
                "DEPOSIT," + amount);
    } catch (InvalidAmountException e) {
        out.println("Deposit failed: " + e.getMessage());
    } catch (IOException e) {
        out.println("Deposit completed, but logging failed: " + e.getMessage());
    }
}

private static void withdraw(Scanner sc, Account[] accounts, Path logFile) {
    int number = readInt(sc, "Enter account number: ");
    Account account = findAccount(accounts, number);

    if (account == null) {
        out.println("Account not found.");
        return;
    }

    long amount = readLong(sc, "Enter withdrawal amount: ");

    try {
        if (account.withdraw(amount)) {
            out.println("Withdrawal successful. New balance: Rs "
                    + account.getBalance());

            TransactionLog.append(logFile, "WITHDRAW," + amount);
        } else {
            out.println("Withdrawal failed. Check your balance, account status, or withdrawal rules.");
        }
    } catch (InvalidAmountException e) {
        out.println("Withdrawal failed: " + e.getMessage());
    } catch (IOException e) {
        out.println("Withdrawal completed, but logging failed: " + e.getMessage());
    }
}

private static void transfer(Scanner sc, Account[] accounts, Path logFile) {
    int fromNumber = readInt(sc, "Enter sender account number: ");
    Account from = findAccount(accounts, fromNumber);

    if (from == null) {
        out.println("Sender account not found.");
        return;
    }

    int toNumber = readInt(sc, "Enter receiver account number: ");
    Account to = findAccount(accounts, toNumber);

    if (to == null) {
        out.println("Receiver account not found.");
        return;
    }

    long amount = readLong(sc, "Enter transfer amount: ");

    try {
        Account.transferSafely(from, to, amount);

        out.println("Transfer successful.");
        out.println("Sender balance: Rs " + from.getBalance());
        out.println("Receiver balance: Rs " + to.getBalance());

        TransactionLog.append(logFile, "TRANSFER," + amount);
    } catch (InvalidAmountException | InsufficientFundsException e) {
        out.println("Transfer failed: " + e.getMessage());
    } catch (IOException e) {
        out.println("Transfer completed, but logging failed: " + e.getMessage());
    }
}

public static void main(String[] args) {
    collectionsTest();

    Scanner sc = new Scanner(System.in);
    BankInfo bank = new BankInfo("MiniBank", "CHARUSAT Branch");

    out.println("\n================================");
    out.println("Welcome to " + bank.name());
    out.println("Branch: " + bank.branch());
    out.println("================================");

    repositoryTest();

    Path dataFile = Path.of("data", "accounts.dat");
    Path logsFolder = Path.of("logs");
    Path logFile = logsFolder.resolve("transactions.txt");
    Path reportFile = Path.of("reports", "end-of-day.txt");

    Account[] accounts;

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
        sc.close();
        return;
    }

    boolean running = true;

    while (running) {
        out.println("\n========== MiniBank Menu ==========");
        out.println("1. Open Account");
        out.println("2. Deposit");
        out.println("3. Withdraw");
        out.println("4. Transfer");
        out.println("5. Exit");

        int choice = readInt(sc, "Enter your choice: ");

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
                accounts = openAccount(sc, accounts, logFile);

            case DEPOSIT ->
                deposit(sc, accounts, logFile);

            case WITHDRAW ->
                withdraw(sc, accounts, logFile);

            case TRANSFER ->
                transfer(sc, accounts, logFile);

            case EXIT -> {
                try {
                    StatePersister.save(accounts, dataFile);
                    ReportGenerator.generate(logsFolder, reportFile);
                    out.println("Account data saved successfully.");
                } catch (IOException e) {
                    out.println("Could not save data: " + e.getMessage());
                }

                out.println("Thank you for using MiniBank!");
                running = false;
            }
        }
    }

    sc.close();
}
}
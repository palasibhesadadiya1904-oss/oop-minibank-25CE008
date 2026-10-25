
package service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import model.Account;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionProcessor {

    private final ExecutorService executor =
            Executors.newFixedThreadPool(4);

    public void submit(Runnable task) {
        executor.execute(task);
    }
    private final ConcurrentHashMap<String, Account> accounts =
        new ConcurrentHashMap<>();

public void addAccount(Account account) {
    if (account == null) {
        throw new IllegalArgumentException("Account cannot be null");
    }

    accounts.put(
        String.valueOf(account.getAccountNumber()),
        account
    );
}

public Account getAccount(String accountNumber) {
    return accounts.get(accountNumber);
}

public ConcurrentHashMap<String, Account> getAccounts() {
    return accounts;
}

    public void stop() {
        executor.shutdown();

        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

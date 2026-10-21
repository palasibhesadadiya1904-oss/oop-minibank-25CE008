
package service;

public class TransactionBuffer {

    private final int[] buffer = new int[5];
    private int count = 0;

    public synchronized void produce(int transaction) throws InterruptedException {
        while (count == buffer.length) {
            wait();
        }

        buffer[count] = transaction;
        count++;

        System.out.println("Produced transaction: " + transaction);

        notify();
    }

    public synchronized int consume() throws InterruptedException {
        while (count == 0) {
            wait();
        }

        int transaction = buffer[0];

        for (int i = 1; i < count; i++) {
            buffer[i - 1] = buffer[i];
        }

        count--;

        System.out.println("Processed transaction: " + transaction);

        notify();
        return transaction;
    }
}

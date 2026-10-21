
package service;

public class DeadlockDemo {

    public static void main(String[] args) {

        Object accountA = new Object();
        Object accountB = new Object();

        Thread thread1 = new Thread(() -> {
            synchronized (accountA) {
                System.out.println("Thread 1 locked Account A.");

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                System.out.println("Thread 1 waiting for Account B.");

                synchronized (accountB) {
                    System.out.println("Thread 1 locked Account B.");
                }
            }
        }, "Transfer-A-to-B");

        Thread thread2 = new Thread(() -> {
            synchronized (accountB) {
                System.out.println("Thread 2 locked Account B.");

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                System.out.println("Thread 2 waiting for Account A.");

                synchronized (accountA) {
                    System.out.println("Thread 2 locked Account A.");
                }
            }
        }, "Transfer-B-to-A");

        thread1.start();
        thread2.start();
    }
}

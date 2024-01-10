package org.example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {

    static Thread textGenerate;
    static ArrayBlockingQueue Queue0 = new ArrayBlockingQueue<String>(100);
    static ArrayBlockingQueue Queue1 = new ArrayBlockingQueue<String>(100);
    static ArrayBlockingQueue Queue2 = new ArrayBlockingQueue<String>(100);

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Start time: " + new SimpleDateFormat("HH:mm:ss:SS").format(new Date()));

        textGenerate = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String text = TextGenerator.generateText("abc", 100_000);

                try {
                    Queue0.put(text);
                    Queue1.put(text);
                    Queue2.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        textGenerate.start();

        Thread thread0 = new Thread(() -> {
            try {
                System.out.println("maxSequence A: " + countMaxCharSequence(Queue0, 'a'));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread0.start();

        Thread thread1 = new Thread(() -> {
            try {
                System.out.println("maxSequence B: " + countMaxCharSequence(Queue1, 'b'));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            try {
                System.out.println("maxSequence C: " + countMaxCharSequence(Queue2, 'c'));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        thread2.start();

        thread0.join();
        thread1.join();
        thread2.join();

        System.out.println("Start time: " + new SimpleDateFormat("HH:mm:ss:SS").format(new Date()));
    }

    public static int countMaxCharSequence(ArrayBlockingQueue<String> queue, char letter) throws InterruptedException {
        int maxSequence = 0;
        int counter = 0;
        while (textGenerate.isAlive()) {
            String text = queue.take();
            for (Character character : text.toCharArray()) {
                if (character.equals(letter)) {
                    counter++;
                }
            }
            if (counter > maxSequence) {
                maxSequence = counter;
            }
            counter = 0;
        }
        return maxSequence;
    }
}
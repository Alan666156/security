package com.security.thread;

/**
 * 如果有子父级关系的时候，子父级的方法都加上synchronizedy也是没问题的
 * synchronized的重入
 *
 * @author fuhongxing
 */
public class SyncDubbo2 {

    static class Main {
        public int i = 10;

        public synchronized void operationSup() {
            try {
                i--;
                System.out.println("Main print i = " + i);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Sub extends Main {
        public synchronized void operationSub() {
            try {
                while (i > 0) {
                    i--;
                    System.out.println("Sub print i = " + i);
                    Thread.sleep(100);
                    this.operationSup();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            Sub sub = new Sub();
            sub.operationSub();
        });

        t1.start();
    }


}

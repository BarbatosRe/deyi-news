package com.heima.tess4j;

public class ThraedTest {

    static int res;

    public static void main(String[] args) {



        Thread thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; i++) {
                    res+=i;
                    System.out.println("thread1执行的"+i);
                }
            }
        });

        thread1.start();
        Thread thread2=new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 50; i < 100; i++) {
                    res+=i;
                    System.out.println("thread2执行的"+i);
                }
            }
        });
        thread2.start();

        System.out.println(res);

    }
}

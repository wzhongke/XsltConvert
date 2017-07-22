package com.wang.test;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2017/7/3.
 */
public class ExecuterTest {

    @Test
    public void test() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(()->{
            long sum = 0;
            for (int i=0; i<10000; i++) {
                sum += i;
            }
            System.out.println(sum);
        });
        executor.submit(()->{
            long sum = 0;
            for (int i=0; i<100000; i++) {
                sum += i;
            }
            System.out.println(sum);
        });
        executor.submit(()->{
            long sum = 0;
            for (int i=0; i<1000000; i++) {
                sum += i;
            }
            System.out.println(sum);
        });
        executor.shutdown();
        Thread.sleep(1000*10);
    }
}

package concurrent.capter2;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用线程安全的类
 */
public class Counter {
    private AtomicInteger atomicI = new AtomicInteger(0);
    private int i = 0;
//    public static void main(String [] args) {
//        final Counter cas = new Counter();
//        List<Thread> ts = new ArrayList<>(600);
//        long start = System.currentTimeMillis();
//        for (int j=0; j<100; j++) {
//            Thread t = new Thread(()->{
//               for (int i=0; i<10000; i++) {
//                   cas.count();
//                   cas.safeCount();
//               }
//            });
//            ts.add(t);
//        }
//        ts.forEach(Thread::start);
//        // 等所有的线程执行完毕
//        for (Thread t: ts) {
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        System.out.println(cas.i);
//        System.out.println(cas.atomicI.get());
//        System.out.println(System.currentTimeMillis() - start);
//
//    }


    /**
     * 使用CAS实现线程安全的计数器
     */
    private void safeCount() {
        for(;;) {
            int i = atomicI.get();
            boolean suc = atomicI.compareAndSet(i, ++i);
            if (suc) {
                break;
            }
        }
    }

    /**
     * 非线程安全的计数器
     * */
    private void count() {
        i++;
    }
}

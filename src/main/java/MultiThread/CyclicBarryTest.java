package MultiThread;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.prefs.BackingStoreException;

/**
 * @author qiss
 * @create 2018-02-01 16:00
 **/
public class CyclicBarryTest {
    public static void main(String[] args) {
        int num = 3;
        CyclicBarrier c = new CyclicBarrier(num, new Runnable() {
            public void run() {
                System.out.println("the thread "+Thread.currentThread().getName());
            }
        });
        for (int i = 0; i < num; i++) {
            new Thread(new Writer(c)).start();
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("---------------------------");
        for (int i = 0; i < num; i++) {
            new Thread(new Writer(c)).start();
        }
    }
}
class Writer implements Runnable{
    private CyclicBarrier cyclicBarrier;
    Writer(CyclicBarrier c){
        this.cyclicBarrier = c;
    }
    public void run() {
        System.out.println(Thread.currentThread().getName()+" is working");
        try {
            System.out.println(Thread.currentThread().getName()+" is end");
            cyclicBarrier.await();
            System.out.println(Thread.currentThread().getName()+" is end,waiting others");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e1){
            e1.printStackTrace();
        }

    }
}

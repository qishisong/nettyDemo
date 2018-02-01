package MultiThread;

import java.util.concurrent.Semaphore;

/**
 * @author qiss
 * @create 2018-02-01 16:09
 **/
public class SemaphoreTest {
    public static void main(String[] args) {
        int num =8;
        Semaphore s = new Semaphore(5);
        for (int i = 0; i < num; i++) {
            new Worker(i,s).start();
        }
    }
}
class Worker extends Thread{
    private int num;
    private Semaphore semaphore;
    Worker(int num,Semaphore s){
        this.num = num;
        this.semaphore = s;
    }

    @Override
    public void run() {
        try {
            while (true){
                semaphore.acquire();
                System.out.println(this.num+" acquire working");
                Thread.sleep(2000);
                semaphore.release();
                System.out.println(this.num+" release working");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package MultiThread;

import java.util.concurrent.CountDownLatch;

/**
 * @author qiss
 * @create 2018-02-01 15:53
 **/
public class CountDownLatchTest {
    public static void main(String[] args) throws InterruptedException {
        final int threadNum = 2;
        final CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        for (int i = 0; i < threadNum; i++) {
            new Thread(){
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+" is begining");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName()+" is end");
                    countDownLatch.countDown();
                }
            }.start();
        }
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName()+" two thread are end");
    }

}

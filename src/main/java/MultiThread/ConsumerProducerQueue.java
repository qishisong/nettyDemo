package MultiThread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qiss
 * @create 2018-02-01 15:32
 **/
public class ConsumerProducerQueue {
    public static void main(String[] args) {
        BlockingQueue queue = new ArrayBlockingQueue(10);
        for (int i = 0; i < 5; i++) {
            new Thread(new ProducerQ(queue)).start();
            new Thread(new ConsumerQ(queue)).start();
        }

    }

}

class ConsumerQ implements  Runnable{
    private BlockingQueue queue;
    ConsumerQ(BlockingQueue queue){
        this.queue = queue;
    }
    public void run() {
        try {
            while (true){
                Object take = queue.take();
                System.out.println("consumer---"+take.toString());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ProducerQ implements  Runnable{
    private BlockingQueue queue;
    static AtomicInteger i = new AtomicInteger(125);
    ProducerQ(BlockingQueue queue){
        this.queue = queue;
    }
    public void run() {
        try {
            while (true){
                Thread.sleep(2000);
                queue.put(Thread.currentThread().getName()+"-----"+ i.getAndIncrement());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

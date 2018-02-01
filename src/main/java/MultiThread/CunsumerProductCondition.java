package MultiThread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author qiss
 * @create 2018-02-01 10:51
 **/
public class CunsumerProductCondition {
    public static void main(String[] args) {
        ResourceByCondition r = new ResourceByCondition();
        Producer producer = new Producer(r);
        Consumer consumer = new Consumer(r);
        Thread t1 = new Thread(producer);
        Thread t2 = new Thread(producer);

        Thread t3 = new Thread(consumer);
        Thread t4 = new Thread(consumer);
        t1.start();t2.start();t3.start();t4.start();
    }

}
class Producer implements  Runnable{
    private ResourceByCondition r;
    Producer(ResourceByCondition r){
        this.r = r;
    }
    public void run() {
        while(true){
            r.pruduct("qweqeqwe");
        }
    }
}

class Consumer implements  Runnable{
    private ResourceByCondition r;
    Consumer(ResourceByCondition r){
        this.r = r;
    }
    public void run() {
        while (true){
            r.consumer();
        }
    }
}
class ResourceByCondition{
    private int count = 0;
    private String name;
    private boolean flag = false;
    Lock lock =new ReentrantLock();
    Condition cus_condition = lock.newCondition();
    Condition pro_condition = lock.newCondition();

    public void pruduct(String name){
        lock.lock();
        try{
            while(flag){
                try {
                    pro_condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.name =name+count;
            count++;
            System.out.println(Thread.currentThread().getName()+" pruduct---"+this.name);
            flag=true;
            cus_condition.signal();
        }finally {
            lock.unlock();
        }
    }

    public void consumer(){
        lock.lock();
        try{
            while (!flag){
                try {
                    cus_condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" consumer---"+this.name);
            flag=false;
            pro_condition.signal();
        }finally {
            lock.unlock();
        }
    }
}
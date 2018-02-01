package MultiThread;

/**
 * @author qiss
 * @create 2018-02-01 15:02
 **/
public class CunProSysnWait {
    public static void main(String[] args) {
        KaoYaResource r = new KaoYaResource();
        ProducerW producerW = new ProducerW(r);
        ConsumerW consumerW = new ConsumerW(r);
        Thread t1 = new Thread(producerW);
        Thread t2 = new Thread(producerW);

        Thread t3 = new Thread(consumerW);
        Thread t4 = new Thread(consumerW);
        t1.start();t2.start();t3.start();t4.start();
    }
}
class ProducerW implements  Runnable{
    private KaoYaResource resource;
    ProducerW(KaoYaResource resource){
        this.resource = resource;
    }
    public void run() {
        while (true){
            resource.pruducer();
        }
    }
}

class ConsumerW implements  Runnable{
    private KaoYaResource resource;
    ConsumerW(KaoYaResource resource){
        this.resource = resource;
    }
    public void run() {
        while (true){
            resource.consumer();
        }
    }
}

class KaoYaResource {
    private int count = 0;
    private boolean flag = false;

    public void pruducer(){
        synchronized (this){
            while (flag){
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count++;
            System.out.println(Thread.currentThread().getName()+"---producer---"+count);
            flag=true;
            notifyAll();
        }
    }

    public synchronized  void consumer(){
        while (!flag){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        count--;
        System.out.println(Thread.currentThread().getName()+"----consumer-----"+count);
        flag=false;
        notifyAll();
    }
}




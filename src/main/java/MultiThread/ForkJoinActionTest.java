package MultiThread;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * @author qiss
 * @create 2018-02-01 17:10
 **/
public class ForkJoinActionTest {
    public static void main(String[] args) throws Exception {
        PrintTask task = new PrintTask(0, 50);
        ForkJoinPool pool = new ForkJoinPool();
        pool.submit(task);
        pool.awaitTermination(2, TimeUnit.SECONDS);
        pool.shutdown();
    }
}

class PrintTask extends RecursiveAction{
    //private static final int LIMIT=10;
    private int LIMIT=10;
    private int start;
    private int end;

    PrintTask(int start,int end){
        this.start = start;
        this.end = end;
    }

    protected void compute() {
        if(end-start<LIMIT){
            System.out.println("--------------start:"+start+",end:"+end);
            for (int i = start; i < end; i++) {
                System.out.println(Thread.currentThread().getName()+" i= "+i);
            }
        }else{
            int middle = (start+end)/2;
            PrintTask left = new PrintTask(start, middle);
            PrintTask right = new PrintTask(middle,end);
            left.fork();
            right.fork();
        }
    }
}

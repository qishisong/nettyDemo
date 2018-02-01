package MultiThread;

import java.util.concurrent.*;

/**
 * @author qiss
 * @create 2018-02-01 16:22
 **/
public class FutureTest {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Callable<String> task = new Callable<String>() {
            public String call() throws Exception {
                return "233333";
            }
        };
        Future<String> result = executor.submit(task);
        String s = result.get();
        System.out.println(s);
        System.out.println("--------------------");

        FutureTask<String> task1 = new FutureTask<String>(task){
            @Override
            protected void done() {
                System.out.println("222222222222");
            }
        };
        executor.submit(task1);
        System.out.println(task1.get());

        System.out.println("--------------------");
        String call = task.call();
        System.out.println(call);

    }
}

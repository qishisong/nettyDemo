package MultiThread;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author qiss
 * @create 2018-02-01 17:26
 * 适合多任务批处理
 **/
public class FJRecursiveTaskTest {
    public static void main(String[] args) {
        List<Person> personList = new ArrayList<Person>();
        for (int i = 0; i <60; i++) {
            personList.add(new Person("name"+i,i));
        }
        SumTask sumTask = new SumTask(10, personList, 0, personList.size());
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<List<Integer>> result = forkJoinPool.submit(sumTask);
        List<Integer> integers = null;
        try {
            integers = result.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        /*for (Integer integer : integers) {
            System.out.println(integer);
        }*/
        forkJoinPool.shutdown();
    }

}

@AllArgsConstructor
class SumTask extends RecursiveTask<List<Integer>>{
    private  int limit;
    private List<Person> list;
    private int start;
    private int end;

    protected List<Integer> compute() {
       List<Integer> list1 = new ArrayList<Integer>();
       if(end-start<limit){
           for (int i = start; i < end; i++) {
               System.out.println(Thread.currentThread().getName()+"---"+list.get(i).getName()+"---"+list.get(i).getId());
                list1.add(list.get(i).getId());
           }
           return list1;
       }else{
           int middle = (start+end)/2;
           SumTask left = new SumTask(limit, list, start, middle);
           SumTask right = new SumTask(limit, list, middle,end);
           // invokeAll(left,right);  等价与下面两句
           left.fork();
           right.fork();
           left.join().addAll(right.join());
           return left.join();
       }
    }
}


@Data
@AllArgsConstructor
class Person{
    private String name;
    private Integer id;
}


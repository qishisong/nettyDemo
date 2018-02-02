/**
 * quickSort
 * @author qiss
 * @create 2018-01-19 15:42
 **/
public class QuickSortTest {
    public static void main(String[] args) {
        int[] array = {8,1,2,6,4,7,9,5,3};
        sort(array,0,array.length-1);
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }

    }

    /**
     * 以lo为基准（key）从右遍历找一个比key小的数，将其值赋值给lo,确定hi,
     * 然后从左遍历找一个比key大的数,将其值赋值给之前确定的hi，确定lo,
     * 循环遍历，最终将其分成以key中心的两部分，然后将key赋值给lo或hi(此时lo=hi)
     * @param array
     * @param lo
     * @param hi
     * @return
     */
    public static int partition(int []array,int lo,int hi){
       int key = array[lo];
        while(lo<hi){
            while(array[hi]>=key && lo<hi){
                hi--;
            }
            array[lo] = array[hi];
            while(array[lo]<=key && lo<hi){
                lo++;
            }
            array[hi] = array[lo];
        }
        System.out.println("lo:"+lo+",hi"+hi);
        array[lo]=key;
        return hi;
    }

    public static void sort(int[] array,int lo ,int hi){
        if(lo>hi){
           return;
        }
        int index = partition(array,lo,hi);
        sort(array,lo,index-1);
        sort(array,index+1,hi);
    }
}

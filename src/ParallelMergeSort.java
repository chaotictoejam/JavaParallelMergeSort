import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort {
    public static void main(String[] args) {
        long startTime;
        long endTime;

        // Small array example
        int[] smallArray1 = {5, 3, 2, 6, 1, 7};
        int[] smallArray2 = {5, 3, 2, 6, 1, 7};

        startTime = System.currentTimeMillis();
        parallelMergeSort(smallArray1);
        endTime = System.currentTimeMillis();

        System.out.println("Small Array Parallel: Total computation time = " + (endTime - startTime) + " ms");

        startTime = System.currentTimeMillis();
        MergeSort.mergeSort(smallArray2);
        endTime = System.currentTimeMillis();

        System.out.println("Small Array Not Parallel: Total computation time = " + (endTime - startTime) + " ms");

        // Large array example
        int[] largeArray1 = new int[7000000];
        int[] largeArray2 = new int[7000000];
        for(int i = 0; i<largeArray1.length; i++){
            largeArray1[i] = largeArray2[i] = (int)(Math.random()*1000000);
        }

        startTime = System.currentTimeMillis();
        parallelMergeSort(largeArray1);
        endTime = System.currentTimeMillis();

        System.out.println("Large Array Parallel: Total computation time = " + (endTime - startTime) + " ms");

        startTime = System.currentTimeMillis();
        MergeSort.mergeSort(largeArray2);
        endTime = System.currentTimeMillis();

        System.out.println("Large Array Not Parallel: Total computation time = " + (endTime - startTime) + " ms");
    }

    public static void parallelMergeSort(int[] array) {
        SortTask mainTask = new SortTask(array);
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(mainTask);
    }

    private static class SortTask extends RecursiveAction {
        private int[] array;

        public SortTask(int[] array) {
            this.array = array;
        }
        @Override
        protected void compute() {
            if(array.length > 1) {
                int mid = array.length/2;

                // Obtain the first half
                int[] firstHalf = new int[mid];
                System.arraycopy(array, 0, firstHalf, 0, mid);

                // Obtain the second half
                int[] secondHalf = new int[array.length - mid];
                System.arraycopy(array, mid, secondHalf, 0, array.length - mid);

                // Recursively sort the two halves
                SortTask firstHalfTask = new SortTask(firstHalf);
                SortTask secondHalfTask = new SortTask(secondHalf);
                // Invoke declared tasks
                invokeAll(firstHalfTask, secondHalfTask);

                //Merge firstHalf with secondHalf into our array
                MergeSort.merge(firstHalf, secondHalf, array);
            }
        }
    }
}

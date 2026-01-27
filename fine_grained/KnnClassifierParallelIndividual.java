package fine_grained;

import java.util.*;
import java.util.concurrent.*;

public class KnnClassifierParallelIndividual {

    private List<? extends Sample> dataSet;
    private int k;
    private ThreadPoolExecutor executor;
    private int numThreads;
    private boolean parallelSort;

    public KnnClassifierParallelIndividual(
        List<? extends Sample> dataSet,
        int k,
        int factor,
        boolean parallelSort) {

        this.dataSet = dataSet;
        this.k = k;
        this.numThreads =
            factor * Runtime.getRuntime().availableProcessors();

        this.executor = (ThreadPoolExecutor)
            Executors.newFixedThreadPool(numThreads);

        this.parallelSort = parallelSort;
    }

    public String classify(Sample example) throws InterruptedException {

        // 1. Mảng lưu khoảng cách
        Distance[] distances = new Distance[dataSet.size()];

        // 2. Latch chờ TẤT CẢ task xong
        CountDownLatch endController =
            new CountDownLatch(dataSet.size());

        // 3. Tạo 1 task cho MỖI sample
        int index = 0;
        for (Sample localExample : dataSet) {
            IndividualDistanceTask task =
                new IndividualDistanceTask(
                    distances,
                    index,
                    localExample,
                    example,
                    endController);

            executor.execute(task);
            index++;
        }
        System.out.println("before");
        // 4. Chờ toàn bộ task hoàn thành
        endController.await();
        System.out.println("het hang");
        // 5. Sort
        if (parallelSort) {
            Arrays.parallelSort(distances);
        } else {
            Arrays.sort(distances);
        }

        // 6. Đếm tag của k phần tử gần nhất
        Map<String, Integer> results = new HashMap<>();

        for (int i = 0; i < k; i++) {
            Sample localExample =
                dataSet.get(distances[i].getIndex());
            String tag = localExample.getTag();
            results.merge(tag, 1, Integer::sum);
        }

        // 7. Trả về tag phổ biến nhất
        return Collections.max(
            results.entrySet(),
            Map.Entry.comparingByValue()
        ).getKey();
    }

    // Shutdown executor
    public void destroy() {
        executor.shutdown();
    }
}


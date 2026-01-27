package fine_grained;

import java.util.*;
import java.util.concurrent.*;

public class KnnClassifierParallelGroup {

    private List<? extends Sample> dataSet;
    private int k;
    private ThreadPoolExecutor executor;
    private int numThreads;
    private boolean parallelSort;

    public KnnClassifierParallelGroup(
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

        // 2. Latch chỉ chờ numThreads task
        CountDownLatch endController =
            new CountDownLatch(numThreads);

        // 3. Chia dataset thành numThreads nhóm
        int length = dataSet.size() / numThreads;
        int startIndex = 0;
        int endIndex = length;

        for (int i = 0; i < numThreads; i++) {

            GroupDistanceTask task =
                new GroupDistanceTask(
                    distances,
                    startIndex,
                    endIndex,
                    dataSet,
                    example,
                    endController);

            executor.execute(task);

            startIndex = endIndex;

            if (i < numThreads - 2) {
                endIndex += length;
            } else {
                endIndex = dataSet.size();
            }
        }

        // 4. Chờ toàn bộ task hoàn thành
        endController.await();

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

    public void destroy() {
        executor.shutdown();
    }
}


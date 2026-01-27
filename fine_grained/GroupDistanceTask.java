package fine_grained;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class GroupDistanceTask implements Runnable {

    private Distance[] distances;
    private int startIndex;
    private int endIndex;
    private Sample example;
    private List<? extends Sample> dataSet;
    private CountDownLatch endController;

    public GroupDistanceTask(
        Distance[] distances,
        int startIndex,
        int endIndex,
        List<? extends Sample> dataSet,
        Sample example,
        CountDownLatch endController) {

        this.distances = distances;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.dataSet = dataSet;
        this.example = example;
        this.endController = endController;
    }

    @Override
    public void run() {
        for (int index = startIndex; index < endIndex; index++) {
            Sample localExample = dataSet.get(index);
            distances[index] = new Distance();
            distances[index].setIndex(index);
            distances[index].setDistance(
                EuclideanDistanceCalculator.calculate(
                    localExample, example));
        }
        endController.countDown();
    }
}


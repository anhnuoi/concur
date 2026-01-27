package fine_grained;

import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        List<Sample> trainData = new ArrayList<>();

        trainData.add(new Sample(new double[]{1.0, 2.0}, "A"));
        trainData.add(new Sample(new double[]{1.5, 1.8}, "A"));
        trainData.add(new Sample(new double[]{5.0, 8.0}, "B"));
        trainData.add(new Sample(new double[]{6.0, 9.0}, "B"));

        KnnClassifierParallelIndividual classifier =
            new KnnClassifierParallelIndividual(
                trainData,
                3,
                1,      // factor
                true    // parallelSort
            );

        Sample test =
            new Sample(new double[]{10.2, 1.9}, "?");

        String result = classifier.classify(test);

        System.out.println("Predicted tag = " + result);

        classifier.destroy();
    }
}

package serial;

import java.util.*;

public class Main {

    public static void main(String[] args) {

        List<Sample> trainData = new ArrayList<>();

        trainData.add(new Sample(new double[]{1.0, 2.0}, "A"));
        trainData.add(new Sample(new double[]{1.5, 1.8}, "A"));
        trainData.add(new Sample(new double[]{5.0, 8.0}, "B"));
        trainData.add(new Sample(new double[]{6.0, 9.0}, "B"));

        KnnClassifier classifier =
            new KnnClassifier(trainData, 3);

        Sample test =
            new Sample(new double[]{1.2, 1.9}, "?");

        String result = classifier.classify(test);

        System.out.println("Predicted tag = " + result);
    }
}

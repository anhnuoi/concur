package serial;

import java.util.*;

public class KnnClassifier {

    private List<? extends Sample> dataSet;
    private int k;

    public KnnClassifier(List<? extends Sample> dataSet, int k) {
        this.dataSet = dataSet;
        this.k = k;
    }

    public String classify(Sample example) {

        // 1. Tính khoảng cách
        Distance[] distances = new Distance[dataSet.size()];
        int index = 0;

        for (Sample localExample : dataSet) {
            distances[index] = new Distance();
            distances[index].setIndex(index);
            distances[index].setDistance(
                EuclideanDistanceCalculator.calculate(
                    localExample, example));
            index++;
        }

        // 2. Sắp xếp theo khoảng cách tăng dần
        Arrays.sort(distances);

        // 3. Đếm tag trong k phần tử gần nhất
        Map<String, Integer> results = new HashMap<>();

        for (int i = 0; i < k; i++) {
            Sample localExample =
                dataSet.get(distances[i].getIndex());
            String tag = localExample.getTag();

            results.merge(tag, 1, (a, b) -> a + b);
        }

        // 4. Trả về tag có số lần xuất hiện lớn nhất
        return Collections.max(
            results.entrySet(),
            Map.Entry.comparingByValue()
        ).getKey();
    }
}

package fine_grained;

public class Sample {

    private double[] example;
    private String tag;

    public Sample(double[] example, String tag) {
        this.example = example;
        this.tag = tag;
    }

    public double[] getExample() {
        return example;
    }

    public String getTag() {
        return tag;
    }
}

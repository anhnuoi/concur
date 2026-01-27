package serial;

public class Distance implements Comparable<Distance> {

    private int index;
    private double distance;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(Distance other) {
        return Double.compare(this.distance, other.distance);
    }
}

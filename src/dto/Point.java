package dto;

import java.util.Objects;

public class Point {

    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public void update(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Point point = (Point) o;
        return Double.compare(point.x, x) == 0 && Double.compare(point.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Get the magnitude of a point
     * This is only useful when the point is treated as a vector
     **/
    public double getMagnitude() {
        return Math.sqrt(x * x + y * y);
    }

    @Override
    public String toString() {
        return "{" + x + ", " + y + "}";
    }
}

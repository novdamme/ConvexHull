package dto;

import java.util.Objects;

public class Disk {

    private Point center;
    private final double radius;

    public Disk(double x, double y, double r) {
        this.center = new Point(x, y);
        this.radius = r;
    }

    public Disk(Point point, double r) {
        this.center = point;
        this.radius = r;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Disk disk = (Disk) o;
        return Double.compare(disk.radius, radius) == 0 && Objects.equals(center, disk.center);
    }

    @Override
    public int hashCode() {
        return Objects.hash(center, radius);
    }

    public double getLeftMostX() {
        return this.center.getX() - this.radius;
    }

    public Point getHighLeftExtremePoint() {
        return new Point(getLeftMostX(), this.center.getY());
    }

    public double getRightMostX() {
        return this.center.getX() + this.radius;
    }

    public Point getLowRightExtremePoint() {
        return new Point(getRightMostX(), this.center.getY());
    }

    public Point getCenter() {
        return this.center;
    }

    public double getRadius() {
        return this.radius;
    }

    public Point findFarthestPoint(Line orientedLine) {
        return null; // TODO
    }
}
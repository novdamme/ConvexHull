package dto;

import java.util.Objects;

public class Disk {

  private Point center;
  private double radius;

  public Disk(double x, double y, double r) {
    this.center = new Point(x, y);
    this.radius = r;
  }

  public Disk(Point point, double r) {
    this.center = point;
    this.radius = r;

  }

  public void update(Disk d) {
    this.center = d.center;
    this.radius = d.radius;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Disk disk = (Disk) o;
    return Double.compare(disk.radius, radius) == 0 && Objects.equals(center, disk.center);
  }

  @Override
  public int hashCode() {
    return Objects.hash(center, radius);
  }

  public Point getCenter() {
    return center;
  }

  public double getRadius() {
    return radius;
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

  public Boolean contains(Disk other) {
    double xc = this.getCenter().getX() - other.getCenter().getX();
    double yc = this.getCenter().getY() - other.getCenter().getY();
    double distance = Math.sqrt((xc * xc) + (yc * yc));
    return (this.radius >= (other.radius + distance));
  }

  public Point findFarthestPoint(Line orientedLinePQ) {

    // Point vector = orientedLinePQ.getNormalVector();
    // Point negativeVector = new Point(-vector.getX(), -vector.getY());
    // Point unitVector = new Point(negativeVector.getX() / negativeVector.getMagnitude(),
    //    negativeVector.getY() / negativeVector.getMagnitude());
    // Point farthesPoint = new Point(unitVector.getX() * this.getRadius() + this.getCenter().getY(),
    //    unitVector.getY() * this.getRadius() + this.getCenter().getY());
    // return farthesPoint;

    Point vector = orientedLinePQ.getNormalVector();
    double distanceToLine = orientedLinePQ.getDistance(this.getCenter());
    double scalingFactor = this.getRadius() / distanceToLine;
    Point farthestPoint = new Point(this.getCenter().getX() + vector.getX() * scalingFactor,
            this.getCenter().getY() + vector.getY() * scalingFactor);
    return farthestPoint;
  }

  @Override
  public String toString() {
    return "{center: " + center.toString() + ", radius: " + radius + "}";
  }
}

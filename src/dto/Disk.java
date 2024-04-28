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
  public Disk(Disk disk) {
    this.center = new Point(disk.center);
    this.radius = disk.radius;
  }

  public void update(Disk d) {
    this.center = new Point(d.center);
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


  @Override
  public String toString() {
    return "{center: " + center.toString() + ", radius: " + radius + "}";
  }

  public static double distanceFromLine(double x, double y, double A, double B, double C) {
    return Math.abs(A * x + B * y + C) / Math.sqrt(A * A + B * B);
  }

  // Function to find the point on the disk's boundary farthest away from the line
  public Point findFarthestPoint(Line line) {
    double cx = this.center.getX();
    double cy = this.center.getY();
    double r = this.radius;
    double x1 = line.getStart().getX();
    double y1 = line.getStart().getY();
    double x2 = line.getEnd().getX();
    double y2 = line.getEnd().getY();

    // Calculate coefficients of the line equation Ax + By + C = 0
    double A = y2 - y1;
    double B = x1 - x2;
    double C = (x2 - x1) * y1 - (y2 - y1) * x1;

    // Calculate slope of the line
    double m = (y2 - y1) / (x2 - x1);

    // Handle case where line is vertical
    if (Double.isInfinite(m)) {
      // The line equation becomes x = x1
      // Distance between the line and a point (x, y) is |x - x1|
      double farthestX = cx + r;
      double distance = Math.abs(farthestX - x1);
      return new Point(farthestX, cy);
    } else {
      // Iterate through points on the boundary of the disk
      double maxDistance = Double.NEGATIVE_INFINITY;
      double farthestX = 0;
      double farthestY = 0;
      for (int i = 0; i <= 360; i++) {
        double theta = Math.toRadians(i);
        double x = cx + r * Math.cos(theta);
        double y = cy + r * Math.sin(theta);

        // Calculate distance between the point and the line
        double distance = distanceFromLine(x, y, A, B, C);

        // Update farthest point if needed
        if (distance > maxDistance) {
          maxDistance = distance;
          farthestX = x;
          farthestY = y;
        }
      }

      return new Point(farthestX, farthestY);
    }
  }

  public static void main(String[] args) {
    double cx = 3; // Center x-coordinate of the disk
    double cy = 4; // Center y-coordinate of the disk
    double r = 2; // Radius of the disk
    Disk disk = new Disk(new Point(cx, cy), r);
    double x1 = 1; // Start point x-coordinate of the line segment
    double y1 = 1; // Start point y-coordinate of the line segment
    double x2 = 5; // End point x-coordinate of the line segment
    double y2 = 5; // End point y-coordinate of the line segment
    disk.findFarthestPoint(new Line(new Point(x1, y1), new Point(x2, y2)));
    //findFarthestPoint(cx, cy, r, x1, y1, x2, y2);
  }

}

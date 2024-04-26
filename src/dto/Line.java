package dto;

public class Line {

  private final Point start;
  private final Point end;

  public Line(Point start, Point end) {
    this.start = start;
    this.end = end;
  }

  public Point getStart() {
    return start;
  }

  public Point getEnd() {
    return end;
  }

  /**
   * Calculate the distance between a point and a line segment
   * Assume the point lies between the endpoints of the segment
   **/
  public double getDistance(Point p) {
    // Point lineVector = new Point(start.getX() - end.getX(), start.getY() -
    // end.getY());
    // Point s2pVector = new Point(p.getX() - start.getX(), p.getY()- start.getY());

    // double length = lineVector.getMagnitude();

    // double distance = (lineVector.getX()*s2pVector.getX() +
    // lineVector.getY()*s2pVector.getY());
    double x0 = p.getX();
    double y0 = p.getY();
    double x1 = start.getX();
    double y1 = start.getY();
    double x2 = end.getX();
    double y2 = end.getY();
    double area = (x2 - x1) * (y0 - y1) - (x0 - x1) * (y2 - y1);
    double length = Math.sqrt(((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1)));
    double distance = area / length;
    return distance;
  }

  public Point getNormalVector() {
    Point lineVector = new Point(start.getX() - end.getX(), start.getY() - end.getY());
    return new Point(-lineVector.getY(), lineVector.getX());

  }
}

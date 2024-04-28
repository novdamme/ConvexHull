package dto;

public class Line {

  private final Point start;
  private final Point end;

  public Line(Point start, Point end) {
    this.start = new Point(start);
    this.end = new Point(end);
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

  public Line makePerpendicularLine(Point point) {
    Point dirVec = getNormalVector();
    Point newPoint = new Point(dirVec.getX() + point.getX(), dirVec.getY() + point.getY());
    return new Line(point, newPoint);
  }

  @Override
  public String toString() {
    return "{start: " + start.toString() + ", end: " + end.toString() + "}";
  }
}

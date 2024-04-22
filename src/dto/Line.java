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
        Point lineVector = new Point(start.getX() - end.getX(), start.getY() - end.getY());        
        Point s2pVector = new Point(p.getX() - start.getX(), p.getY()- start.getY());

        double length = lineVector.getMagnitude(); 

        double distance = (lineVector.getX()*s2pVector.getX() + lineVector.getY()*s2pVector.getY());
        return distance / length;
    }
}

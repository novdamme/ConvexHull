package dto;

public class Line {

    private final Point start;
    private final Point end;

    public Line(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public double signedDistance(Point point) {
        return 0.0; // TODO
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
}

package ConvexHull;

import dto.Point;

import java.util.*;

public class ConvexHull {
    private static final Random rand = new Random(0);

    public static void main(String[] args) {
        ConvexHull convexHull = new ConvexHull();
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            points.add(new Point(rand.nextDouble(), rand.nextDouble()));
        }
        List<Point> sol = convexHull.hull(points);
        for (Point p : sol) {
            System.out.println(p.getX() + ";" + p.getY());
        }
    }

    public List<Point> hull(List<Point> points) {
        points.sort(Comparator.comparingDouble(Point::getX).thenComparingDouble(Point::getY));
        List<Point> upper = new ArrayList<>(Arrays.asList(points.get(0), points.get(1)));
        for (int i = 3; i < points.size(); i++) {
            upper.add(points.get(i));
            while (upper.size() > 2 && !rightTurn(upper)) {
                upper.remove(upper.size() - 2);
            }
        }
        List<Point> lower = new ArrayList<>(Arrays.asList(points.get(points.size() - 1), points.get(points.size() - 2)));
        for (int i = points.size() - 3; i >= 0; i--) {
            lower.add(points.get(i));
            while (lower.size() > 2 && !rightTurn(lower)) {
                lower.remove(lower.size() - 2);
            }
        }
        lower.remove(lower.size() - 1);
        lower.remove(0);
        upper.addAll(lower);
        return upper;
    }

    public boolean rightTurn(List<Point> points) {
        Point p1 = points.get(points.size() - 3);
        Point p2 = points.get(points.size() - 2);
        Point p3 = points.get(points.size() - 1);
        double check = (p2.getX() - p1.getX()) * (p3.getY() - p1.getY()) - (p2.getY() - p1.getY()) * (p3.getX() - p1.getX());
        return check < 0;
    }
}

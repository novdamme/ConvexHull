package ConvexHull;

import java.awt.geom.Point2D;
import java.util.*;

public class ConvexHull {
    private static final Random rand = new Random(0);

    public static void main(String[] args) {
        ConvexHull convexHull = new ConvexHull();
        List<Point2D> points = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            points.add(new Point2D.Double(rand.nextDouble(), rand.nextDouble()));
        }
        List<Point2D> sol = convexHull.hull(points);
        for (Point2D p : sol) {
            System.out.println(p);
        }
    }

    public List<Point2D> hull(List<Point2D> points) {
        points.sort(Comparator.comparingDouble(Point2D::getX).thenComparingDouble(Point2D::getY));
        List<Point2D> upper = new ArrayList<>(Arrays.asList(points.get(0), points.get(1)));
        for (int i = 3; i < points.size(); i++) {
            upper.add(points.get(i));
            while (upper.size() > 2 && !rightTurn(upper)) {
                upper.remove(upper.size() - 2);
            }
        }
        List<Point2D> lower = new ArrayList<>(Arrays.asList(points.get(points.size() - 1), points.get(points.size() - 2)));
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

    private boolean rightTurn(List<Point2D> points) {
        Point2D p1 = points.get(points.size() - 3);
        Point2D p2 = points.get(points.size() - 2);
        Point2D p3 = points.get(points.size() - 1);
        if (p1.equals(p2) || p2.equals(p3)) {
            return false;
        } else if (p1.getX() == p2.getX()) {
            if (p1.getY() < p2.getY()) {
                return p3.getX() > p1.getX();
            } else {
                return p3.getX() < p1.getX();
            }
        } else if (p1.getY() == p2.getY()) {
            if (p1.getX() < p2.getX()) {
                return p3.getY() < p1.getY();
            } else {
                return p3.getY() > p1.getY();
            }
        } else {
            double x = (p3.getY() - p1.getY()) * (p1.getX() - p2.getX()) / (p1.getY() - p2.getY()) + p1.getX();
            if (p1.getY() < p2.getY()) {
                return p3.getX() > x;
            } else {
                return p3.getX() < x;
            }
        }
    }
}

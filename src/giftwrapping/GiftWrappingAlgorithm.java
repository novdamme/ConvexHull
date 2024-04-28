package giftwrapping;

import convexHullPoints.ConvexHullAlgorithm;
import dto.Point;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class GiftWrappingAlgorithm implements ConvexHullAlgorithm {

    public List<Point> convexHull(List<Point> points) {
        List<Point> hull = new ArrayList<>();
        Optional<Point> leftMostPoint = findLeftMost(points);
        if (leftMostPoint.isPresent()) {

            Point pointOnHull = leftMostPoint.get();
            Point endpoint;
            int i = 0;
            do {
                hull.add(pointOnHull);
                endpoint = points.get(0); // Start from the first point

                for (Point point : points) {
                    if (endpoint == pointOnHull || isLeftOfLine(hull.get(i), endpoint, point)) {
                        endpoint = point;
                    }
                }

                i++;
                pointOnHull = endpoint;
            } while (endpoint != hull.get(0));
        }
        return hull;
    }

    private static boolean isLeftOfLine(Point a, Point b, Point c) {
        // Use cross product to determine if c is to the left of line ab
        return (b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX()) > 0;
    }


    private static Optional<Point> findLeftMost(List<Point> points) {
        return points.stream().min(Comparator.comparingDouble(Point::getX));
    }

    public static void main(String[] args) {
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 3));
        points.add(new Point(2, 2));
        points.add(new Point(1, 1));
        points.add(new Point(2, 1));
        points.add(new Point(3, 0));
        points.add(new Point(0, 0));
        points.add(new Point(3, 3));

        ConvexHullAlgorithm giftWrappingAlgorithm = new GiftWrappingAlgorithm();
        List<Point> hull = giftWrappingAlgorithm.convexHull(points);
        for (Point p : hull) {
            System.out.println("(" + p.getX() + ", " + p.getY() + ")");
        }
    }
}

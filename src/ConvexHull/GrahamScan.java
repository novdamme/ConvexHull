package ConvexHull;

import java.awt.geom.Point2D.Double;
import java.util.*;

public class GrahamScan {
    private static final Random rand = new Random(0);

    public static void main(String[] args) {
        GrahamScan grahamScan = new GrahamScan();
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            points.add(new Point(rand.nextDouble(), rand.nextDouble()));
        }
        List<Point> sol = grahamScan.getConvexHull(points);
        for (Point p : sol) {
            System.out.println(p);
        }
    }

    public List<Point> getConvexHull(List<Point> points) {
        Point lowest = getLowestPoint(points);
        points.sort(Comparator.comparingDouble((Point a) -> a.getAngle(lowest)).thenComparingDouble((a) -> a.getDistance(lowest)));
        Stack<Point> stack = new Stack<>();
        stack.push(points.get(0));
        stack.push(points.get(1));
        for (int i = 2; i < points.size(); i++) {
            Point head = points.get(i);
            Point middle = stack.pop();
            Point tail = stack.peek();
            double crossProduct = ((middle.x - tail.x) * (head.y - tail.y)) - ((middle.y - tail.y) * (head.x - tail.x));
            if (crossProduct > 0) {
                stack.push(middle);
                stack.push(head);
            } else if (crossProduct < 0) {
                i--;
            } else {
                stack.push(head);
            }
        }
        Collections.reverse(stack);
        return new ArrayList<>(stack);
    }

    private Point getLowestPoint(List<Point> points) {
        Point lowest = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            Point temp = points.get(i);
            if (temp.y < lowest.y || (temp.y == lowest.y && temp.x < lowest.x)) {
                lowest = temp;
            }
        }
        return lowest;
    }

    private static class Point extends Double {
        public Point(double x, double y) {
            super(x, y);
        }

        public double getAngle(Point p) {
            return Math.atan2(y - p.y, x - p.x);
        }

        public double getDistance(Point p) {
            return Math.sqrt(((p.x - x) * (p.x - x)) + ((p.y - y) * (p.y - y)));
        }
    }
}
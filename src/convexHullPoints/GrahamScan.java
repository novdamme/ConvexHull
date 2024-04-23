package convexHullPoints;

import dto.Point;

import java.util.*;

public class GrahamScan {
    private static final Random rand = new Random(0);
    private Point lowest;

    public static void main(String[] args) {
        GrahamScan grahamScan = new GrahamScan();
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            points.add(new Point(rand.nextDouble(), rand.nextDouble()));
        }
        List<Point> sol = grahamScan.getConvexHull(points);
        for (Point p : sol) {
            System.out.println(p.getX() + " " + p.getY());
        }
    }

    public List<Point> getConvexHull(List<Point> points) {
        lowest = Collections.min(points, Comparator.comparingDouble(Point::getY).thenComparingDouble(Point::getX));
        points.sort(Comparator.comparingDouble(this::getAngle).thenComparingDouble(this::getDistance));
        Stack<Point> stack = new Stack<>();
        stack.push(points.get(0));
        stack.push(points.get(1));
        for (int i = 2; i < points.size(); i++) {
            Point p1 = points.get(i);
            Point p2 = stack.pop();
            Point p3 = stack.peek();
            double crossProduct = ((p2.getX() - p3.getX()) * (p1.getY() - p3.getY())) - ((p2.getY() - p3.getY()) * (p1.getX() - p3.getX()));
            if (crossProduct > 0) {
                stack.push(p2);
                stack.push(p1);
            } else if (crossProduct < 0) {
                i--;
            } else {
                stack.push(p1);
            }
        }
        Collections.reverse(stack);
        return new ArrayList<>(stack);
    }

    public double getAngle(Point p1) {
        return Math.atan2(p1.getY() - lowest.getY(), p1.getX() - lowest.getX());
    }

    public double getDistance(Point p1) {
        return Math.sqrt(Math.pow(lowest.getX() - p1.getX(), 2) + Math.pow(lowest.getY() - p1.getY(), 2));
    }
}
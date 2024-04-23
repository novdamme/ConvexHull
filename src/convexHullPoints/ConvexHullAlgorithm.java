package convexHullPoints;

import dto.Point;

import java.util.List;

public interface ConvexHullAlgorithm {
    List<Point> convexHull(List<Point> points);
}

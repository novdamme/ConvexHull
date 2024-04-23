package benchmark;

import convexHullPoints.ConvexHull;
import convexHullPoints.ConvexHullAlgorithm;
import convexHullPoints.GrahamScan;
import dto.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class benchmarkPoints {
    private static final ConvexHullAlgorithm[] algorithms = {
            new ConvexHull(),
            new GrahamScan()
    };
    private static final String[] files = {
            "resources/MIXED/N100000_10.txt"
    };

    private static List<Point> parse(String filename) {
        List<Point> points = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length == 4) {
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    points.add(new Point(x, y));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return points;
    }

    public static void main(String[] args) {
        for (String file : files) {
            for (ConvexHullAlgorithm algorithm : algorithms) {
                List<Point> inputPoints = parse(file);
                long start = System.currentTimeMillis();
                System.out.println("Testing file: " + file);
                System.out.println("Testing algorithm: " + algorithm.getClass().getSimpleName());
                List<Point> hull = algorithm.convexHull(inputPoints);
                long end = System.currentTimeMillis();
                System.out.println("Convex hull: " + hull);
                System.out.println("Size: " + hull.size());
                System.out.println("Time: " + (end - start));
                System.out.println();
            }
        }
    }
}

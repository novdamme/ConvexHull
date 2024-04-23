package benchmark;

import convexHullPoints.ConvexHull;
import convexHullPoints.GrahamScan;
import dto.Point;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class benchmarkPoints {
    private static List<Point> parse(String filename) {
        List<Point> points = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                double x = Double.parseDouble(parts[1]);
                double y = Double.parseDouble(parts[2]);
                points.add(new Point(x, y));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return points;
    }

    public static void main(String[] args) {
        List<Point> inputPoints = parse("resources/MIXED/N100000_10.txt");
        ConvexHull cvh = new ConvexHull();
        GrahamScan grs = new GrahamScan();
        cvh.hull(inputPoints);
        grs.getConvexHull(inputPoints);
    }
}

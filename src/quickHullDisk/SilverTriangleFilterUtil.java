package quickHullDisk;

import dto.Disk;
import dto.Line;
import dto.Pair;
import dto.Point;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class SilverTriangleFilterUtil {

  /**
   * Enumerator for different configurations
   */
  public enum SilverConfig {
    CASE_A,
    CASE_B,
    CASE_C1,
    CASE_C2
  };

  private final static Random random = new Random(0);

  /**
   * Quick method to check whether a triangle configuration is silver
   * 
   * @param numOfDisks
   * @param numOfDisksOnFrontEdge
   * @param numOfDisksOnBackEdge
   * @param preApexDisk
   * @param postApexDisk
   * @return
   */
  public static boolean triangleFilterIsSilver(
      int numOfDisks,
      int numOfDisksOnFrontEdge,
      int numOfDisksOnBackEdge,
      Disk preApexDisk,
      Disk postApexDisk) {
    return (!preApexDisk.equals(postApexDisk) && ((numOfDisksOnFrontEdge == numOfDisks && numOfDisksOnBackEdge == 1)
        || (numOfDisksOnFrontEdge == 1 && numOfDisksOnBackEdge == numOfDisks)));
  }

  /**
   * Method to retrieve the current triangleconfiguration assuming there is one
   * 
   * @param disks
   * @param orientedLinePQ
   * @param startDisk
   * @param endDisk
   * @param onPositiveDisks
   * @return
   */
  public static SilverConfig getSilverTriangleConfiguration(
      List<Disk> nonPositiveDisks,
      List<Disk> onPositiveDisks,
      Line orientedLinePQ) {

    if (nonPositiveDisks.size() == 0) {
      if (onPositiveDisks.size() <= 2) {
        return SilverConfig.CASE_A;
      } else {
        return SilverConfig.CASE_B;
      }
    } else {
      if (nonPositiveDisks.size() == 1) {
        return SilverConfig.CASE_C1;
      } else {
        return SilverConfig.CASE_C2;
      }
    }
  }

  /**
   * Method to regularize the triangle configuration
   * 
   * @param disks
   * @param preApexDisk
   * @param postApexDisk
   * @param hullPointP
   * @param hullPointQ
   * @param frontEdgeDisks
   * @param backEdgeDisks
   * @param apexDisk
   * @param triangleApexX
   */
  public static void regularizeSliverTriangleNPivotDisks(
      List<Disk> disks,
      Disk preApexDisk,
      Disk postApexDisk,
      Point hullPointP,
      Point hullPointQ,
      List<Disk> frontEdgeDisks,
      List<Disk> backEdgeDisks,
      Disk apexDisk,
      Point triangleApexX) {

    Line orientedNonNegativeTangentLine = computeOrientedTangentLine(preApexDisk, postApexDisk);
    List<Disk> onPositiveDisks = DisksUtil.findOnPositiveDisks(disks, orientedNonNegativeTangentLine,
        preApexDisk, postApexDisk);
    List<Disk> nonPositiveDisks = DisksUtil.findExpandedNonPositiveDisks(disks,
        orientedNonNegativeTangentLine, preApexDisk, postApexDisk);
    nonPositiveDisks = nonPositiveDisks.stream().filter((x) -> (!x.equals(preApexDisk) && !x.equals(postApexDisk)))
        .collect(Collectors.toList());

    // System.out.println("onPositiveDisks=" + onPositiveDisks.toString());
    // System.out.println("nonPositiveDisks=" + nonPositiveDisks.toString());

    SilverConfig config = getSilverTriangleConfiguration(
        nonPositiveDisks, onPositiveDisks, orientedNonNegativeTangentLine);
    System.out.println("SilverTriangleConfig found");
    System.out.println("inside silverconfig, disk.size=" + disks.size());
    System.out.println("configuration=" + config);

    switch (config) {
      case CASE_A: {
        if (1 == random.nextInt(1)) {
          triangleApexX.update(orientedNonNegativeTangentLine.getStart());
          apexDisk.update(preApexDisk);
        } else {
          triangleApexX.update(orientedNonNegativeTangentLine.getEnd());
          apexDisk.update(postApexDisk);
        }
        break;
      }

      case CASE_B: {
        List<Disk> onPositiveDisksWithouthDqDp = onPositiveDisks.stream()
            .filter((x) -> (x.equals(preApexDisk) || x.equals(postApexDisk))).collect(Collectors.toList());

        apexDisk.update(onPositiveDisksWithouthDqDp.get(random.nextInt(onPositiveDisksWithouthDqDp.size() - 1)));
        triangleApexX.update(apexDisk.findFarthestPoint(orientedNonNegativeTangentLine));
        break;
      }

      case CASE_C1: {
        apexDisk.update(onPositiveDisks.get(0));
        triangleApexX.update(onPositiveDisks.get(0).findFarthestPoint(orientedNonNegativeTangentLine));
        break;
      }

      case CASE_C2: {
        Disk newApexDisk = DisksUtil.findApexDisk(nonPositiveDisks, orientedNonNegativeTangentLine);

        Point apexDiskFarthestPoint = newApexDisk.findFarthestPoint(orientedNonNegativeTangentLine);
        apexDisk.update(newApexDisk);
        triangleApexX.update(apexDiskFarthestPoint);
        break;
      }
    }
    Line orientedFrontEdgeLine = new Line(hullPointP, triangleApexX);
    Line orientedBackEdgeLine = new Line(triangleApexX, hullPointQ);
    frontEdgeDisks.addAll(DisksUtil.findExpandedNonPositiveDisks(disks, orientedFrontEdgeLine, preApexDisk, apexDisk));
    backEdgeDisks.addAll(DisksUtil.findExpandedNonPositiveDisks(disks, orientedBackEdgeLine, apexDisk, postApexDisk));
  }

  /**
   * Method to compute the outer tangent line between two circles
   * These circles cannot be above eachother and have the exact same width
   * #TODO: check wheter this actually ever turns up, the paper doesnt deal with
   * this
   * 
   * @param d1
   * @param d2
   * @return
   */
  public static Line computeOrientedTangentLine(Disk d1, Disk d2) {
    Double[] lines = getTangentLines(d1, d2);
    // convert ax+by+c=0 to ax+b=y
    Point tan1 = new Point(lines[0] / (-lines[1]), (lines[2] / -lines[1]));
    Point tan2 = new Point(lines[3] / (-lines[4]), (lines[5] / -lines[4]));

    Line t1 = new Line(getTangentPoint(d1, tan1), getTangentPoint(d2, tan1));
    Line t2 = new Line(getTangentPoint(d1, tan2), getTangentPoint(d2, tan2));

    double totalDistance = t1.getDistance(d1.getCenter()) + t1.getDistance(d2.getCenter());

    if (totalDistance > 0) {
      // System.out.println("t1 was chosen");
      return t1;
    } else {
      // System.out.println("t2 was chosen");
      return t2;
    }
  }

  /**
   * Method to find the intersection between a tangent line and a circle
   * 
   * @param d
   * @param line
   * @return
   */
  public static Point getTangentPoint(Disk d, Point line) {
    double m = line.getX();
    double c = line.getY();
    double p = d.getCenter().getX();
    double q = d.getCenter().getY();
    double r = d.getRadius();
    double A = (m * m) + 1;
    double B = 2 * ((m * c) - (m * q) - p);
    double x = -B / (2 * A);
    double y = m * x + c;
    double C = (q * q) - (r * r) + (p * p) - (2 * c * q) + (c * c);
    assert (0 == B * B - (4 * A * C));
    return new Point(x, y);
  }

  /**
   * Method to retrieve the outer tangent lines between 2 circles
   * 
   * @param inputD1
   * @param inputD2
   * @return
   */
  public static Double[] getTangentLines(Disk inputD1, Disk inputD2) {
    // System.out.println(inputD1.getCenter().getX() + ", " +
    // inputD1.getCenter().getY() + ", " + inputD1.getRadius());
    // System.out.println(inputD2.getCenter().getX() + ", " +
    // inputD2.getCenter().getY() + ", " + inputD2.getRadius());
    Disk d1;
    Disk d2;
    if (inputD2.getRadius() > inputD1.getRadius()) {
      d1 = new Disk(inputD2.getCenter(), inputD2.getRadius());
      d2 = new Disk(inputD1.getCenter(), inputD1.getRadius());
    } else {
      d2 = new Disk(inputD2.getCenter(), inputD2.getRadius());
      d1 = new Disk(inputD1.getCenter(), inputD1.getRadius());
    }

    double Dx = d2.getCenter().getX() - d1.getCenter().getX();
    double Dy = d2.getCenter().getY() - d1.getCenter().getY();
    double Dr = d2.getRadius() - d1.getRadius();
    double d = Math.sqrt((Dx * Dx) + (Dy * Dy));

    double X = Dx / d;
    double Y = Dy / d;
    double R = Dr / d;

    double a1 = R * X - (Y * Math.sqrt(1 - (R * R)));
    double b1 = R * Y + (X * Math.sqrt(1 - (R * R)));
    double c1 = d1.getRadius() - (a1 * d1.getCenter().getX() + (b1 * d1.getCenter().getY()));

    double a2 = R * X + (Y * Math.sqrt(1 - (R * R)));
    double b2 = R * Y - (X * Math.sqrt(1 - (R * R)));
    double c2 = d1.getRadius() - (a2 * d1.getCenter().getX() + (b2 * d1.getCenter().getY()));

    // System.out.println(a1 + " " + b1 + " " + c1);
    // System.out.println(a2 + " " + b2 + " " + c2);

    Double[] res = { a1, b1, c1, a2, b2, c2 };

    return res;
  }

  public static void main(String[] args) {
    Disk d1 = new Disk(new Point(0, 0), 1.01);
    Disk d2 = new Disk(new Point(1, 0), 1);
    Disk d3 = new Disk(new Point(1.5, 0.25), 0.5);

    // test tangentLine
    Line x = computeOrientedTangentLine(d1, d2);
    System.out.println("correct: {0.01, -1.01}, {1.01, -0.999}");
    System.out.println("calculated: " + x.getStart().toString() + ", " + x.getEnd().toString());

    // test case_A
    List<Disk> disks = new ArrayList<>();
    disks.add(d1);
    disks.add(d2);
    disks.add(d3);
    Point triangleApex = new Point(0, 0);

    regularizeSliverTriangleNPivotDisks(disks, d1, d2, d1.getHighLeftExtremePoint(), d2.getLowRightExtremePoint(),
        new ArrayList<>(), new ArrayList<>(), new Disk(null, 0), triangleApex);
    System.out.println(triangleApex);

    // test case_C1

  }
}

package quickHullDisk;

import dto.Disk;
import dto.Line;
import dto.Pair;
import dto.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DisksUtil {

  private final static Random random = new Random(1);

  public static Disk findDiskWithHighLeftExtremePoint(List<Disk> disks) {
    Disk diskWithHighLeftExtremePoint = disks.get(0);

    for (Disk disk : disks) {
      if (disk.getLeftMostX() < diskWithHighLeftExtremePoint.getLeftMostX()) {
        diskWithHighLeftExtremePoint = disk;
      } else if (disk.getLeftMostX() == diskWithHighLeftExtremePoint.getLeftMostX()) {
        if (disk.getCenter().getY() > diskWithHighLeftExtremePoint.getCenter().getY()) {
          diskWithHighLeftExtremePoint = disk;
        } else if ((disk.getCenter().getY() == diskWithHighLeftExtremePoint.getCenter().getY()
            && disk.getRadius() > diskWithHighLeftExtremePoint.getRadius())) {
          diskWithHighLeftExtremePoint = disk;
        }
      }
    }
    return diskWithHighLeftExtremePoint;
  }

  public static Disk findDiskWithLowRightExtremePoint(List<Disk> disks) {
    Disk diskWithLowRightExtremePoint = disks.get(0);
    for (Disk disk : disks) {
      if (disk.getRightMostX() > diskWithLowRightExtremePoint.getRightMostX()) {
        diskWithLowRightExtremePoint = disk;
      } else if (disk.getRightMostX() == diskWithLowRightExtremePoint.getRightMostX()) {
        if (disk.getCenter().getY() < diskWithLowRightExtremePoint.getCenter().getY()) {
          diskWithLowRightExtremePoint = disk;
        } else if (disk.getCenter().getY() == diskWithLowRightExtremePoint.getCenter().getY()
            && disk.getRadius() > diskWithLowRightExtremePoint.getRadius()) {
          diskWithLowRightExtremePoint = disk;
        }
      }
    }
    return diskWithLowRightExtremePoint;
  }

  private static boolean diskIsAMemberOfExpandedNonPositiveSet(Disk disk, Line orientedLine) {
    Line orthogonalLineAtStartPoint = orientedLine.makePerpendicularLine(orientedLine.getStart());
    Line orthogonalLineAtEndPoint = orientedLine.makePerpendicularLine(orientedLine.getEnd());
    double signedDistance = orientedLine.getDistance(disk.getCenter());

    if (signedDistance <= -disk.getRadius() + 1e-6) { // negative or on-negative
      double d1 = orthogonalLineAtStartPoint.getDistance(disk.getCenter());
      double d2 = orthogonalLineAtEndPoint.getDistance(disk.getCenter());
      return d1 > disk.getRadius() - 1e-6
          && d2 < -disk.getRadius() + 1e-6;
    } else if (signedDistance > -disk.getRadius() + 1e-6 && signedDistance < disk.getRadius()) { // crossing
      return orthogonalLineAtStartPoint.getDistance(disk.getCenter()) > -1e-6
          && orthogonalLineAtEndPoint.getDistance(disk.getCenter()) < 1e-6;
    } else if (Math.abs(signedDistance - disk.getRadius()) < 1e-6) { // on-positive
      return orthogonalLineAtStartPoint.getDistance(disk.getCenter()) > -1e-6
          && orthogonalLineAtEndPoint.getDistance(disk.getCenter()) < 1e-6;
    } else { // positive
      return false;
    }
  }

  public static void findInitialDiskSets(List<Disk> disks, Line orientedLinePQ, List<Disk> initialDR,
      List<Disk> initialDL) {
    for (Disk disk : disks) {
      if (orientedLinePQ.getDistance(disk.getCenter()) <= disk.getRadius() + 1e-6) { // nonPositive
        initialDR.add(disk);
      } else if (orientedLinePQ.getDistance(disk.getCenter()) >= -disk.getRadius() - 1e-6) { // nonNegative
        initialDL.add(disk);
      }
    }
  }

  public static Disk findApexDisk(List<Disk> disks, Line orientedLinePQ) {
    double largestDistance = 0;
    Disk apexDisk = null;
    System.out.println(disks.size());
    for (Disk disk : disks) {
      double distanceOfCurrentDisk = disk.getRadius() + orientedLinePQ.getDistance(disk.getCenter());
      if (apexDisk == null || distanceOfCurrentDisk > largestDistance) {
        largestDistance = distanceOfCurrentDisk;
        apexDisk = disk;
        // System.out.println("LargestDistance=" + largestDistance + ",disk=" + disk);
      } else if (distanceOfCurrentDisk == largestDistance) {
        if (random.nextInt(2) == 1) {
          apexDisk = disk;
        }
      }
    }
    return apexDisk;
  }

  // Atm this is double work but I dont want to break anything youve made at this
  // point
  public static List<Disk> findOnPositiveDisks(List<Disk> disks, Line orientedLinePQ, Disk startDisk,
      Disk endDisk) {
    double largestDistance = 0.0;
    List<Disk> onPositiveDisks = new ArrayList<>();

    for (Disk disk : disks) {
      if (disk.equals(startDisk) || disk.equals(endDisk)) {
        continue;
      }
      double distanceOfCurrentDisk = disk.getRadius() - orientedLinePQ.getDistance(disk.getCenter());
      if (distanceOfCurrentDisk > largestDistance) {
        largestDistance = distanceOfCurrentDisk;
        onPositiveDisks = new ArrayList<>();

        onPositiveDisks.add(disk);
      } else if (distanceOfCurrentDisk == largestDistance) {
        onPositiveDisks.add(disk);
      }
    }
    if (startDisk.equals(endDisk)) {
      onPositiveDisks.add(startDisk);
    } else {
      onPositiveDisks.add(startDisk);
      onPositiveDisks.add(endDisk);
    }
    return onPositiveDisks;
  }

  public static List<Disk> findExpandedNonPositiveDisks(List<Disk> disks, Line orientedLine, Disk startDisk,
      Disk endDisk) {
    List<Disk> nonPositiveDisks = new ArrayList<>();
    Line orthogonalLineAtStartPoint = orientedLine.makePerpendicularLine(orientedLine.getStart());
    Line orthogonalLineAtEndPoint = orientedLine.makePerpendicularLine(orientedLine.getEnd());
    // System.out.println();
    // System.out.println("orientedLine=" + orientedLine);
    // System.out.println("start=" + orthogonalLineAtStartPoint);
    // System.out.println("end=" + orthogonalLineAtEndPoint);
    if (orientedLine.getStart() != orientedLine.getEnd()) {
      for (Disk disk : disks) {
        if (disk.equals(startDisk) || disk.equals(endDisk)) {
          continue;
        }
        if (diskIsAMemberOfExpandedNonPositiveSet(disk, orientedLine)) { // nonPositive
          nonPositiveDisks.add(disk);
        }
      }
    }

    if (startDisk.equals(endDisk)) {
      nonPositiveDisks.add(startDisk);
    } else {
      nonPositiveDisks.add(startDisk);
      nonPositiveDisks.add(endDisk);
    }
    return nonPositiveDisks;
  }

  public static Disk findLargestContainingDisks(Disk apexDisk, List<Disk> disks, List<Disk> containedDisks) {
    Disk largestDisk = apexDisk;
    for (Disk d : disks) {
      if (largestDisk == d)
        continue;
      if (d.contains(largestDisk)) {
        containedDisks.add(largestDisk);
        largestDisk = d;
      } else if (largestDisk.contains(d)) {
        containedDisks.add(d);
      }
    }
    return largestDisk;
  }
}

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
            double leftMostXDiff = disk.getLeftMostX() - diskWithHighLeftExtremePoint.getLeftMostX();
            if(leftMostXDiff < 0) {
                diskWithHighLeftExtremePoint = disk;
            } else if (leftMostXDiff == 0) {
                double yDiff = disk.getCenter().getY() - diskWithHighLeftExtremePoint.getCenter().getY();
                if(yDiff > 0 || (yDiff == 0 && disk.getRadius() > diskWithHighLeftExtremePoint.getRadius())) {
                    diskWithHighLeftExtremePoint = disk;
                }
            }
        }
        return diskWithHighLeftExtremePoint;
    }

    public static Disk findDiskWithLowRightExtremePoint(List<Disk> disks) {
        Disk diskWithLowRightExtremePoint = disks.get(0);
        for (Disk disk : disks) {
            double rightMostXDiff = disk.getRightMostX() - diskWithLowRightExtremePoint.getRightMostX();
            if(rightMostXDiff > 0) {
                diskWithLowRightExtremePoint = disk;
            } else if (rightMostXDiff == 0) {
                double yDiff = disk.getCenter().getY() - diskWithLowRightExtremePoint.getCenter().getY();
                if(yDiff < 0 || (yDiff == 0 && disk.getRadius() > diskWithLowRightExtremePoint.getRadius())) {
                    diskWithLowRightExtremePoint = disk;
                }
            }
        }
        return diskWithLowRightExtremePoint;
    }

    public static void findInitialDiskSets(List<Disk> disks, Line orientedLinePQ, List<Disk> initialDR, List<Disk> initialDL) {
        for (Disk disk : disks) {
            if (orientedLinePQ.getDistance(disk.getCenter()) <= disk.getRadius() + 1e-6) { // nonPositive
                initialDR.add(disk);
            } else if(orientedLinePQ.getDistance(disk.getCenter()) >= -disk.getRadius() - 1e-6) { // nonNegative
                initialDL.add(disk);
            }
        }
    }

    public static Disk findApexDisk(List<Disk> disks, Line orientedLinePQ) {
        double largestDistance = 0.0;
        Disk apexDisk = null;

        for (Disk disk : disks) {
            double distanceOfCurrentDisk = disk.getRadius() - orientedLinePQ.getDistance(disk.getCenter());
            if (distanceOfCurrentDisk > largestDistance) {
                largestDistance = distanceOfCurrentDisk;
                apexDisk = disk;
            } else if (distanceOfCurrentDisk == largestDistance) {
                if(random.nextInt(2) == 1) {
                    apexDisk = disk;
                }
            }
        }
        return apexDisk;
    }

    // Atm this is double work but I dont want to break anything youve made at this point
    public static List<Pair<Disk, Point>> findOnPositiveDisks(List<Disk> disks, Line orientedLinePQ, Disk startDisk, Disk endDisk) {
        double largestDistance = 0.0;
        List<Pair<Disk, Point>> apexDisks = new ArrayList<>();

        for (Disk disk : disks) {
            double distanceOfCurrentDisk = disk.getRadius() - orientedLinePQ.getDistance(disk.getCenter());
            if (distanceOfCurrentDisk > largestDistance) {
                largestDistance = distanceOfCurrentDisk;
                apexDisks = new ArrayList<>();
                Point farthesPoint = disk.findFarthestPoint(orientedLinePQ);
                
                apexDisks.add(new Pair<Disk,Point>(disk, farthesPoint));
            } else if (distanceOfCurrentDisk == largestDistance) {
                Point farthesPoint = disk.findFarthestPoint(orientedLinePQ);
                apexDisks.add(new Pair<Disk,Point>(disk, farthesPoint));
            }
        }
        return apexDisks;
    }
    
    public static List<Disk> findExpandedNonPositiveDisks(List<Disk> disks, Line orientedLine, Disk startDisk, Disk endDisk) {
        List<Disk> nonPositiveDisks = new ArrayList<>();
        if(orientedLine.getStart() != orientedLine.getEnd()) {
            for(Disk disk : disks) {
                if(disk.equals(startDisk) || disk.equals(endDisk)) {
                    continue;
                }
                if (orientedLine.getDistance(disk.getCenter()) <= disk.getRadius() + 1e-6) { // nonPositive
                    nonPositiveDisks.add(disk);
                }
            }
        }

        if(startDisk.equals(endDisk)) {
            nonPositiveDisks.add(startDisk);
        } else {
            nonPositiveDisks.add(startDisk);
            nonPositiveDisks.add(endDisk);
        }

        return nonPositiveDisks;
    }
}

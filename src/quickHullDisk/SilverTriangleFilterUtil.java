package quickHullDisk;

import dto.Disk;
import dto.Line;
import dto.Pair;
import dto.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SilverTriangleFilterUtil {

    public enum SilverConfig {
            CASE_A,
            CASE_B,
            CASE_C1,
            CASE_C2
    };
    public static boolean triangleFilterIsSilver(
            int numOfDisks,
            int numOfDisksOnFrontEdge,
            int numOfDisksOnBackEdge,
            Disk preApexDisk,
            Disk postApexDisk
    ) {
        return !preApexDisk.equals(postApexDisk) && (
                (numOfDisksOnFrontEdge == numOfDisks && numOfDisksOnBackEdge == 1)
                        || (numOfDisksOnFrontEdge == 1 && numOfDisksOnBackEdge == numOfDisks)
        );
    }

    public static SilverConfig getSilverTriangleConfiguration(List<Disk> disks, Line orientedLinePQ, Disk startDisk, Disk endDisk, List<Pair<Disk, Point>> apexDisks) {
         Disk apex = apexDisks.get(0).x;
         double height = apex.getRadius() - orientedLinePQ.getDistance(apex.getCenter()); 
         if (height == 0) {
                 if (apexDisks.size() > 2) {
                         return SilverConfig.CASE_A;
                 } else {
                         return SilverConfig.CASE_B;
                 }
         } else {
                 if(apexDisks.size() == 1) {
                        return SilverConfig.CASE_C1; 
                 } else {
                         return SilverConfig.CASE_C2;
                 }
         }
    } 

    public static void regularizeSliverTriangleNPivotDisks(
            List<Disk> disks,
            Disk preApexDisk,
            Disk postApexDisk,
            Point hullPointP,
            Point hullPointQ,
            List<Disk> frontEdgeDisks,
            List<Disk> backEdgeDisks,
            Disk apexDisk,
            Point triangleApexX
            ) {

        Line orientedNonNegativeTangentLine = new Line(null, null);
        List<Pair<Disk, Point>> apexDisks = DisksUtil.findOnPositiveDisks(disks, orientedNonNegativeTangentLine, preApexDisk, postApexDisk);
        SilverConfig config = getSilverTriangleConfiguration(disks, orientedNonNegativeTangentLine, preApexDisk, postApexDisk, apexDisks);

        switch(config) {
                case CASE_A: {
                        triangleApexX = orientedNonNegativeTangentLine.getStart(); 
                        apexDisk = preApexDisk;
                        
                } case CASE_B : {
                        Random random = new Random(apexDisks.size() - 1);
                        
                        Pair<Disk, Point> apexDiskPair = apexDisks.get(random.nextInt());
                        while(apexDisk.equals(preApexDisk) || apexDisk.equals(postApexDisk)) {
                                apexDiskPair = apexDisks.get(random.nextInt());
                        }
                        apexDisk = apexDiskPair.x;
                        triangleApexX = apexDiskPair.y;
                        break;
                } case CASE_C1: {
                        apexDisk = apexDisks.get(0).x;
                        triangleApexX = apexDisks.get(0).y;
                        break;
                } case CASE_C2 : {
                        Random random = new Random(apexDisks.size() - 1);
                        
                        Pair<Disk, Point> apexDiskPair = apexDisks.get(random.nextInt());
                        while(apexDisk.equals(preApexDisk) || apexDisk.equals(postApexDisk)) {
                                apexDiskPair = apexDisks.get(random.nextInt());
                        }
                        apexDisk = apexDiskPair.x;
                        triangleApexX = apexDiskPair.y;
                        break;
                }
        }
    }
}

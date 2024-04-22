package quickHullDisk;

import dto.Disk;
import dto.Line;
import dto.Pair;
import dto.Point;

import java.util.ArrayList;
import java.util.List;

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
                 List<Disk> nonPositiveDisks = DisksUtil.findExpandedNonPositiveDisks(disks, orientedLinePQ, startDisk, endDisk);
                 if (nonPositiveDisks.size() > 2) {
                         return SilverConfig.CASE_A;
                 } else {
                         return SilverConfig.CASE_B;
                 }
         } else {
                return SilverConfig.CASE_C1;
                 // } else {
                 //         return SilverConfig.CASE_C2;
                 // }
         }
    } 

    /**
    * regularizes silver triangles 
    * @param disks
    * @param preApexDisk
    * @param postApexDisk
    * @param hullPointP
    * @param hullPointQ
    * @param frontEdgeDisks
    * @param backEdgeDisks
    * @param possibleApexDisks
    * @return A Pair object containing the pivot disk and the pivot point
    **/
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
                        List<Disk> nonPositiveDisks = DisksUtil.findExpandedNonPositiveDisks(disks, orientedNonNegativeTangentLine, preApexDisk, postApexDisk);
                        for (Disk d : disks) {
                                if (!d.equals(preApexDisk) && !d.equals(postApexDisk) && !nonPositiveDisks.contains(d)) {
                                        apexDisk = d;      
                                }
                        }
                        
                        break;
                } case CASE_C1: {
                        break;
                } case CASE_C2 : {
                        break;
                }
        }
    }
}

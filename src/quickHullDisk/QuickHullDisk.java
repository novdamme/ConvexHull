package quickHullDisk;

import dto.*;

import java.util.ArrayList;
import java.util.List;

public class QuickHullDisk {

    public List<Disk> hullDisks;

    public QuickHullDisk() {
        this.hullDisks = new ArrayList<>();
    }

    public void run(List<Disk> disks) {
        if(disks.size() == 0) {
            return;
        }

        if(disks.size() == 1) {
            hullDisks.add(disks.get(0));
            return;
        }

        Disk diskHavingHighLeftPoint = DisksUtil.findDiskWithHighLeftExtremePoint(disks);
        Point highLeftExtremePoint = diskHavingHighLeftPoint.getHighLeftExtremePoint();

        Disk diskHavingLowRightPoint = DisksUtil.findDiskWithLowRightExtremePoint(disks);
        Point lowRightExtremePoint = diskHavingHighLeftPoint.getLowRightExtremePoint();

        Line orientedLinePQ = new Line(highLeftExtremePoint, lowRightExtremePoint);

        List<Disk> initialDR = new ArrayList<>();
        List<Disk> initialDL = new ArrayList<>();
        DisksUtil.findInitialDiskSets(disks, orientedLinePQ, initialDR, initialDL);

        findHull(initialDL, diskHavingLowRightPoint, diskHavingHighLeftPoint, lowRightExtremePoint, highLeftExtremePoint);
        findHull(initialDR, diskHavingHighLeftPoint, diskHavingLowRightPoint, highLeftExtremePoint, lowRightExtremePoint);
    }

    private void findHull(List<Disk> disks, Disk preApexDisk, Disk postApexDisk, Point hullPointP, Point hullPointQ) {
        if (disks.size() == 1) {
            this.hullDisks.add(disks.get(0));
            return;
        }
        if (disks.size() == 2 && preApexDisk.equals(postApexDisk)) {
            hullDisks.add(preApexDisk);
            hullDisks.add(postApexDisk);
            return;
        }

        Line orientedLine = new Line(hullPointP, hullPointQ);
        Disk apexDisk = DisksUtil.findApexDisk(disks, orientedLine);

        Point triangleApexX = apexDisk.findFarthestPoint(orientedLine);
        Line orientedFrontEdgeLine = new Line(hullPointP, triangleApexX);
        Line orientedBackEdgeLine = new Line(triangleApexX, hullPointQ);

        List<Disk> frontEdgeDisks = DisksUtil.findExpandedNonPositiveDisks(disks, orientedFrontEdgeLine, preApexDisk, apexDisk);
        List<Disk> backEdgeDisks = DisksUtil.findExpandedNonPositiveDisks(disks, orientedBackEdgeLine, apexDisk, postApexDisk);

        if (SilverTriangleFilterUtil.triangleFilterIsSilver(disks.size(), frontEdgeDisks.size(), backEdgeDisks.size(), preApexDisk, postApexDisk)) {
            frontEdgeDisks.clear();
            backEdgeDisks.clear();
            SilverTriangleFilterUtil.regularizeSliverTriangleNPivotDisks(
                    disks,
                    preApexDisk,
                    postApexDisk,
                    hullPointP,
                    hullPointQ,
                    frontEdgeDisks,
                    backEdgeDisks,
                    apexDisk,
                    triangleApexX
            );
        }

        findHull(frontEdgeDisks, preApexDisk, apexDisk, hullPointP, triangleApexX);
        findHull(backEdgeDisks, apexDisk, postApexDisk, triangleApexX, hullPointQ);
    }



    public static void main(String[] args) {
        List<Disk> inputDisks = DiskIO.parse("resources/N100000_10.txt");
        QuickHullDisk qhd = new QuickHullDisk();
        qhd.run(inputDisks);
    }
}

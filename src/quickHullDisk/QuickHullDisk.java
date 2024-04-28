package quickHullDisk;

import dto.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuickHullDisk {

  public List<Disk> hullDisks;

  public QuickHullDisk() {
    this.hullDisks = new ArrayList<>();
  }

  public void run(List<Disk> disks) {
    if (disks.size() == 0) {
      return;
    }

    if (disks.size() == 1) {
      hullDisks.add(disks.get(0));
      return;
    }

    Disk diskHavingHighLeftPoint = DisksUtil.findDiskWithHighLeftExtremePoint(disks);
    Point highLeftExtremePoint = diskHavingHighLeftPoint.getHighLeftExtremePoint();

    Disk diskHavingLowRightPoint = DisksUtil.findDiskWithLowRightExtremePoint(disks);
    Point lowRightExtremePoint = diskHavingLowRightPoint.getLowRightExtremePoint();

    Line orientedLinePQ = new Line(highLeftExtremePoint, lowRightExtremePoint);

    List<Disk> initialDR = new ArrayList<>();
    List<Disk> initialDL = new ArrayList<>();
    DisksUtil.findInitialDiskSets(disks, orientedLinePQ, initialDR, initialDL);

    findHull(initialDR, diskHavingHighLeftPoint, diskHavingLowRightPoint, highLeftExtremePoint, lowRightExtremePoint);
    findHull(initialDL, diskHavingLowRightPoint, diskHavingHighLeftPoint, lowRightExtremePoint, highLeftExtremePoint);
  }

  private void findHull(List<Disk> disks, Disk preApexDisk, Disk postApexDisk, Point hullPointP, Point hullPointQ) {

    if (disks.size() == 1) {
      hullDisks.add(preApexDisk);
      return;
    }
    if (disks.size() == 2) {
      hullDisks.add(preApexDisk);
      hullDisks.add(postApexDisk);
      return;
    }

    Line orientedLine = new Line(hullPointP, hullPointQ);
    Disk apexDisk = DisksUtil.findApexDisk(disks, orientedLine);

    Point apexDiskFarthestPoint = apexDisk.findFarthestPoint(orientedLine);
    Line orientedFrontEdgeLine = new Line(hullPointP, apexDiskFarthestPoint);
    Line orientedBackEdgeLine = new Line(apexDiskFarthestPoint, hullPointQ);

    List<Disk> frontEdgeDisks = DisksUtil.findExpandedNonPositiveDisks(disks, orientedFrontEdgeLine, preApexDisk,
        apexDisk);
    List<Disk> backEdgeDisks = DisksUtil.findExpandedNonPositiveDisks(disks, orientedBackEdgeLine, apexDisk,
        postApexDisk);

    if (SilverTriangleFilterUtil.triangleFilterIsSilver(disks.size(),
        frontEdgeDisks.size(), backEdgeDisks.size(),
        preApexDisk, postApexDisk)) {

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
          apexDiskFarthestPoint);
    }


    findHull(new ArrayList<>(frontEdgeDisks), new Disk(preApexDisk), new Disk(apexDisk), new Point(hullPointP), new Point(apexDiskFarthestPoint));
    findHull(new ArrayList<>(backEdgeDisks), new Disk(apexDisk), new Disk(postApexDisk), new Point(apexDiskFarthestPoint), new Point(hullPointQ));
  }

  public static void main(String[] args) {
    List<Disk> inputDisks = DiskIO.parse("resources/RANDOM/N100000.txt");
    QuickHullDisk qhd = new QuickHullDisk();
    qhd.run(inputDisks);
    System.out.println(qhd.hullDisks);
    Set<Disk> uniqueItemsSet = new HashSet<>(qhd.hullDisks);

    System.out.println("size=" + uniqueItemsSet.size());
  }
}

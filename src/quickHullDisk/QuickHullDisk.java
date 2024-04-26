package quickHullDisk;

import dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    List<Disk> containedDisks = new ArrayList<>();
    apexDisk = DisksUtil.findLargestContainingDisks(apexDisk, disks, containedDisks);
    System.out.println("containedDisks size: " + containedDisks.size());
    disks.removeAll(containedDisks);
    disks.add(apexDisk);

    Point apexDiskFarthestPoint = apexDisk.findFarthestPoint(orientedLine);
    Line orientedFrontEdgeLine = new Line(hullPointP, apexDiskFarthestPoint);
    Line orientedBackEdgeLine = new Line(apexDiskFarthestPoint, hullPointQ);

    List<Disk> frontEdgeDisks = DisksUtil.findExpandedNonPositiveDisks(disks, orientedFrontEdgeLine, preApexDisk,
        apexDisk);
    List<Disk> backEdgeDisks = DisksUtil.findExpandedNonPositiveDisks(disks, orientedBackEdgeLine, apexDisk,
        postApexDisk);

    if (SilverTriangleFilterUtil.triangleFilterIsSilver(disks.size(), frontEdgeDisks.size(), backEdgeDisks.size(),
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

    // System.out.println(frontEdgeDisks.size());
    // System.out.println(backEdgeDisks.size());
    // try {
    //   TimeUnit.SECONDS.sleep(1);
    // } catch (InterruptedException e) {
    //   e.printStackTrace();
    // }

    findHull(frontEdgeDisks, preApexDisk, apexDisk, hullPointP, apexDiskFarthestPoint);
    findHull(backEdgeDisks, apexDisk, postApexDisk, apexDiskFarthestPoint, hullPointQ);
  }

  public static void main(String[] args) {
    List<Disk> inputDisks = DiskIO.parse("resources/MIXED/N100000_10.txt");
    QuickHullDisk qhd = new QuickHullDisk();
    qhd.run(inputDisks);
  }
}

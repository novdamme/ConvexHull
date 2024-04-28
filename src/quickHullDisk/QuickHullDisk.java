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
    Point lowRightExtremePoint = diskHavingLowRightPoint.getLowRightExtremePoint();

    Line orientedLinePQ = new Line(highLeftExtremePoint, lowRightExtremePoint);

    List<Disk> initialDR = new ArrayList<>();
    List<Disk> initialDL = new ArrayList<>();
    DisksUtil.findInitialDiskSets(disks, orientedLinePQ, initialDR, initialDL);
    System.out.println(diskHavingHighLeftPoint);
    System.out.println(diskHavingLowRightPoint);
    System.out.println("initialDR.size=" + initialDR.size());
    System.out.println("initialDL.size=" + initialDL.size());
    findHull(initialDR, diskHavingHighLeftPoint, diskHavingLowRightPoint, highLeftExtremePoint, lowRightExtremePoint);
    findHull(initialDL, diskHavingLowRightPoint, diskHavingHighLeftPoint, lowRightExtremePoint, highLeftExtremePoint);
  }

  private void findHull(List<Disk> disks, Disk preApexDisk, Disk postApexDisk, Point hullPointP, Point hullPointQ) {

    if (disks.size() == 1) {
      hullDisks.add(preApexDisk);
      return;
    }
    if (disks.size() == 2 && !preApexDisk.equals(postApexDisk)) {
      hullDisks.add(preApexDisk);
      hullDisks.add(postApexDisk);
      return;
    }

    Line orientedLine = new Line(hullPointP, hullPointQ);
    Disk apexDisk = new Disk(new Point(0, 0), 0);
    apexDisk.update(DisksUtil.findApexDisk(disks, orientedLine));

    Point apexDiskFarthestPoint = apexDisk.findFarthestPoint(orientedLine);
    Line orientedFrontEdgeLine = new Line(hullPointP, apexDiskFarthestPoint);
    Line orientedBackEdgeLine = new Line(apexDiskFarthestPoint, hullPointQ);

    // This would help with performace we can test it when it actually works
    // System.out.println(disks);
    // List<Disk> containedDisks = new ArrayList<>();
    // apexDisk = DisksUtil.findLargestContainingDisks(apexDisk, disks,
    // containedDisks);
    // System.out.println("containedDisks size: " + containedDisks.size());
    // disks.removeAll(containedDisks);
    List<Disk> frontEdgeDisks = DisksUtil.findExpandedNonPositiveDisks(disks, orientedFrontEdgeLine, preApexDisk,
        apexDisk);
    List<Disk> backEdgeDisks = DisksUtil.findExpandedNonPositiveDisks(disks, orientedBackEdgeLine, apexDisk,
        postApexDisk);

    if (SilverTriangleFilterUtil.triangleFilterIsSilver(disks.size(), frontEdgeDisks.size(), backEdgeDisks.size(),
        preApexDisk, postApexDisk)) {
      System.out.println("disks.size()=" + disks.size() + " frontEdgeDisks.size()="
          + frontEdgeDisks.size()
          + " and backEdgeDisks.size()=" + backEdgeDisks.size());
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
    try {
      TimeUnit.MILLISECONDS.sleep(10);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("disks.size()=" + disks.size() + " frontEdgeDisks.size()="
        + frontEdgeDisks.size()
        + " and backEdgeDisks.size()=" + backEdgeDisks.size());
    System.out.println("dp=" + preApexDisk.toString());
    System.out.println("dq=" + postApexDisk.toString());
    System.out.println("dx=" + apexDisk.toString());
    // System.out.println("ApexPoint=" + apexDiskFarthestPoint);
    // System.out.println(disks);
    System.out.flush();
    System.out.println();
    findHull(new ArrayList<>(frontEdgeDisks), preApexDisk, apexDisk, hullPointP, apexDiskFarthestPoint);
    findHull(new ArrayList<>(backEdgeDisks), apexDisk, postApexDisk, apexDiskFarthestPoint, hullPointQ);
  }

  public static void main(String[] args) {
    List<Disk> inputDisks = DiskIO.parse("resources/RANDOM/N10000.txt");
    QuickHullDisk qhd = new QuickHullDisk();
    qhd.run(inputDisks);
    System.out.println(qhd.hullDisks);
    System.out.println(qhd.hullDisks.size());
  }
}

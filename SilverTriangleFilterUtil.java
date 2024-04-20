import java.util.List;

public class SilverTriangleFilterUtil {

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
        // TODO
    }
}

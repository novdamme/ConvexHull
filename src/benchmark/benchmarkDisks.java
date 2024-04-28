package benchmark;

import dto.Disk;
import dto.DiskIO;
import quickHullDisk.QuickHullDisk;

import java.util.List;

public class benchmarkDisks {
    private static String[] files = {
            "resources/MIXED/N100000_10.txt"
    };

    public static void main(String[] args) {
        System.out.println("Testing algorithm: QuickHullDisk");
        QuickHullDisk qhd = new QuickHullDisk();
        for (String file : files) {
            System.out.println("Testing file: " + file);
            List<Disk> inputDisks = DiskIO.parse(file);
            long start = System.currentTimeMillis();
            qhd.run(inputDisks);
            System.out.println(qhd.hullDisks.size());
            long end = System.currentTimeMillis();
            System.out.println("Time: " + (end - start));
            System.out.println();
        }
    }
}

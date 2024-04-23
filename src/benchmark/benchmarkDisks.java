package benchmark;

import dto.Disk;
import dto.DiskIO;
import quickHullDisk.QuickHullDisk;

import java.util.List;

public class benchmarkDisks {
    public static void main(String[] args) {
        List<Disk> inputDisks = DiskIO.parse("resources/MIXED/N100000_10.txt");
        QuickHullDisk qhd = new QuickHullDisk();
        qhd.run(inputDisks);
    }
}

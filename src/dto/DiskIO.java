package dto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DiskIO {
    public static List<Disk> parse(String filename) {
        List<Disk> disks = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length == 4) {
                    double x = Double.parseDouble(parts[1]);
                    double y = Double.parseDouble(parts[2]);
                    double r = Double.parseDouble(parts[3]);
                    disks.add(new Disk(x, y, r));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return disks;
    }

    public static void main(String[] args) {
        List<Disk> disks = parse("resources/MIXED/N100000_10.txt");
        for (Disk disk : disks) {
            System.out.println(disk);
        }
    }
}

package Tucil3_13523025_13523030.src;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Selamat datang di permainan Rush Hour!");
        System.out.printf("Silakan masukkan nama file papan (e.g: papan1): ");

        String fileName = scanner.nextLine();
        String filePath = "../data/" + fileName + ".txt";
        System.out.println(filePath);

        Papan tes = Load.Load_Papan(filePath);
        if (tes == null) {
            System.out.println("Gagal memuat papan dari file. Pastikan file ada dan format benar.");
            return;
        }

        if (!tes.isPapanValid()){
            System.out.println("Papan tidak valid. Silakan periksa file input.");
            return;
        }
        Papan copy = new Papan(tes);

        System.out.println("Strategi pencarian yang tersedia:");
        System.out.println("1. Uniform Cost Search");
        System.out.println("2. Greedy Best First Search");
        System.out.println("3. A* Search");
        System.out.printf("Silakan pilih strategi pencarian (1/2/3): ");
        
        int pilihan = scanner.nextInt();
        System.out.println();

        if (pilihan < 1 || pilihan > 3) {
            System.out.println("Pilihan tidak valid. Silakan pilih 1, 2, atau 3.");
            return;
        }
        
        if (pilihan == 1) {
            long startTime = System.nanoTime();

            UCS ucs = new UCS(tes);
            List<Gerakan> steps = ucs.solve();
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000; // in milliseconds

            if (steps == null || steps.isEmpty()) {
                System.out.println("Tidak ada solusi ditemukan untuk input yang diberikan.");
                System.out.println("Waktu eksekusi: " + duration + " ms");
                return;
            }
            else{
                GUI gui = new GUI(copy,steps, "UCS");
                System.out.println("Waktu eksekusi: " + duration + " ms");
            }

        } else if (pilihan == 2) {
                        long startTime = System.nanoTime();

            GreedyBestFirstSearch greedy = new GreedyBestFirstSearch(tes);
            List<Gerakan> steps = greedy.solve();
             long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000; // in milliseconds
            if (steps == null || steps.isEmpty()) {
                System.out.println("Tidak ada solusi ditemukan untuk input yang diberikan.");
                System.out.println("Waktu eksekusi: " + duration + " ms");
                return;
            }
            else{
                GUI gui = new GUI(tes,steps, "GBFS");
                System.out.println("Waktu eksekusi: " + duration + " ms");

            }

        } else if (pilihan == 3) {
                        long startTime = System.nanoTime();

            AStarSearch astar = new AStarSearch(tes);
            List<Gerakan> steps = astar.solve();
             long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1000000; // in milliseconds
            if (steps == null || steps.isEmpty()) {
                System.out.println("Tidak ada solusi ditemukan untuk input yang diberikan.");
                System.out.println("Waktu eksekusi: " + duration + " ms");
                return;
            }
            else{
                GUI gui = new GUI(tes,steps, "AStar");
                System.out.println("Waktu eksekusi: " + duration + " ms");
            }
        }
    }
}
    

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
        System.out.println("Memuat papan dari " + filePath + "...");

        Papan tes = Load.Load_Papan("../data/2.txt");

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
            new UCS(tes);
        } else if (pilihan == 2) {
            new GreedyBestFirstSearch(tes);
        } else if (pilihan == 3) {
            new AStarSearch(tes);
        }
        
       
        
        
        

        
    }
}
    

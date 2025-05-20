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
            UCS ucs = new UCS(tes);
            List<Gerakan> steps = ucs.solve();
            GUI gui = new GUI(copy,steps);
        } else if (pilihan == 2) {
            GreedyBestFirstSearch greedy = new GreedyBestFirstSearch(tes);
            List<Gerakan> steps = greedy.solve();
            GUI gui = new GUI(tes,steps);
        } else if (pilihan == 3) {
            AStarSearch astar = new AStarSearch(tes);
            List<Gerakan> steps = astar.solve();
            GUI gui = new GUI(tes,steps);
        }
    }
}
    

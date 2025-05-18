package Tucil3_13523025_13523030.src;

public class Main {
    public static void main(String[] args){
        // Buat papan 6x6
        Papan papan = new Papan(6, 6);
        // Piece(int panjang, boolean horizontal, char nama, int baris, int kolom, boolean utama)
        // Tambahkan beberapa piece
        Piece primaryPiece = new Piece(2, true, 'X', 2, 1, true); // panjang 2, horizontal, mulai di (2,1)
        Piece p1 = new Piece(3, false, 'A', 0, 0, false);
        Piece p2 = new Piece(2, true, 'B', 0, 2, false);
        Piece p3 = new Piece(2, false, 'C', 3, 5, false);
        Piece p4 = new Piece(2, true, 'D', 4, 0, false);

        // Tambahkan ke papan
        papan.addPiece(primaryPiece);
        papan.addPiece(p1);
        papan.addPiece(p2);
        papan.addPiece(p3);
        papan.addPiece(p4);

        System.out.println("Board awal:");
        papan.displayBoard();

        // Tentukan koordinat pintu keluar
        int exitRow = 2;
        int exitCol = 5;

        // Jalankan algoritma greedy
        System.out.println("\nMenjalankan Greedy Best-First Search...");
        new Greedy(papan, primaryPiece, exitRow, exitCol);
    }
    
}

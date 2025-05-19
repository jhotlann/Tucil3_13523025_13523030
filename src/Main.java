package Tucil3_13523025_13523030.src;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Buat papan ukuran 6x6, exit di baris ke-2, kolom ke-5 (indeks mulai dari 0)
        Papan papan = new Papan(6, 6, 2, 6);

        // Tambahkan piece ke papan
        // Format: panjang, horizontal (true/false), nama, baris, kolom, utama (true/false)
        Piece A = new Piece(2, true, 'A', 0, 0, false);
        Piece B = new Piece(2, false, 'B', 0, 2, false);
        Piece C = new Piece(2, false, 'C', 1, 3, false);
        Piece D = new Piece(2, false, 'D', 1, 4, false);

        Piece P = new Piece(2, true, 'P', 2, 1, true);  // primary piece
        
        Piece F = new Piece(3, false, 'F', 0, 5, false);
        Piece G = new Piece(3, false, 'G', 2, 0, false);
        Piece H = new Piece(2, false, 'H', 3, 1, false);
        Piece I = new Piece(3, true, 'I', 3, 3, false);
        Piece J = new Piece(2, false, 'J', 4, 2, false);
        
        Piece L = new Piece(2, true, 'L', 5, 0, false);
        Piece M = new Piece(2, true, 'M', 5, 3, false);

        // Tambahkan piece ke papan
        papan.addPiece(A);
        papan.addPiece(B);
        papan.addPiece(C);
        papan.addPiece(D);
        papan.addPiece(P);
        papan.addPiece(F);
        papan.addPiece(G);
        papan.addPiece(H);
        papan.addPiece(I);
        papan.addPiece(J);
        
        papan.addPiece(L);
        // papan.displayBoard();
        // Jalankan solver Greedy
        GreedyBestFirstSearch solver = new GreedyBestFirstSearch(papan);
        // List<Gerakan> solusi = solver.solve();
        solver.solve();
        // for (Gerakan g : solusi) {
        //     g.printGerakan();
        //     papan.movePiece(g);
        //     papan.displayBoard();
        // }
        
    }
}
    

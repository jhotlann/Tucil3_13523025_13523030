package Tucil3_13523025_13523030.src;

public class Main {
    public static void main(String[] args){
        Piece p1 = new Piece(3, true, 'A', 0, 0);
        Piece p2 = new Piece(2, false, 'B', 0, 3);
        Papan board = new Papan(5, 5);
        
        board.addPiece(p1);
        board.addPiece(p2);

        board.displayBoard();
        System.out.println();
        Gerakan g1 = new Gerakan(p2, "bawah",  1);
        board.movePiece(g1);
        board.displayBoard();

        board.displayBoard();
        System.out.println();
        Gerakan g2 = new Gerakan(p1, "kanan",  2);
        board.movePiece(g2);
        board.displayBoard();
    }
}

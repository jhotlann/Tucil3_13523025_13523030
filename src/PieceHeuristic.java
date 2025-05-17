package Tucil3_13523025_13523030.src;

public class PieceHeuristic {
    private int euclideanDistance;
    private char nama;

    public PieceHeuristic(Piece piece){
        this.nama = piece.getNama();
        this.euclideanDistance = piece.getBaris() + piece.getKolom();
    }

    public int getEuclideanDistance(){
        return euclideanDistance;
    }

    public char getName(){
        return nama;
    }
}

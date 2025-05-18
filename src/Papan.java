package Tucil3_13523025_13523030.src;
import java.util.ArrayList;

public class Papan{
    private int baris;
    private int kolom;
    private ArrayList<Piece> pieces;
    private char board[][];
    private int exitRow;
    private int exitCol;


    public Papan(int n, int m){
        this.baris = n;
        this.kolom = m;
        this.pieces = new ArrayList<Piece>();
        this.board = new char[n][m];

        for (int i = 0; i < baris; i++){
            for (int j = 0; j < kolom; j++){
                board[i][j] = '.';
            }
        }

    }

    public Papan(Papan papan){
        this.baris = papan.baris;
        this.kolom = papan.kolom;
        this.pieces = new ArrayList<Piece>(papan.pieces);
        this.board = new char[baris][kolom];
        for (int i = 0; i < baris; i++){
            for (int j = 0; j < kolom; j++){
                board[i][j] = papan.board[i][j];
            }
        }
    }

    public boolean addPiece(Piece p){
        int barisPiece = p.getBaris();
        int kolomPiece = p.getKolom();
        if (barisPiece >= this.baris || barisPiece < 0|| kolomPiece >= this.kolom || kolomPiece < 0){
            System.out.println("Input melebihi board");
            return false;
        }
        else if (p.isHorizontal()){
            for (int i = 0; i < p.getPanjang(); i++){
                if (i >= this.kolom || board[barisPiece][i + kolomPiece] != '.'){
                    System.out.println("Gagal");
                    return false;
                }
            }

            for (int i = 0; i < p.getPanjang(); i++){
                this.board[barisPiece][i + kolomPiece] = p.getNama();
            }
            this.pieces.add(p);
            System.out.println("Piece berhasil ditambahkan");
            return true;
        }

        else if (!p.isHorizontal()){
            for (int i = 0; i < p.getPanjang(); i++){
                if (i >= this.baris || board[i + barisPiece][kolomPiece] != '.'){
                    System.out.println("Gagal");
                    return false;
                }
            }

            for (int i = 0; i < p.getPanjang(); i++){
                this.board[i + barisPiece][kolomPiece] = p.getNama();
            }
            this.pieces.add(p);
            System.out.println("Piece berhasil ditambahkan");
            return true;
        }
        return true;
    }

    public void remove_piece(Piece p){
        if (p.isHorizontal()){
            for (int i = p.getKolom(); i < p.getPanjang(); i++){
                board[p.getBaris()][i] = '.';
            }
        }

        if (!p.isHorizontal()){
            for (int i = p.getBaris(); i < p.getPanjang(); i++){
                board[i][p.getKolom()] = '.';
            }
        }

        // pieces.remove(p);
    }

    public void displayBoard(){
        for (int i = 0; i < this.baris; i++){
            for (int j = 0; j < this.kolom; j++){
                System.out.print(board[i][j]);
            }
            System.out.println("");
        }
    }

    public boolean movePiece(Gerakan g){
        Piece p = g.getPiece();
        remove_piece(p);

        int oldBaris = p.getBaris();
        int oldKolom = p.getKolom();

        if (g.arah == "kanan" || g.arah == "kiri"){
            int jarak = g.jumlahKotak;
            if (g.arah == "kiri"){
                jarak = (-1) * g.jumlahKotak;
            }
            p.set_position(oldBaris, oldKolom + jarak);
        }
        else{
            int jarak = g.jumlahKotak;

            if (g.arah == "atas"){
                jarak *= (-1) * g.jumlahKotak;
            }
            p.set_position(oldBaris + jarak, oldKolom);
        }
        
        boolean add = addPiece(p);

        if (add){
            System.out.println("Berhasil pindah");
            return true;
        }
        
        else{
            System.out.println("Gagal pindah");
            p.set_position(oldBaris, oldKolom);
            addPiece(p);
            return false;
        }
    }

    public char[][] getPapan(){
        return board;
    }

    public ArrayList<Piece> getPieces(){
        return pieces;
    }

    public void setExit(int row, int col) {
        this.exitRow = row;
        this.exitCol = col;
    }

    public int getExitRow() {
        return exitRow;
    }

    public int getExitCol() {
        return exitCol;
    }

    public int getBaris() {
        return baris;
    }

    public int getKolom() {
        return kolom;
    }

    public String hashString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                sb.append(board[i][j]);
            }
        }
        return sb.toString();
    }


}
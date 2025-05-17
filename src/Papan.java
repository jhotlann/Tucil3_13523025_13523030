package Tucil3_13523025_13523030.src;

import java.util.ArrayList;

public class Papan{
    private int baris;
    private int kolom;
    private ArrayList<Piece> pieces;
    private char board[][];

    public Papan(int n, int m){
        this.baris = n;
        this.kolom = m;
        // this.pieces = new ArrayList<Piece>();
        this.board = new char[n][m];

        for (int i = 0; i < baris; i++){
            for (int j = 0; j < kolom; j++){
                board[i][j] = '.';
            }
        }

    }

    public boolean addPiece(Piece p){
        int barisPiece = p.getBaris();
        int kolomPiece = p.getKolom();
        if (barisPiece >= this.baris || kolomPiece >= this.kolom){
            System.out.println("Input melebihi board");
            return false;
        }
        else if (p.isHorizontal()){
            for (int i = kolomPiece; i < p.getPanjang(); i++){
                if (i >= this.kolom || board[barisPiece][i] != '.'){
                    System.out.println("Gagal");
                    return false;
                }
            }

            for (int i = kolomPiece; i < p.getPanjang(); i++){
                this.board[barisPiece][i] = p.getNama();
            }
            System.out.println("Piece berhasil ditambahkan");
            return true;
        }

        else if (!p.isHorizontal()){
            for (int i = barisPiece; i < p.getPanjang(); i++){
                if (i >= this.baris || board[i][kolomPiece] != '.'){
                    System.out.println("Gagal");
                    return false;
                }
            }

            for (int i = barisPiece; i < p.getPanjang(); i++){
                this.board[i][kolomPiece] = p.getNama();
            }
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

    public boolean movePiece(Piece p, int newBaris, int newKolom){
        remove_piece(p);

        int oldBaris = p.getBaris();
        int oldKolom = p.getKolom();

        p.set_position(newBaris, newKolom);
        
        boolean add = addPiece(p);

        if (add){
            System.out.println("Berhasil pindah");
            return true;
        }
        
        else{
            System.out.println("Gagal pindah");
            p.set_position(oldBaris, oldKolom);
            return false;
        }
    }

    public ArrayList<Piece> getPiece(){
        return pieces;
    }
}
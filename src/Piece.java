package Tucil3_13523025_13523030.src;

public class Piece {
    private int panjang;
    private boolean horizontal;
    private char nama;
    private int baris, kolom;
    private boolean utama;

    public Piece(int panjang, boolean horizontal, char nama, int baris, int kolom, boolean utama){
        this.panjang = panjang;
        this.horizontal = horizontal;
        this.nama = nama;
        this.baris = baris;
        this.kolom = kolom;
        this.utama = utama;
    }

    public boolean isHorizontal(){
        return horizontal;
    }

    public int getPanjang(){
        return panjang;
    }

    public char getNama(){
        return nama;
    }

    public void set_position(int baris, int kolom){
        this.baris = baris;
        this.kolom = kolom;
    }

    public int getBaris(){
        return this.baris; 
    }

    public int getKolom(){
        return kolom;
    }
}

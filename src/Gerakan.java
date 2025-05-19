package Tucil3_13523025_13523030.src;

class Gerakan{
    private Piece piece;
    public String arah;
    public int jumlahKotak;


    /**
     * Move 1 kotak
     * 
    * @param {string} arah kanan, kiri, atas, bawah
    */
    public Gerakan(Piece p, String arah, int jumlahKotak){
        this.piece = p;
        this.arah = arah;
        this.jumlahKotak = jumlahKotak;
    }

    public String getArah(){
        return arah;
    }

    public Piece getPiece(){
        return piece;
    }

    public int getJumlahKotak(){
        return jumlahKotak;
    }

    public void printGerakan(){
        System.out.println("Piece " + piece.getNama() + " bergerak " + arah + " sejauh " + jumlahKotak);
    }
    
}
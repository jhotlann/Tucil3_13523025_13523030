package Tucil3_13523025_13523030.src;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

public class Papan{
    private int baris;
    private int kolom;
    private ArrayList<Piece> pieces;
    private char board[][];
    private int exitRow;
    private int exitCol;


    public Papan(int n, int m, int exitRow, int exitCol){
        this.baris = n;
        this.kolom = m;
        this.pieces = new ArrayList<Piece>();
        this.board = new char[n][m];
        this.exitRow = exitRow;
        this.exitCol = exitCol;

        for (int i = 0; i < baris; i++){
            for (int j = 0; j < kolom; j++){
                board[i][j] = '.';
            }
        }

    }

    public Papan(int n, int m, char[][] matrix, int exitRow, int exitCol){
        this.baris = n;
        this.kolom = m;
        this.pieces = new ArrayList<Piece>();
        this.board = matrix;
        this.exitRow = exitRow;
        this.exitCol = exitCol;

        HashSet<Character> piecesSet = new HashSet<>();
        for (int i = 0; i < n; i++){
            for (int j = 0; j < m; j++){
                char pieceChar = matrix[i][j];
                    if (pieceChar != '.'){
                        if (!piecesSet.contains(pieceChar)){
                            boolean utama = false;
                            if (pieceChar == 'P'){
                                utama = true;
                            }
                            piecesSet.add(pieceChar);
                            int tempBaris = i + 1;
                            int tempKol = j + 1;
                            int panjang = 1;
                            boolean hor = false;
                            while (tempKol < kolom && matrix[i][tempKol] == pieceChar){//horizontal
                                hor = true;
                                panjang++;
                                tempKol++;
                            }
                            while (tempBaris < baris && matrix[tempBaris][j] == pieceChar){//Vertikal
                                panjang++;
                                tempBaris++;    
                            }

                            Piece p = new Piece(panjang, hor, pieceChar, i, j, utama);
                            pieces.add(p);
                        }
                    }
                }
            }
        }
    

    public Papan(Papan other) {
        this.baris = other.baris;
        this.kolom = other.kolom;
        this.exitRow = other.exitRow;
        this.exitCol = other.exitCol;
        
        // Deep copy the board
        this.board = new char[baris][kolom];
        for (int i = 0; i < baris; i++) {
            for (int j = 0; j < kolom; j++) {
                this.board[i][j] = other.board[i][j];
            }
        }
        
        // Deep copy the pieces
        this.pieces = new ArrayList<>();
        for (Piece piece : other.pieces) {
            this.pieces.add(new Piece(piece));
        }
    }
    

    public boolean addPiece(Piece p) {
        if (this.pieces.contains(p)){
            System.out.println("Piece " + p.getNama() + " sudah ada di papan");
            return false;
        }
        
        int barisPiece = p.getBaris();
        int kolomPiece = p.getKolom();
        
        
        // Check if piece is out of bounds
        if (barisPiece < 0 || barisPiece >= this.baris || kolomPiece < 0 || kolomPiece >= this.kolom) {
            // System.out.println("Piece " + p.getNama() + " gagal ditambahkan");
            return false;
        }

        // Check if horizontal piece exceeds board bounds or overlaps with exit (if not the main piece)
        if (p.isHorizontal() && (kolomPiece + p.getPanjang() - 1 >= this.kolom )) {
            // System.out.println("Piece " + p.getNama() + " gagal ditambahkan");
            return false;
        }

        // Check if vertical piece exceeds board bounds or overlaps with exit (if not the main piece)
        if (!p.isHorizontal() && (barisPiece + p.getPanjang() - 1 >= this.baris)) {
            // System.out.println("Piece " + p.getNama() + " gagal ditambahkan");
            return false;
        }
        
        
        
        if (p.isHorizontal()) {          
            // For horizontal piece, check if all cells are empty
            
            for (int i = 0; i < p.getPanjang(); i++) {
                // if (p.isUtama() && barisPiece == this.getExitRow() && kolomPiece + i >= this.getExitCol()) {
                //     continue; // Skip the exit cell for the main piece
                // }

                // Check if position is empty or is the exit (only for main piece)
                if (board[barisPiece][kolomPiece + i] != '.') {
                    // System.out.println("Piece " + p.getNama() + " gagal ditambahkan");
                    return false;
                }
            }
            
            // Place the piece on the board
            for (int i = 0; i < p.getPanjang(); i++) {
                // Skip placing the piece on the exit cell if it's the main piece and at the exit
                // if (p.isUtama() && barisPiece == this.getExitRow() && kolomPiece + i >= this.getExitCol()) {
                //     continue;
                // }
                // Skip placing beyond the board if it's the main piece exiting
                // if (kolomPiece + i >= this.kolom) {
                //     continue;
                // }
                this.board[barisPiece][kolomPiece + i] = p.getNama();
            }
        } 
        else { // Vertical piece
            
            // For vertical piece, check if all cells are empty
            for (int i = 0; i < p.getPanjang(); i++) {

                // if (p.isUtama() && barisPiece + 1 >= this.getExitRow() && kolomPiece == this.getExitCol()) {
                //     continue; // Skip the exit cell for the main piece
                // }
                
                // Check if position is empty or is the exit (only for main piece)
                if (board[barisPiece + i][kolomPiece] != '.') {
                    // System.out.println("Piece " + p.getNama() + " gagal ditambahkan");
                    return false;
                }
            }
            
            // Place the piece on the board
            for (int i = 0; i < p.getPanjang(); i++) {
                // Skip placing the piece on the exit cell if it's the main piece and at the exit
                // if (p.isUtama() && kolomPiece == this.getExitCol() && barisPiece + i >= this.getExitRow()) {
                //     continue;
                // }
                // // Skip placing beyond the board if it's the main piece exiting
                // if (barisPiece + i >= this.baris) {
                //     continue;
                // }
                this.board[barisPiece + i][kolomPiece] = p.getNama();
            }
        }
        
        // Add the piece to the list
        this.pieces.add(p);
        // System.out.println("Piece " + p.getNama() + " berhasil ditambahkan");
        return true;
    }

    public boolean remove_piece(Piece p){
        if (!this.pieces.contains(p)){
            System.out.println("Piece " + p.getNama() + " tidak ada di papan");
            return false;
        }

        int barisPiece = p.getBaris();
        int kolomPiece = p.getKolom();
        if (p.isHorizontal()){
            for (int i = 0; i < p.getPanjang(); i++){
                this.board[barisPiece][i + kolomPiece] = '.';
            }
        }
        else{
            for (int i = 0; i < p.getPanjang(); i++){
                this.board[i + barisPiece][kolomPiece] = '.';
            }
        }

        pieces.remove(p);
        return true;
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
        boolean remove = remove_piece(p);
        if (!remove){
            // System.out.println("Piece " + p.getNama() + " gagal dipindahkan");
            return false;
        }

        int oldBaris = p.getBaris();
        int oldKolom = p.getKolom();

        if (g.getArah() == "kanan" || g.getArah() == "kiri"){
            int jarak = g.getJumlahKotak();
            if (g.getArah() == "kiri"){
                jarak = (-1) * g.getJumlahKotak();
            }
            p.set_position(oldBaris, oldKolom + jarak);

        }
        else{
            int jarak = g.getJumlahKotak();

            if (g.getArah() == "atas"){
                jarak *= (-1) * g.getJumlahKotak();
            }
            p.set_position(oldBaris + jarak, oldKolom);
        }
        
        boolean add = addPiece(p);

        if (!add){
            // System.out.println("Piece " + p.getNama() + " gagal dipindahkan");
            // Revert the piece to its original position
            p.set_position(oldBaris, oldKolom);
            addPiece(p);
            return false;
        }

        // System.out.println("Piece " + p.getNama() + " berhasil dipindahkan");
        return true;
    }

    public char[][] getPapan(){
        return board;
    }

    public boolean isSolved(){
        return true;
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

    public Piece getPieceByName(char symbol) {
        for (Piece piece : pieces) {
            if (piece.getNama() == symbol) {
                return piece;
            }
        }
        return null;
    }

    public Piece getPrimaryPiece() {
        for (Piece piece : pieces) {
            if (piece.isUtama()) {
                return piece;
            }
        }
        return null; // jika tidak ada piece utama
    }

    public boolean isPapanValid() {
        // Check if the primary piece is present and in a valid position
        Piece primaryPiece = getPrimaryPiece();
        if (primaryPiece == null) {
            return false;
        }

        if (primaryPiece.getBaris() != exitRow && primaryPiece.isHorizontal()) {
            return false; 
        }

        if (primaryPiece.getKolom() != exitCol && !primaryPiece.isHorizontal()) {
            return false; 
        }

        return true; 
    }

}
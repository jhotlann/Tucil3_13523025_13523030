package Tucil3_13523025_13523030.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

public class UCS {
    public UCS(Papan initBoard) {
        if (!initBoard.isPapanValid()) {
            System.out.println("Papan tidak valid. Silakan periksa input.");
            return;
        }

        System.out.println("Uniform Cost Search");
        System.out.println("Papan awal: ");
        initBoard.displayBoard();

        PriorityQueue<State> pq = new PriorityQueue<>();
        ArrayList<Gerakan> langkahAwal = new ArrayList<>();
        HashSet<String> visited = new HashSet<>();

        State currentState = new State(initBoard, langkahAwal, 0);
        pq.add(currentState);

        while (!pq.isEmpty()) {
            currentState = pq.poll();
            Papan papanSekarang = currentState.getPapan();

            String hashString = papanSekarang.hashString();
            if (visited.contains(hashString)) {
                continue;
            }
            visited.add(hashString);

            // Cek apakah primary piece sudah di exit
            Piece primary = papanSekarang.getPrimaryPiece();
            if (isAtExit(papanSekarang, primary)) {
                System.out.println("\n=== SOLUSI ===");
                // Display original board
                initBoard.displayBoard();
                System.out.println();
                int langkah = 1;

                Papan displayBoard = new Papan(initBoard);
                for (Gerakan g : currentState.getGerakan()) {
                    
                    System.out.println("\nGerakan "+ langkah + ": "+ g.getPiece().getNama() + "-" + g.getArah());
                    langkah++;

                    Piece displayPiece = displayBoard.getPieceByName(g.getPiece().getNama());
                    Gerakan displayGerakan = new Gerakan(displayPiece, g.getArah(), g.getJumlahKotak());
                    
                    boolean moveSuccess = displayBoard.movePiece(displayGerakan);
                    if (!moveSuccess) {
                        System.out.println("Warning: Failed to apply move in solution print.");
                    }
                    displayBoard.displayBoard();
                    System.out.println();
                } 
                return;
            }

            // Memeriksa semua gerakan yang mungkin
            for (Piece piece : papanSekarang.getPieces()) {
                char pieceName = piece.getNama();
                int row = piece.getBaris();
                int col = piece.getKolom();
                int length = piece.getPanjang();
                boolean isHorizontal = piece.isHorizontal();
                char[][] board = papanSekarang.getPapan();
                
                if (isHorizontal) {
                    int steps = 1;
                    if (col - steps >= 0 && board[row][col - steps] == '.') {
                        Papan newState = new Papan(papanSekarang);
                        Piece newPiece = newState.getPieceByName(pieceName);
                        
                        if (newPiece != null) {
                            Gerakan move = new Gerakan(newPiece, "kiri", steps);
                            boolean isValid = newState.movePiece(move);
                            
                            if (isValid) {
                                String stateHash = newState.hashString();
                                if (!visited.contains(stateHash)) {
                                    ArrayList<Gerakan> newMoves = new ArrayList<>(currentState.getGerakan());
                                    newMoves.add(move);
                                    pq.add(new State(newState, newMoves, currentState.getSkor() + steps));
                                }
                            }
                        }
                    }
                    
                    int rightEnd = col + length - 1;
                    steps = 1;
                    if (rightEnd + steps < papanSekarang.getKolom() && 
                        (board[row][rightEnd + steps] == '.' || 
                         (piece.isUtama() && row == papanSekarang.getExitRow() && rightEnd + steps == papanSekarang.getExitCol()))) {
                        
                        Papan newState = new Papan(papanSekarang);
                        Piece newPiece = newState.getPieceByName(pieceName);
                        
                        if (newPiece != null) {
                            Gerakan move = new Gerakan(newPiece, "kanan", steps);
                            boolean isValid = newState.movePiece(move);
                            
                            if (isValid) {
                                String stateHash = newState.hashString();
                                if (!visited.contains(stateHash)) {
                                    ArrayList<Gerakan> newMoves = new ArrayList<>(currentState.getGerakan());
                                    newMoves.add(move);
                                    pq.add(new State(newState, newMoves, currentState.getSkor() + steps));
                                }
                            }
                        }
                    }
                } else {
                  
                    int steps = 1;
                    if (row - steps >= 0 && board[row - steps][col] == '.') {
                        Papan newState = new Papan(papanSekarang);
                        Piece newPiece = newState.getPieceByName(pieceName);
                        
                        if (newPiece != null) {
                            Gerakan move = new Gerakan(newPiece, "atas", steps);
                            boolean isValid = newState.movePiece(move);
                            
                            if (isValid) {
                                String stateHash = newState.hashString();
                                if (!visited.contains(stateHash)) {
                                    ArrayList<Gerakan> newMoves = new ArrayList<>(currentState.getGerakan());
                                    newMoves.add(move);
                                    pq.add(new State(newState, newMoves, currentState.getSkor() + steps));
                                }
                            }
                        }
                    }
          
                    int bottomEnd = row + length - 1;
                    steps = 1;
                    if (bottomEnd + steps < papanSekarang.getBaris() && 
                        (board[bottomEnd + steps][col] == '.' || 
                         (piece.isUtama() && bottomEnd + steps == papanSekarang.getExitRow() && col == papanSekarang.getExitCol()))) {
                        
                        Papan newState = new Papan(papanSekarang);
                        Piece newPiece = newState.getPieceByName(pieceName);
                        
                        if (newPiece != null) {
                            Gerakan move = new Gerakan(newPiece, "bawah", steps);
                            boolean isValid = newState.movePiece(move);
                            
                            if (isValid) {
                                String stateHash = newState.hashString();
                                if (!visited.contains(stateHash)) {
                                    ArrayList<Gerakan> newMoves = new ArrayList<>(currentState.getGerakan());
                                    newMoves.add(move);
                                    pq.add(new State(newState, newMoves, currentState.getSkor() + steps));
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Solusi tidak ditemukan!");
    }

    private boolean isAtExit(Papan papan, Piece primary) {
        int exitRow = papan.getExitRow();
        int exitCol = papan.getExitCol();

        if (primary.isHorizontal()) {
            int pieceRow = primary.getBaris();
            int pieceRightCol = primary.getKolom() + primary.getPanjang() - 1;
            
            if (exitCol >= papan.getKolom()) {
                return pieceRow == exitRow && pieceRightCol == papan.getKolom() - 1;
            } else {
                return pieceRow == exitRow && pieceRightCol >= exitCol;
            }
        } else {
            int pieceCol = primary.getKolom();
            int pieceBottomRow = primary.getBaris() + primary.getPanjang() - 1;
            
            if (exitRow >= papan.getBaris()) {
                return pieceCol == exitCol && pieceBottomRow == papan.getBaris() - 1;
            } else {
                return pieceBottomRow >= exitRow && pieceCol == exitCol;
            }
        }
    }
}
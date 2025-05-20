package Tucil3_13523025_13523030.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

public class UCS {
    public UCS(Papan initBoard) {
        System.out.println("Starting UCS class");
        PriorityQueue<State> pq = new PriorityQueue<>();
        ArrayList<Gerakan> langkahAwal = new ArrayList<>();
        HashSet<String> visited = new HashSet<>();

        State currentState = new State(initBoard, langkahAwal, 0);
        pq.add(currentState);

        while (!pq.isEmpty()) {
            currentState = pq.poll();
            Papan papanSekarang = currentState.getPapan();

            // Skip if already visited
            String hashString = papanSekarang.hashString();
            if (visited.contains(hashString)) {
                continue;
            }
            visited.add(hashString);

            // Cek apakah primary piece sudah di exit
            Piece primary = papanSekarang.getPrimaryPiece();
            if (isAtExit(papanSekarang, primary)) {
                System.out.println("JAWABAN:");
                // Display original board
                initBoard.displayBoard();
                System.out.println();
                
                // Display solution
                Papan displayBoard = new Papan(initBoard);
                for (Gerakan g : currentState.getGerakan()) {
                    System.out.println("Piece " + g.getPiece().getNama() + " bergerak ke " + g.getArah() + " sebanyak " + g.getJumlahKotak() + " langkah");
                    
                    // Find the corresponding piece in displayBoard
                    Piece displayPiece = displayBoard.getPieceByName(g.getPiece().getNama());
                    Gerakan displayGerakan = new Gerakan(displayPiece, g.getArah(), g.getJumlahKotak());
                    
                    boolean moveSuccess = displayBoard.movePiece(displayGerakan);
                    if (!moveSuccess) {
                        System.out.println("Warning: Failed to apply move in solution print.");
                    }
                    displayBoard.displayBoard();
                    System.out.println();
                }
                System.out.printf("Primary Piece keluar di Baris: %d, Kolom: %d\n", primary.getBaris(), primary.getKolom());
                System.out.println("Jumlah langkah: " + currentState.getGerakan().size());
                return;
            }

            // Generate all possible moves for each piece
            for (Piece piece : papanSekarang.getPieces()) {
                char pieceName = piece.getNama();
                int row = piece.getBaris();
                int col = piece.getKolom();
                int length = piece.getPanjang();
                boolean isHorizontal = piece.isHorizontal();
                char[][] board = papanSekarang.getPapan();
                
                if (isHorizontal) {
                    // Try moving left one step
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
                    
                    // Try moving right one step
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
                    // Try moving up one step
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
                    
                    // Try moving down one step
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
        System.out.println("No solution found!");
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
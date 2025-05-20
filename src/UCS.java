package Tucil3_13523025_13523030.src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

public class UCS {
    private Papan initBoard;
    public UCS(Papan initBoard) {
        this.initBoard = initBoard;
    }

    public List<Gerakan> solve(){
        System.out.println("Starting UCS class ");
        PriorityQueue<State> pq = new PriorityQueue<State>();
        ArrayList<Gerakan> step = new ArrayList<>();
        HashSet<String> visited = new HashSet<>();

        Papan papanCopy = new Papan(initBoard);

        State current_state = new State(papanCopy, step, 0);
        pq.add(current_state);

        while (!pq.isEmpty()) {
            current_state = pq.poll();

            papanCopy = new Papan(current_state.getPapan());
            // papanCopy.displayBoard();
            // System.out.println();
            if (visited.contains(papanCopy.hashString())){
                continue;
            }
            else{
                visited.add(papanCopy.hashString());
            }

            
            if (isAtExit2(papanCopy, papanCopy.getPrimaryPiece(), papanCopy.getExitRow(), papanCopy.getExitCol())){
                System.out.println("JAWABAN:");
                initBoard.displayBoard();
                System.out.println("");

                for (Gerakan g : current_state.getGerakan()){
                    initBoard.movePiece(g);
                    initBoard.displayBoard();
                    System.out.println("");
                }
                System.out.println("Exit from loop");
                System.out.printf("Baris: %d  ", papanCopy.getPrimaryPiece().getBaris());
                System.out.printf("Kolom: %d\n", papanCopy.getPrimaryPiece().getKolom());
                return current_state.getGerakan();
            } 

            ArrayList<Piece> current_pieces = new ArrayList<>();
            for (Piece temp : papanCopy.getPieces()){
                current_pieces.add(new Piece(temp));
            }
            // current_state.getPapan().displayBoard();
            for (Piece p : current_pieces){
                if (p.isHorizontal()){
                    for (int steps = 1; steps <= papanCopy.getKolom(); steps++) {
                        Gerakan move = new Gerakan(new Piece(p), "kanan", steps);
                        Papan tempBoard = new Papan(current_state.getPapan());

                        if (tempBoard.movePiece(move)) {

                            ArrayList<Gerakan> newSteps = new ArrayList<>(current_state.getGerakan());
                            newSteps.add(move);

                            State newState = new State(tempBoard, newSteps, current_state.getSkor() + 1);
                            if (!visited.contains(tempBoard.hashString())) {
                                pq.add(newState);
                            }
                        } else {
                            break;
                        }
                    }

                    for (int steps = 1; steps <= papanCopy.getKolom(); steps++) {
                        Gerakan move = new Gerakan(new Piece(p), "kiri", steps);
                        Papan tempBoard = new Papan(current_state.getPapan());

                        if (tempBoard.movePiece(move)) {
                            ArrayList<Gerakan> newSteps = new ArrayList<>(current_state.getGerakan());
                            newSteps.add(move);

                            State newState = new State(tempBoard, newSteps, current_state.getSkor() + 1);
                            if (!visited.contains(tempBoard.hashString())) {
                                pq.add(newState);
                            }
                        } else {
                            break;
                        }
                    }

                  
                }else{
                    for (int steps = 1; steps <= papanCopy.getBaris(); steps++) {
                        Gerakan move = new Gerakan(new Piece(p), "atas", steps);
                        Papan tempBoard = new Papan(current_state.getPapan());

                        if (tempBoard.movePiece(move)) {
                            ArrayList<Gerakan> newSteps = new ArrayList<>(current_state.getGerakan());
                            newSteps.add(move);

                            State newState = new State(tempBoard, newSteps, current_state.getSkor() + 1);
                            if (!visited.contains(tempBoard.hashString())) {
                                pq.add(newState);
                            }
                        } else {
                            break;
                        }
                    }

                    for (int steps = 1; steps <= papanCopy.getBaris(); steps++) {
                        Gerakan move = new Gerakan(new Piece(p), "bawah", steps);
                        Papan tempBoard = new Papan(current_state.getPapan());

                        if (tempBoard.movePiece(move)) {
                            ArrayList<Gerakan> newSteps = new ArrayList<>(current_state.getGerakan());
                            newSteps.add(move);

                            State newState = new State(tempBoard, newSteps, current_state.getSkor() + 1);
                            if (!visited.contains(tempBoard.hashString())) {
                                pq.add(newState);
                            }
                        } else {
                            break;
                        }
                    }

                }
            }
            
        }

        System.out.println("no solution");
        return null;
    }

    private boolean isAtExit2(Papan papan, Piece primary, int exitRow, int exitCol) {
        int startRow = primary.getBaris();
        int startCol = primary.getKolom();
        int length = primary.getPanjang();

        if (primary.isHorizontal()) {
            // Check if primary piece covers exit column on the same row
            return (startRow == exitRow &&
                    startCol <= exitCol &&
                    startCol + length - 1 >= exitCol);
        } else {
            // Check if primary piece covers exit row on the same column
            return (startCol == exitCol &&
                    startRow <= exitRow &&
                    startRow + length - 1 >= exitRow);
        }
    }
}
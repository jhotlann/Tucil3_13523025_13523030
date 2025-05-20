package Tucil3_13523025_13523030.src;

import java.util.*;

public class GreedyBestFirstSearch {
    private Papan initialState;
    private HashMap<String, Boolean> visited;
    private PriorityQueue<Node> priorityQueue;
    private int expandedNodes;
    private Heuristic heuristic;
    private int maxIterations = 100000; 
    
    public GreedyBestFirstSearch(Papan initialState) {
        this.initialState = initialState;
        this.visited = new HashMap<>();
        this.expandedNodes = 0;
        this.heuristic = new BlockingDistanceHeuristic();
        this.priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Node::getHeuristic));

        System.out.println("Greedy Best First Search");
        System.out.println("Papan awal: ");
        initialState.displayBoard();

        if (!initialState.isPapanValid()) {
            System.out.println("Papan tidak valid. Silakan periksa input.");
            return;
        }
        solve();
    }
    
    public void solve() {
        Node startNode = new Node(initialState, null, null, heuristic.calculate(initialState));
        priorityQueue.add(startNode);
    
        visited.put(initialState.hashString(), true);

        while (!priorityQueue.isEmpty() && expandedNodes < maxIterations) {
            Node currentNode = priorityQueue.poll();
            expandedNodes++;

            if (isSolved(currentNode.getState())) {
                List<Gerakan> solution = reconstructPath(currentNode);
                printSolution(solution, initialState); 
                return;
            }
  
            List<Node> successors = generateSuccessors(currentNode);
            
            for (Node successor : successors) {
                String stateHash = successor.getState().hashString();
                
                if (!visited.containsKey(stateHash)) {
                    visited.put(stateHash, true);
                    priorityQueue.add(successor);
                }
            }
        }
        
        if (expandedNodes >= maxIterations) {
            System.out.println("Reached maximum iterations (" + maxIterations + "). Search terminated.");
        } else {
            System.out.println("Solusi tidak ditemukan.");
        }
    }
    
    private List<Node> generateSuccessors(Node node) {
        List<Node> successors = new ArrayList<>();
        Papan currentState = node.getState();
        int boardSize = currentState.getBaris();
     
        for (Piece piece : currentState.getPieces()) {
            char pieceName = piece.getNama();
            int row = piece.getBaris();
            int col = piece.getKolom();
            int length = piece.getPanjang();
            boolean isHorizontal = piece.isHorizontal();
            char[][] board = currentState.getPapan();
            
            if (isHorizontal) {

                int steps = 1;
                if (col - steps >= 0 && board[row][col - steps] == '.') {
                    Papan newState = new Papan(currentState);
                    
                    Piece newPiece = newState.getPieceByName(pieceName);
                    if (newPiece == null) {
                        System.out.println("Error: Piece " + pieceName + " tidak ditemukan di state.");
                        continue;
                    }
                    
                    Gerakan move = new Gerakan(newPiece, "kiri", steps);
                    boolean isValid = newState.movePiece(move);
                    
                    if (isValid) {
                        String stateHash = newState.hashString();
                        if (!visited.containsKey(stateHash)) {
                            int heuristicValue = heuristic.calculate(newState);
                            Node successor = new Node(newState, node, move, heuristicValue);
                            successors.add(successor);
                        }
                    }
                }

                int rightEnd = col + length - 1;
                steps = 1;
                if (rightEnd + steps < currentState.getKolom() && 
                    (board[row][rightEnd + steps] == '.' || 
                     (piece.isUtama() && row == currentState.getExitRow() && rightEnd + steps == currentState.getExitCol()))) {
                    
                    Papan newState = new Papan(currentState);
                    
                    Piece newPiece = newState.getPieceByName(pieceName);
                    if (newPiece == null) {
                        System.out.println("Error: Piece " + pieceName + " tidak ditemukan di state.");
                        continue;
                    }
                    
                    Gerakan move = new Gerakan(newPiece, "kanan", steps);
                    boolean isValid = newState.movePiece(move);

                    if (isValid) {
                        String stateHash = newState.hashString();
                        if (!visited.containsKey(stateHash)) {
                            int heuristicValue = heuristic.calculate(newState);
                            Node successor = new Node(newState, node, move, heuristicValue);
                            successors.add(successor);
                        }
                    }
                }
            } else {
                
                int steps = 1;
                if (row - steps >= 0 && board[row - steps][col] == '.') {
                    Papan newState = new Papan(currentState);
                    
                    Piece newPiece = newState.getPieceByName(pieceName);
                    if (newPiece == null) {
                        System.out.println("Error: Piece " + pieceName + " tidak ditemukan di state.");
                        continue;
                    }
                    
                    Gerakan move = new Gerakan(newPiece, "atas", steps);
                    boolean isValid = newState.movePiece(move);
                    
                    if (isValid) {
                        String stateHash = newState.hashString();
                        if (!visited.containsKey(stateHash)) {
                            int heuristicValue = heuristic.calculate(newState);
                            Node successor = new Node(newState, node, move, heuristicValue);
                            successors.add(successor);
                        }
                    }
                }

                int bottomEnd = row + length - 1;
                steps = 1;
                if (bottomEnd + steps < currentState.getBaris() && 
                    (board[bottomEnd + steps][col] == '.' || 
                     (piece.isUtama() && bottomEnd + steps == currentState.getExitRow() && col == currentState.getExitCol()))) {
                    
                    Papan newState = new Papan(currentState);
                    
                    Piece newPiece = newState.getPieceByName(pieceName);
                    if (newPiece == null) {
                        System.out.println("Error: Piece " + pieceName + " tidak ditemukan di state.");
                        continue;
                    }
                    
                    Gerakan move = new Gerakan(newPiece, "bawah", steps);
                    boolean isValid = newState.movePiece(move);
                    
                    if (isValid) {
                        String stateHash = newState.hashString();
                        if (!visited.containsKey(stateHash)) {
                            int heuristicValue = heuristic.calculate(newState);
                            Node successor = new Node(newState, node, move, heuristicValue);
                            successors.add(successor);
                        }
                    }
                }
            }
        }
        
        return successors;
    }
    
    private boolean isSolved(Papan state) {
        Piece primaryPiece = state.getPrimaryPiece();
        if (primaryPiece == null) {
            return false;
        }
        
        int exitRow = state.getExitRow();
        int exitCol = state.getExitCol();
        
        if (primaryPiece.isHorizontal()) {
            int pieceRow = primaryPiece.getBaris();
            int pieceCol = primaryPiece.getKolom();

            if (pieceCol == exitCol && pieceRow == exitRow) {
                return true; 
            }

            if (pieceCol < exitCol) {
                int pieceRightCol = pieceCol + primaryPiece.getPanjang() - 1;
                
                if (pieceRightCol >= exitCol) {
                    return pieceRow == exitRow;
                } 
            } else {
                int pieceLeftCol = pieceCol;
                
                if (pieceLeftCol <= exitCol) {
                    return pieceRow == exitRow;
                }
            }

            
        } 
      
        else {
            int pieceCol = primaryPiece.getKolom();
            int pieceRow = primaryPiece.getBaris();

            if (pieceCol == exitCol && pieceRow == exitRow) {
                return true; 
            }

            if (pieceRow < exitRow) {
                int pieceTopRow = pieceRow;
                
                if (pieceTopRow >= exitRow) {
                    return pieceCol == exitCol;
                } 
            } else {
                int pieceBottomRow = pieceRow + primaryPiece.getPanjang() - 1;
                
                if (pieceBottomRow <= exitRow) {
                    return pieceCol == exitCol;
                }
            }
        }
        return false;
    }
    
    private List<Gerakan> reconstructPath(Node node) {
        List<Gerakan> path = new ArrayList<>();
        
        while (node.getParent() != null) {
            path.add(0, node.getAction());
            node = node.getParent();
        }
        
        return path;
    }
    
    private void printSolution(List<Gerakan> solution, Papan initialState) {
        if (solution.isEmpty()) {
            System.out.println("Puzzle sudah diselesaikan!");
            return;
        }
        System.out.println("\n=== SOLUSI ===");
        
        Papan currentState = new Papan(initialState); // Create a deep copy to manipulate
        
        for (int i = 0; i < solution.size(); i++) {
            Gerakan originalMove = solution.get(i);
            Piece originalPiece = originalMove.getPiece();
            String direction = originalMove.getArah();
            int steps = originalMove.getJumlahKotak();
            
            Piece currentPiece = currentState.getPieceByName(originalPiece.getNama());
            if (currentPiece == null) {
                System.out.println("Error: Piece " + originalPiece.getNama() + " not found in current state.");
                continue;
            }

            Gerakan currentMove = new Gerakan(currentPiece, direction, steps);
            
            System.out.println("Gerakan " + (i+1) + ": " + currentPiece.getNama() + "-" + direction);
            
            boolean moveSuccess = currentState.movePiece(currentMove);
            if (!moveSuccess) {
                System.out.println("Warning: Failed to apply move " + (i+1) + " in solution print.");
            }
            
            currentState.displayBoard();
            System.out.println();
        }
    }
}
package Tucil3_13523025_13523030.src;

import java.util.*;

public class AStarSearch {
    private Papan initialState;
    private HashMap<String, Boolean> visited;
    private HashMap<String, Integer> gScore; // Track path cost to reach each state
    private PriorityQueue<Node> openSet;
    private int expandedNodes;
    private Heuristic heuristic;
    private int maxIterations = 100000; 
    
    public AStarSearch(Papan initialState) {
        this.initialState = initialState;
        this.visited = new HashMap<>();
        this.gScore = new HashMap<>();
        this.expandedNodes = 0;
        this.heuristic = new BlockingDistanceHeuristic();
        
        this.openSet = new PriorityQueue<>(Comparator.comparingInt(node -> 
            node.getPathCost() + node.getHeuristic()));
        
        System.out.println("Inisialisasi A* Search");
        System.out.println("Papan awal: ");
        initialState.displayBoard();
        solve();
    }
    
    public void solve() {
        int initialHeuristic = heuristic.calculate(initialState);
        
        Node startNode = new Node(initialState, null, null, initialHeuristic);
        startNode.setPathCost(0); // g-score for start node is 0
        
        String startStateHash = initialState.hashString();
        gScore.put(startStateHash, 0);
        
        openSet.add(startNode);
        // System.out.println("Heuristik awal (h): " + startNode.getHeuristic());
        // System.out.println("Path cost awal (g): " + startNode.getPathCost());
        // System.out.println("Total cost awal (f = g + h): " + 
        //                   (startNode.getPathCost() + startNode.getHeuristic()));
        
        // long startTime = System.currentTimeMillis();
        
        while (!openSet.isEmpty() && expandedNodes < maxIterations) {
            Node currentNode = openSet.poll();
            String currentStateHash = currentNode.getState().hashString();
            
            expandedNodes++;
            
            // Penting: jangan lewati node yang sudah dikunjungi jika kita 
            // menemukan jalur yang lebih baik ke node tersebut
            if (visited.containsKey(currentStateHash) && 
                gScore.get(currentStateHash) <= currentNode.getPathCost()) {
                continue;
            }
            
            visited.put(currentStateHash, true);
            
            // Debug setiap 1000 node di-expand
            // if (expandedNodes % 1000 == 0) {
            //     System.out.println("Nodes expanded: " + expandedNodes + 
            //                       ", Queue size: " + openSet.size() + 
            //                       ", Visited states: " + visited.size());
            // }
            
            if (isSolved(currentNode.getState())) {
                // long endTime = System.currentTimeMillis();
                // System.out.println("Solved in " + (endTime - startTime) + " ms");
                // System.out.println("Nodes expanded: " + expandedNodes);
                
                List<Gerakan> solution = reconstructPath(currentNode);
                printSolution(solution, initialState);
                return;
            }
            
            List<Node> successors = generateSuccessors(currentNode);
            
            for (Node successor : successors) {
                String stateHash = successor.getState().hashString();
                int tentativeGScore = currentNode.getPathCost() + 1; // Each move costs 1
                
                // Jika state ini sudah dikunjungi dengan jalur yang lebih baik, skip
                if (gScore.containsKey(stateHash) && tentativeGScore >= gScore.get(stateHash)) {
                    continue;
                }
                
                gScore.put(stateHash, tentativeGScore);
                successor.setPathCost(tentativeGScore);
                
                // Penting: tambahkan ke openSet meskipun sudah dikunjungi,
                // karena kita mungkin menemukan jalur lebih baik
                openSet.add(successor);
            }
        }
        
        if (expandedNodes >= maxIterations) {
            System.out.println("Reached maximum iterations (" + maxIterations + "). Search terminated.");
        } else {
            System.out.println("No solution found after expanding " + expandedNodes + " nodes");
        }
    }
    
    private List<Node> generateSuccessors(Node node) {
        List<Node> successors = new ArrayList<>();
        Papan currentState = node.getState();
        
        for (Piece piece : currentState.getPieces()) {
            char pieceName = piece.getNama();
            int row = piece.getBaris();
            int col = piece.getKolom();
            int length = piece.getPanjang();
            boolean isHorizontal = piece.isHorizontal();
            char[][] board = currentState.getPapan();
            
            if (isHorizontal) {
                // Bergerak 1 langkah ke kiri
                int steps = 1;
                if (col - steps >= 0 && board[row][col - steps] == '.') {
                    Papan newState = new Papan(currentState);
                    Piece newPiece = newState.getPieceByName(pieceName);
                    
                    if (newPiece == null) {
                        System.out.println("Error: Piece " + pieceName + " not found in new state.");
                        continue;
                    }
                    
                    Gerakan move = new Gerakan(newPiece, "kiri", steps);
                    boolean isValid = newState.movePiece(move);
                    
                    if (isValid) {
                        String stateHash = newState.hashString();
                        int heuristicValue = heuristic.calculate(newState);
                        Node successor = new Node(newState, node, move, heuristicValue);
                        successors.add(successor);
                    }
                }
                
                // Bergerak 1 langkah ke kanan
                int rightEnd = col + length - 1;
                steps = 1;
                if (rightEnd + steps < currentState.getKolom() && 
                    (board[row][rightEnd + steps] == '.' || 
                     (piece.isUtama() && row == currentState.getExitRow() && rightEnd + steps == currentState.getExitCol()))) {
                    
                    Papan newState = new Papan(currentState);
                    Piece newPiece = newState.getPieceByName(pieceName);
                    
                    if (newPiece == null) {
                        System.out.println("Error: Piece " + pieceName + " not found in new state.");
                        continue;
                    }
                    
                    Gerakan move = new Gerakan(newPiece, "kanan", steps);
                    boolean isValid = newState.movePiece(move);

                    if (isValid) {
                        String stateHash = newState.hashString();
                        int heuristicValue = heuristic.calculate(newState);
                        Node successor = new Node(newState, node, move, heuristicValue);
                        successors.add(successor);
                    }
                }
            } else {
                // Bergerak 1 langkah ke atas
                int steps = 1;
                if (row - steps >= 0 && board[row - steps][col] == '.') {
                    Papan newState = new Papan(currentState);
                    Piece newPiece = newState.getPieceByName(pieceName);
                    
                    if (newPiece == null) {
                        System.out.println("Error: Piece " + pieceName + " not found in new state.");
                        continue;
                    }
                    
                    Gerakan move = new Gerakan(newPiece, "atas", steps);
                    boolean isValid = newState.movePiece(move);
                    
                    if (isValid) {
                        String stateHash = newState.hashString();
                        int heuristicValue = heuristic.calculate(newState);
                        Node successor = new Node(newState, node, move, heuristicValue);
                        successors.add(successor);
                    }
                }
                
                // Bergerak 1 langkah ke bawah
                int bottomEnd = row + length - 1;
                steps = 1;
                if (bottomEnd + steps < currentState.getBaris() && 
                    (board[bottomEnd + steps][col] == '.' || 
                     (piece.isUtama() && bottomEnd + steps == currentState.getExitRow() && col == currentState.getExitCol()))) {
                    
                    Papan newState = new Papan(currentState);
                    Piece newPiece = newState.getPieceByName(pieceName);
                    
                    if (newPiece == null) {
                        System.out.println("Error: Piece " + pieceName + " not found in new state.");
                        continue;
                    }
                    
                    Gerakan move = new Gerakan(newPiece, "bawah", steps);
                    boolean isValid = newState.movePiece(move);
                    
                    if (isValid) {
                        String stateHash = newState.hashString();
                        int heuristicValue = heuristic.calculate(newState);
                        Node successor = new Node(newState, node, move, heuristicValue);
                        successors.add(successor);
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
            int pieceRightCol = primaryPiece.getKolom() + primaryPiece.getPanjang() - 1;
            
            if (exitCol >= state.getKolom()) {
                return pieceRow == exitRow && pieceRightCol == state.getKolom() - 1;
            } else {
                return pieceRow == exitRow && pieceRightCol >= exitCol;
            }
        } 
      
        else {
            int pieceCol = primaryPiece.getKolom();
            int pieceBottomRow = primaryPiece.getBaris() + primaryPiece.getPanjang() - 1;
            
            if (exitRow >= state.getBaris()) {
                return pieceCol == exitCol && pieceBottomRow == state.getBaris() - 1;
            } else {
                return pieceBottomRow >= exitRow && pieceCol == exitCol;
            }
        }
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
            System.out.println("No moves needed - puzzle is already solved!");
            return;
        }
        
        System.out.println("\n=== SOLUTION ===");
        System.out.println("Solution found! Number of moves: " + solution.size());
        Papan currentState = new Papan(initialState); 
        
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
            
            System.out.println("\nGerakan " + (i+1) + ": " + currentPiece.getNama() + 
                              "-" + direction + " by " + steps + " spaces");
            
            boolean moveSuccess = currentState.movePiece(currentMove);
            if (!moveSuccess) {
                System.out.println("Warning: Failed to apply move " + (i+1) + " in solution print.");
            }
            
            currentState.displayBoard();
            
            // int heuristicValue = heuristic.calculate(currentState);
            // System.out.println("Heuristic value after move: " + heuristicValue);
        }
        // System.out.println("\nPuzzle solved!");
    }
}
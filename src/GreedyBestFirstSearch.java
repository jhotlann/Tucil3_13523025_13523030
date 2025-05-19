package Tucil3_13523025_13523030.src;

import java.util.*;

public class GreedyBestFirstSearch {
    private Papan initialState;
    private HashMap<String, Boolean> visited;
    private PriorityQueue<Node> priorityQueue;
    private int expandedNodes;
    private BoardHeuristic heuristic;
    private int maxIterations = 100000; // Safety limit to prevent infinite loops
    
    public GreedyBestFirstSearch(Papan initialState) {
        this.initialState = initialState;
        this.visited = new HashMap<>();
        this.expandedNodes = 0;
        this.heuristic = new BlockingDistanceHeuristic();
        
        // Create priority queue that sorts nodes based on their heuristic value
        this.priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Node::getHeuristic));
        System.out.println("Inisialisasi Greedy Best First Search");
        System.out.println("Papan awal: ");
        initialState.displayBoard();
    }
    
    public void solve() {
        Node startNode = new Node(initialState, null, null, heuristic.calculate(initialState));
        priorityQueue.add(startNode);
        System.out.println("Heuristik: " + startNode.getHeuristic());
        visited.put(initialState.hashString(), true);
        
        long startTime = System.currentTimeMillis();
        
        while (!priorityQueue.isEmpty() && expandedNodes < maxIterations) {
            Node currentNode = priorityQueue.poll();
            expandedNodes++;
            
            // Debugging output
            if (expandedNodes % 1000 == 0) {
                System.out.println("Nodes expanded: " + expandedNodes + 
                                  ", Queue size: " + priorityQueue.size() + 
                                  ", Visited states: " + visited.size());
            }
            
            // Check if the puzzle is solved
            if (isSolved(currentNode.getState())) {
                long endTime = System.currentTimeMillis();
                System.out.println("Solved in " + (endTime - startTime) + " ms");
                System.out.println("Nodes expanded: " + expandedNodes);
                List<Gerakan> solution = reconstructPath(currentNode);
                printSolution(solution, initialState); // Use the original initial state
                return;
            }
            
            // Generate all possible moves from the current state
            List<Node> successors = generateSuccessors(currentNode);
            
            for (Node successor : successors) {
                String stateHash = successor.getState().hashString();
                
                if (!visited.containsKey(stateHash)) {
                    visited.put(stateHash, true);
                    priorityQueue.add(successor);
                }
            }
        }
        
        // If no solution is found or max iterations reached
        if (expandedNodes >= maxIterations) {
            System.out.println("Reached maximum iterations (" + maxIterations + "). Search terminated.");
        } else {
            System.out.println("No solution found after expanding " + expandedNodes + " nodes");
        }
    }
    
    private List<Node> generateSuccessors(Node node) {
        List<Node> successors = new ArrayList<>();
        Papan currentState = node.getState();
        int boardSize = currentState.getBaris(); // Get board size using getter method
        
        // For each piece on the board
        for (Piece piece : currentState.getPieces()) {
            char pieceName = piece.getNama();
            int row = piece.getBaris();
            int col = piece.getKolom();
            int length = piece.getPanjang();
            boolean isHorizontal = piece.isHorizontal();
            char[][] board = currentState.getPapan();
            
            // Only try valid directions based on piece orientation
            if (isHorizontal) {
                // Horizontal piece can only move left and right
                
                // Try moving left
                for (int steps = 1; col - steps >= 0; steps++) {
                    // Check if the position is empty
                    if (board[row][col - steps] != '.') {
                        break; // Blocked, can't move further in this direction
                    }
                    
                    Papan newState = new Papan(currentState); // Create a deep copy
                    // Get the corresponding piece in the new state
                    Piece newPiece = newState.getPieceByName(pieceName);
                    if (newPiece == null) {
                        System.out.println("Error: Piece " + pieceName + " not found in new state.");
                        continue;
                    }
                    
                    Gerakan move = new Gerakan(newPiece, "kiri", steps);
                    boolean isValid = newState.movePiece(move);
                    
                    if (isValid) {
                        // If this state hasn't been visited yet
                        String stateHash = newState.hashString();
                        if (!visited.containsKey(stateHash)) {
                            // Calculate heuristic for the new state
                            int heuristicValue = heuristic.calculate(newState);
                            
                            // Create a new node and add to successors
                            Node successor = new Node(newState, node, move, heuristicValue);
                            successors.add(successor);
                        }
                    } else {
                        break; // If move is invalid, don't try more steps in this direction
                    }
                }
                
                // Try moving right
                int rightEnd = col + length - 1;
                for (int steps = 1; rightEnd + steps < currentState.getKolom(); steps++) {
                    // Check if the position is empty, but be careful about the exit cell
                    if (board[row][rightEnd + steps] != '.' && 
                        !(piece.isUtama() && row == currentState.getBarisExit() && 
                          rightEnd + steps == currentState.getKolomExit())) {
                        break; // Blocked, can't move further in this direction
                    }
                    
                    Papan newState = new Papan(currentState); // Create a deep copy
                    // Get the corresponding piece in the new state
                    Piece newPiece = newState.getPieceByName(pieceName);
                    if (newPiece == null) {
                        System.out.println("Error: Piece " + pieceName + " not found in new state.");
                        continue;
                    }
                    
                    Gerakan move = new Gerakan(newPiece, "kanan", steps);
                    boolean isValid = newState.movePiece(move);

                    if (isValid) {
                        // If this state hasn't been visited yet
                        String stateHash = newState.hashString();
                        if (!visited.containsKey(stateHash)) {
                            // Calculate heuristic for the new state
                            int heuristicValue = heuristic.calculate(newState);
                            
                            // Create a new node and add to successors
                            Node successor = new Node(newState, node, move, heuristicValue);
                            successors.add(successor);
                        }
                    } else {
                        break; // If move is invalid, don't try more steps in this direction
                    }
                }
            } else {
                // Vertical piece can only move up and down
                
                // Try moving up
                for (int steps = 1; row - steps >= 0; steps++) {
                    // Check if the position is empty
                    if (board[row - steps][col] != '.') {
                        break; // Blocked, can't move further in this direction
                    }
                    
                    Papan newState = new Papan(currentState); // Create a deep copy
                    // Get the corresponding piece in the new state
                    Piece newPiece = newState.getPieceByName(pieceName);
                    if (newPiece == null) {
                        System.out.println("Error: Piece " + pieceName + " not found in new state.");
                        continue;
                    }
                    
                    Gerakan move = new Gerakan(newPiece, "atas", steps);
                    boolean isValid = newState.movePiece(move);
                    
                    if (isValid) {
                        // If this state hasn't been visited yet
                        String stateHash = newState.hashString();
                        if (!visited.containsKey(stateHash)) {
                            // Calculate heuristic for the new state
                            int heuristicValue = heuristic.calculate(newState);
                            
                            // Create a new node and add to successors
                            Node successor = new Node(newState, node, move, heuristicValue);
                            successors.add(successor);
                        }
                    } else {
                        break; // If move is invalid, don't try more steps in this direction
                    }
                }
                
                // Try moving down
                int bottomEnd = row + length - 1;
                for (int steps = 1; bottomEnd + steps < currentState.getBaris(); steps++) {
                    // Check if the position is empty, but be careful about the exit cell
                    if (board[bottomEnd + steps][col] != '.' && 
                        !(piece.isUtama() && bottomEnd + steps == currentState.getBarisExit() && 
                          col == currentState.getKolomExit())) {
                        break; // Blocked, can't move further in this direction
                    }
                    
                    Papan newState = new Papan(currentState); // Create a deep copy
                    // Get the corresponding piece in the new state
                    Piece newPiece = newState.getPieceByName(pieceName);
                    if (newPiece == null) {
                        System.out.println("Error: Piece " + pieceName + " not found in new state.");
                        continue;
                    }
                    
                    Gerakan move = new Gerakan(newPiece, "bawah", steps);
                    boolean isValid = newState.movePiece(move);
                    
                    if (isValid) {
                        // If this state hasn't been visited yet
                        String stateHash = newState.hashString();
                        if (!visited.containsKey(stateHash)) {
                            // Calculate heuristic for the new state
                            int heuristicValue = heuristic.calculate(newState);
                            
                            // Create a new node and add to successors
                            Node successor = new Node(newState, node, move, heuristicValue);
                            successors.add(successor);
                        }
                    } else {
                        break; // If move is invalid, don't try more steps in this direction
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
        
        int exitRow = state.getBarisExit();
        int exitCol = state.getKolomExit();
        
        // For horizontal primary piece
        if (primaryPiece.isHorizontal()) {
            int pieceRow = primaryPiece.getBaris();
            int pieceRightCol = primaryPiece.getKolom() + primaryPiece.getPanjang() - 1;
            
            // If exit is outside the board on the right
            if (exitCol >= state.getKolom()) {
                return pieceRow == exitRow && pieceRightCol == state.getKolom() - 1;
            } else {
                // If exit is inside the board
                return pieceRow == exitRow && pieceRightCol >= exitCol;
            }
        } 
        // For vertical primary piece
        else {
            int pieceCol = primaryPiece.getKolom();
            int pieceBottomRow = primaryPiece.getBaris() + primaryPiece.getPanjang() - 1;
            
            // If exit is outside the board on the bottom
            if (exitRow >= state.getBaris()) {
                return pieceCol == exitCol && pieceBottomRow == state.getBaris() - 1;
            } else {
                // If exit is inside the board
                return pieceBottomRow >= exitRow && pieceCol == exitCol;
            }
        }
    }
    
    private List<Gerakan> reconstructPath(Node node) {
        List<Gerakan> path = new ArrayList<>();
        
        // Trace back from goal node to start node
        while (node.getParent() != null) {
            path.add(0, node.getAction()); // Add to front of list
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
        Papan currentState = new Papan(initialState); // Create a deep copy to manipulate
        
        for (int i = 0; i < solution.size(); i++) {
            Gerakan originalMove = solution.get(i);
            Piece originalPiece = originalMove.getPiece();
            String direction = originalMove.getArah();
            int steps = originalMove.getJumlahKotak();
            
            // Get the corresponding piece in the current state
            Piece currentPiece = currentState.getPieceByName(originalPiece.getNama());
            if (currentPiece == null) {
                System.out.println("Error: Piece " + originalPiece.getNama() + " not found in current state.");
                continue;
            }
            
            // Create a new move with the current piece reference
            Gerakan currentMove = new Gerakan(currentPiece, direction, steps);
            
            System.out.println("Gerakan " + (i+1) + ": " + currentPiece.getNama() + 
                              "-" + direction + " by " + steps + " spaces");
            
            boolean moveSuccess = currentState.movePiece(currentMove);
            if (!moveSuccess) {
                System.out.println("Warning: Failed to apply move " + (i+1) + " in solution print.");
            }
            
            currentState.displayBoard();
            // System.out.println("Heuristic: " + heuristic.calculate(currentState));
            System.out.println();
        }
    }
}
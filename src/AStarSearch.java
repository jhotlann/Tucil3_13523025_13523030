package Tucil3_13523025_13523030.src;

import java.util.*;

public class AStarSearch {
    private Papan initialState;
    private HashMap<String, Boolean> visited;
    private HashMap<String, Integer> gScore; // Track path cost to reach each state
    private PriorityQueue<Node> openSet;
    private int expandedNodes;
    private BoardHeuristic heuristic;
    private int maxIterations = 100000; // Safety limit to prevent infinite loops
    
    public AStarSearch(Papan initialState) {
        this.initialState = initialState;
        this.visited = new HashMap<>();
        this.gScore = new HashMap<>();
        this.expandedNodes = 0;
        this.heuristic = new BlockingDistanceHeuristic();
        
        // Create priority queue that sorts nodes based on their f-score (g + h)
        this.openSet = new PriorityQueue<>(Comparator.comparingInt(node -> 
            node.getPathCost() + node.getHeuristic()));
        
        System.out.println("Inisialisasi A* Search");
        System.out.println("Papan awal: ");
        initialState.displayBoard();
        
        // Print information about primary piece
        Piece primaryPiece = initialState.getPrimaryPiece();
        if (primaryPiece != null) {
            System.out.println("Primary piece: " + primaryPiece.getNama() + 
                              " at position (" + primaryPiece.getBaris() + "," + 
                              primaryPiece.getKolom() + ")");
            System.out.println("Exit position: (" + initialState.getBarisExit() + 
                              "," + initialState.getKolomExit() + ")");
        }
    }
    
    public void solve() {
        // Calculate heuristic for the initial state
        int initialHeuristic = heuristic.calculate(initialState);
        
        // Create starting node with path cost 0 and calculated heuristic
        Node startNode = new Node(initialState, null, null, initialHeuristic);
        startNode.setPathCost(0); // g-score for start node is 0
        
        String startStateHash = initialState.hashString();
        gScore.put(startStateHash, 0);
        
        openSet.add(startNode);
        System.out.println("Heuristik awal (h): " + startNode.getHeuristic());
        System.out.println("Path cost awal (g): " + startNode.getPathCost());
        System.out.println("Total cost awal (f = g + h): " + 
                          (startNode.getPathCost() + startNode.getHeuristic()));
        
        long startTime = System.currentTimeMillis();
        
        while (!openSet.isEmpty() && expandedNodes < maxIterations) {
            Node currentNode = openSet.poll();
            String currentStateHash = currentNode.getState().hashString();
            
            expandedNodes++;
            
            // Debugging output
            if (expandedNodes % 1000 == 0) {
                System.out.println("Nodes expanded: " + expandedNodes + 
                                  ", Queue size: " + openSet.size() + 
                                  ", Visited states: " + visited.size());
                System.out.println("Current f-score: " + 
                                  (currentNode.getPathCost() + currentNode.getHeuristic()) +
                                  " (g=" + currentNode.getPathCost() + 
                                  ", h=" + currentNode.getHeuristic() + ")");
            }
            
            // Check if we've already processed this state more efficiently
            if (visited.containsKey(currentStateHash)) {
                continue;
            }
            
            // Mark this state as visited
            visited.put(currentStateHash, true);
            
            // Check if the puzzle is solved
            if (isSolved(currentNode.getState())) {
                long endTime = System.currentTimeMillis();
                long timeTaken = endTime - startTime;
                System.out.println("Solved in " + timeTaken + " ms");
                System.out.println("Nodes expanded: " + expandedNodes);
                System.out.println("Solution path cost: " + currentNode.getPathCost() + " moves");
                System.out.println("States visited: " + visited.size());
                System.out.println("Final f-score: " + 
                                  (currentNode.getPathCost() + currentNode.getHeuristic()) +
                                  " (g=" + currentNode.getPathCost() + 
                                  ", h=" + currentNode.getHeuristic() + ")");
                
                List<Gerakan> solution = reconstructPath(currentNode);
                printSolution(solution, initialState);
                return;
            }
            
            // Generate all possible moves from the current state
            List<Node> successors = generateSuccessors(currentNode);
            
            for (Node successor : successors) {
                String stateHash = successor.getState().hashString();
                int tentativeGScore = currentNode.getPathCost() + 1; // Each move costs 1
                
                // If we already found a better path to this state, skip it
                if (gScore.containsKey(stateHash) && tentativeGScore >= gScore.get(stateHash)) {
                    continue;
                }
                
                // This is the best path so far to this successor
                gScore.put(stateHash, tentativeGScore);
                successor.setPathCost(tentativeGScore);
                
                // Only add to queue if we haven't processed this state yet
                if (!visited.containsKey(stateHash)) {
                    openSet.add(successor);
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
                        String stateHash = newState.hashString();
                        // Skip if we've already processed this state
                        if (visited.containsKey(stateHash)) {
                            continue;
                        }
                        
                        // Calculate heuristic for the new state
                        int heuristicValue = heuristic.calculate(newState);
                        
                        // Create a new node and add to successors
                        Node successor = new Node(newState, node, move, heuristicValue);
                        successors.add(successor);
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
                        String stateHash = newState.hashString();
                        // Skip if we've already processed this state
                        if (visited.containsKey(stateHash)) {
                            continue;
                        }
                        
                        // Calculate heuristic for the new state
                        int heuristicValue = heuristic.calculate(newState);
                        
                        // Create a new node and add to successors
                        Node successor = new Node(newState, node, move, heuristicValue);
                        successors.add(successor);
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
                        String stateHash = newState.hashString();
                        // Skip if we've already processed this state
                        if (visited.containsKey(stateHash)) {
                            continue;
                        }
                        
                        // Calculate heuristic for the new state
                        int heuristicValue = heuristic.calculate(newState);
                        
                        // Create a new node and add to successors
                        Node successor = new Node(newState, node, move, heuristicValue);
                        successors.add(successor);
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
                        String stateHash = newState.hashString();
                        // Skip if we've already processed this state
                        if (visited.containsKey(stateHash)) {
                            continue;
                        }
                        
                        // Calculate heuristic for the new state
                        int heuristicValue = heuristic.calculate(newState);
                        
                        // Create a new node and add to successors
                        Node successor = new Node(newState, node, move, heuristicValue);
                        successors.add(successor);
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
        System.out.println("Number of moves: " + solution.size());
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
            
            System.out.println("\nMove " + (i+1) + ": Piece " + currentPiece.getNama() + 
                              " moves " + direction + " by " + steps + " spaces");
            
            boolean moveSuccess = currentState.movePiece(currentMove);
            if (!moveSuccess) {
                System.out.println("Warning: Failed to apply move " + (i+1) + " in solution print.");
            }
            
            currentState.displayBoard();
            
            // Optional: Print the state's heuristic value after each move
            int heuristicValue = heuristic.calculate(currentState);
            System.out.println("Heuristic value after move: " + heuristicValue);
        }
        
        System.out.println("\nPuzzle solved!");
    }
}

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
        
        
        // Piece primaryPiece = initialState.getPrimaryPiece();
        // if (primaryPiece != null) {
        //     System.out.println("Primary piece: " + primaryPiece.getNama() + 
        //                       " at position (" + primaryPiece.getBaris() + "," + 
        //                       primaryPiece.getKolom() + ")");
        //     System.out.println("Exit position: (" + initialState.getBarisExit() + 
        //                       "," + initialState.getKolomExit() + ")");
        // }
    }
    
    public void solve() {
       
        int initialHeuristic = heuristic.calculate(initialState);
        
        
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
            
            
            // if (expandedNodes % 1000 == 0) {
            //     System.out.println("Nodes expanded: " + expandedNodes + 
            //                       ", Queue size: " + openSet.size() + 
            //                       ", Visited states: " + visited.size());
                // System.out.println("Current f-score: " + 
                //                   (currentNode.getPathCost() + currentNode.getHeuristic()) +
                //                   " (g=" + currentNode.getPathCost() + 
                //                   ", h=" + currentNode.getHeuristic() + ")");
            // }
            
           
            if (visited.containsKey(currentStateHash)) {
                continue;
            }
            
            
            visited.put(currentStateHash, true);
            
            
            if (isSolved(currentNode.getState())) {
                long endTime = System.currentTimeMillis();
                long timeTaken = endTime - startTime;
                System.out.println("Solved in " + timeTaken + " ms");
                System.out.println("Nodes expanded: " + expandedNodes);
                // System.out.println("Solution path cost: " + currentNode.getPathCost() + " moves");
                // System.out.println("States visited: " + visited.size());
                // System.out.println("Final f-score: " + 
                //                   (currentNode.getPathCost() + currentNode.getHeuristic()) +
                //                   " (g=" + currentNode.getPathCost() + 
                //                   ", h=" + currentNode.getHeuristic() + ")");
                
                List<Gerakan> solution = reconstructPath(currentNode);
                printSolution(solution, initialState);
                return;
            }
            
            
            List<Node> successors = generateSuccessors(currentNode);
            
            for (Node successor : successors) {
                String stateHash = successor.getState().hashString();
                int tentativeGScore = currentNode.getPathCost() + 1; // Each move costs 1
                
                
                if (gScore.containsKey(stateHash) && tentativeGScore >= gScore.get(stateHash)) {
                    continue;
                }
                
               
                gScore.put(stateHash, tentativeGScore);
                successor.setPathCost(tentativeGScore);
                
              
                if (!visited.containsKey(stateHash)) {
                    openSet.add(successor);
                }
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
                
                for (int steps = 1; col - steps >= 0; steps++) {
                    
                    if (board[row][col - steps] != '.') {
                        break; 
                    }
                    
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
                       
                        if (visited.containsKey(stateHash)) {
                            continue;
                        }
                        
                     
                        int heuristicValue = heuristic.calculate(newState);
                        
                        Node successor = new Node(newState, node, move, heuristicValue);
                        successors.add(successor);
                    } else {
                        break;
                    }
                }
                
              
                int rightEnd = col + length - 1;
                for (int steps = 1; rightEnd + steps < currentState.getKolom(); steps++) {
                   
                    if (board[row][rightEnd + steps] != '.' && 
                        !(piece.isUtama() && row == currentState.getBarisExit() && 
                          rightEnd + steps == currentState.getKolomExit())) {
                        break; 
                    }
                    
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
                       
                        if (visited.containsKey(stateHash)) {
                            continue;
                        }
                        
                      
                        int heuristicValue = heuristic.calculate(newState);
                        
                        Node successor = new Node(newState, node, move, heuristicValue);
                        successors.add(successor);
                    } else {
                        break;
                    }
                }
            } else {
                
                for (int steps = 1; row - steps >= 0; steps++) {
                    
                    if (board[row - steps][col] != '.') {
                        break; 
                    }
                    
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
                        
                        if (visited.containsKey(stateHash)) {
                            continue;
                        }
                        
                   
                        int heuristicValue = heuristic.calculate(newState);
                        
                      
                        Node successor = new Node(newState, node, move, heuristicValue);
                        successors.add(successor);
                    } else {
                        break;
                    }
                }
                
                int bottomEnd = row + length - 1;
                for (int steps = 1; bottomEnd + steps < currentState.getBaris(); steps++) {
                  
                    if (board[bottomEnd + steps][col] != '.' && 
                        !(piece.isUtama() && bottomEnd + steps == currentState.getBarisExit() && 
                          col == currentState.getKolomExit())) {
                        break; 
                    }
                    
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
                        
                        if (visited.containsKey(stateHash)) {
                            continue;
                        }
                        
                        int heuristicValue = heuristic.calculate(newState);
                        
                       
                        Node successor = new Node(newState, node, move, heuristicValue);
                        successors.add(successor);
                    } else {
                        break; 
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

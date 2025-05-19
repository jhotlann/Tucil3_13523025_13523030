package Tucil3_13523025_13523030.src;

import java.util.*;

public class GreedyBestFirstSearch {
    private Papan initialState;
    private HashMap<String, Boolean> visited;
    private PriorityQueue<Node> priorityQueue;
    private int expandedNodes;
    private BoardHeuristic heuristic;
    
    public GreedyBestFirstSearch(Papan initialState) {
        this.initialState = initialState;
        this.visited = new HashMap<>();
        this.expandedNodes = 0;
        this.heuristic = new BlockingDistanceHeuristic();
        
        // Create priority queue that sorts nodes based on their heuristic value
        this.priorityQueue = new PriorityQueue<>(Comparator.comparingInt(Node::getHeuristic));
    }
    
    public List<Gerakan> solve() {
        Node startNode = new Node(initialState, null, null, heuristic.calculate(initialState));
        priorityQueue.add(startNode);
        visited.put(initialState.hashString(), true);
        
        long startTime = System.currentTimeMillis();
        
        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();
            expandedNodes++;
            
            // Check if the puzzle is solved
            if (isSolved(currentNode.getState())) {
                long endTime = System.currentTimeMillis();
                System.out.println("Solved in " + (endTime - startTime) + " ms");
                System.out.println("Nodes expanded: " + expandedNodes);
                return reconstructPath(currentNode);
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
        
        // If no solution is found
        System.out.println("No solution found after expanding " + expandedNodes + " nodes");
        return null;
    }
    
    private List<Node> generateSuccessors(Node node) {
        List<Node> successors = new ArrayList<>();
        Papan currentState = node.getState();
        
        // For each piece on the board
        for (Piece piece : currentState.getPieces()) {
            // Try moving in all four directions
            String[] directions = {"atas", "bawah", "kiri", "kanan"};
            
            for (String direction : directions) {
                // Try moving 1 to piece length spaces
                int maxMove = piece.isHorizontal() ? 
                    (direction.equals("kiri") || direction.equals("kanan") ? currentState.getKolom() : 1) :
                    (direction.equals("atas") || direction.equals("bawah") ? currentState.getBaris() : 1);
                
                for (int steps = 1; steps <= maxMove; steps++) {
                    Gerakan move = new Gerakan(piece, direction, steps);
                    Papan newState = new Papan(currentState);
                    
                    // Try applying the move
                    if (isValidMove(newState, move)) {
                        newState.movePiece(move);
                        
                        // If this state hasn't been visited yet
                        if (!visited.containsKey(newState.hashString())) {
                            // Calculate heuristic for the new state
                            int heuristicValue = heuristic.calculate(newState);
                            
                            // Create a new node and add to successors
                            Node successor = new Node(newState, node, move, heuristicValue);
                            successors.add(successor);
                        }
                    } else {
                        // If current move is invalid, no point trying larger steps in the same direction
                        break;
                    }
                }
            }
        }
        
        return successors;
    }
    
    private boolean isValidMove(Papan state, Gerakan move) {
        Piece piece = move.getPiece();
        String direction = move.getArah();
        int steps = move.getJumlahKotak();
        
        int row = piece.getBaris();
        int col = piece.getKolom();
        int length = piece.getPanjang();
        boolean isHorizontal = piece.isHorizontal();
        
        char[][] board = state.getPapan();
        
        // Check if the move is valid based on the direction and piece orientation
        if (isHorizontal) {
            if (direction.equals("atas") || direction.equals("bawah")) {
                // Horizontal pieces can only move horizontally
                return false;
            } else if (direction.equals("kiri")) {
                // Check if there's enough space to the left
                if (col - steps < 0) {
                    return false;
                }
                
                // Check if there are no obstacles
                for (int i = 1; i <= steps; i++) {
                    if (board[row][col - i] != '.') {
                        return false;
                    }
                }
            } else if (direction.equals("kanan")) {
                // Check if there's enough space to the right
                if (col + length - 1 + steps >= state.getKolom()) {
                    return false;
                }
                
                // Check if there are no obstacles
                for (int i = 1; i <= steps; i++) {
                    if (board[row][col + length - 1 + i] != '.') {
                        return false;
                    }
                }
            }
        } else {
            // Vertical piece
            if (direction.equals("kiri") || direction.equals("kanan")) {
                // Vertical pieces can only move vertically
                return false;
            } else if (direction.equals("atas")) {
                // Check if there's enough space above
                if (row - steps < 0) {
                    return false;
                }
                
                // Check if there are no obstacles
                for (int i = 1; i <= steps; i++) {
                    if (board[row - i][col] != '.') {
                        return false;
                    }
                }
            } else if (direction.equals("bawah")) {
                // Check if there's enough space below
                if (row + length - 1 + steps >= state.getBaris()) {
                    return false;
                }
                
                // Check if there are no obstacles
                for (int i = 1; i <= steps; i++) {
                    if (board[row + length - 1 + i][col] != '.') {
                        return false;
                    }
                }
            }
        }
        
        return true;
    }
    
    private boolean isSolved(Papan state) {
        Piece primaryPiece = state.getPrimaryPiece();
        if (primaryPiece == null) {
            return false;
        }
        
        int exitRow = state.getBarisExit();
        int exitCol = state.getKolomExit();
        
        // Check if the primary piece is at or adjacent to the exit
        if (primaryPiece.isHorizontal()) {
            // For horizontal piece, check if right end is at exit
            int pieceRightCol = primaryPiece.getKolom() + primaryPiece.getPanjang() - 1;
            return primaryPiece.getBaris() == exitRow && pieceRightCol == exitCol;
        } else {
            // For vertical piece, check if bottom end is at exit
            int pieceBottomRow = primaryPiece.getBaris() + primaryPiece.getPanjang() - 1;
            return pieceBottomRow == exitRow && primaryPiece.getKolom() == exitCol;
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
}
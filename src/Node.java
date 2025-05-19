package Tucil3_13523025_13523030.src;

/**
 * Node class for search algorithm
 * Represents a state in the search tree
 */
public class Node {
    private Papan state;
    private Node parent;
    private Gerakan action;
    private int heuristic;
    private int pathCost;
    
    /**
     * Create a new node with the given state, parent, action, and heuristic value
     */
    public Node(Papan state, Node parent, Gerakan action, int heuristic) {
        this.state = state;
        this.parent = parent;
        this.action = action;
        this.heuristic = heuristic;
    }
    
    /**
     * Get the state represented by this node
     */
    public Papan getState() {
        return state;
    }
    
    /**
     * Get the parent node
     */
    public Node getParent() {
        return parent;
    }
    
    /**
     * Get the action that led to this node from its parent
     */
    public Gerakan getAction() {
        return action;
    }
    
    /**
     * Get the heuristic value of this node
     */
    public int getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }

    public int getPathCost() {
        return pathCost;
    }

    public void setPathCost(int pathCost) {
        this.pathCost = pathCost;
    }
}
package Tucil3_13523025_13523030.src;

public class Node {
    private Papan state;
    private Node parent;
    private Gerakan action;
    private int heuristic;
    private int pathCost;
    
    public Node(Papan state, Node parent, Gerakan action, int heuristic) {
        this.state = state;
        this.parent = parent;
        this.action = action;
        this.heuristic = heuristic;
    }
    
    public Papan getState() {
        return state;
    }
    
    public Node getParent() {
        return parent;
    }
    
    public Gerakan getAction() {
        return action;
    }
    
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
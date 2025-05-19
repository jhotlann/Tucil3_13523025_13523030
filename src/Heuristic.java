package Tucil3_13523025_13523030.src;

/**
 * Interface for heuristic functions to evaluate board states
 */
public interface Heuristic {
    /**
     * Calculate the heuristic value for the given board state
     * Lower values indicate states that are closer to the goal
     * 
     * @param state The board state to evaluate
     * @return The heuristic value
     */
    int calculate(Papan state);
}